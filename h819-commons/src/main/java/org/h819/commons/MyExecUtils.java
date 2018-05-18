package org.h819.commons;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FilenameUtils;
import org.h819.commons.file.MyPdfUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/7/27
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
public class MyExecUtils {

    /**
     * 用这个简单的例子吧
     * 需要注意的是，如果命令行中，exe 文件所在路径有空给，需要用双引号引起来，如
     * echo show databases; | "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe" -h localhost -u root -p123456 -P3306 > "D:\02\databaselist.txt"
     * 个别的命令行，不能包含空格，即使是用引号引起来也不行，如 mysqldump
     */


    /**
     * pdf2swf
     *
     * @param exePath          命令所在路径
     * @param pdfPath          pdf 文件路径
     * @param swfPath          生成的 swf 路径
     * @param xpdflanguagePath 语言文件所在文件夹路径
     * @return 转换是否成功
     */
    public static boolean pdf2Swf(Path exePath, Path pdfPath, Path swfPath, Path xpdflanguagePath) {

//        String srcPdf = "d:\\test\\src.pdf";
//        String descSwf = "d:\\test\\src%.swf";
//        String pdf2swfComandPath = "E:\\program\\flexpaper\\swftools-2013-04-09-1007\\pdf2swf.exe";
//        String xpdflanguagePath = "E:\\program\\flexpaper\\xpdf-chinese-simplified";

/*
E:\program\flexpaper\swftools-2013-04-09-1007\pdf2swf.exe D:\pdfprint\Visio-xxsNetwork_final.pdf -o D:\pdfprint\Visio-xxsNetwork_final%.swf -f -T 9 -t -j=100 -s protect -s storeallcharacters -s languagedir=E:\program\flexpaper\xpdf-chinese-simplified
 */


        if (!Files.exists(exePath)) {
            System.out.println(exePath + " 不存在");
            return false;
        }

        String name = FilenameUtils.getName(swfPath.toString()); // GB~1094.1-2013%.swf
        if (!name.contains("%")) {
            System.out.println(swfPath + " 转换目标必须含有分页标记 '%' ");
            return false;
        }

        createDescDirs(swfPath);

        String cmd = exePath + " " + pdfPath + " -o " + swfPath +
                " -f -T 9 -t -j=100 -s protect -s storeallcharacters -s languagedir=" + xpdflanguagePath;

        System.out.println(cmd);


        boolean su = execWindowsCommand(cmd);

        //检查是否转换成功，分页形式，pdf 页数和生成的 swf 相等, 需要加入 itext 包
        try {
            int pdfPage = MyPdfUtils.getFilePages(pdfPath);
            int swfPage = getDescDirs(swfPath).list().length;
            if (pdfPage != swfPage)
                su = false;

        } catch (IOException e) {
            su = false;
            e.printStackTrace();
        }

        return su;
    }

    /**
     * 执行指定命令
     *
     * @param command 待执行的命令行字符串
     * @return 执行是否成功
     */
    private static boolean execWindowsCommand(String command) {
        CommandLine cmd = new CommandLine("cmd.exe ");
        cmd.addArgument("/c");
        cmd.addArgument(command, false);
        try {
            new DefaultExecutor().execute(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 利用  pdfdecrypt.exe 命令行工具破解加密的 pdf
     *
     * @param exePath        pdfdecrypt.exe 命令所在路径
     * @param encryptPdfPath 加密的 pdf 源文件
     * @return 转换是否成功
     * @paramd decryptPdfPath 解密后的 pdf 目标文件
     */
    public static boolean pdfDecrypt(Path exePath, Path encryptPdfPath, Path decryptPdfPath) {
        return pdfDecrypt(exePath, encryptPdfPath, decryptPdfPath, null, null);
    }

    /**
     * 利用  pdfdecrypt.exe 命令行工具破解加密的 pdf
     *
     * @param exePath        pdfdecrypt.exe 命令所在路径
     * @param encryptPdfPath 加密的 pdf 源文件
     * @param decryptPdfPath 解密后的 pdf 目标文件
     * @param userPassword   加密的 pdf 源文件的 userPassword (加密的 pdf 的一个属性，可选项)
     * @param ownerPassword  加密的 pdf 源文件的 ownerPassword (加密的 pdf 的一个属性，可选项)
     * @return 转换是否成功
     */
    public static boolean pdfDecrypt(Path exePath, Path encryptPdfPath, Path decryptPdfPath, String userPassword, String ownerPassword) {

//        String srcPdf = "d:\\test\\src.pdf";
//        String descPdf = "d:\\test\\desc.pdf";
//        String pdf2swfComandPath = "D:\\swap\\local\\java_jar_source_temp\\pdfdecrypt";
//        String userPassword = "upass";
//        String ownerPassword = "opass";

        if (!Files.exists(encryptPdfPath)) {
            System.out.println(encryptPdfPath + " 不存在");
            return false;
        }

        if (!Files.exists(exePath)) {
            System.out.println(exePath + " 不存在");
            return false;
        }

        createDescDirs(decryptPdfPath);

        StringBuilder builder = new StringBuilder();
        builder.append(exePath);
        builder.append(" -i " + encryptPdfPath);
        builder.append(" -o " + decryptPdfPath);

        if (userPassword != null)
            builder.append(" -u " + userPassword);
        if (ownerPassword != null)
            builder.append(" -w " + ownerPassword);


        //检查转换是否成功
        boolean su = execWindowsCommand(builder.toString());
        if (!Files.exists(decryptPdfPath))
            su = false;

        return su;

    }

    /**
     * 递归创建目标文件所在的文件夹
     *
     * @param descFilePath
     */
    private static void createDescDirs(Path descFilePath) {
        File f = getDescDirs(descFilePath);
        if (!f.exists() || !f.isDirectory())
            f.mkdirs();

    }

    private static File getDescDirs(Path descFilePath) {
        //String s = "E:\\program\\txfile\\standardfile/swf//GN2/2014.09/GB~1094.1-2013%.swf";
        return descFilePath.getParent().toFile();
    }


    private static void testPdf2Swf() {
        Path srcPdf = Paths.get("D:\\pdfprint\\Visio-xxsNetwork_final.pdf");
        Path descSwf = Paths.get("D:\\pdfprint\\Visio-xxsNetwork_final%.swf");
        Path pdf2swfComandPath = Paths.get("E:\\program\\flexpaper\\swftools-2013-04-09-1007\\pdf2swf.exe");
        Path xpdflanguagePath = Paths.get("E:\\program\\flexpaper\\xpdf-chinese-simplified");
        MyExecUtils.pdf2Swf(pdf2swfComandPath, srcPdf, descSwf, xpdflanguagePath);
    }

    public static void main(String args[]) {
        MyExecUtils.testPdf2Swf();
    }
}

