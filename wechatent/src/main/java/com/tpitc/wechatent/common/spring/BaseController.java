package com.tpitc.wechatent.common.spring;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.multipart.support.StringMultipartFileEditor;

import com.tpitc.wechatent.common.propertyEditor.StringEscapeEditor;




public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder) {
		/*t* 自动转换日期类型的字段格式 */
		binder.registerCustomEditor(Date.class,
				new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		/** 防止XSS攻击 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
		
	}
}
