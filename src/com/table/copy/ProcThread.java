package com.table.copy;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProcThread extends Thread{
	List<String> sqlList;
	String sql = "";
	DataBase db ;
	
    public ProcThread(String sql){
        this.sql = sql;
    }
    
    public ProcThread(List<String> sqlList){
        this.sqlList = sqlList;
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		try {
	        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间 
	        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			db =  new DataBase(); 
			int errnum = 0;
			for(int i = 0 ; i<sqlList.size();i++){
				try {
					boolean iRet = db.executeSql(sqlList.get(i));
				}catch(Exception e1) {
					System.out.println("Thread ["+Thread.currentThread().getName()+"] SQL["+sqlList.get(i)+"] Execution failed");
					errnum += 1;
				}
			}
			Date date = new Date();// 获取当前时间 
			System.out.println("Thread ["+Thread.currentThread().getName()+"] executeNum:["+sqlList.size()+"] ErrNum["+errnum+"]  at  "+sdf.format(date)+" end");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}        
	}
}
