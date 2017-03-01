package com.tpitc.wechatent.common.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tpitc.wechatent.common.utils.DbUtils;

/**
 * Created by JUi on 2016/8/30.
 */
public class DBJob {

    public void work() {
    	ResultSet result = null;
    	PreparedStatement pre = null;
    	Connection conn = null;
    	
    	conn = DbUtils.getConnection();
    	String sql = "SELECT 1 FROM DUAL";
    	try{
	    	pre = conn.prepareStatement(sql);
	    	result = pre.executeQuery();
    	}catch(Exception e){
        	e.printStackTrace();
        }finally{
        	System.out.println("connect is Scheduling ");
        	DbUtils.release(conn, pre, result);
        }
        
    }
}