package com.easyplaylist.engine;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class FileUtils {

	public static String walk(String path) {
		String result = "";
		int level = 1;
		File dir = new File(path);
		
		if(dir.isDirectory()) {
			level++;
			String[] files = dir.list();
			for(String file : files) {
				result += FileUtils.walk(path+"/"+file);	
			}
		}else if(dir.isFile() && !dir.isHidden()) {
			result += (StringUtils.repeat("-", level)+dir.getName()+"\r\n");
		}
		
		
		return result;
	}
}
