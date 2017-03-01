package com.tjport.wechat.salary.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tjport.wechat.common.utils.DbUtils;
import com.tjport.wechat.salary.service.ISalaryService;

@Service("salaryService")
public class SalaryService implements ISalaryService{
	
	/***
	 * 获得基本工资信息
	 *  
	 */
	@Transactional(readOnly = true)
	@Override
	public String getBaseSalaryInfo(String id) {
		StringBuilder reContent = new StringBuilder();
		
		Connection conn = null;
	    PreparedStatement pre = null;
	    ResultSet result = null;
	    
	    conn = DbUtils.getConnection();
	    
	    String sql = "select t_ryxx.sbsbh as id, t_gzb.* " 
						+ "from rs_d_ryjb t_ryxx "
						+ "right join rs_d_gzb t_gzb  on t_ryxx.rybm = t_gzb.rybm  and t_gzb.qrbz = 'Y' and  to_char(t_gzb.rq,'yyyymm') = to_char(sysdate,'yyyymm') "
						+ "where t_ryxx.sbsbh = ? ";
        try{
		    pre = conn.prepareStatement(sql);
			
			pre.setString(1, id);
			
			result = pre.executeQuery();
			
			// 实扣 合计1 合计2  实发
			Double gzSk = 0.0, gzSum1 = 0.0, gzSum2 = 0.0;
			DecimalFormat df = new DecimalFormat("#.00");//保留两位小数
		    
			
			while(result.next()){
				 reContent.append("    本月工资\n");//加薪
				 if(result.getString("gjgz") != null && result.getDouble("gjgz") != 0){
					 reContent.append("岗级工资：" + result.getString("gjgz") + "\n");//岗级工资
					 gzSum1 += result.getDouble("gjgz");
				 }
				 ///////////////计件
				 if(result.getString("zygz") != null && result.getDouble("zygz") != 0){
					 reContent.append("作业工资：" + result.getString("zygz") + "\n");//岗级工资
					 gzSum1 += result.getDouble("zygz");
				 }
				 if(result.getString("jfgz") != null && result.getDouble("jfgz") != 0){
					 reContent.append("加发工资：" + result.getString("jfgz") + "\n");//岗级工资
					 gzSum1 += result.getDouble("jfgz");
				 }
				 if(result.getString("dx") != null && result.getDouble("dx") != 0){
					 reContent.append("底薪：" + result.getString("dx") + "\n");//岗级工资
					 gzSum1 += result.getDouble("dx");
				 }
				 if(result.getString("tgjt") != null && result.getDouble("tgjt") != 0){
					 reContent.append("特岗津贴：" + result.getString("tgjt") + "\n");//年功津贴
					 gzSum1 += result.getDouble("tgjt");
				 }
				 //////////
				 if(result.getString("ngjt") != null && result.getDouble("ngjt") != 0){
					 reContent.append("年功津贴：" + result.getString("ngjt") + "\n");//年功津贴
					 gzSum1 += result.getDouble("ngjt");
				 }
				 if(result.getString("zcjt") != null && result.getDouble("zcjt") != 0){
					 reContent.append("职称津贴：" + result.getString("zcjt") + "\n");//职称津贴
					 gzSum1 += result.getDouble("zcjt");
				 }
				 if(result.getString("jsjt") != null && result.getDouble("jsjt") != 0){
					 reContent.append("技师津贴：" + result.getString("jsjt") + "\n");//职称津贴
					 gzSum1 += result.getDouble("jsjt");
				 }
				 if(result.getString("dzzjt") != null && result.getDouble("dzzjt") != 0){
					 reContent.append("队站长津贴：" + result.getString("dzzjt") + "\n");//职称津贴
					 gzSum1 += result.getDouble("dzzjt");
				 }
				 if(result.getString("bljjgz") != null && result.getDouble("bljjgz") != 0){
					 reContent.append("保留晋级工资：" + result.getString("bljjgz") + "\n");//职称津贴
					 gzSum1 += result.getDouble("bljjgz");
				 }
				 if(result.getString("blgz") != null && result.getDouble("blgz") != 0){
					 reContent.append("保留工资：" + result.getString("blgz") + "\n");//职称津贴
					 gzSum1 += result.getDouble("blgz");
				 }
				 if(result.getString("jsgz") != null && result.getDouble("jsgz") != 0){
					 reContent.append("计时工资：" + result.getString("jsgz") + "\n");//职称津贴
					 gzSum1 += result.getDouble("jsgz");
				 }
				 if(result.getString("hmbt") != null && result.getDouble("hmbt") != 0){
					 reContent.append("回民补贴：" + result.getString("hmbt") + "\n");//职称津贴
					 gzSum1 += result.getDouble("hmbt");
				 }
				 if(result.getString("djc") != null && result.getDouble("djc") != 0){
					 reContent.append("倒级差：" + result.getString("djc") + "\n");//职称津贴
					 gzSum1 += result.getDouble("djc");
				 }
				 if(result.getString("bzzjt") != null && result.getDouble("bzzjt") != 0){
					 reContent.append("班组长津贴：" + result.getString("bzzjt") + "\n");//职称津贴
					 gzSum1 += result.getDouble("bzzjt");
				 }
				 if(result.getString("jbf") != null && result.getDouble("jbf") != 0){
					 reContent.append("加班费：" + result.getString("jbf") + "\n");//财务补贴
					 gzSum1 += result.getDouble("jbf");
				 }
				 if(result.getString("ybjt") != null && result.getDouble("ybjt") != 0){
					 reContent.append("夜班津贴：" + result.getString("ybjt") + "\n");//实发工资
					 gzSum1 += result.getDouble("ybjt");
				 }
				 if(result.getString("bfgz") != null && result.getDouble("bfgz") != 0){
					 reContent.append("补发工资：" + result.getString("bfgz") + "\n");//实发工资
					 gzSum1 += result.getDouble("bfgz");
				 }
				 if(result.getString("sj") != null && result.getDouble("sj") != 0){
					 reContent.append("事假：" + result.getString("sj") + "\n");//实发工资
				 }
				 if(result.getString("kg") != null && result.getDouble("kg") != 0){
					 reContent.append("旷工：" + result.getString("kg") + "\n");//实发工资
				 }
				 if(result.getString("bj") != null && result.getDouble("bj") != 0){
					 reContent.append("病假：" + result.getString("bj") + "\n");//实发工资
				 }
				 if(result.getString("qtj") != null && result.getDouble("qtj") != 0){
					 reContent.append("其他假：" + result.getString("qtj") + "\n");//实发工资
				 }
				 if(result.getString("ykgz") != null && result.getDouble("ykgz") != 0){
					 reContent.append("应扣工资：" + result.getString("ykgz") + "\n");//实发工资
					 gzSum1 -= result.getDouble("ykgz");
				 }
				 if(result.getString("yfgz") != null && result.getDouble("yfgz") != 0){
					 reContent.append("应发工资：" + result.getString("yfgz") + "\n");//实发工资
				 }
				 if(result.getString("scj") != null && result.getDouble("scj") != 0){
					 reContent.append("月奖：" + result.getString("scj") + "\n");//实发工资
					 gzSum1 += result.getDouble("scj");
				 }
				 
				 if(result.getString("yybx") != null && result.getDouble("yybx") != 0){
					 reContent.append("医疗保险：" + result.getString("yybx") + "\n");//实发工资
					 gzSk += result.getDouble("yybx");
					 gzSum1 -= result.getDouble("yybx");
				 }
				 if(result.getString("gjj") != null && result.getDouble("gjj") != 0){
					 reContent.append("公积金：" + result.getString("gjj") + "\n");//实发工资
					 gzSk += result.getDouble("gjj");
					 gzSum1 -= result.getDouble("gjj");
				 }
				 if(result.getString("ylbx") != null && result.getDouble("ylbx") != 0){
					 reContent.append("养老保险：" + result.getString("ylbx") + "\n");//实发工资
					 gzSk += result.getDouble("ylbx");
					 gzSum1 -= result.getDouble("ylbx");
				 }
				 if(result.getString("sybx") != null && result.getDouble("sybx") != 0){
					 reContent.append("失业保险：" + result.getString("sybx") + "\n");//实发工资
					 gzSk += result.getDouble("sybx");
					 gzSum1 -= result.getDouble("sybx");
				 }
				 if(result.getString("qynj") != null && result.getDouble("qynj") != 0){
					 reContent.append("企业年金：" + result.getString("qynj") + "\n");//实发工资
					 gzSk += result.getDouble("qynj");
					 gzSum1 -= result.getDouble("qynj");
				 }
				 if(result.getString("grtj") != null && result.getDouble("grtj") != 0){
					 reContent.append("个调税：" + result.getString("grtj") + "\n");//实发工资
					 gzSk += result.getDouble("grtj");
					 gzSum1 -= result.getDouble("grtj");
				 }
				 if(result.getString("deyljz") != null && result.getDouble("deyljz") != 0){
					 reContent.append("大额医疗：" + result.getString("deyljz") + "\n");//实发工资
					 gzSk += result.getDouble("deyljz");
					 gzSum1 -= result.getDouble("deyljz");
				 }
				 
				 reContent.append("实扣：" + df.format(gzSk).toString() +  "\n");
				 reContent.append("合计：" + df.format(gzSum1).toString() +  "\n\n");
				 
				 // 工资条二
				 if(result.getString("cnbt") != null && result.getDouble("cnbt") != 0){
					 reContent.append("采暖补贴：" + result.getString("cnbt") + "\n");//实发工资
					 gzSum2 += result.getDouble("cnbt");
				 }
				 if(result.getString("dszn") != null && result.getDouble("dszn") != 0){
					 reContent.append("独子费：" + result.getString("dszn") + "\n");//实发工资
					 gzSum2 += result.getDouble("dszn");
				 }
				 if(result.getString("txf") != null && result.getDouble("txf") != 0){
					 reContent.append("电话费：" + result.getString("txf") + "\n");//实发工资
					 gzSum2 += result.getDouble("txf");
				 }
				 if(result.getString("bcgjj") != null && result.getDouble("bcgjj") != 0){
					 reContent.append("公积金补贴：" + result.getString("bcgjj") + "\n");//实发工资
					 gzSum2 += result.getDouble("bcgjj");
				 }
				 if(result.getString("gjjbt2") != null && result.getDouble("gjjbt2") != 0){
					 reContent.append("公积金补贴2：" + result.getString("gjjbt2") + "\n");//实发工资
					 gzSum2 += result.getDouble("gjjbt2");
				 }
				 if(result.getString("dkfz") != null && result.getDouble("dkfz") != 0){
					 reContent.append("代扣房租：" + result.getString("dkfz") + "\n");//实发工资
					 gzSum2 -= result.getDouble("dkfz");
				 }
				 if(result.getString("fwbt") != null && result.getDouble("fwbt") != 0){
					 reContent.append("房屋补贴：" + result.getString("fwbt") + "\n");//实发工资
					 gzSum2 += result.getDouble("fwbt");
				 }
				 if(result.getString("cwbt") != null && result.getDouble("cwbt") != 0){
					 reContent.append("财务补贴：" + result.getString("cwbt") + "\n");//实发工资
					 gzSum2 += result.getDouble("cwbt");
				 }
				 if(result.getString("cwkk") != null && result.getDouble("cwkk") != 0){
					 reContent.append("财务扣款：" + result.getString("cwkk") + "\n");//实发工资
					 gzSum2 -= result.getDouble("cwkk");
				 }
				 
				 reContent.append("合计：" + df.format(gzSum2).toString() +  "\n");
				 if(result.getString("sf") != null && result.getDouble("sf") != 0){
					 reContent.append("实发：" + result.getString("sf") + "\n");//实发工资
				 }
				 
				 
			}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	DbUtils.release(conn, pre, result);
        }
        
		
		return reContent.toString();
	}
	
	
	/***
	 * 获得历史基本工资信息
	 *  
	 */
	@Transactional(readOnly = true)
	@Override
	public String getBaseSalaryInfoByDate(String id, String date) {
		StringBuilder reContent = new StringBuilder();
		
		String yearOrMonth = "";
		if(date.length() == 4){
			yearOrMonth = "yyyy";
			reContent.append( "    " + date + "各月工资情况\n");//加薪
		}
		else if(date.length() == 6){
			yearOrMonth = "yyyymm";
			String year = date.substring(0,4);
			String month = date.substring(4,6);
			reContent.append( "    " + year + "年 " + month + "月工资\n");//加薪
		}
		
		Connection conn = null;
	    PreparedStatement pre = null;
	    ResultSet result = null;
	    
	    conn = DbUtils.getConnection();
	    
	    String sql = "select t_ryxx.sbsbh as id, t_gzb.* " 
						+ "from rs_d_ryjb t_ryxx "
						+ "right join wx_t_salary_his_mmt t_gzb  on t_ryxx.rybm = t_gzb.rybm  and t_gzb.qrbz = 'Y' and  to_char(t_gzb.rq,'"+ yearOrMonth + "') = ? "
						+ "where t_ryxx.sbsbh = ?";
        try{
		    pre = conn.prepareStatement(sql);
			
			pre.setString(1, date);
			pre.setString(2, id);
			
			result = pre.executeQuery();
			
			// 获取当前时间
			//Date curDate = new Date();
			
			// 实扣 合计1 合计2  实发
			Double gzSk = 0.0, gzSum1 = 0.0, gzSum2 = 0.0;
			DecimalFormat df = new DecimalFormat("#.00");//保留两位小数
			
			while(result.next()){
				if(date.length() == 4){
					 if(result.getString("rq") != null){
						 reContent.append(result.getString("rq").substring(5, 7) + "月\n");//加薪
					 }
				 }
				else{
					if(result.getString("gjgz") != null && result.getDouble("gjgz") != 0){
						 reContent.append("岗级工资：" + result.getString("gjgz") + "\n");//岗级工资
						 gzSum1 += result.getDouble("gjgz");
					 }
					///////////////计件
					 if(result.getString("zygz") != null && result.getDouble("zygz") != 0){
						 reContent.append("作业工资：" + result.getString("zygz") + "\n");//岗级工资
						 gzSum1 += result.getDouble("zygz");
					 }
					 if(result.getString("jfgz") != null && result.getDouble("jfgz") != 0){
						 reContent.append("加发工资：" + result.getString("jfgz") + "\n");//岗级工资
						 gzSum1 += result.getDouble("jfgz");
					 }
					 if(result.getString("dx") != null && result.getDouble("dx") != 0){
						 reContent.append("底薪：" + result.getString("dx") + "\n");//岗级工资
						 gzSum1 += result.getDouble("dx");
					 }
					 if(result.getString("tgjt") != null && result.getDouble("tgjt") != 0){
						 reContent.append("特岗津贴：" + result.getString("tgjt") + "\n");//年功津贴
						 gzSum1 += result.getDouble("tgjt");
					 }
					 //////////
					 if(result.getString("ngjt") != null && result.getDouble("ngjt") != 0){
						 reContent.append("年功津贴：" + result.getString("ngjt") + "\n");//年功津贴
						 gzSum1 += result.getDouble("ngjt");
					 }
					 if(result.getString("zcjt") != null && result.getDouble("zcjt") != 0){
						 reContent.append("职称津贴：" + result.getString("zcjt") + "\n");//职称津贴
						 gzSum1 += result.getDouble("zcjt");
					 }
					 if(result.getString("jsjt") != null && result.getDouble("jsjt") != 0){
						 reContent.append("技师津贴：" + result.getString("jsjt") + "\n");//职称津贴
						 gzSum1 += result.getDouble("jsjt");
					 }
					 if(result.getString("dzzjt") != null && result.getDouble("dzzjt") != 0){
						 reContent.append("队站长津贴：" + result.getString("dzzjt") + "\n");//职称津贴
						 gzSum1 += result.getDouble("dzzjt");
					 }
					 if(result.getString("bljjgz") != null && result.getDouble("bljjgz") != 0){
						 reContent.append("保留晋级工资：" + result.getString("bljjgz") + "\n");//职称津贴
						 gzSum1 += result.getDouble("bljjgz");
					 }
					 if(result.getString("blgz") != null && result.getDouble("blgz") != 0){
						 reContent.append("保留工资：" + result.getString("blgz") + "\n");//职称津贴
						 gzSum1 += result.getDouble("blgz");
					 }
					 if(result.getString("jsgz") != null && result.getDouble("jsgz") != 0){
						 reContent.append("计时工资：" + result.getString("jsgz") + "\n");//职称津贴
						 gzSum1 += result.getDouble("jsgz");
					 }
					 if(result.getString("hmbt") != null && result.getDouble("hmbt") != 0){
						 reContent.append("回民补贴：" + result.getString("hmbt") + "\n");//职称津贴
						 gzSum1 += result.getDouble("hmbt");
					 }
					 if(result.getString("djc") != null && result.getDouble("djc") != 0){
						 reContent.append("倒级差：" + result.getString("djc") + "\n");//职称津贴
						 gzSum1 += result.getDouble("djc");
					 }
					 if(result.getString("bzzjt") != null && result.getDouble("bzzjt") != 0){
						 reContent.append("班组长津贴：" + result.getString("bzzjt") + "\n");//职称津贴
						 gzSum1 += result.getDouble("bzzjt");
					 }
					 if(result.getString("jbf") != null && result.getDouble("jbf") != 0){
						 reContent.append("加班费：" + result.getString("jbf") + "\n");//财务补贴
						 gzSum1 += result.getDouble("jbf");
					 }
					 if(result.getString("ybjt") != null && result.getDouble("ybjt") != 0){
						 reContent.append("夜班津贴：" + result.getString("ybjt") + "\n");//实发工资
						 gzSum1 += result.getDouble("ybjt");
					 }
					 if(result.getString("bfgz") != null && result.getDouble("bfgz") != 0){
						 reContent.append("补发工资：" + result.getString("bfgz") + "\n");//实发工资
						 gzSum1 += result.getDouble("bfgz");
					 }
					 if(result.getString("sj") != null && result.getDouble("sj") != 0){
						 reContent.append("事假：" + result.getString("sj") + "\n");//实发工资
					 }
					 if(result.getString("kg") != null && result.getDouble("kg") != 0){
						 reContent.append("旷工：" + result.getString("kg") + "\n");//实发工资
					 }
					 if(result.getString("bj") != null && result.getDouble("bj") != 0){
						 reContent.append("病假：" + result.getString("bj") + "\n");//实发工资
					 }
					 if(result.getString("qtj") != null && result.getDouble("qtj") != 0){
						 reContent.append("其他假：" + result.getString("qtj") + "\n");//实发工资
					 }
					 if(result.getString("ykgz") != null && result.getDouble("ykgz") != 0){
						 reContent.append("应扣工资：" + result.getString("ykgz") + "\n");//实发工资
						 gzSum1 -= result.getDouble("ykgz");
					 }
					 if(result.getString("bkgz") != null && result.getDouble("bkgz") != 0){
						 reContent.append("补扣工资：" + result.getString("bkgz") + "\n");//实发工资
						 gzSum1 -= result.getDouble("bkgz");
					 }
					 if(result.getString("yfgz") != null && result.getDouble("yfgz") != 0){
						 reContent.append("应发工资：" + result.getString("yfgz") + "\n");//实发工资
					 }
					 if(result.getString("scj") != null && result.getDouble("scj") != 0){
						 reContent.append("月奖：" + result.getString("scj") + "\n");//实发工资
						 gzSum1 += result.getDouble("scj");
					 }
					 
					 if(result.getString("yybx") != null && result.getDouble("yybx") != 0){
						 reContent.append("医疗保险：" + result.getString("yybx") + "\n");//实发工资
						 gzSk += result.getDouble("yybx");
						 gzSum1 -= result.getDouble("yybx");
					 }
					 if(result.getString("gjj") != null && result.getDouble("gjj") != 0){
						 reContent.append("公积金：" + result.getString("gjj") + "\n");//实发工资
						 gzSk += result.getDouble("gjj");
						 gzSum1 -= result.getDouble("gjj");
					 }
					 if(result.getString("ylbx") != null && result.getDouble("ylbx") != 0){
						 reContent.append("养老保险：" + result.getString("ylbx") + "\n");//实发工资
						 gzSk += result.getDouble("ylbx");
						 gzSum1 -= result.getDouble("ylbx");
					 }
					 if(result.getString("sybx") != null && result.getDouble("sybx") != 0){
						 reContent.append("失业保险：" + result.getString("sybx") + "\n");//实发工资
						 gzSk += result.getDouble("sybx");
						 gzSum1 -= result.getDouble("sybx");
					 }
					 if(result.getString("qynj") != null && result.getDouble("qynj") != 0){
						 reContent.append("企业年金：" + result.getString("qynj") + "\n");//实发工资
						 gzSk += result.getDouble("qynj");
						 gzSum1 -= result.getDouble("qynj");
					 }
					 if(result.getString("grtj") != null && result.getDouble("grtj") != 0){
						 reContent.append("个调税：" + result.getString("grtj") + "\n");//实发工资
						 gzSk += result.getDouble("grtj");
						 gzSum1 -= result.getDouble("grtj");
					 }
					 if(result.getString("deyljz") != null && result.getDouble("deyljz") != 0){
						 reContent.append("大额医疗：" + result.getString("deyljz") + "\n");//实发工资
						 gzSk += result.getDouble("deyljz");
						 gzSum1 -= result.getDouble("deyljz");
					 }
					 
					 reContent.append("实扣：" + df.format(gzSk).toString() +  "\n");
					 reContent.append("合计：" + df.format(gzSum1).toString() +  "\n\n");
					 
					 // 工资条二
					 if(result.getString("cnbt") != null && result.getDouble("cnbt") != 0){
						 reContent.append("采暖补贴：" + result.getString("cnbt") + "\n");//实发工资
						 gzSum2 += result.getDouble("cnbt");
					 }
					 if(result.getString("dszn") != null && result.getDouble("dszn") != 0){
						 reContent.append("独子费：" + result.getString("dszn") + "\n");//实发工资
						 gzSum2 += result.getDouble("dszn");
					 }
					 if(result.getString("txf") != null && result.getDouble("txf") != 0){
						 reContent.append("电话费：" + result.getString("txf") + "\n");//实发工资
						 gzSum2 += result.getDouble("txf");
					 }
					 if(result.getString("bcgjj") != null && result.getDouble("bcgjj") != 0){
						 reContent.append("公积金补贴：" + result.getString("bcgjj") + "\n");//实发工资
						 gzSum2 += result.getDouble("bcgjj");
					 }
					 if(result.getString("gjjbt2") != null && result.getDouble("gjjbt2") != 0){
						 reContent.append("公积金补贴2：" + result.getString("gjjbt2") + "\n");//实发工资
						 gzSum2 += result.getDouble("gjjbt2");
					 }
					 if(result.getString("dkfz") != null && result.getDouble("dkfz") != 0){
						 reContent.append("代扣房租：" + result.getString("dkfz") + "\n");//实发工资
						 gzSum2 -= result.getDouble("dkfz");
					 }
					 if(result.getString("fwbt") != null && result.getDouble("fwbt") != 0){
						 reContent.append("房屋补贴：" + result.getString("fwbt") + "\n");//实发工资
						 gzSum2 += result.getDouble("fwbt");
					 }
					 if(result.getString("cwbt") != null && result.getDouble("cwbt") != 0){
						 reContent.append("财务补贴：" + result.getString("cwbt") + "\n");//实发工资
						 gzSum2 += result.getDouble("cwbt");
					 }
					 if(result.getString("cwkk") != null && result.getDouble("cwkk") != 0){
						 reContent.append("财务扣款：" + result.getString("cwkk") + "\n");//实发工资
						 gzSum2 -= result.getDouble("cwkk");
					 }
					 
					 reContent.append("合计：" + df.format(gzSum2).toString() +  "\n");
				 }
				 if(result.getString("sf") != null && result.getDouble("sf") != 0){
					 reContent.append("实发：" + result.getString("sf") + "\n");//实发工资
				 }
				 
				 
			}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	DbUtils.release(conn, pre, result);
        }
        
		
		return reContent.toString();
	}

	/***
	 * 获取奖金信息
	 * @throws SQLException 
	 */
	@Transactional(readOnly = true)
	@Override
	public String getBonusInfo(String id) {
		// TODO Auto-generated method stub
		
		StringBuilder reContent = new StringBuilder();
		
		Connection conn = null;
	    PreparedStatement pre = null;
	    ResultSet result = null;
	    
	    try{
		    conn = DbUtils.getConnection();
	
		    String sql = "select t_ryxx.sbsbh as id, t_jx_gs.je as jx_gs,t_ycx_gs.je as ycx_gs , t_glb.je as glb, t_scj.je as scj, t_jnj.je as jnj, t_jx.je as jx, t_ycx.je as ycx,"
				         + "t_bf.je as bf,t_bldcj.je as bldcj,t_bzjsj.je as bzjsj, t_aqj.je as aqj,t_hlhjyj.je as hlhjyj, t_khfuzlj.je as khfuzlj, t_kjjbj.je as kjjbj,  t_ldjsj.je as  ldjsj,  t_lwf.je as  lwf, t_sqj.je as sqj,"
				         + "t_sqjjy.je as sqjjy, t_wmdwj.je as wmdwj,t_yhstrcj.je as yhstrcj, t_cnf.je as cnf, t_cbdtdxj.je as cbdtdxj,t_jjzrj.je as jjzrj "
		            + "from rs_d_ryjb t_ryxx "
		            + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from rs_d_jj_jx_gs group by rybm,to_char(rq,'yyyymm'),qrbz) t_jx_gs on t_ryxx.rybm = t_jx_gs.rybm and t_jx_gs.qrbz = 'Y' and   t_jx_gs.rq = to_char(sysdate,'yyyymm') "
		            + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_ycx_gs group by rybm,to_char(rq,'yyyymm'),qrbz) t_ycx_gs on t_ryxx.rybm = t_ycx_gs.rybm  and t_ycx_gs.qrbz = 'Y' and  t_ycx_gs.rq = to_char(sysdate,'yyyymm') " 
		            + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_glb group by rybm,to_char(rq,'yyyymm'),qrbz) t_glb on t_ryxx.rybm = t_glb.rybm  and t_glb.qrbz = 'Y' and   t_glb.rq = to_char(sysdate,'yyyymm') " 
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_scj group by rybm,to_char(rq,'yyyymm'),qrbz) t_scj on t_ryxx.rybm = t_scj.rybm  and t_scj.qrbz = 'Y' and   t_scj.rq = to_char(sysdate,'yyyymm') " 
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_jnj group by rybm,to_char(rq,'yyyymm'),qrbz) t_jnj on t_ryxx.rybm = t_jnj.rybm  and t_jnj.qrbz = 'Y' and  t_jnj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_jx group by rybm,to_char(rq,'yyyymm'),qrbz) t_jx on t_ryxx.rybm = t_jx.rybm  and t_jx.qrbz = 'Y' and   t_jx.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_ycx group by rybm,to_char(rq,'yyyymm'),qrbz) t_ycx on t_ryxx.rybm = t_ycx.rybm  and t_ycx.qrbz = 'Y' and  t_ycx.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_bf group by rybm,to_char(rq,'yyyymm'),qrbz) t_bf on t_ryxx.rybm = t_bf.rybm  and t_bf.qrbz = 'Y' and  t_bf.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_bldcj group by rybm,to_char(rq,'yyyymm'),qrbz) t_bldcj on t_ryxx.rybm = t_bldcj.rybm  and t_bldcj.qrbz = 'Y' and  t_bldcj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_bzjsj group by rybm,to_char(rq,'yyyymm'),qrbz) t_bzjsj on t_ryxx.rybm = t_bzjsj.rybm  and t_bzjsj.qrbz = 'Y' and  t_bzjsj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_aqj group by rybm,to_char(rq,'yyyymm'),qrbz) t_aqj on t_ryxx.rybm = t_aqj.rybm  and t_aqj.qrbz = 'Y' and  t_aqj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_hlhjyj group by rybm,to_char(rq,'yyyymm'),qrbz) t_hlhjyj on t_ryxx.rybm = t_hlhjyj.rybm  and t_hlhjyj.qrbz = 'Y' and  t_hlhjyj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_khfuzlj group by rybm,to_char(rq,'yyyymm'),qrbz) t_khfuzlj on t_ryxx.rybm = t_khfuzlj.rybm  and t_khfuzlj.qrbz = 'Y' and  t_khfuzlj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_kjjbj group by rybm,to_char(rq,'yyyymm'),qrbz) t_kjjbj on t_ryxx.rybm = t_kjjbj.rybm  and t_kjjbj.qrbz = 'Y' and  t_kjjbj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_ldjsj group by rybm,to_char(rq,'yyyymm'),qrbz) t_ldjsj on t_ryxx.rybm = t_ldjsj.rybm  and t_ldjsj.qrbz = 'Y' and   t_ldjsj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_lwf group by rybm,to_char(rq,'yyyymm'),qrbz) t_lwf on t_ryxx.rybm = t_lwf.rybm  and t_lwf.qrbz = 'Y' and  t_lwf.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_sqj group by rybm,to_char(rq,'yyyymm'),qrbz) t_sqj on t_ryxx.rybm = t_sqj.rybm  and t_sqj.qrbz = 'Y' and  t_sqj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_sqjjy group by rybm,to_char(rq,'yyyymm'),qrbz) t_sqjjy on t_ryxx.rybm = t_sqjjy.rybm  and t_sqjjy.qrbz = 'Y' and  t_sqjjy.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_yhstrcj group by rybm,to_char(rq,'yyyymm'),qrbz) t_yhstrcj on t_ryxx.rybm = t_yhstrcj.rybm  and t_yhstrcj.qrbz = 'Y' and   t_yhstrcj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_wmdwj group by rybm,to_char(rq,'yyyymm'),qrbz) t_wmdwj on t_ryxx.rybm = t_wmdwj.rybm  and t_wmdwj.qrbz = 'Y' and   t_wmdwj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_cnf group by rybm,to_char(rq,'yyyymm'),qrbz) t_cnf on t_ryxx.rybm = t_cnf.rybm  and t_cnf.qrbz = 'Y' and  t_cnf.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_cbdtdxj group by rybm,to_char(rq,'yyyymm'),qrbz) t_cbdtdxj on t_ryxx.rybm = t_cbdtdxj.rybm  and t_cbdtdxj.qrbz = 'Y' and  t_cbdtdxj.rq = to_char(sysdate,'yyyymm') "
	                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_jjzrj group by rybm,to_char(rq,'yyyymm'),qrbz) t_jjzrj on t_ryxx.rybm = t_jjzrj.rybm  and t_jjzrj.qrbz = 'Y' and  t_jjzrj.rq = to_char(sysdate,'yyyymm') "
	                + "where t_ryxx.sbsbh = ?";
			pre = conn.prepareStatement(sql);
			
			pre.setString(1, id);
			
			result = pre.executeQuery();
			
			// 获取当前时间
			//Date curDate = new Date();
		    
			
			while(result.next()){
				 reContent.append("    本月奖金\n");//加薪
				 if(result.getString("jx_gs") != null){
					 reContent.append("加薪：" + result.getString("jx_gs") + "\n");//加薪
				 }
				 if(result.getString("ycx_gs") != null){
					 reContent.append("一次性：" + result.getString("ycx_gs") + "\n");//一次性
				 }
				 if(result.getString("glb") != null){
					 reContent.append("管理杯：" + result.getString("glb") + "\n");//管理呗
				 }
				 if(result.getString("scj") != null){
					 reContent.append("生产奖：" + result.getString("scj") + "\n");//生产奖
				 }
				 if(result.getString("jnj") != null){
					 reContent.append("节能奖：" + result.getString("jnj") + "\n");//节能奖
				 }
				 if(result.getString("ycx") != null){
					 reContent.append("一次性：" + result.getString("ycx") + "\n");//局一次性
				 }
				 if(result.getString("jx") != null){
					 reContent.append("加薪：" + result.getString("jx") + "\n");//局加薪
				 }
				 if(result.getString("bf") != null){
					 reContent.append("加发奖金：" + result.getString("bf") + "\n");//加发奖金
				 }
				 if(result.getString("bldcj") != null){
					 reContent.append("班轮单船奖：" + result.getString("bldcj") + "\n");//生产奖
				 }
				 if(result.getString("bzjsj") != null){
					 reContent.append("班组建设奖：" + result.getString("bzjsj") + "\n");//班组建设奖
				 }
				 if(result.getString("hlhjyj") != null){
					 reContent.append("合理化建议奖：" + result.getString("hlhjyj") + "\n");//生产奖
				 }
				 if(result.getString("jnj") != null){
					 reContent.append("节能奖：" + result.getString("jnj") + "\n");//生产奖
				 }
				 if(result.getString("khfuzlj") != null){
					 reContent.append("客货服务质量奖：" + result.getString("khfuzlj") + "\n");//生产奖
				 }
				 if(result.getString("scj") != null){
					 reContent.append("科技进步奖：" + result.getString("kjjbj") + "\n");//生产奖
				 }
				 if(result.getString("ldjsj") != null){
					 reContent.append("劳动竞赛奖：" + result.getString("ldjsj") + "\n");//生产奖
				 }
				 if(result.getString("lwf") != null){
					 reContent.append("劳务费：" + result.getString("lwf") + "\n");//生产奖
				 }
				 if(result.getString("sqj") != null){
					 reContent.append("速遣奖：" + result.getString("sqj") + "\n");//生产奖
				 }
				 if(result.getString("sqjjy") != null){
					 reContent.append("速遣奖结余：" + result.getString("sqjjy") + "\n");//生产奖
				 }
				 if(result.getString("yhstrcj") != null){
					 reContent.append("特殊引航收入奖：" + result.getString("yhstrcj") + "\n");//生产奖
				 }
				 if(result.getString("wmdwj") != null){
					 reContent.append("文明单位奖：" + result.getString("wmdwj") + "\n");//生产奖
				 }
				 if(result.getString("cnf") != null){
					 reContent.append("采暖费：" + result.getString("cnf") + "\n");//生产奖
				 }
				 if(result.getString("cbdtdxj") != null){
					 reContent.append("引航员船舶动态兑现：" + result.getString("cbdtdxj") + "\n");//生产奖
				 }
	
			}
	    }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	DbUtils.release(conn, pre, result);
        }
	    
		
		return reContent.toString();
	}
	
	/**
	 * 根据日期获取对应时间工资信息
	 * id 身份证号
	 * date 日期
	 */
	@Transactional(readOnly = true)
	@Override
	public String getBonusInfoByDateNew(String id, String date){
		StringBuilder reContent = new StringBuilder();
		
		String yearOrMonth = "";
		if(date.length() == 4){
			yearOrMonth = "yyyy";
			reContent.append( "    " + date + "年各项奖金情况\n");//加薪
		}
		else if(date.length() == 6){
			yearOrMonth = "yyyymm";
			String year = date.substring(0,4);
			String month = date.substring(4,6);
			reContent.append( "    " + year + "年 " + month + "月奖金\n");//加薪
		}
		
		// TODO Auto-generated method stub
		Connection conn = null;
	    PreparedStatement pre = null;
	    ResultSet result = null;
	    
	    try{
		    conn = DbUtils.getConnection();
		    /*String sql = "select sum(jx_gs) as jx_gs, sum(ycx_gs) as ycx_gs, sum(glb) as glb, sum(scj) as scj, sum(jnj) as jnj, sum(jx) as jx, sum(ycx) as ycx,"
	          + "sum(bf) as bf,sum(bldcj) as bldcj,sum(bzjsj) as bzjsj, sum(aqj) as aqj,sum(hlhjyj) as hlhjyj, sum(khfuzlj) as khfuzlj, sum(kjjbj) as kjjbj,"
	          + "sum(ldjsj) as ldjsj,  sum(lwf) as lwf, sum(sqj) as sqj,"
	          + "sum(sqjjy) as sqjjy , sum(wmdwj) as wmdwj,sum(yhstrcj) as yhstrcj, sum(cnf) as cnf, sum(cbdtdxj) as cbdtdxj,sum(jjzrj) as jjzrj"
		      + " from wx_t_bonus_his where id = ? and to_char(create_time,'"
	          + yearOrMonth + "') = ?" ;*/
		    
		    String sql = "select *"
				      + " from wx_t_bonus_his_mmt where id = ? and to_char(create_time,'"
			          + yearOrMonth + "') = ? order by create_time asc" ;
	           
			pre = conn.prepareStatement(sql);
			
			pre.setString(1, id);
			pre.setString(2, date);
			
			
			result = pre.executeQuery();
			
			// 获取当前时间
			//Date curDate = new Date();
			
			while(result.next()){
				 if(date.length() == 4){
					 if(result.getString("create_time") != null){
						 reContent.append(result.getString("create_time").substring(5, 7) + "月\n");//加薪
					 }
				 }
				 if(result.getString("jx_gs") != null){
					 reContent.append("加薪：" + result.getString("jx_gs") + "\n");//加薪
				 }
				 if(result.getString("ycx_gs") != null){
					 reContent.append("一次性：" + result.getString("ycx_gs") + "\n");//一次性
				 }
				 if(result.getString("glb") != null){
					 reContent.append("管理杯：" + result.getString("glb") + "\n");//管理呗
				 }
				 if(result.getString("scj") != null){
					 reContent.append("生产奖：" + result.getString("scj") + "\n");//生产奖
				 }
				 if(result.getString("jnj") != null){
					 reContent.append("节能奖：" + result.getString("jnj") + "\n");//节能奖
				 }
				 if(result.getString("ycx") != null){
					 reContent.append("一次性：" + result.getString("ycx") + "\n");//局一次性
				 }
				 if(result.getString("jx") != null){
					 reContent.append("加薪：" + result.getString("jx") + "\n");//局加薪
				 }
				 if(result.getString("bf") != null){
					 reContent.append("加发奖金：" + result.getString("bf") + "\n");//加发奖金
				 }
				 if(result.getString("bldcj") != null){
					 reContent.append("班轮单船奖：" + result.getString("bldcj") + "\n");//生产奖
				 }
				 if(result.getString("bzjsj") != null){
					 reContent.append("班组建设奖：" + result.getString("bzjsj") + "\n");//班组建设奖
				 }
				 if(result.getString("hlhjyj") != null){
					 reContent.append("合理化建议奖：" + result.getString("hlhjyj") + "\n");//生产奖
				 }
				 if(result.getString("jnj") != null){
					 reContent.append("节能奖：" + result.getString("jnj") + "\n");//生产奖
				 }
				 if(result.getString("khfuzlj") != null){
					 reContent.append("客货服务质量奖：" + result.getString("khfuzlj") + "\n");//生产奖
				 }
				 if(result.getString("kjjbj") != null){
					 reContent.append("科技进步奖：" + result.getString("kjjbj") + "\n");//生产奖
				 }
				 if(result.getString("ldjsj") != null){
					 reContent.append("劳动竞赛奖：" + result.getString("ldjsj") + "\n");//生产奖
				 }
				 if(result.getString("lwf") != null){
					 reContent.append("劳务费：" + result.getString("lwf") + "\n");//生产奖
				 }
				 if(result.getString("sqj") != null){
					 reContent.append("速遣奖：" + result.getString("sqj") + "\n");//生产奖
				 }
				 if(result.getString("sqjjy") != null){
					 reContent.append("速遣奖结余：" + result.getString("sqjjy") + "\n");//生产奖
				 }
				 if(result.getString("yhstrcj") != null){
					 reContent.append("特殊引航收入奖：" + result.getString("yhstrcj") + "\n");//生产奖
				 }
				 if(result.getString("wmdwj") != null){
					 reContent.append("文明单位奖：" + result.getString("wmdwj") + "\n");//生产奖
				 }
				 if(result.getString("cnf") != null){
					 reContent.append("采暖费：" + result.getString("cnf") + "\n");//生产奖
				 }
				 if(result.getString("cbdtdxj") != null){
					 reContent.append("引航员船舶动态兑现：" + result.getString("cbdtdxj") + "\n");//生产奖
				 }
		
			}
	    }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	DbUtils.release(conn, pre, result);
        }
		
		return reContent.toString();
	}
	
	
	/***
	 * 根据日期获取奖金信息
	 * @throws SQLException 
	 */
	@Transactional(readOnly = true)
	@Override
	public String getBonusInfoByDate(String id, String date) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = null;
	    PreparedStatement pre = null;
	    
	    conn = DbUtils.getConnection();

	    String sql = "select t_ryxx.sbsbh as id, t_jx_gs.je as jx_gs,t_ycx_gs.je as ycx_gs , t_glb.je as glb, t_scj.je as scj, t_jnj.je as jnj, t_jx.je as jx, t_ycx.je as ycx,"
			         + "t_bf.je as bf,t_bldcj.je as bldcj,t_bzjsj.je as bzjsj, t_aqj.je as aqj,t_hlhjyj.je as hlhjyj, t_khfuzlj.je as khfuzlj, t_kjjbj.je as kjjbj,  t_ldjsj.je as  ldjsj,  t_lwf.je as  lwf, t_sqj.je as sqj,"
			         + "t_sqjjy.je as sqjjy, t_wmdwj.je as wmdwj,t_yhstrcj.je as yhstrcj, t_cnf.je as cnf, t_cbdtdxj.je as cbdtdxj,t_jjzrj.je as jjzrj "
	            + "from rs_d_ryjb t_ryxx "
	            + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from rs_d_jj_jx_gs_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_jx_gs on t_ryxx.rybm = t_jx_gs.rybm and t_jx_gs.qrbz = 'Y' and   t_jx_gs.rq = " + date + " "
	            + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_ycx_gs_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_ycx_gs on t_ryxx.rybm = t_ycx_gs.rybm  and t_ycx_gs.qrbz = 'Y' and  t_ycx_gs.rq = " + date + " " 
	            + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_glb_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_glb on t_ryxx.rybm = t_glb.rybm  and t_glb.qrbz = 'Y' and   t_glb.rq = " + date + " " 
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_scj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_scj on t_ryxx.rybm = t_scj.rybm  and t_scj.qrbz = 'Y' and   t_scj.rq = " + date + " " 
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_jnj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_jnj on t_ryxx.rybm = t_jnj.rybm  and t_jnj.qrbz = 'Y' and  t_jnj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_jx_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_jx on t_ryxx.rybm = t_jx.rybm  and t_jx.qrbz = 'Y' and   t_jx.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_ycx_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_ycx on t_ryxx.rybm = t_ycx.rybm  and t_ycx.qrbz = 'Y' and  t_ycx.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_bf_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_bf on t_ryxx.rybm = t_bf.rybm  and t_bf.qrbz = 'Y' and  t_bf.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_bldcj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_bldcj on t_ryxx.rybm = t_bldcj.rybm  and t_bldcj.qrbz = 'Y' and  t_bldcj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_bzjsj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_bzjsj on t_ryxx.rybm = t_bzjsj.rybm  and t_bzjsj.qrbz = 'Y' and  t_bzjsj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_aqj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_aqj on t_ryxx.rybm = t_aqj.rybm  and t_aqj.qrbz = 'Y' and  t_aqj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_hlhjyj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_hlhjyj on t_ryxx.rybm = t_hlhjyj.rybm  and t_hlhjyj.qrbz = 'Y' and  t_hlhjyj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_khfuzlj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_khfuzlj on t_ryxx.rybm = t_khfuzlj.rybm  and t_khfuzlj.qrbz = 'Y' and  t_khfuzlj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_kjjbj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_kjjbj on t_ryxx.rybm = t_kjjbj.rybm  and t_kjjbj.qrbz = 'Y' and  t_kjjbj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_ldjsj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_ldjsj on t_ryxx.rybm = t_ldjsj.rybm  and t_ldjsj.qrbz = 'Y' and   t_ldjsj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_lwf_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_lwf on t_ryxx.rybm = t_lwf.rybm  and t_lwf.qrbz = 'Y' and  t_lwf.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_sqj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_sqj on t_ryxx.rybm = t_sqj.rybm  and t_sqj.qrbz = 'Y' and  t_sqj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_sqjjy_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_sqjjy on t_ryxx.rybm = t_sqjjy.rybm  and t_sqjjy.qrbz = 'Y' and  t_sqjjy.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_yhstrcj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_yhstrcj on t_ryxx.rybm = t_yhstrcj.rybm  and t_yhstrcj.qrbz = 'Y' and   t_yhstrcj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_wmdwj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_wmdwj on t_ryxx.rybm = t_wmdwj.rybm  and t_wmdwj.qrbz = 'Y' and   t_wmdwj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_cnf_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_cnf on t_ryxx.rybm = t_cnf.rybm  and t_cnf.qrbz = 'Y' and  t_cnf.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_cbdtdxj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_cbdtdxj on t_ryxx.rybm = t_cbdtdxj.rybm  and t_cbdtdxj.qrbz = 'Y' and  t_cbdtdxj.rq = " + date + " "
                + "left join (select to_char(rq,'yyyymm') as rq,rybm,qrbz,sum(je) as je from  rs_d_jj_jjzrj_ls group by rybm,to_char(rq,'yyyymm'),qrbz) t_jjzrj on t_ryxx.rybm = t_jjzrj.rybm  and t_jjzrj.qrbz = 'Y' and  t_jjzrj.rq = " + date + " "
                + "where t_ryxx.sbsbh = ?";
		pre = conn.prepareStatement(sql);
		
		pre.setString(1, id);
		
		ResultSet result = null;
		result = pre.executeQuery();
		
		// 获取当前时间
		//Date curDate = new Date();
	    
		StringBuilder reContent = new StringBuilder();
		while(result.next()){
			 reContent.append("    本月奖金 " +  "\n");//加薪
			 if(result.getString("jx_gs") != null){
				 reContent.append("本月加薪：" + result.getString("jx_gs") + "\n");//加薪
			 }
			 if(result.getString("ycx_gs") != null){
				 reContent.append("一次性：" + result.getString("ycx_gs") + "\n");//一次性
			 }
			 if(result.getString("glb") != null){
				 reContent.append("管理杯：" + result.getString("glb") + "\n");//管理呗
			 }
			 if(result.getString("scj") != null){
				 reContent.append("生产奖：" + result.getString("scj") + "\n");//生产奖
			 }
			 if(result.getString("jnj") != null){
				 reContent.append("节能奖：" + result.getString("jnj") + "\n");//节能奖
			 }
			 if(result.getString("ycx") != null){
				 reContent.append("一次性：" + result.getString("ycx") + "\n");//局一次性
			 }
			 if(result.getString("jx") != null){
				 reContent.append("加薪：" + result.getString("jx") + "\n");//局加薪
			 }
			 if(result.getString("bf") != null){
				 reContent.append("加发奖金：" + result.getString("bf") + "\n");//加发奖金
			 }
			 if(result.getString("bldcj") != null){
				 reContent.append("班轮单船奖：" + result.getString("bldcj") + "\n");//生产奖
			 }
			 if(result.getString("bzjsj") != null){
				 reContent.append("班组建设奖：" + result.getString("bzjsj") + "\n");//班组建设奖
			 }
			 if(result.getString("hlhjyj") != null){
				 reContent.append("合理化建议奖：" + result.getString("hlhjyj") + "\n");//生产奖
			 }
			 if(result.getString("jnj") != null){
				 reContent.append("节能奖：" + result.getString("jnj") + "\n");//生产奖
			 }
			 if(result.getString("khfuzlj") != null){
				 reContent.append("客货服务质量奖：" + result.getString("khfuzlj") + "\n");//生产奖
			 }
			 if(result.getString("scj") != null){
				 reContent.append("科技进步奖：" + result.getString("kjjbj") + "\n");//生产奖
			 }
			 if(result.getString("ldjsj") != null){
				 reContent.append("劳动竞赛奖：" + result.getString("ldjsj") + "\n");//生产奖
			 }
			 if(result.getString("lwf") != null){
				 reContent.append("劳务费：" + result.getString("lwf") + "\n");//生产奖
			 }
			 if(result.getString("sqj") != null){
				 reContent.append("速遣奖：" + result.getString("sqj") + "\n");//生产奖
			 }
			 if(result.getString("sqjjy") != null){
				 reContent.append("速遣奖结余：" + result.getString("sqjjy") + "\n");//生产奖
			 }
			 if(result.getString("yhstrcj") != null){
				 reContent.append("特殊引航收入奖：" + result.getString("yhstrcj") + "\n");//生产奖
			 }
			 if(result.getString("wmdwj") != null){
				 reContent.append("文明单位奖：" + result.getString("wmdwj") + "\n");//生产奖
			 }
			 if(result.getString("cnf") != null){
				 reContent.append("采暖费：" + result.getString("cnf") + "\n");//生产奖
			 }
			 if(result.getString("cbdtdxj") != null){
				 reContent.append("引航员船舶动态兑现：" + result.getString("cbdtdxj") + "\n");//生产奖
			 }

		}
		
		return reContent.toString();
	}
}
