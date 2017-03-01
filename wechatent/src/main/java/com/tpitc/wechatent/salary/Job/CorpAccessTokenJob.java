package com.tjport.wechat.salary.Job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.tjport.wechat.common.utils.CorpWeChatUtils;
import com.tjport.wechat.common.utils.po.AccessTokenPo;
import com.tjport.wechat.salary.controller.StartController;

public class CorpAccessTokenJob implements Job {

	public static AccessTokenPo token = null;
	
    public void execute(JobExecutionContext context) 
            throws JobExecutionException {
    	
        StartController.setToken(CorpWeChatUtils.getAccessToken());//货物AccessToken   	
    }
}