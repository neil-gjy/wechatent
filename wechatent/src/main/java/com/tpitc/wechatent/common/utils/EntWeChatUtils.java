package com.tpitc.wechatent.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.tpitc.wechatent.common.utils.corp.UserInfo;
import com.tpitc.wechatent.common.utils.menu.Button;
import com.tpitc.wechatent.common.utils.menu.ClickButton;
import com.tpitc.wechatent.common.utils.menu.Menu;
import com.tpitc.wechatent.common.utils.menu.ViewButton;
import com.tpitc.wechatent.common.utils.po.AccessTokenPo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 菜单
 * @author Neil
 * Date: 2016-03-18
 */

public class EntWeChatUtils {
	
	private static final String CORPID = "wxf526f4da64acb9eb";
	private static final String CORPSECRET = "94491ad6160a8f1a7c19d8014ea50765";
	private static final String AGENTID = "1";
	
	private static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=id&corpsecret=secrect";
	
	private static final String CREATE_MENU_URL = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN&agentid=AGENTID";
	
	private static final String USER_INFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";
	
	private static final String SEND_MESSAGE = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
	/**
	 * Get
	 * @param url
	 * @return
	 */
	public static JSONObject getStr(String url){
		@SuppressWarnings({ "deprecation", "resource" })
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		
		try {
			HttpResponse response = httpClient.execute(httpGet);
			
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.parseObject(result);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
		
	}
	
	/**
	 * Post
	 * @param url
	 * @param out
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static JSONObject postStr(String url, String out){
		@SuppressWarnings("resource")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		HttpPost httpPost  = new HttpPost(url);
		JSONObject jsonObject = null;
		
		
		try {
			httpPost.setEntity(new StringEntity(out,"UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			
			jsonObject = JSONObject.parseObject(result);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	/**
	 * 获取Access_Token
	 * @return
	 */
	public static AccessTokenPo getAccessToken(){
		AccessTokenPo tokenPo = new AccessTokenPo();
		
		String url = ACCESS_TOKEN_URL.replace("id", CORPID).replace("secrect", CORPSECRET);
		
		JSONObject jsonObject = getStr(url);
		if(jsonObject != null){
			tokenPo.setToken(jsonObject.getString("access_token"));
			tokenPo.setExpiresIn(jsonObject.getIntValue("expires_in"));
		}
		
		return tokenPo;
	}
	
	/**
	 * 主菜单
	 * @return Menu
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		
		ClickButton curSalaryButton = new ClickButton();
		curSalaryButton.setName("本月工资");
		curSalaryButton.setType("click");
		curSalaryButton.setKey("curSalary");
		
		ClickButton curBonusButton = new ClickButton();
		curBonusButton.setName("本月奖金");
		curBonusButton.setType("click");
		curBonusButton.setKey("curBonus");
		
		ClickButton hisSalaryButton = new ClickButton();
		hisSalaryButton.setName("历史工资");
		hisSalaryButton.setType("click");
		hisSalaryButton.setKey("hisSalary");
		
		ClickButton hisBonusButton = new ClickButton();
		hisBonusButton.setName("历史奖金");
		hisBonusButton.setType("click");
		hisBonusButton.setKey("hisBonus");
		
		
		ClickButton scanButton = new ClickButton();
		scanButton.setName("测试扫码");
		scanButton.setType("scancode_push");
		scanButton.setKey("scanCode");
		
		ClickButton locationButton = new ClickButton();
		locationButton.setName("地理位置");
		locationButton.setType("location_select");
		locationButton.setKey("location");
		
		Button salaryButton = new Button();
		salaryButton.setName("工资查询");
		salaryButton.setSub_button(new Button[]{curSalaryButton,curBonusButton, hisSalaryButton, hisBonusButton});
		
		menu.setButton(new Button[]{salaryButton,scanButton,locationButton});
		
		return menu;
	}
	
	
	public static int createMenu(String token,String menu){
		int result = 0;
		
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token).replace("AGENTID", AGENTID);
		
		JSONObject jsonOject = postStr(url, menu);
		
		if(jsonOject != null){
			result = jsonOject.getIntValue("errorcode");
		}
		
		return result;
	}
	
	
	/**
	 * 获取用户信息
	 * @param token
	 * @param openid
	 * @return
	 */
	public static UserInfo getUserInfo(String token, String openid){
		UserInfo userInfo = new UserInfo();
		
		String url = USER_INFO_URL.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
		
		JSONObject jsonObject = getStr(url);
		
		if(jsonObject != null){
			userInfo.setName(jsonObject.getString("name"));
			userInfo.setUserid(jsonObject.getString("userid"));
			userInfo.setEmail(jsonObject.getString("email"));
			userInfo.setErrorcode(jsonObject.getIntValue("errorcode"));
			userInfo.setDepartment(jsonObject.getString("department"));
			userInfo.setGender(jsonObject.getIntValue("gender"));
			userInfo.setMobile(jsonObject.getString("mobile"));
			userInfo.setStatus(jsonObject.getIntValue("status"));
			userInfo.setPosition(jsonObject.getString("position"));
			userInfo.setWeixinid(jsonObject.getString("weixinid"));
			userInfo.setAvatar(jsonObject.getString("avatar"));
			userInfo.setExtattr(jsonObject.getString("extattr"));
		}
		
		return userInfo;
		
	}
	
	
}
