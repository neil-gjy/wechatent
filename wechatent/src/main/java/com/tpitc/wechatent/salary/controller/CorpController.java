package com.tjport.wechat.salary.controller;

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

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.pool.DruidDataSource;
import com.tjport.wechat.common.spring.BaseController;
import com.tjport.wechat.common.utils.CheckUtils;
import com.tjport.wechat.common.utils.CorpWeChatUtils;
import com.tjport.wechat.common.utils.DbUtils;
import com.tjport.wechat.common.utils.MessageUtil;
import com.tjport.wechat.common.utils.po.TextMessagePo;
import com.tjport.wechat.common.utils.po.UserInfoPo;
import com.tjport.wechat.salary.service.ISalaryService;
import com.tjport.wechat.common.utils.weChatUtil;
import com.tjport.wechat.common.utils.ase.AesException;
import com.tjport.wechat.common.utils.ase.WXBizMsgCrypt;
import com.tjport.wechat.common.utils.corp.UserInfo;

@Controller
@RequestMapping(value = CorpController.BASE)
public class CorpController extends BaseController {
	final static String BASE = "corp";
	//final static String PATH = "check";
	
	public static final String BIND_INFO = "请您输入个人身份证号绑定个人信息！";
	
	private static final String TOKEN = "";
	private static final String CORP_ID = "";
	private static final String ENCODING_AESKEY = "";
	
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
		
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(TOKEN, ENCODING_AESKEY, CORP_ID);
		
		String sEchoStr = wxcpt.VerifyURL(msgSignature, timestamp, nonce, echostr);//echostr明文
		
		PrintWriter out = response.getWriter();
		
		out.print(sEchoStr);
		out.flush();
		out.close();
	}
	
	
	@RequestMapping(value = "check", method = RequestMethod.POST)
	public void checkPost (HttpServletResponse response, HttpServletRequest request) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		
		try{
			Map<String,String> map = MessageUtil.parseXml(request);
			
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
				
			    if(content.length() == 6){
					UserInfo userInfo = CorpWeChatUtils.getUserInfo(StartController.token.getToken(),fromUserName);
					
					String hisBonus = salaryService.getBonusInfoByDate(userInfo.getUserid(), content);
			    	
			    	message = MessageUtil.initText(toUserName, fromUserName, hisBonus);
				}
				
				//message = MessageUtil.MessageToXml(textPo);
			}
			else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					String key = map.get("EventKey");
					// 获取用信息
					UserInfo userInfo = CorpWeChatUtils.getUserInfo(StartController.token.getToken(),fromUserName);
				    if(key.equals("personal")){

	    			    if(!userInfo.getUserid().equals("")){
		    				
		    				Connection conn = null;
		    			    PreparedStatement pre = null;
		    			    
		    			    conn = DbUtils.getConnection();
		    			    String sql = "select ryxx.sbsbh as id, ryxx.bxm as name, dwdm.name as org from rs_d_ryjb ryxx left outer join rs_c_dwdm dwdm on ryxx.dwbm = dwdm.code where sbsbh = ?";
		    				//String sql = "select ryxx.sbsbh as id, ryxx.bxm as name from sf_d_ryjb ryxx  where sbsbh = ?";
		    				pre = conn.prepareStatement(sql);
		    				
		    				pre.setString(1, userInfo.getUserid());
		    				
		    				ResultSet result = null;
		    				result = pre.executeQuery();
		    			    
		    				StringBuilder reContent = new StringBuilder();
		    				while(result.next()){
		    					//String name = "": 
		    					 reContent.append("身份证号：" + result.getString("id") + "\n");
		    					 reContent.append("姓名：" + new String(result.getString("name").getBytes("ISO-8859-1"), "GBK") + "\n");
		    					 reContent.append("公司：" + new String(result.getString("org").getBytes("ISO-8859-1"), "GBK") + "\n");
		    					 //reContent.append("部门：" + result.getString("dept") + "\n");
		    				}
		    				
		    				message = MessageUtil.initText(toUserName, fromUserName, reContent.toString());
	    			    }
	    			    else{
	    			    	message = MessageUtil.initText(toUserName, fromUserName, BIND_INFO);
	    			    }
	    			    
				    }
				    else if(key.equals("curSalary")){
				    	Connection conn = null;
	    			    PreparedStatement pre = null;
	    			    
	    			    conn = DbUtils.getConnection();
	    			    
	    			    String sql = "select t_ryxx.sbsbh as id, t_gzb.* " 
										+ "from rs_d_ryjb t_ryxx "
										+ "right join rs_d_gzb t_gzb  on t_ryxx.rybm = t_gzb.rybm  and t_gzb.qrbz = 'Y' and  to_char(t_gzb.rq,'yyyymm') = to_char(sysdate,'yyyymm') "
										+ "where t_ryxx.sbsbh = ? ";
                        pre = conn.prepareStatement(sql);
	    				
	    				pre.setString(1, userInfo.getUserid());
	    				
	    				ResultSet result = null;
	    				result = pre.executeQuery();
	    				
	    				// 获取当前时间
	    				Date curDate = new Date();
	    			    
	    				StringBuilder reContent = new StringBuilder();
	    				while(result.next()){
	    					 reContent.append("    本月工资(" + curDate.getMonth() + "月)\n");//加薪
	    					 if(result.getString("gjgz") != null){
	    						 reContent.append("岗级工资：" + result.getString("gjgz") + "\n");//岗级工资
	    					 }
	    					 if(result.getString("zcjt") != null){
	    						 reContent.append("职称津贴：" + result.getString("zcjt") + "\n");//职称津贴
	    					 }
	    					 if(result.getString("ngjt") != null){
	    						 reContent.append("年功津贴：" + result.getString("ngjt") + "\n");//年功津贴
	    					 }
	    					 if(result.getString("scj") != null){
	    						 reContent.append("月奖：" + result.getString("scj") + "\n");//生产奖
	    					 }
	    					 if(result.getString("cwbt") != null){
	    						 reContent.append("财务补贴：" + result.getString("cwbt") + "\n");//财务补贴
	    					 }
	    					 
	    					 if(result.getString("sf") != null){
	    						 reContent.append("实发工资：" + result.getString("sf") + "\n");//实发工资
	    					 }
	    					 
	    					 // 五险一金
	    					 reContent.append("    本月五险一金个人缴纳部分(" + curDate.getMonth() + "月)\n");//
	    					 if(result.getString("ylbx") != null){
	    						 reContent.append("养老保险：" + result.getString("ylbx") + "\n");//生产奖
	    					 }
	    					 if(result.getString("yybx") != null){
	    						 reContent.append("医疗保险：" + result.getString("yybx") + "\n");//医疗保险
	    					 }
	    					 if(result.getString("sybx") != null){
	    						 reContent.append("失业保险：" + result.getString("sybx") + "\n");//失业保险
	    					 }
	    					 if(result.getString("gjj") != null){
	    						 reContent.append("公积金：" + result.getString("gjj") + "\n");//生产奖
	    					 }
	    					 if(result.getString("qynj") != null){
	    						 reContent.append("企业年金：" + result.getString("qynj") + "\n");//企业年金
	    					 }
	    					 //reContent.append("姓名：" + new String(result.getString("name").getBytes("ISO-8859-1"), "GBK") + "\n");
	    					 //reContent.append("公司：" + new String(result.getString("org").getBytes("ISO-8859-1"), "GBK") + "\n");
	    				}
	    				if(reContent.length() != 0){
	    				    message = MessageUtil.initText(toUserName, fromUserName, reContent.toString());
	    				}else{
	    					message = MessageUtil.initText(toUserName, fromUserName, "本月工资未出或已经进入历史工资中！");
	    				}
					}
				    else if(key.equals("curBonus")){
				    	
				    	String bonus = salaryService.getBonusInfo(userInfo.getUserid());
				    	
				    	message = MessageUtil.initText(toUserName, fromUserName, bonus);
				    	
				    }
				    else if(key.equals("hisSalary")){
				    	
				    }
				    else if(key.equals("hisBonus")){
				    	
				    }
				}
				
			}
		    
			out.print(message);
		}catch(DocumentException e){
			e.printStackTrace();
		}
		
	}
	

}
