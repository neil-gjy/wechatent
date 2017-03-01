package com.tjport.wechat.salary.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;

import com.tjport.wechat.common.spring.BaseController;
import com.tjport.wechat.common.utils.CorpWeChatUtils;
import com.tjport.wechat.common.utils.DbUtils;
import com.tjport.wechat.common.utils.weChatUtil;
import com.tjport.wechat.common.utils.po.AccessTokenPo;
import com.tjport.wechat.salary.Job.AccessTokenJob;


@Controller
@RequestMapping()
public class StartController extends BaseController {
	final static String BASE = "/";
	
	public static AccessTokenPo token = null;
	
	public static void setToken(AccessTokenPo accessTokenPo){
		token = accessTokenPo;
	}

	@RequestMapping(value = "corpStart")
	public String corpStart( Map<String, Object> map) throws SQLException {
		
		token = CorpWeChatUtils.getAccessToken();
		
		System.out.println("token:" + token.getToken());
		System.out.println("TimeL:" + token.getExpiresIn());
		
		String menu = JSONObject.toJSON(CorpWeChatUtils.initMenu()).toString();
		int result = CorpWeChatUtils.createMenu(token.getToken(), menu);
		
		if(result == 0){
			System.out.println("Success！");
		}
		else{
			System.out.println("Error Code:" + result);
		}
		
		return "home";
	}
	
	/**
	 * 启动获取Access_Token Job
	 * @throws SchedulerException
	 */
	@SuppressWarnings("deprecation")
	public void schedulerRun() throws SchedulerException{
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		
		Scheduler scheduler = schedulerFactory.getScheduler();
		
		JobDetail jobDetail = new JobDetailImpl("access_token_job",
				Scheduler.DEFAULT_GROUP,AccessTokenJob.class);
		
		SimpleTrigger simpleTrigger = new SimpleTriggerImpl("access_token_trigger", 
				Scheduler.DEFAULT_GROUP);
		
		((SimpleTriggerImpl) simpleTrigger).setStartTime(new Date(System.currentTimeMillis()));
        ((SimpleTriggerImpl) simpleTrigger).setRepeatInterval(7000000);//7000s获取一次
        ((SimpleTriggerImpl) simpleTrigger).setRepeatCount(-1);// -1位不限制次数

        scheduler.scheduleJob(jobDetail, simpleTrigger);

        scheduler.start();	
	}

}
