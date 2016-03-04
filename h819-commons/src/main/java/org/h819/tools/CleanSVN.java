package org.h819.tools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CleanSVN {

	/**
	 * 递归删除指定文件夹的 .svn 文件夹
	 * @param srcDir
	 */
	private void removeSVNDirectory(String srcDir) {

		File svn = new File(srcDir);
		File[] dir = svn.listFiles();

		for (File listFile : dir) {
			if (listFile.isDirectory()) {
				if (listFile.getName().equals(".svn")) {
					try {
						FileUtils.deleteDirectory(listFile);
						System.out.println(listFile + " delete.");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					removeSVNDirectory(listFile.getAbsolutePath());
				}

			} else
				continue;
		}
		
		System.out.println("remove svn compelet.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// E:\Program\workspace\h819
		CleanSVN s = new CleanSVN();
		
		//家里的路径 h819
		 //s.removeSVNDirectory("E:\\program\\myeclipse\\workspace\\h819");		
		//=====
		//单位的路径 h819	
		//	s.removeSVNDirectory("E:\\Program\\workspace\\h819\\");
		//s.removeSVNDirectory("E:\\Program\\workspace\\2760");
		
		//托管系统
		s.removeSVNDirectory("E:\\Program\\workspace\\trust\\");
		

		//单位的路径 waterq.cn
		// s.removeSVNDirectory("E:\\Program\\workspace\\waterq.cn\\");		
		
	}

}
