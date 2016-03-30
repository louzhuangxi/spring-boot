package org.h819.commons.file;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.io.FileChannelRandomAccessSource;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.h819.commons.MyStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author H819
 * @version V1.0
 * @Title: PDFBaseUtils.java
 * @Description: TODO(基础pdf处理类)
 * @date 2015-3-1
 */

public class PdfExamples {

    // 该变量专门为 countPages() 函数设立，由于使用了递归，故提到全局变量，否则每次的返回值不能累计
    private static StringBuffer encryptPdfNamesTemp;

    // 该变量专门为 countPages() 函数设立，由于使用了递归，故提到全局变量，否则每次的返回值不能累计
    private static int numberOfPagesTemp;

    private static Logger logger = LoggerFactory.getLogger(PdfExamples.class);


    /**
     * 为了能继承，设置为 public
     */
    public PdfExamples() {

    }

    /**
     * 添加 javaScript action 例子. PDF 文档象 html 静态网页一样，可执行 javsScrip 脚本。
     * <p>
     * 所谓的 adobe acrobat 开发，指的就是，如何用 acrobat 设计PDF 文件，就行设计网页文件一样。
     * <p>
     * 在该文件中，可以设计布局，颜色，可以编写 javaScript 代码，指定表单操作等。
     * <p>
     * 在进行 adobe reader 进行浏览 PDF 文件的时候，就会执行相应的脚本。
     * <p>
     * 可以在 reader 阅读器中指定是否执行脚本。
     *
     * @param srciptOutFile 输出的样例文件
     */
    public static void addScriptActionsExample(File srciptOutFile) {

        //
        // PdfWriter.setAdditionalAction() 接受的参数：
        // The action is triggered just before closing the document.
        // PdfWriter.DOCUMENT_CLOSE

        // The action is triggered just before saving the document.
        // 意思是，填写 pdf 表单之后，点击保存按钮唤起的动作，"另存为" 操作不响应
        // PdfWriter.WILL_SAVE

        // The action is triggered just after saving the document.
        // PdfWriter.DID_SAVE

        // The action is triggered just before printing (part of) the
        // document.
        // PdfWriter.WILL_PRINT

        // The action is triggered just after printing.
        // PdfWriter.DID_PRINT

        // PdfWriter.setPageAction 接受的参数：
        // The action is triggered when you enter a certain page.
        // PdfWriter.PAGE_OPEN

        // The action is triggered when you leave a certain page.
        // PdfWriter.PAGE_CLOSE

        // 另外：PdfStamper stamper setPageAction() 方法，可以设置具体的页数

        Document document = new Document();
        PdfWriter writer;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(
                    srciptOutFile));

            document.open();
            document.add(new Paragraph("test."));

            String actionsScripe = "";
            PdfAction action;

            // 执行内置的警告语句
            actionsScripe = "app.alert('your action is PAGE_OPEN');";
            action = PdfAction.javaScript(actionsScripe, writer);
            writer.setPageAction(PdfWriter.PAGE_OPEN, action);

            // === 执行自定义函数
            actionsScripe = "var work = true; function loadform() { if(work=true) app.alert(\"action :print will work!\");  }";
            writer.addJavaScript(actionsScripe);
            action = PdfAction.javaScript("loadform();", writer);
            writer.setAdditionalAction(PdfWriter.WILL_PRINT, action);

            document.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




    /**
     * 测试 pdf 文件读取方法
     *
     * @throws IOException
     */
    private void testBigPdfFileRead() throws IOException {

        System.out.print(" 最大可用内存，对应-Xmx ：" + Runtime.getRuntime().maxMemory());  //最大可用内存，对应-Xmx
        System.out.print(" 当前JVM空闲内存 ：" + Runtime.getRuntime().freeMemory());  //当前JVM空闲内存
        System.out.print(" 当前JVM占用的内存总数 ：" + Runtime.getRuntime().totalMemory());  //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和

        String fstr = "D:\\download\\all.pdf";

        File f = new File(fstr);

        FileInputStream fileStream = new FileInputStream(f);
        //可以读取大文件
        PdfReader pdfr = new PdfReader(new RandomAccessFileOrArray(new FileChannelRandomAccessSource(fileStream.getChannel())), null);

        // encryptPdf()

        System.out.print("\n" + fstr + " 加密 ：" + pdfr.isEncrypted());

    }

    /**
     * Manipulates a PDF file src with the file dest as result
     *
     * @param src  the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void addScriptExample(File src, File dest) throws IOException,
            DocumentException {
        // Create a reader
        PdfReader reader = MyPDFUtils.getPdfReader(src);
        // Create a stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // Get the writer (to add actions and annotations)
        PdfWriter writer = stamper.getWriter();

        PdfAction action = PdfAction
                .javaScript(FileUtils.readFileToString(new File("d:\\js.js")),
                        writer, true);
        stamper.setPageAction(PdfWriter.PAGE_OPEN, action, 1);

        // Close the stamper
        stamper.close();
    }

    /**
     * 获取 pdf 文件属性信息
     *
     * @param srcPdfFile
     */
    public void getPageInfomation(File srcPdfFile) {

        try {

            PdfReader reader = MyPDFUtils.getPdfReader(srcPdfFile);

            Map info = reader.getInfo();

            for (Iterator i = info.keySet().iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                String value = (String) info.get(key);
                System.out.println(key + ": " + value);
            }

            System.out.println(MyStringUtils.center("pdf 信息", 80, "==="));

            System.out.println("PDF Version: " + reader.getPdfVersion());
            System.out.println("File length: " + reader.getFileLength());
            System.out.println("file size: " + reader.getFileLength()
                    / (1024.00 * 1024.00) + "M");

            System.out.print("Number of pages: ");
            System.out.println(reader.getNumberOfPages());
            Rectangle mediabox = reader.getPageSize(1);
            System.out.print("Size of page 1: [");
            System.out.print(mediabox.getLeft());
            System.out.print(',');
            System.out.print(mediabox.getBottom());
            System.out.print(',');
            System.out.print(mediabox.getRight());
            System.out.print(',');
            System.out.print(mediabox.getTop());
            System.out.println("]");
            System.out.print("Rotation of page 1: ");
            System.out.println(reader.getPageRotation(1));
            System.out.print("Page size with rotation of page 1: ");
            System.out.println(reader.getPageSizeWithRotation(1));
            System.out.print("Is rebuilt? ");
            System.out.println(reader.isRebuilt());
            System.out.print("Is encrypted? ");
            System.out.println(reader.isEncrypted());
            System.out.println("filed type 'expireCNS'? "
                    + reader.getAcroFields().getFieldType("expireCNS"));

            // 所有表单信息
            System.out.println(MyStringUtils.center("所有的 filed 信息", 80, "==="));
            // Get the fields from the reader (read-only!!!)
            AcroFields form = reader.getAcroFields();

            // Loop over the fields and get info about them
            Set<String> fields = form.getFields().keySet();
            for (String key : fields) {
                System.out.println(key + ": ");
                switch (form.getFieldType(key)) {
                    case AcroFields.FIELD_TYPE_CHECKBOX:
                        System.out.println("Checkbox");
                        break;
                    case AcroFields.FIELD_TYPE_COMBO:
                        System.out.println("Combobox");
                        break;
                    case AcroFields.FIELD_TYPE_LIST:
                        System.out.println("List");
                        break;
                    case AcroFields.FIELD_TYPE_NONE:
                        System.out.println("None");
                        break;
                    case AcroFields.FIELD_TYPE_PUSHBUTTON:
                        System.out.println("Pushbutton");
                        break;
                    case AcroFields.FIELD_TYPE_RADIOBUTTON:
                        System.out.println("Radiobutton");
                        break;
                    case AcroFields.FIELD_TYPE_SIGNATURE:
                        System.out.println("Signature");
                        break;
                    case AcroFields.FIELD_TYPE_TEXT:
                        System.out.println("Text");
                        break;
                    default:
                        System.out.println("?");
                }
            }

            System.out.println(MyStringUtils.center("javaScrpt 信息", 80, "==="));
            System.out.println("javaScript : " + reader.getJavaScript());

            reader.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
