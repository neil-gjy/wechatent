package com.tpitc.wechatent.salary.controller;

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
import com.tpitc.wechatent.common.spring.BaseController;
import com.tpitc.wechatent.common.utils.DbUtils;
import com.tpitc.wechatent.common.utils.EntWeChatUtils;
import com.tpitc.wechatent.common.utils.po.AccessTokenPo;
import com.tpitc.wechatent.salary.Job.AccessTokenJob;


@Controller
@RequestMapping()
public class HomeController extends BaseController {
	final static String BASE = "/";
	
	public static AccessTokenPo token = null;
	
	public static void setToken(AccessTokenPo accessTokenPo){
		token = accessTokenPo;
	}
	
	static{
        token = EntWeChatUtils.getAccessToken();
		
		System.out.println("token:" + token.getToken());
		System.out.println("TimeL:" + token.getExpiresIn());
		
		int delMenuReuslt = EntWeChatUtils.deleteMenu(token.getToken());
		if(delMenuReuslt == 0){
			System.out.println("删除菜单 Success！");
		}
		else{
			System.out.println("Error Code:" + delMenuReuslt);
		}
		
		String menu = JSONObject.toJSON(EntWeChatUtils.initMenu()).toString();
		int result = EntWeChatUtils.createMenu(token.getToken(), menu);
		
		if(result == 0){
			System.out.println("Success！");
		}
		else{
			System.out.println("Error Code:" + result);
		}
		
		
		// 循环获取Token 任务
		try {
			schedulerRun();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "home")
	public String sysHome( Map<String, Object> map) {
		
		/*token = EntWeChatUtils.getAccessToken();
		
		System.out.println("token:" + token.getToken());
		System.out.println("TimeL:" + token.getExpiresIn());
		
		String menu = JSONObject.toJSON(EntWeChatUtils.initMenu()).toString();
		int result = EntWeChatUtils.createMenu(token.getToken(), menu);
		
		if(result == 0){
			System.out.println("Success！");
		}
		else{
			System.out.println("Error Code:" + result);
		}
		
		
		// 循环获取Token 任务
		try {
			schedulerRun();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver"); 
		dataSource.setUsername("spms");
		dataSource.setPassword("spmsadmin");
		dataSource.setUrl("jdbc:oracle:thin:@10.128.141.111:1521:spms"); 
		dataSource.setInitialSize(5); dataSource.setMinIdle(1); 
		dataSource.setMaxActive(10); // 启用监控统计功能  
		dataSource.setFilters("stat");// for oracle  
		dataSource.setPoolPreparedStatements(false);
		dataSource.setValidationQuery("SELECT 1 FROM DUAL");*/
		
		//Connection cn = dataSource.getConnection();
		
		
		
		return "home";
	}
	
	/**
	 * 启动获取Access_Token Job
	 * @throws SchedulerException
	 */
	@SuppressWarnings("deprecation")
	public static void schedulerRun() throws SchedulerException{
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
