package com.table.copy;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		ArrayList<String> clientList;
		List<List<String>> nList;
		DBUtil dbutil = new DBUtil();
		
		Map map = null;
		try {
			map = dbutil.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int threadNum =  Integer.parseInt((String)map.get("THREADNUM"));
		

		try {
			DataBase db = new DataBase();
			if(args.length > 0 && "proc".equals(args[0])){
				clientList = new ArrayList<String>(Arrays.asList(((String)map.get("procs")).split(",")));
				for (int i = 0; i < clientList.size(); i++) {
					clientList.set(i, "call "+clientList.get(i));
				}
//				System.out.println(clientList);
			}else{
				clientList = db.selectFromTestBase();/*.forEach(System.out::println);*/
			}
			
			//将clientList分为threadNum份存入nList
			nList = averageAssign(clientList,threadNum);
			
	        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间 
	        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			for(int i = 0 ; i<nList.size();i++){
				ProcThread t = new ProcThread(nList.get(i));
				Date date = new Date();// 获取当前时间 
				t.start();
				System.out.println("Thread["+t.getName()+"] start at "+sdf.format(date));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }


}
