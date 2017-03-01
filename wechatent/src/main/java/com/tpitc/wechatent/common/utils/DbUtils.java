package com.tpitc.wechatent.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * Druid 数据库连接池
 * @author Neil
 * Date: 2016-03-21
 */
public class DbUtils {
	
	
	 private static DruidDataSource dataSource = null;

	    static {
	        try {
	            /*String driver = "oracle.jdbc.driver.OracleDriver";
	            String url = "jdbc:oracle:thin:@10.128.141.111:1521:spms";
	            String user = "spms";
	            String password = "spmsadmin";*/
	            
	            String driver = "oracle.jdbc.driver.OracleDriver";
	            String url = "jdbc:oracle:thin:@10.128.5.43:1521:dbast";
	            String user = "rssys";
	            String password = "mlmgwlwl";

	            dataSource = new DruidDataSource();
	            dataSource.setDriverClassName(driver);
	            dataSource.setUrl(url);
	            dataSource.setUsername(user);
	            dataSource.setPassword(password);
	            dataSource.setInitialSize(0);
	            dataSource.setMinIdle(0);
	            dataSource.setMaxActive(20);
	            
	            dataSource.setMaxWait(60000);
	            dataSource.setTimeBetweenEvictionRunsMillis(60000);
	            dataSource.setMinEvictableIdleTimeMillis(25200000);
	            
	            dataSource.setValidationQuery("SELECT 1 FROM DUAL");
	            dataSource.setTestOnBorrow(false);
	            dataSource.setTestOnReturn(false);
	            dataSource.setTestWhileIdle(true);
	            
	            dataSource.setRemoveAbandoned(true);
	            dataSource.setRemoveAbandonedTimeout(1800);
	            dataSource.setLogAbandoned(true);
	            

	            dataSource.setPoolPreparedStatements(true);
	            dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public static synchronized Connection getConnection() {
	        Connection conn = null;
	        try {
	            conn = dataSource.getConnection();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return conn;
	    }
	    
	    public static void release(Connection conn,Statement st,ResultSet rs){
	    	if(rs != null){
	    		try{
	    			rs.close();
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    		
	    		rs = null;
	    	}
	    	
	    	if(st != null){
	    		try{
	    			st.close();
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    		
	    		st = null;
	    	}
	    	
	    	if(conn != null){
	    		try{
	    			conn.close();
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    		
	    		conn = null;
	    	}
	    	
	    }
	

    /** 使用配置文件构建Druid数据源. */
    /*public static final int DRUID_ORACLE_SOURCE = 0;

    *//** 使用配置文件构建Druid数据源. *//*
    public static final int DRUID_ORACLE_SOURCE2 = 1;

    *//** 使用配置文件构建Dbcp数据源. *//*
    public static final int DBCP_SOURCE = 4;
    public static String confile = "config.properties";
    public static Properties p = null;

    static {
        p = new Properties();
        InputStream inputStream = null;
        try {
            //java应用
            confile = DbUtils.class.getClassLoader().getResource("").getPath()
                    + confile;
            System.out.println(confile);
            File file = new File(confile);
            inputStream = new BufferedInputStream(new FileInputStream(file));
            p.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    *//**
     * 根据类型获取数据源
     * 
     * @param sourceType
     *            数据源类型
     * @return druid或者dbcp数据源
     * @throws Exception
     *             the exception
     *//*
    public static final DataSource getDataSource(int sourceType) throws Exception {
        DataSource dataSource = null;
        switch (sourceType) {
        case DRUID_ORACLE_SOURCE:
            dataSource = DruidDataSourceFactory.createDataSource(p);
            break;
        case DRUID_ORACLE_SOURCE2:
            dataSource = DruidDataSourceFactory.createDataSource(p);
            break;
        case DBCP_SOURCE:
            // dataSource = BasicDataSourceFactory.createDataSource(
            // MySqlConfigProperty.getInstance().getProperties());
            break;
        }
        return dataSource;
    }*/
    
    
    
}
