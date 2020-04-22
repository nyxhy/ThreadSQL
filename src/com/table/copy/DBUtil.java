package com.table.copy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBUtil {
	static File directory = new File("");
	private static String filePath = directory.getAbsolutePath(); // 设定为上级文件夹
																	// 获取绝对路径
	private static Properties prop = new Properties();

	public static Properties read() throws IOException {
		BufferedInputStream ipstream = new BufferedInputStream(
				new FileInputStream(filePath + "/settings.properties"));
		prop.load(ipstream);
		return prop;
	}
}
