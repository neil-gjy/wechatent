package com.tpitc.wechatent.menu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tpitc.wechatent.common.spring.BaseController;


@Controller
@RequestMapping(value = MenuController.BASE)
public class MenuController extends BaseController {
	final static String BASE = "menu";

	
	// 左菜单1
	@RequestMapping(value = "leftOne")
	public String leftOne(){
		
		return BASE + "/" + "leftOne";
	}
	
	// 中菜单1
	@RequestMapping(value = "midOne")
	public String midOne(){
		
		return BASE + "/" + "midOne";
	}
	
	// 中菜单2
	@RequestMapping(value = "midTwo")
	public String midTwo(){
		
		return BASE + "/" + "midTwo";
	}
	
	// 中菜单3
	@RequestMapping(value = "midThree")
	public String midThree(){
		
		return BASE + "/" + "midThree";
	}
	
	// 中菜单4
	@RequestMapping(value = "midFour")
	public String midFour(){
		
		return BASE + "/" + "midFour";
	}
		
	// 中菜单5
	@RequestMapping(value = "midFive")
	public String midFive(){
		
		return BASE + "/" + "midFive";
	}	
	

}
