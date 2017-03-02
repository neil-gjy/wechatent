package com.tpitc.wechatent.salary.service;

import java.sql.SQLException;

public interface ISalaryService {

	public String getBaseSalaryInfo(String id);
	
	public String getBaseSalaryInfoByDate(String id, String date);
	
	public String getBonusInfo(String id);
	
	public String getBonusInfoByDateNew(String id, String date);
	
	public String getBonusInfoByDate(String id, String date) throws SQLException;
}
