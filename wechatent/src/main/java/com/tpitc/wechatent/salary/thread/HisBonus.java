/*package com.tjport.wechat.salary.thread;

import java.util.Date;

import com.tjport.wechat.common.utils.MessageUtil;
import com.tjport.wechat.common.utils.weChatUtil;
import com.tjport.wechat.common.utils.po.AccessTokenPo;
import com.tjport.wechat.common.utils.po.UserInfoPo;
import com.tjport.wechat.salary.controller.HomeController;

public class HisBonus implements Runnable {

	public HisBonus(String fromUserName, )
	@Override
	public void run() {
		// TODO Auto-generated method stub
		UserInfoPo userInfo = weChatUtil.getUserInfo(HomeController.token.getToken(),fromUserName);
		
		String hisBonus = salaryService.getBonusInfoByDate(userInfo.getRemark(),content);
    	
    	message = MessageUtil.initText(toUserName, fromUserName, hisBonus);
	}
}*/