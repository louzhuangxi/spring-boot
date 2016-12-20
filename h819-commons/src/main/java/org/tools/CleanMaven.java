package org.tools;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/30
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public class CleanMaven {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        // E:\Program\workspace\h819
        CleanMaven s = new CleanMaven();

        //家里的路径 h819
        //s.removeSVNDirectory("E:\\program\\myeclipse\\workspace\\h819");
        //=====
        //单位的路径 h819
        //	s.removeSVNDirectory("E:\\Program\\workspace\\h819\\");
        //s.removeSVNDirectory("E:\\Program\\workspace\\2760");

        //托管系统
        s.clean(new File("F:\\baiduyun\\maven\\repository\\"));


        //单位的路径 waterq.cn
        // s.removeSVNDirectory("E:\\Program\\workspace\\waterq.cn\\");

    }

    /**
     * 下载失败后在本地仓库对应的文件夹中有一个以.lastUpdated结尾的文件
     * 如果不手动删除这个文件，就不能重新更新依赖，重新下载对应的jar包。
     *
     * @param srcDir
     */
    private void clean(File srcDir) {


        for (File f : srcDir.listFiles()) {
            if (f.isFile()) {

                if (FilenameUtils.getExtension(f.getName()).equals("lastUpdated")) {
                    f.delete();
                    System.out.println("delete "+ f.getAbsolutePath()+" ... ");
                }

            } else {
                clean(f);
            }
        }

    }
}
