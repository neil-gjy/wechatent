package com.tjport.wechat.salary.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.runners.Parameterized.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tjport.wechat.common.model.Result;
import com.tjport.wechat.common.spring.BaseController;
import com.tjport.wechat.common.utils.weChatUtil;
import com.tjport.wechat.sys.vo.UserVo;


@Controller
@RequestMapping(value = ServiceController.BASE)
public class ServiceController extends BaseController {
	final static String BASE = "service";

	
	@RequestMapping(value = "textMessage/{content}")
	public Result textMessage(@PathVariable String content){
		Result result = null;
		
		//int rs = weChatUtil.customMessage(HomeController.token.getToken(), "o_1A9wMXvKVTDXT4BiHW8_ugEzaU", content);
		
		//int rs = weChatUtil.templateMessage(HomeController.token.getToken(), "o_1A9wMXvKVTDXT4BiHW8_ugEzaU", "标题","请审批【软件研发部】..申请","2016年4月12日 18:26");
		
		/*if(rs == 0){
		    result = Result.successResult().setMsg("消息发送成功");
		}
		else{
			result = Result.errorResult().setMsg("消息发送失败");
		}*/
		
		return result;
	}
	
	// 登陆授权测试页面
	@RequestMapping(value = "/login/{openid}")
	public String login(@PathVariable String openid, Map<String,String> map){
		map.put("openid", openid);
		
		return BASE + "/" + "login";
	}
	
	@RequestMapping(value = "submit")
	public Result submit(UserVo userVo){
		Result result = null;
		
		return result;
	}
	

}
