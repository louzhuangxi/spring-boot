package org.h819.commons.printer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;


/**
 * @author H819
 * @version V1.0
 * @Title: PrintUtils.java
 * @Description: TODO(打印工具类)
 * @date 2010-3-28
 */

// jacob 调用 windows 命令，打印任意文档
// https://sourceforge.net/projects/jacob-project/
@Slf4j
public class MyPrinterUtils {

    //private static final Logger log = LoggerFactory.getLogger(MyPrinterUtils.class);


    /**
     *
     */
    public MyPrinterUtils() {
        // TODO Auto-generated constructor stub
        // E:/Program Files/Adobe/Reader 9.0/Reader/acrord32
    }

    /**
     * 得到本机所有可用的打印机列表
     */
    public void getPrinter() {
        //PrinterJob printJob = PrinterJob.getPrinterJob();
        //jdk 可以完成此功能
    }

    /**
     * 只能打印 pdf 文件 </br>
     * windows 中，发送 pdf 格式文件，到指定打印机打印(仅能打印 pdf 格式文件)。因为是调用 windows
     * 命令，故文件路径分隔符要完全满足 windows 要求，否则找不到文件</br>
     * <p/>
     * 本方法应用开源软件 Ghostscript 和 GSView 打印 pdf 文件，使用之前，首先安装 Ghostscript 和
     * GSView。他们可以下述地址下载得到:</br>
     * <p/>
     * Ghostscript #http://www.ghostscript.com/ . 它是一个 windows 下面的安装文件，文件名是 gs871w32.exe</br>
     * <p/>
     * GSView # http://pages.cs.wisc.edu/~ghost/gsview/index.htm.  安装文件名 gsv49w32.exe </br>
     * <p/>
     * gsprint 命令的其他参数见 http://pages.cs.wisc.edu/~ghost/gsview/index.htm</br>
     * <p/>
     * 具体的打印设置，需要设置打印机驱动本身的设置。
     *
     * @param gspprintPath gsprint 命令所在目录，写法如 e:\\Program
     *                     Files\\Ghostgum\\gsview\\gsprint.exe 注意路径是双斜线,可以不带 exe 后缀名
     * @param printerName  windows 中的打印机名称，即 windows 打印机控制面板中看到的打印机名字，如 Adobe PDF
     * @param pdfFilePath  打印的 pdf 文件路径，如 d:\\sample.pdf 注意路径是双斜线
     */
    public static void printPDFFile(String gspprintPath, String printerName,
                                    String pdfFilePath) {
        try {

            // cmd.exe /c call 和 cmd.exe /c start 命令区别
            // start命令路径参数中不支持空格，call通过引号括起来，路径参数可以支持空格。
            // gspprintPath: e:\\Program
            // Files\\Ghostgum\\gsview\\gsprint.exe(exe 可以不写)
            // printerName: Adobe PDF
            // pdfFilePath: d:\sample.pdf

            if (!new File(gspprintPath).exists()) {
                log.info("file does not exsit: " + gspprintPath);
                return;
            }
            if (!new File(pdfFilePath).exists()) {
                log.info("file does not exsit: " + pdfFilePath);
                return;
            }
            //所有的命令字符串，都用 引号括起来
            String cmd = "cmd.exe /c call \""
                    + FilenameUtils.separatorsToSystem(gspprintPath)
                    + "\" -printer " + "\"" + printerName + "\" "
                    + FilenameUtils.separatorsToSystem(pdfFilePath);
            log.info("print cmd is: " + cmd);
            // cmd.exe /c call "e:\Program
            // Files\Ghostgum\gsview\gsprint" -printer "Adobe PDF" d:/1.pdf

            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        MyPrinterUtils.printPDFFile(
                "e:\\Program Files\\Ghostgum\\gsview\\gsprint.exe",
                "Adobe PDF", "d:\\1.pdf");
    }

}
