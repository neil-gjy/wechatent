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

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.pool.DruidDataSource;
import com.tjport.wechat.common.spring.BaseController;
import com.tjport.wechat.common.utils.CheckUtils;
import com.tjport.wechat.common.utils.DbUtils;
import com.tjport.wechat.common.utils.MessageUtil;
import com.tjport.wechat.common.utils.po.TextMessagePo;
import com.tjport.wechat.common.utils.po.UserInfoPo;
import com.tjport.wechat.salary.service.ISalaryService;
import com.tjport.wechat.sys.service.IWxUserService;
import com.tjport.wechat.common.utils.weChatUtil;

@Controller
@RequestMapping(value = ConnectController.BASE)
public class ConnectController extends BaseController {
	final static String BASE = "connect";
	//final static String PATH = "check";
	
	public static final String BIND_INFO = "请您输入个人身份证号绑定个人信息！";
	
	@Autowired
	private ISalaryService salaryService;
	
	@Autowired
	private IWxUserService wxUserService;

	@RequestMapping(value = "check", method = RequestMethod.GET)
	public void checkGet (HttpServletResponse response, HttpServletRequest request) throws IOException {
		
		// 加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		
		
		if(CheckUtils.checkSingnature(signature, timestamp, nonce)){
			PrintWriter out = response.getWriter();
			
			out.print(echostr);
			out.flush();
			out.close();
		}
		
		//return "home";
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
				
			    // 设置备注
				if(content.length() == 18){
					 weChatUtil.setUserRemark(HomeController.token.getToken(), fromUserName, content);
				}
			    if(content.contains("gz") && (content.length() == 6 || content.length() == 8)){
                   /* UserInfoPo userInfo = weChatUtil.getUserInfo(HomeController.token.getToken(),fromUserName);
					
					String hisSalary = salaryService.getBonusInfoByDateNew(userInfo.getRemark(),content.substring(2));*/
			    	
			    	//message = MessageUtil.initText(toUserName, fromUserName, "功能将在5月开放...");
                    UserInfoPo userInfo = weChatUtil.getUserInfo(HomeController.token.getToken(),fromUserName);
					
					String hisBonus = salaryService.getBaseSalaryInfoByDate(userInfo.getRemark(),content.substring(2));
					
					message = MessageUtil.initText(toUserName, fromUserName, hisBonus);
				}
				else if(content.contains("jj") && (content.length() == 6 || content.length() == 8)){
					UserInfoPo userInfo = weChatUtil.getUserInfo(HomeController.token.getToken(),fromUserName);
					
					String hisBonus = salaryService.getBonusInfoByDateNew(userInfo.getRemark(),content.substring(2));
			    	
			    	message = MessageUtil.initText(toUserName, fromUserName, hisBonus);
				}
				else{
					message = MessageUtil.initText(toUserName, fromUserName, "查询条件输入有误，请重新输入");
				}
				
				//message = MessageUtil.MessageToXml(textPo);
			}
			else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.welText());
				}
				else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					String key = map.get("EventKey");
					// 获取用信息
					UserInfoPo userInfo = weChatUtil.getUserInfo(HomeController.token.getToken(),fromUserName);
					if(!userInfo.getRemark().equals("")){
					    if(key.equals("curSalary")){
		    					 //reContent.append("姓名：" + new String(result.getString("name").getBytes("ISO-8859-1"), "GBK") + "\n");
		    					 //reContent.append("公司：" + new String(result.getString("org").getBytes("ISO-8859-1"), "GBK") + "\n");
		    				String salary = salaryService.getBaseSalaryInfo(userInfo.getRemark());
		    				if(salary.length() != 0){
		    				    message = MessageUtil.initText(toUserName, fromUserName, salary);
		    				}else{
		    					message = MessageUtil.initText(toUserName, fromUserName, "本月工资未出或已经进入历史工资中！");
		    				}
						}
					    else if(key.equals("curBonus")){
					    	String bonus = salaryService.getBonusInfo(userInfo.getRemark());
					    	
					    	if(bonus.length() != 0){
					    	    message = MessageUtil.initText(toUserName, fromUserName, bonus);
					    	}else{
		    					message = MessageUtil.initText(toUserName, fromUserName, "本月奖金未出或已经进入历史奖金中！");
		    				}
					    	
					    }
					    else if(key.equals("hisSalary")){
					    	message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.hisSalary());
					    	//weChatUtil.sendMessage(HomeController.token.getToken(), "o_1A9wMXvKVTDXT4BiHW8_ugEzaU", "测试");
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
		    
			out.print(message);
		}catch(DocumentException e){
			e.printStackTrace();
		}
		
	}

}



/*// 连接数据库
Connection con = null;
Class.forName("oracle.jdbc.driver.OracleDriver");
con = DriverManager.getConnection("jdbc:oracle:thin:@10.128.141.111:1521:spms","spms","spmsadmin");

PreparedStatement pre = null;
String sql = "select * from t_sf_d_employee where id = ?";
pre = con.prepareStatement(sql);

pre.setString(1, userInfo.getRemark());

ResultSet result = null;
result = pre.executeQuery();
String temp = "";
while(result.next()){
	 System.out.println("Id:" + result.getString("id") + "姓名:"
                + result.getString("name"));
	 temp += result.getString("name");
}*/


/*if(key.equals("personal")){

    if(!userInfo.getRemark().equals("")){
		
		Connection conn = null;
	    PreparedStatement pre = null;
	    
	    conn = DbUtils.getConnection();
	    String sql = "select ryxx.sbsbh as id, ryxx.bxm as name, dwdm.name as org from rs_d_ryjb ryxx left outer join rs_c_dwdm dwdm on ryxx.dwbm = dwdm.code where sbsbh = ?";
		//String sql = "select ryxx.sbsbh as id, ryxx.bxm as name from sf_d_ryjb ryxx  where sbsbh = ?";
		pre = conn.prepareStatement(sql);
		
		pre.setString(1, userInfo.getRemark());
		
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
		
		message = MessageUtil.initText(toUserName, fromUserName, "");
    }
    else{
    	message = MessageUtil.initText(toUserName, fromUserName, BIND_INFO);
    }
    
}
else */
