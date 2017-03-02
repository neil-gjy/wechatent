package com.tpitc.wechatent.salary.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tpitc.wechatent.common.model.Result;
import com.tpitc.wechatent.common.spring.BaseController;
import com.tpitc.wechatent.common.utils.po.UserInfoPo;


@Controller
@RequestMapping(WebController.BASE + "/" + WebController.PATH)
public class WebController extends BaseController {
	final static String BASE = "web";
	final static String PATH = "test"; 

	@RequestMapping(value = "home")
	public String sysHome( Map<String, Object> map) throws SQLException {
		
		return  BASE + "/" + PATH + "/home";
	}
	
	@ResponseBody
	@RequestMapping("testAdd")
	public Result add() {
    	
    	Result mReturn = null;
		try {
			
			mReturn = Result.successResult().setMsg("添加成功");
		} catch (Exception e) {
			//logger.error("添加失败", e);
			mReturn = Result.errorResult().setMsg(e.getMessage());
		}
		return mReturn;
	}
	
	@ResponseBody
	@RequestMapping("login")
	public Result login(UserInfoPo user) {
    	
    	Result mReturn = null;
		try {
			
			mReturn = Result.successResult().setMsg("添加成功");
		} catch (Exception e) {
			//logger.error("添加失败", e);
			mReturn = Result.errorResult().setMsg(e.getMessage());
		}
		return mReturn;
	}

}
