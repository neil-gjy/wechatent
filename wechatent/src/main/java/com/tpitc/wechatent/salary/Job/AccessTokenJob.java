package com.tpitc.wechatent.salary.Job;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.tjport.wechat.common.utils.weChatUtil;
import com.tjport.wechat.common.utils.po.AccessTokenPo;
import com.tjport.wechat.salary.controller.HomeController;

public class AccessTokenJob implements Job {

	public static AccessTokenPo token = null;
	
    public void execute(JobExecutionContext context) 
            throws JobExecutionException {
    	
        HomeController.setToken(weChatUtil.getAccessToken());//货物AccessToken
/*    	
        System.out.println("Hello, Quartz! - executing its JOB at "+ 
            new Date() + " by " + context.getTrigger().getCalendarName());*/
    }
}