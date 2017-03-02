package com.tpitc.wechatent.salary.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.pool.DruidDataSource;
import com.tpitc.wechatent.common.spring.BaseController;
import com.tpitc.wechatent.common.utils.CheckUtils;
import com.tpitc.wechatent.common.utils.EntWeChatUtils;
import com.tpitc.wechatent.common.utils.DbUtils;
import com.tpitc.wechatent.common.utils.MessageUtil;
import com.tpitc.wechatent.common.utils.po.TextMessagePo;
import com.tpitc.wechatent.common.utils.po.UserInfoPo;
import com.tpitc.wechatent.salary.service.ISalaryService;
import com.tpitc.wechatent.common.utils.aes.AesException;
import com.tpitc.wechatent.common.utils.aes.WXBizMsgCrypt;
import com.tpitc.wechatent.common.utils.corp.UserInfo;

@Controller
@RequestMapping(value = EntController.BASE)
public class EntController extends BaseController {
	final static String BASE = "ent";
	//final static String PATH = "check";
	
	public static final String BIND_INFO = "请您输入个人身份证号绑定个人信息！";
	
	private static final String TOKEN = "xinxi";
	private static final String CORP_ID = "wx166ef67e8dce57eb";
	private static final String ENCODING_AESKEY = "J4s1zOcOqX5Efa2MWUEDkuGeCKMvvFS6AnbzbABwCLm";
	
	@Autowired
	private ISalaryService salaryService;

	@RequestMapping(value = "check", method = RequestMethod.GET)
	public void checkGet (HttpServletResponse response, HttpServletRequest request) throws IOException, AesException {
		
		// 加密签名
		String msgSignature = request.getParameter("msg_signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		
		WXBizMsgCrypt wxcpt;
		try {  
			wxcpt = new WXBizMsgCrypt(TOKEN, ENCODING_AESKEY, CORP_ID);
		
			String sEchoStr = wxcpt.VerifyURL(msgSignature, timestamp, nonce, echostr);//echostr明文
		
			PrintWriter out = response.getWriter();
			
			out.print(sEchoStr);
			out.flush();
			out.close();
		}catch (AesException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "check", method = RequestMethod.POST)
	public void checkPost (HttpServletResponse response, HttpServletRequest request) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		// 加密签名
		String msgSignature = request.getParameter("msg_signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		
		InputStream inputStream = request.getInputStream();
		String postData = IOUtils.toString(inputStream, "UTF-8");
		
		String msg = "";
		WXBizMsgCrypt wxcpt = null;
		try { 
			wxcpt = new WXBizMsgCrypt(TOKEN, ENCODING_AESKEY, CORP_ID);
			
			msg = wxcpt.DecryptMsg(msgSignature, timestamp, nonce, postData);
		} catch (AesException e) { 
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		
		try{
			Map<String,String> map = MessageUtil.parseXml(msg);
			
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			String message = null;// 待发送消息
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
				TextMessagePo textPo = new TextMessagePo();
				
				textPo.setFromUserName(toUserName);
				textPo.setToUserName(fromUserName);
				textPo.setMsgType(msgType);
				
				Date date = new Date();
				textPo.setCreateTime(date.getTime());
				
			    // 设置备注
				if(content.length() == 18){
					//EntWeChatUtils.setUserRemark(HomeController.token.getToken(), fromUserName, content);
				}
			    if(content.contains("gz") && (content.length() == 6 || content.length() == 8)){
                    UserInfo userInfo = EntWeChatUtils.getUserInfo(HomeController.token.getToken(),fromUserName);
					
					String hisBonus = salaryService.getBaseSalaryInfoByDate(userInfo.getUserid(),content.substring(2));
					
					message = MessageUtil.initText(toUserName, fromUserName, hisBonus);
				}
				else if(content.contains("jj") && (content.length() == 6 || content.length() == 8)){
					UserInfo userInfo = EntWeChatUtils.getUserInfo(HomeController.token.getToken(),fromUserName);
					
					String hisBonus = salaryService.getBonusInfoByDateNew(userInfo.getUserid(),content.substring(2));
			    	
			    	message = MessageUtil.initText(toUserName, fromUserName, hisBonus);
				}
				else{
					message = MessageUtil.initText(toUserName, fromUserName, "查询条件输入有误，请重新输入");
				}
				
			}
			else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.welText());
				}
				else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					String key = map.get("EventKey");
					// 获取用信息
					UserInfo userInfo = EntWeChatUtils.getUserInfo(HomeController.token.getToken(),fromUserName);
					if(!userInfo.getUserid().equals("")){
					    if(key.equals("curSalary")){
		    				String salary = salaryService.getBaseSalaryInfo(userInfo.getUserid());
		    				if(salary.length() != 0){
		    				    message = MessageUtil.initText(toUserName, fromUserName, salary);
		    				}else{
		    					message = MessageUtil.initText(toUserName, fromUserName, "本月工资未出或已经进入历史工资中！");
		    				}
						}
					    else if(key.equals("curBonus")){
					    	String bonus = salaryService.getBonusInfo(userInfo.getUserid());
					    	
					    	if(bonus.length() != 0){
					    	    message = MessageUtil.initText(toUserName, fromUserName, bonus);
					    	}else{
		    					message = MessageUtil.initText(toUserName, fromUserName, "本月奖金未出或已经进入历史奖金中！");
		    				}
					    	
					    }
					    else if(key.equals("hisSalary")){
					    	message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.hisSalary());
					    	//EntWeChatUtils.sendMessage(HomeController.token.getToken(), "o_1A9wMXvKVTDXT4BiHW8_ugEzaU", "测试");
					    }
					    else if(key.equals("hisBonus")){
					    	message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.hisBonus());
					    }
					    else if(key.equals("bindInfo")){
					    	//message = MessageUtil.initText(toUserName, fromUserName, "功能尚未开放！");
					    	String link = "http://www.tpitc.com.cn/wechat/service/login";
					    	message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.bindInfoLink(link, toUserName));
					    	/*if(wxUserService.bindWxUser(userInfo)){
					    	    message = MessageUtil.initText(toUserName, fromUserName, "绑定成功！");
					    	}
					    	else{
					    		message = MessageUtil.initText(toUserName, fromUserName, "绑定失败！");
					    	}*/
					    }
					}
					else{
						message = MessageUtil.initText(toUserName, fromUserName, "请与管理员联系绑定个人信息！");
					}
				}
				
			}
			
			String encryptMsg = "";
			try {
				//加密回复消息
				encryptMsg = wxcpt.EncryptMsg(message, timestamp, nonce);
			} catch (AesException e) {
				e.printStackTrace();
			}
		    
			out.print(encryptMsg);
		}catch(DocumentException e){
			e.printStackTrace();
		}
		
	}
	

}
