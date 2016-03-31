package org.h819.commons.file;

import org.h819.commons.MyExceptionUtils;
import org.h819.commons.file.pdf.itext.PDFUtilsBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014-08-22
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */

//http://tutorials.jenkov.com/java-itext/index.html

//html->pdf
    //http://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-creating-pdf-documents-with-wkhtmltopdf/
    //http://wkhtmltopdf.org/

public class MyPDFUtils extends PDFUtilsBase {

    private static Logger logger = LoggerFactory.getLogger(MyPDFUtils.class);

    /**
     * 为了能继承，设置为 public
     */



    /**
     * 加密 pdf 文件，不允许打印，不允许拷贝，不允许保存
     *
     * @param srcPdfFile  源文件
     * @param descPdfFile 目标文件
     * @throws java.io.IOException
     */
    public static void encryptPdf(File srcPdfFile, File descPdfFile)
            throws IOException {

        encryptPdf(srcPdfFile, descPdfFile, null, null, false, false, false,
                false);

    }



    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MyPDFUtils p = new MyPDFUtils();
        // p.addWaterMarkToCapital(new File("D:\\002"), new File("D:\\002\\03"),
        // null, null);

        // decryptPdf(new File("D:\\download\\02\\"), new File(
        // "D:\\download\\02pdf\\"),new File(
        // "D:\\download\\02badpdf\\"));

        // decryptPdf(new File("I:\\cctv 2009\\01\\IB2_4\\"));
        // decryptPdf(new File("d:\\001"));

    }
}
