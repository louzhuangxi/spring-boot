package org.h819.commons.file.pdf.itext;


import com.itextpdf.text.*;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.io.FileChannelRandomAccessSource;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.h819.commons.MyExceptionUtils;
import org.h819.commons.file.MyFileUtils;
import org.h819.commons.MyStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

/**
 * @author H819
 * @version V1.0
 * @Title: PDFBaseUtils.java
 * @Description: TODO(基础pdf处理类)
 * @date 2015-3-1
 */

/*
 * 关于 pdf 文件加密
 * 
 * ========== 目前最常见的要求是 ==========
 * 
 * 1. pdf 文件只能浏览，不能保存和打印
 * 
 * 2. 只能浏览打印，不能保存
 * 
 * 3. 有浏览、打印、保存功能的全部权限
 * 
 * 用户用上面的方法来作为版权保护的一种措施。
 * 
 * ========== 分析 ==========
 * 
 * 1. pdf 文档标准和标准 pdf 阅读器
 * 
 * pdf 文件是一种有通用标准的文档，其文档格式已经成为 iso 组织的标准。
 * 
 * pdf 文档标准规定了 pdf 文档的一些基本属性元素，其中包括安全元素(security )，如 打印，修改，拷贝内容等。
 * 
 * (adobe reader 打开文档之后，查看属性，即可看到).
 * 
 * 遗憾的是，并没有不能保存属性，也就是说，无论哪种 pdf 阅读器，在浏览 pdf 文件的时候，都不能阻止用户保存。
 * 
 * pdf 文档的另外一些属性，可以定义阅读器打开文档的时候，在阅读器中不显示工具栏和菜单栏(可以通过某些特殊的快捷键呼出)。
 * 
 * 标准的 pdf 阅读器在浏览 pdf 文件的时候，是按照 pdf 文档的规范来显示其内容的，而 pdf 文档并不能规定不能保存，
 * 
 * 所以标准的 pdf 阅读器是不能阻止保存的。
 * 
 * 结论：pdf 只能限制打印，拷贝，不能限制保持。如果开放了打印权限，实际上就是开放了保存权限
 * 
 * 2. 个性化 pdf 阅读器
 * 
 * 在另外一些非标准的 pdf 阅读器中，实现了自己的显示 pdf 文档的方法，基本的原理是逐页读入 pdf 文档，之后按照自己的方式重新绘出文档内容。
 * 
 * 这些个性化开发的阅读器阅，实际上是用另外一种方式，重新生成了一种他自己的格式，自己实现了打印、浏览功能，文件已经不是 pdf 文件了。
 * 
 * 类似这种阅读器实现方法各不相同，有的把 pdf 文件都 转换为图片来显示，有的抽取了 pdf 文件的内容，重新输出。无论那种形式，都取决于
 * 
 * 这些pdf文件阅读器的实现方法。
 * 
 * 个性化阅读器测试结论
 * 
 * 在用 java 实现的各种个性化阅读器实现中，测试了几种，速度都很慢，对中文支持的也不好，所以最终还是放弃了这种做法。
 * 
 * 日后可以自己开发一个阅读器。
 * 
 * 测试软件:
 * 
 * (1). icepdf(4.0 版 开源版)：ice pdf 无论是开源版本，还是商业版本，经过测试，浏览中文 pdf的时候，
 * 
 * 会很长时间才可以显示出内容，所以这个方案放弃。
 * 
 * (2) pdf-renderer https://pdf-renderer.dev.java.net/,
 * 
 * 好像不更新了，显示中文文件有问题(报异常，查了一天资料，无法解决，放弃)，并且速度很慢，这个方案放弃。
 * 
 * (3) adobe 自己实现的浏览方案(2001 年产品，已经停止更新，这个方案放弃)
 * http://www.adobe.com/products/acrviewer/acrvdnld.html?name=Accept#java
 * 
 * 商业软件(未测试)
 * 
 * http://big.faceless.org/products/pdfviewer/
 * 
 * http://www.jpedal.org/
 * 
 * http://www.qoppa.com/jpvindex.html(好像是免费的，咨询研究一下，有评论说还可以)
 * 
 * google 的在线文档 api 可否？
 * 
 * ============================= 各个 java 开发包侧重点不同，其中：
 * 
 * 编辑：itext,
 * 
 * 浏览：pdf-renderer,qoppa pdf viewer,icepdf viewer
 * 
 * 
 * 相比之下，显示 pdf 文件，还是 adobe reader 是最快，并且应用的最为广泛，所以还是应该立足于用它来打开文件。
 * 
 * ========== pdf 文件字体问题 ==========
 * 
 * 对于生成 pdf 文件和读取并显示 pdf 文件，都涉及到字体。
 * 
 * 例如中文，那么在新建pdf的时候，要指定字体，这样生产的pdf文件才可以被阅读器按照指定的字体读取并显示。
 * 
 * 同样，读取pdf，读取程序要知道被读取的 pdf 文件的字体，才可以调用该字体来显示文件内容。
 * 
 * 因为世界上字体编码太多，应用软件不可能默认都包含，所以，一个好的pdf creater or viwer ，
 * 
 * 一定要提供设置字体的方法，否则一定会有编码问题。
 * 
 * pdf-renderer 没有提供设置字体的方法，所以显示中文就有问题。
 * 
 * 
 * ========== 用 itext 在线显示解决方案 ==========
 * 
 * 1. 禁止打印;
 * 
 * 2. 禁止拷贝文本内容;(需要根据业务情况而定)
 * 
 * 3. 禁止显示工具栏和菜单栏;
 * 
 * 4. servlet 把文件数据流发送到客户端，客户端禁止缓存
 * 
 * 5. 如有可能，屏蔽快捷键，使之不能呼出。
 * 
 * 上述方法，除了"打印"和"拷贝文本内容"是 pdf 文档标准的属性之外，"禁止显示工具栏和菜单栏"仅是浏览选项，
 * 
 * 标准阅读器临时不显示，但用快捷键 F8 或者 Ctrl+Shift+S 即可呼出保存选项。
 * 
 * 也可以在用 adobe reader 打开文件的时候，传入参数来指定。详见adobe 官方文档：
 * 
 * google "Parameters for Opening PDF Files" 可以得到
 * 
 * 所以上述方法也仅仅是一个临时的方法，并不能阻止电脑知识稍好的用户。
 * 
 * ========== 用 itext 其他属性 ==========
 * 
 * 1. 在 itext 中，可以设置 pdf 文件的打开(userpassword)和编辑(ownerpassword)密码
 * 
 * 经过测试，在 itext 提供的解密方法中，仅提供 ownerpassword 项，如果提供了 ownerpassword ，用户就拥有完全的编辑权限了。
 * 
 * 所以，如果用户用完全权限浏览文件，就可以进行保存等操作。
 * 
 * 案例：
 * 
 * 当用户登录以后，记录其登录信息，生成一个随机数，按照此随机数，
 * 
 * 生成一个需要登录信息的临时文件，用户可以在登录系统后用此登录信息打开文件。
 * 
 * 此种方法，用户必须在系统中打开文件，即使是把文件下载到本地，由于不知道打开密码，也是不能进行打开等操作的，从另外一个角度实现了保护。
 * 
 * (经过测试，5 的方案不可行，详见 decryptPdf 方法. 不能单独解密打开密码)
 * 
 * 2. 数字签名保护
 * 
 * 通过签发证书，把证书留在服务器端，服务器端用证书打开文件，即使是用户把文件下载到了客户端，因为没有证书，也不能打开。有时间的时候，测试一下???
 * 
 * (估计和 1 结论一样)
 * 
 * ========== pdf 文档保护结论 ==========
 * 
 * 1. 目前看，基于标准浏览器的浏览方案，仅能做的不可打印和不可拷贝内容，无法做到不能保存，开放了打印权限，同时也开放了保存权限
 * 
 * 2. 非标准的浏览器实现方案可能做到不能保存等要求。需要单独的阅读器，文档格式发生了变化
 * 
 * 3. 更加细致的保护，如打印次数，应该借助于数字版权保护系统来实现，是普通的 java 操作所达不到的了
 * 
 * pdf 文件源码分析 http://superleo.javaeye.com/blog/2838484
 * 
 * === 非 java 实现
 * 
 * 1. pdf -> swf ，之后浏览 swf 格式文件。
 * 
 * http://nlslzf.javaeye.com/blog/395518 http://www.javaeye.com/topic/538361
 * 
 * javaeye 搜索 swftools,pdf2swf,flexpaper
 * 
 * 参考:
 * 
 * http://flashpdfviewer.com/ //收费
 * 
 * http://www.qoppa.com/pdfviewer/demo/jpvapplets.html
 * 
 * http://flexpaper.devaldi.com/
 * 
 * http://www.artofsolving.com/opensource
 */

/*
 * 依赖包
 * 
 * bcprov.jar
 * 
 * bcmail.jar
 * 
 * itext 官方网站有说明
 *
 *
 * getPdfReader 方法，演示了 PdfReader读取大文件时的优化，itext 5.3.5 版本以上新特性
 *
 */

public class PDFUtilsBase {

    // 该变量专门为 countPages() 函数设立，由于使用了递归，故提到全局变量，否则每次的返回值不能累计
    private static StringBuffer encryptPdfNamesTemp;

    // 该变量专门为 countPages() 函数设立，由于使用了递归，故提到全局变量，否则每次的返回值不能累计
    private static int numberOfPagesTemp;

    private static Logger logger = LoggerFactory.getLogger(PDFUtilsBase.class);


    /**
     * 为了能继承，设置为 public
     */
    public PDFUtilsBase() {

    }

	/*
     * === 使用中文字体的三种方式
	 * 
	 * 1、使用 iTextAsian.jar 中的字体(项目需要加载 iTextAsian.jar)
	 * BaseFont.createFont("STSong-Light",UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
	 * 
	 * 引入方法已经写好，本类中的 getChineseFont()
	 * 
	 * 2、使用Windows系统字体(TrueType)
	 * BaseFont.createFont("C:/WINDOWS/Fonts/SIMYOU.TTF",
	 * BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
	 * 
	 * 3、使用资源字体(ClassPath)
	 * BaseFont.createFont("/SIMYOU.TTF",BaseFont.IDENTITY_H,
	 * BaseFont.NOT_EMBEDDED);
	 * 
	 * 
	 * === 字体颜色变浅
	 * 
	 * itext 字体不能设置透明度，所以字体的颜色深浅只能靠字体本身线条粗细和
	 * 
	 * over.setColorFill(Color.LIGHT_GRAY) 方法 两个属性来控制。
	 * 
	 * === getOverContent(),getUnderContent() 方法
	 * 
	 * getOverContent() 方法是在 pdf 内容(文字层)的上部添加内容
	 * 
	 * getUnderContent()方法是在下部添加内容。
	 * 
	 * 两个方法和itext提取 pdf 内相关。如果提取正确，把 pdf 文字内容(Content)都提取出来了
	 * 
	 * (如 word 文件直接转换的 pdf 文档)，那么 加在 Content下部的内容 就会仅被 Content(文字内容)
	 * 遮挡，应该是理想的情况。
	 * 
	 * 但是如果 pdf本身不规范，如部分扫描的文件生成的 pdf 文档，itext 提取不出 Content(如仅把文字层当作一个图片层)
	 * ，那么加在下部 的内容就会被图片层遮挡而看不到。
	 * 
	 * 而 getOverContent() 方法加在 Content 上部的内容，会遮挡 Content，所以加在上部的内容需要进行虚化处理。
	 * 
	 * 经过测试，"华文彩云" 字体是中空的，产生的 遮挡比较小，再和 setColorFill(Color.LIGHT_GRAY)联合使用，效果比较好。
	 * 
	 * 信息所扫描的文档，大部分都会产生遮挡现象，所以用文字方式吧。
	 * 
	 * === 关于中空字体
	 * 
	 * 选择了很久，目前网上没有合适的中空字体，流传的"王汉之中空字体"有缺陷，不是所有的汉字都能为中空， 所以不好用，暂时用"华文彩云" 字体吧
	 */

    /**
     * 添加 javaScript action 例子. PDF 文档象 html 静态网页一样，可执行 javsScrip 脚本。
     * <p/>
     * 所谓的 adobe acrobat 开发，指的就是，如何用 acrobat 设计PDF 文件，就行设计网页文件一样。
     * <p/>
     * 在该文件中，可以设计布局，颜色，可以编写 javaScript 代码，指定表单操作等。
     * <p/>
     * 在进行 adobe reader 进行浏览 PDF 文件的时候，就会执行相应的脚本。
     * <p/>
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
     * 为制定文件夹内的 pdf 文件添加水印
     * <p/>
     * 添加方法，用 addWatermarkWithTemplate() 代替
     *
     * @param srcDir                 源文件夹
     * @param destDir                目标
     * @param imageFile              水印图片,jpeg 和 gif 格式都可以，其他的没有测试。如果不添加图片，参数需设置为 null
     * @param fondFile               字体文件
     * @param companyName            授权接受者公司名称，如"东城稻香村集团"
     * @param copyRight              授权声明，如" . 专用 . 不得传播" ,和 companyName
     *                               联合使用，会在文本上添加："东城稻香村集团 . 专用 . 不得传播"
     * @param adminCommpanyName      授权方名称，如："首都标准网"
     * @param adminCompanyWebSiteUrl 授权方公司网址名称，如："http://www.capital-std.com"
     * @throws java.io.IOException
     * @throws MyExceptionUtils
     */
    public static void addWatermark(File srcDir, File destDir, File imageFile,
                                    File fondFile, String companyName, String copyRight,
                                    String adminCommpanyName, String adminCompanyWebSiteUrl)
            throws IOException, MyExceptionUtils {

        // 判断源文件夹信息文件
        if (srcDir == null || !srcDir.exists() || !srcDir.isDirectory()) {
            throw new IOException("src directory '" + srcDir
                    + "' does not exsit.");
        }

        // 判断字体文件
        if (fondFile == null || !fondFile.exists()) {
            throw new IOException("fondFile '" + fondFile + "' does not exsit.");
        }

        // 判断图片文件信息
        if (imageFile != null) {
            // 图片信息不存在
            if (!imageFile.exists())
                throw new IOException("src directory '" + imageFile.getPath()
                        + "' does not exsit.");

            if (companyName != null) {
                throw new MyExceptionUtils(
                        "'image or companyName only exit one.");
            }
        }

        // 判断需要添加的单位名称. 此处抛出异常写法不合理，日后需要总结异常写法
        if (imageFile == null) {
            if (companyName == null || companyName.equals("")) {
                throw new MyExceptionUtils("'image and companyName are null.");
            }
        }

        try {
            MyFileUtils.forceMkdir(destDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 基本属性
        Image img = null;
        BaseFont bfont = null;
        BaseFont bfurl = null;
        PdfContentByte under;
        PdfContentByte over;

        try {

            if (imageFile != null) {
                img = Image.getInstance(imageFile.getAbsolutePath());
                img.setAbsolutePosition(150, 300);
            }

            bfont = BaseFont.createFont(fondFile.getAbsolutePath(),
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            // 默认构造方法
            bfurl = BaseFont.createFont();

        } catch (BadElementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 得到目标目录的所有文件
        File[] listRootFiles = srcDir.listFiles();
        String desFileName = "";
        int count = 0;

        for (int m = 0; m < listRootFiles.length; m++) {// 循环每个文件

            String filePath = listRootFiles[m].getAbsolutePath();
            String fileName = listRootFiles[m].getName();

            if (!FilenameUtils.getExtension(fileName.toUpperCase()).equals(
                    "PDF"))
                continue;

            // 目标文件
            desFileName = destDir.getPath() + File.separator + "watermark_"
                    + fileName;

            try {
                // we create a reader for a certain document
                PdfReader reader = getPdfReader(listRootFiles[m]);
                int pageNumbers = reader.getNumberOfPages();
                // we create a stamper that will copy the document to a new file
                PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
                        desFileName));
                // adding some metadata
                // 可以设置文档的各种属性，例如作者,title 等
                HashMap<String, String> moreInfo = new HashMap<String, String>();
                moreInfo.put("Author", "H819 create");

                stamp.setMoreInfo(moreInfo);

                // adding content to each page
                int number = 0;
                while (number < pageNumbers) {// 逐页添加
                    number++;

                    // 图片
                    if (imageFile != null) {
                        under = stamp.getUnderContent(number);
                        // under = stamp.getOverContent(number);
                        under.addImage(img);
                    }

                    // === 添加文字
                    if (companyName != null && !companyName.equals("")) {
                        over = stamp.getOverContent(number);

                        // 文字开始声明
                        over.beginText();

                        // 设置字体颜色灰度
                        // gray - a value between 0 (black) and 1 (white)
                        over.setGrayFill(0.6f);

                        // 开始设置文字
                        // Shows text kerned right, left or center aligned with
                        // rotation.
                        // showTextAlignedKerned(int alignment, String text,
                        // float
                        // x, float y, float rotation)
                        //
                        // over.showTextAligned(Element.ALIGN_LEFT, "page " +
                        // number,30, 30, 0);

                        // 分别设置字体,大小,颜色:单位名称
                        over.setFontAndSize(bfont, 35);
                        // red ,green ,blue
                        over.setRGBColorFill(32, 178, 170);
                        over.showTextAligned(Element.ALIGN_CENTER, companyName
                                + copyRight, 290, 470, 45);
                        // 分别设置字体,大小,颜色 :发布单位名称
                        over.setFontAndSize(bfont, 18);
                        over.showTextAligned(Element.ALIGN_CENTER,
                                adminCommpanyName, 310, 430, 45);
                        // 分别设置字体,大小,颜色 :发布单位网址
                        over.setGrayFill(0.8f);
                        over.setFontAndSize(bfurl, 10);
                        over.setRGBColorFill(32, 178, 170);
                        over.showTextAligned(Element.ALIGN_CENTER,
                                adminCompanyWebSiteUrl, 320, 420, 45);

                        // 文字结束声明
                        over.endText();
                    }

                }
                stamp.close();
                count++;
                logger.info(desFileName + " create.");

            } catch (Exception e) {
                logger.info(filePath + " create error.");
                e.printStackTrace();
            }
        }// 文件循环完毕
        logger.info("total " + count + " files create.");
    }

    /**
     * 压缩已经存在的 pdf 文件。实例了处理已经存在的文件的方法
     *
     * @param srcPdfFile  the original PDF
     * @param descPdfFile the resulting PDF
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public static void compressPdf(File srcPdfFile, File descPdfFile)
            throws IOException, DocumentException {

        if (srcPdfFile == null || !srcPdfFile.exists())
            throw new IOException("src pdf file '" + srcPdfFile
                    + "' does not exsit.");

        PdfReader reader = getPdfReader(srcPdfFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                descPdfFile.getAbsoluteFile()), PdfWriter.VERSION_1_5);
        stamper.getWriter().setCompressionLevel(9);
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            reader.setPageContent(i, reader.getPageContent(i));
        }
        stamper.setFullCompression();
        stamper.close();
    }

    /**
     * 计算指定目录中所有的pdf文本的页数，包括子目录。用到了递归方法，返回值被全局变量记录，通过 getPageCount()函数调用
     *
     * @param srcPDFFileDir 待计算的文件夹路径
     * @return
     * @throws MyExceptionUtils
     */
    private static void countNumberOfPages(File srcPDFFileDir)
            throws MyExceptionUtils {

        if (srcPDFFileDir == null || !srcPDFFileDir.isDirectory())
            throw new MyExceptionUtils(srcPDFFileDir
                    + "'is null or dose not exist.");

        try {

            Collection<File> listPDFs = FileUtils.listFiles(srcPDFFileDir,
                    null, true);
            PdfReader reader = null;
            for (File f : listPDFs) {

                // 递归文件夹
                if (f.isDirectory())
                    countNumberOfPages(f);

                if (!FilenameUtils.getExtension(f.getName().toUpperCase())
                        .equals("PDF"))
                    continue;
                // System.out.println(f.getPath());
                // we create a reader for a certain document
                reader = getPdfReader(f);
                // we retrieve the total number of pages
                // 对加密的文件会抛出异常
                numberOfPagesTemp = numberOfPagesTemp
                        + reader.getNumberOfPages();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates a PDF file src with the file dest as result
     *
     * @param srcPdfFile  the original PDF
     * @param descPdfFile the resulting PDF
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public static void decompressPdf(File srcPdfFile, File descPdfFile)
            throws IOException, DocumentException {

        if (srcPdfFile == null || !srcPdfFile.exists())
            throw new IOException("src pdf file '" + srcPdfFile
                    + "' does not exsit.");

        PdfReader reader = getPdfReader(srcPdfFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                descPdfFile.getAbsoluteFile()));
        Document.compress = false;
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            reader.setPageContent(i, reader.getPageContent(i));
        }
        stamper.close();
        Document.compress = true;
    }

    /**
     * 通过第三方提供的工具，破解没有设置打开密码的 pdf 文件。可以递归破解指定文件夹内的所有文件，并保留原来的目录结构
     * <p/>
     * windows 下面 "PDF Password Remover v3.1" 软件提供命令行破解，通过系统调用该命令行，进行破解
     *
     * @param srcPdfFileDir 存放待破解 pdf 文件的文件夹. 破解之后的文件，存放子默认的文件夹内。
     * @throws java.io.IOException
     */
    public static void decryptFiles(File srcPdfFileDir) throws IOException {

        String descPdfFileDir = FilenameUtils.separatorsToSystem(srcPdfFileDir
                .getParent() + File.separator + "decryptPdfDir");
        String badPDFFileDir = FilenameUtils.separatorsToSystem(srcPdfFileDir
                .getParent() + File.separator + "badPdfDir");
        decryptFiles(srcPdfFileDir, new File(descPdfFileDir), new File(
                badPDFFileDir));

    }

    /**
     * 通过第三方提供的工具，破解没有设置打开密码的 pdf 文件。可以递归破解指定文件夹内的所有文件，并保留原来的目录结构
     * <p/>
     * windows 下面 "PDF Password Remover v3.1" 软件提供命令行破解，通过系统调用该命令行，进行破解
     *
     * @param srcDirectoryPath  存放待破解 pdf 文件的文件夹
     * @param descDirectoryPath 存放破解之后的 pdf 文件的文件夹
     * @param badDirectoryPath  存放有打开密码，不能破解的 pdf 的文件夹。
     * @throws java.io.IOException
     */
    public static void decryptFiles(File srcDirectoryPath, File descDirectoryPath,
                                    File badDirectoryPath) throws IOException {

        if (!isEnoughtSpace(srcDirectoryPath, descDirectoryPath))
            return;

        // Finds files within a given directory (and optionally its subdirectories) which match an array of extensions.
        // false : 不递归查找
        File[] listFiles = srcDirectoryPath.listFiles();

        if (listFiles.length == 0) {
            logger.info("srcPdfFileDir has not file. " + srcDirectoryPath.getAbsolutePath());
            return;
        }

        for (File f : listFiles) {
            String fileName = f.getName();
            String extension = FilenameUtils.getExtension(fileName);
            if (f.isFile()) {
                if (!extension.equalsIgnoreCase("pdf"))
                    continue;

                File badFilePath = new File(badDirectoryPath.getAbsolutePath()
                        + File.separator + fileName);

                // 损坏的 0 字节文件，直接拷贝到统一的文件夹
                if (FileUtils.sizeOf(f) == 0) {
                    // 拷贝
                    FileUtils.copyFile(f, badFilePath);
                    continue;
                }

                PdfReader reader = null;
                try {
                    reader = getPdfReader(f);
                } catch (BadPasswordException e) {
                    // 有打开密码的文件，不能破解，统一拷贝至一个文件夹。
                    e.printStackTrace();
                    logger.info("\""
                            + f.getAbsolutePath()
                            + "\" has user password,can'nt decrypt.copy to \""
                            + badDirectoryPath.getAbsolutePath() + "\" .");

                    if (!badDirectoryPath.exists() || !badDirectoryPath.isDirectory())
                        FileUtils.forceMkdir(badDirectoryPath);
                    // 拷贝
                    FileUtils.copyFile(f, badFilePath);
                    continue;
                } catch (Exception e2) {


                    if (!badDirectoryPath.exists() || !badDirectoryPath.isDirectory())
                        FileUtils.forceMkdir(badDirectoryPath);
                    // 损坏的 0 字节文件，也应该发生异常
                    // 其他的任何处理不了的异常，均拷贝logger.info(f.getAbsolutePath() + " is exception!");

                    // 拷贝
                    FileUtils.copyFile(f, badFilePath);
                    e2.printStackTrace();
                    continue;
                }

                File fileDesc = new File(descDirectoryPath.getAbsolutePath()
                        + File.separator + fileName);

                // logger.info("fileDesc name :" + fileDesc.getAbsolutePath());

                if (!reader.isEncrypted()) {// 未加密的文件，直接拷贝到目标文件夹
                    FileUtils.copyFile(f, fileDesc);
                    logger.info("not encrypted,copy " + f.getAbsolutePath()
                            + " to " + fileDesc.getAbsolutePath());
                    continue;
                }

                // 注意命令行的写法：有空格的命令字符串，都要加上引号
                String cmd = "cmd.exe /c call \""
                        + FilenameUtils
                        .separatorsToSystem("C:\\pdfutils\\pdfdecrypt.exe \" -i "
                                + "\""
                                + f.getAbsolutePath()
                                + "\""
                                + " -o "
                                + "\""
                                + fileDesc.getAbsolutePath())
                        + "\"";

                logger.info("encrypted " + f.getAbsolutePath() + " to "
                        + fileDesc.getAbsolutePath());

                logger.info("cmd is :" + cmd);
                Runtime.getRuntime().exec(cmd);
                // 关闭处理完成的文件
                reader.close();


            }// end if f.isFile
            else if (f.isDirectory()) {
                decryptFiles(f, new File(descDirectoryPath.getAbsolutePath()
                        + File.separator + fileName), badDirectoryPath);
            }// end if f.isDirectory
            else
                continue;

        }// end for
        logger.info("finished !");

    }

    /**
     * pdf 解密。仅能解密 owner 属性，不能解密 user 属性(owner 和 user 含义见加密方法)。
     * <p/>
     * 如果 pdf 文件 设置了打开密码，没有设置拥有者密码，不能解密
     * <p/>
     * 如果 pdf 文件设置了打开密码和拥有者密码，用拥有者密码可以解密,解密之后，pdf 文件拥有了所有编辑属性
     *
     * @param srcPdfFile    源文件
     * @param descPdfFile   目标文件
     * @param ownerPassword 拥有者密码，授权编辑文件.
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public static void decryptFile(File srcPdfFile, File descPdfFile,
                                   String ownerPassword) throws IOException, DocumentException {

        byte[] OWNER = null;
        /** Owner password. */
        if (ownerPassword != null)
            OWNER = ownerPassword.getBytes();

        PdfReader reader = getPdfReader(srcPdfFile, ownerPassword);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                descPdfFile.getAbsoluteFile()));
        stamper.close();

    }


    /**
     * 加密 pdf 文件
     *
     * @param srcPdfFile        源文件
     * @param descPdfFile       目标文件
     * @param userPassword      用户密码，授权打开文件 ,null 为没有密码
     * @param ownerPassword     拥有者密码，授权编辑文件 ,null 为没有密码
     * @param allow_HideMenubar 是否显示菜单栏
     * @param allow_HideToolbar 是否显示工具栏
     * @param allow_printing    是否允许打印
     * @param allow_copy        是否允许拷贝文字
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public static void encryptPdf(File srcPdfFile, File descPdfFile,
                                  String userPassword, String ownerPassword,
                                  boolean allow_HideMenubar, boolean allow_HideToolbar,
                                  boolean allow_printing, boolean allow_copy) throws IOException {

        if (srcPdfFile == null || !srcPdfFile.exists())
            throw new IOException("src pdf file '" + srcPdfFile
                    + "' does not exsit.");

        byte[] USER = null;
        byte[] OWNER = null;

        /** User password. */
        if (userPassword != null)
            USER = userPassword.getBytes();
        /** Owner password. */
        if (ownerPassword != null)
            OWNER = ownerPassword.getBytes();

        try {

            PdfReader reader = getPdfReader(srcPdfFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                    descPdfFile.getAbsoluteFile()));

            // 设置显示属性
            // 参数含义参见 PdfWriter.setViewerPreferences 方法 和 addViewerPreference
            // 根据 api 提示，有的参数利用 addViewerPreference 方法比较合适
            // 参数可以按照位"|"(或)方式连续写, 但个别参数，如 PdfWriter.HideWindowUI
            // 加上之后，其他参数就不好用了, 实际使用的时候，要逐个测试

            // 默认属性(这几个属性用 addViewerPreference 方法设置)
            stamper.getWriter().addViewerPreference(PdfName.CENTERWINDOW,
                    PdfBoolean.PDFTRUE);
            stamper.getWriter().addViewerPreference(PdfName.DISPLAYDOCTITLE,
                    PdfBoolean.PDFTRUE);
            stamper.getWriter().addViewerPreference(PdfName.FITWINDOW,
                    PdfBoolean.PDFTRUE);
            // writer.addViewerPreference(PdfName.HIDEWINDOWUI,
            // PdfBoolean.PDFTRUE);

            if (!allow_HideToolbar)
                stamper.getWriter().addViewerPreference(PdfName.HIDETOOLBAR,
                        PdfBoolean.PDFTRUE);

            if (!allow_HideMenubar)
                stamper.getWriter().addViewerPreference(PdfName.HIDEMENUBAR,
                        PdfBoolean.PDFTRUE);

            // 设置 allow 属性
            if (allow_printing && allow_copy)
                stamper.setEncryption(USER, OWNER, PdfWriter.ALLOW_PRINTING
                        | PdfWriter.ALLOW_COPY, true);

            else if (allow_printing)
                stamper.setEncryption(USER, OWNER, PdfWriter.ALLOW_PRINTING,
                        true);

            else if (allow_copy)
                stamper.setEncryption(USER, OWNER, PdfWriter.ALLOW_COPY, true);

            else
                stamper.setEncryption(USER, OWNER, 0, true);

            stamper.close();

            logger.info(srcPdfFile.getAbsoluteFile() + " encrypt finished.");

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 检查文件夹内的文件是否被加密，可以递归
     *
     * @param srcPdfFileDir 待检查的文件夹
     * @throws java.io.IOException
     */
    public static void findEcryptPdf(File srcPdfFileDir) throws IOException {

        encryptPdfNamesTemp = new StringBuffer();

        testEcryptPdf(srcPdfFileDir);

        if (encryptPdfNamesTemp.length() == 0)
            FileUtils.writeStringToFile(new File(srcPdfFileDir.getParent()
                    + File.separator + "ecrypt.txt"), "没有被加密的文件");
        else
            FileUtils.writeStringToFile(new File(srcPdfFileDir.getParent()
                            + File.separator + "ecrypt.txt"),
                    encryptPdfNamesTemp.toString());

        logger.info(" finished!");

    }

    /**
     * 定义中文字体，项目需要加载 iTextAsian.jar
     *
     * @return
     */
    public static Font getChineseFont() {
        Font fontChinese = null;
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bfChinese, 12, Font.NORMAL);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return fontChinese;
    }

    /**
     * 计算指定目录中所有的pdf文本的页数，包括子目录。
     *
     * @param srcPdfFileDir 待计算的文件夹路径
     * @return
     * @throws MyExceptionUtils
     */
    public static int getNumberOfPages(File srcPdfFileDir)
            throws MyExceptionUtils {

        numberOfPagesTemp = 0;
        countNumberOfPages(srcPdfFileDir);
        return numberOfPagesTemp;
    }

    /**
     * 判断目标盘是否有足够的空间拷贝源文件夹
     *
     * @param srcPdfFileDir
     * @param descPdfFileDir
     * @return
     */
    private static boolean isEnoughtSpace(File srcPdfFileDir,
                                          File descPdfFileDir) {


        if (!srcPdfFileDir.exists() && !srcPdfFileDir.isDirectory()) {
            logger.info(srcPdfFileDir.getAbsolutePath() + " not exist.");
            return false;
        }

        try {
            MyFileUtils.forceMkdir(descPdfFileDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // 检查磁盘空间是否充足

        // 目标盘剩余空间
        long prefixDiskFreeSize = descPdfFileDir.getFreeSpace();
        // 源文件夹大小
        long srcSize = FileUtils.sizeOf(srcPdfFileDir);

        // logger.info(descPdfFileDir.getAbsolutePath() + " 目标盘剩余空间："
        // + prefixDiskFreeSize / 1000000.00 + " M");
        // logger.info(srcPdfFileDir.getAbsolutePath() + " 原文件夹大小 :" + srcSize
        // / 1000000.00 + " M");

        if (prefixDiskFreeSize < srcSize) {

            logger.info(FilenameUtils.getPrefix(descPdfFileDir.getAbsolutePath())
                    + " has not enoght disk size .");

            return false;
        }

        return true;

    }

    /**
     * 合并文件
     * <p>
     * 源自 iText in Action 2nd Edition，Chapter 6: Working with existing PDFs
     * ，Concatenate
     * </p>
     *
     * @param files  待合并的文件列表
     * @param result 输出合并后的结果
     * @throws DocumentException
     * @throws java.io.IOException
     */
    public static void merge(File[] files, File result)
            throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2

        /**
         * PdfCopy 与 PdfSmartCopy 区别
         *
         * PdfCopy：直接合并，生成速度快，节省内存
         *
         * PdfSmartCopy：会合并冗余数据，文件体积变小，但需要运算，耗时
         */
        //
        // PdfCopy copy = new PdfCopy(document, new FileOutputStream(result));
        PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(
                result));
        // step 3
        document.open();
        // step 4
        PdfReader reader;
        int n;
        // loop over the documents you want to concatenate
        for (int i = 0; i < files.length; i++) {
            reader = getPdfReader(files[i]);
            // loop over the pages in that document
            n = reader.getNumberOfPages();
            for (int page = 0; page < n; ) {
                copy.addPage(copy.getImportedPage(reader, ++page));
            }
            copy.freeReader(reader);
        }
        // step 5
        document.close();
    }

    /**
     * 去除附在 PDF 属性 field 上的 javaScript
     * <p/>
     * pdf 文件可以添加 javaScript，来实现某种功能，例如激发对话框等动作。
     * <p/>
     * javaScript 可以附加于某个 filed，此时去掉 这个 filed，使 javaScript 代码因缺少变量而无法执行
     * <p/>
     * 即可以达到去除 javaScript 的目的。
     * <p/>
     * 通过 itext 的 reader.getJavaScript()方法，可以查看 pdf 文件 JavaScript源代码(adobe pro
     * 控制台也可以看到源码)
     * <p/>
     * 通过源代码可以得到具体的 field 名称
     *
     * @param srcPdfFileDir   源文件夹
     * @param descPdfFileDir  目标文件夹
     * @param removeFieldName 有 javaScript 限制的字段名称
     * @throws java.io.IOException
     */
    public static void removeFieldJavaScript(File srcPdfFileDir,
                                             File descPdfFileDir, String removeFieldName) throws IOException {

        if (!srcPdfFileDir.isDirectory()) {
            logger.info("srcPdfFileDir is not a Directory: "
                    + srcPdfFileDir.getAbsolutePath());
            return;
        }

        File listFiles[] = srcPdfFileDir.listFiles();

        if (listFiles.length == 0) {
            logger.info("srcPdfFileDir has not file. "
                    + srcPdfFileDir.getAbsolutePath());
            return;
        }

        MyFileUtils.forceMkdir(descPdfFileDir);

        // 检查磁盘空间是否充足

        // 目标盘剩余空间
        long prefixDiskFreeSize = descPdfFileDir.getFreeSpace();
        // 源文件夹大小
        long srcSize = FileUtils.sizeOf(srcPdfFileDir);

        logger.info(descPdfFileDir.getAbsolutePath() + " 目标盘剩余空间："
                + prefixDiskFreeSize / 1000000.00 + " M");
        logger.info(srcPdfFileDir.getAbsolutePath() + " 原文件夹大小 :" + srcSize
                / 1000000.00 + " M");

        if (prefixDiskFreeSize < srcSize) {

            logger.info(FilenameUtils.getPrefix(descPdfFileDir.getAbsolutePath())
                    + " has not enoght disk size .");

            return;
        }

        // logger.info(descPdfFileDir.getAbsolutePath());

        for (File f : listFiles) {
            String fileName = f.getName();
            String extensiion = FilenameUtils.getExtension(fileName)
                    .toLowerCase();

            PdfReader reader = null;

            PdfStamper stamper = null;

            if (f.isFile()) {
                if (extensiion.equals("pdf")) {

                    reader = getPdfReader(f);

                    File fileDesc = new File(descPdfFileDir.getAbsolutePath()
                            + File.separator + fileName);

                    try {
                        stamper = new PdfStamper(reader,
                                FileUtils.openOutputStream(fileDesc));

                        /**
                         * 在无法确定表单名称的时候，都去掉
                         * **/
                        // reader.removeFields();

                        /**
                         * 能确定表单名称，仅去掉附带 javaScript 的表单
                         * **/
                        AcroFields form = stamper.getAcroFields();
                        form.removeField(removeFieldName);

                        stamper.close();
                        reader.close();

                    } catch (DocumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else
                    continue;

            }// end if f.isFile

            else if (f.isDirectory()) {
                removeFieldJavaScript(f,
                        new File(descPdfFileDir.getAbsolutePath()
                                + File.separator + fileName), removeFieldName);
            }// end if f.isDirectory
            else
                continue;

        }// end for

        logger.info("finished !");
    }

    /**
     * 为已经存在的pdf文件添加使用日期限制，在警告期内可以查看，过期之后，直接关闭。
     * <p>
     * 由于是用 javaScript 做限制，浏览器禁止 javaScript 或者调整客户端电脑的时间，均失效。
     * </p>
     *
     * @param srcPdfFileDir  源文件夹
     * @param descPdfFileDir 目标文件夹
     * @param startDate      开始日期，字符串格式需为 "2011,01,01"
     * @param alerDays       从开始日期计算，出现警告日期的天数。到达警告期后，出现警告信息，但可以查看文本
     * @param expiredDays    从开始日期计算，到达失效日期的天数。到达失效期后，出现警告信息后，直接关闭文本，不允许查看文本。
     * @throws java.io.IOException
     */
    public static void setExpireDateWithJavaScript(File srcPdfFileDir,
                                                   File descPdfFileDir, String startDate, int alerDays, int expiredDays)
            throws IOException {

        // 简单判断开始日期格式
        if (alerDays >= expiredDays) {
            logger.info(" '警告天数 ' " + alerDays + " 大于 '过期天数' " + expiredDays);
            return;
        }

        // 简单判断开始日期格式，可以用 正则表达式
        if (MyStringUtils.countMatches(startDate, ",") != 2) {
            logger.info(startDate + " 开始日期格式录入不对，应该型如  '2011,01,01' 形式");
            return;
        }

        if (!isEnoughtSpace(srcPdfFileDir, descPdfFileDir))
            return;

        File listFiles[] = srcPdfFileDir.listFiles();

        if (listFiles.length == 0) {
            logger.info("srcPdfFileDir has not file. "
                    + srcPdfFileDir.getAbsolutePath());
            return;
        }

        // 拷贝资源文件到临时目录
        String[] resources = new String[]{"/pdfexpiration.js"};
        String[] resPath = MyFileUtils.copyResourceFileFromJarLibToTmpDir(
                resources, "jspdftemp", PDFUtilsBase.class);
        // 得到资源文件内容
        String jsStr = FileUtils.readFileToString(new File(resPath[0]));

        /** 替换 js 文件中的字符串，作为输入条件 */
        // 替换开始日期
        jsStr = MyStringUtils.replace(jsStr, "2011,01,01", startDate);
        // 替换到达警告期的天数
        jsStr = MyStringUtils.replace(jsStr, "alertDays = 355", "alertDays = "
                + Integer.toString(alerDays));
        // 替换到达过期天数
        jsStr = MyStringUtils.replace(jsStr, "expiredDays = 365",
                "expiredDays = " + Integer.toString(expiredDays));

        System.out.println(jsStr);

        // logger.info(descPdfFileDir.getAbsolutePath());

        for (File f : listFiles) {
            String fileName = f.getName();
            String extensiion = FilenameUtils.getExtension(fileName)
                    .toLowerCase();

            if (f.isFile()) {
                if (extensiion.equals("pdf")) {

                    PdfReader reader = getPdfReader(f);

                    File fileDesc = new File(descPdfFileDir.getAbsolutePath()
                            + File.separator + fileName);

                    try {
                        PdfStamper stamper = new PdfStamper(reader,
                                new FileOutputStream(fileDesc));
                        // Get the writer (to add actions and annotations)
                        PdfWriter writer = stamper.getWriter();
                        PdfAction action = PdfAction.javaScript(jsStr, writer,
                                true);
                        // 给文件添加 javaScript 代码
                        stamper.setPageAction(PdfWriter.PAGE_OPEN, action, 1);
                        // Close the stamper
                        stamper.close();
                        reader.close();

                        logger.info(fileDesc.getAbsolutePath() + " 加密完成 !");

                    } catch (DocumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else
                    continue;

            }// end if f.isFile

            else if (f.isDirectory()) {
                setExpireDateWithJavaScript(f,
                        new File(descPdfFileDir.getAbsolutePath()
                                + File.separator + fileName), startDate,
                        alerDays, expiredDays);
            }// end if f.isDirectory
            else
                continue;

        }// end for
    }

    /**
     * 分割文件
     * <p/>
     * <p>
     * 源自 iText in Action 2nd Edition，Chapter 6: Working with existing PDFs
     * ，SelectPages
     * </p>
     *
     * @param src       源文件
     * @param desc      目标文件
     * @param beginPage 起始页
     * @param endPage   结束页
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public static void splitToPages(File src, File desc,
                                    int beginPage, int endPage) throws IOException, DocumentException {

        PdfReader reader = getPdfReader(src);
        // reader.selectPages("4-8");
        reader.selectPages(Integer.toString(beginPage) + "-"
                + Integer.toString(endPage));
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                desc));
        stamper.close();
    }

    /**
     * 检查文件夹内的文件是否被加密，用到了递归方法，返回值被全局变量记录，通过 findEcryptPdf()函数调用
     *
     * @param srcPdfFileDir 待检查的文件夹
     * @throws java.io.IOException
     */
    private static void testEcryptPdf(File srcPdfFileDir) throws IOException {

        encryptPdfNamesTemp = new StringBuffer();

        if (!srcPdfFileDir.isDirectory()) {
            logger.info("srcPdfFileDir is not a Directory: "
                    + srcPdfFileDir.getAbsolutePath());
            return;
        }

        File listFiles[] = srcPdfFileDir.listFiles();

        if (listFiles.length == 0) {
            logger.info("srcPdfFileDir has not file. "
                    + srcPdfFileDir.getAbsolutePath());
            return;
        }

        // logger.info(descPdfFileDir.getAbsolutePath());

        for (File f : listFiles) {
            String fileName = f.getName();
            String extensiion = FilenameUtils.getExtension(fileName)
                    .toLowerCase();

            if (f.isFile()) {

                if (extensiion.equals("pdf")) {

                    // 损坏的 0 字节文件，文件体积大于 40 M， 直接登记
                    if (FileUtils.sizeOf(f) == 0
                            || FileUtils.sizeOf(f) > 40 * 1000000) {

                        encryptPdfNamesTemp.append("size > 40M :"
                                + f.getAbsolutePath() + "\n");
                        continue;
                    }

                    PdfReader reader = null;
                    try {
                        reader = getPdfReader(f);
                    } catch (BadPasswordException e) {
                        // 有打开密码的文件，不能破解，直接登记
                        e.printStackTrace();
                        encryptPdfNamesTemp.append("can not open :"
                                + f.getAbsolutePath() + "\n");
                        continue;
                    } catch (Exception e2) {

                        encryptPdfNamesTemp.append("other exception :"
                                + f.getAbsolutePath() + "\n");
                        e2.printStackTrace();
                        continue;

                    }
                    // logger.info("fileDesc name :" + fileDesc.getAbsolutePath());

                    if (reader.isEncrypted()) {// 未加密的文件，，直接登记
                        encryptPdfNamesTemp.append("encrypted :"
                                + f.getAbsolutePath() + "\n");
                        reader.close();
                        continue;
                    } else {
                        logger.info("not encrypted :" + f.getAbsoluteFile());
                        reader.close();
                        continue;
                    }

                }// end if f.isFile
                else
                    continue;
            } else if (f.isDirectory()) {
                testEcryptPdf(f);
            }// end if f.isDirectory
            else
                continue;

        }// end for

    }

    public static void main(String[] args) throws IOException,
            MyExceptionUtils, DocumentException {
        // TODO Auto-generated method stub

        File f = new File("d:\\C1068500.pdf");
        File f2 = new File("d:\\C1068500.pdf");
        File f3 = new File("d://field_actions.pdf");
        File f4 = new File("d://sm.pdf");
        File f5 = new File("d://sm_after.pdf");
        File f6 = new File("d://000");
        File f7 = new File("d://0000");
        File f8 = new File("D:\\download\\DINISO2859-1Y2014(ED).PDF");
        File f9 = new File("D:\\download\\all.pdf");
        FileInputStream f8Stream = new FileInputStream(f8);

        File[] ff = {f8, f9};

        PDFUtilsBase t = new PDFUtilsBase();
        // p.addWaterMarkToCapital(new File("D:/001/"), new File("d:/002/"),
        // null,
        // "北京");

        // 测试加密解密
        //  t.encryptPdf(f8, new File("D:/encryptjava.pdf"), null, null, false, false, true, true);
        // test.decryptPdf(new File("D:/001/encryptjava.pdf"), new File(
        // "D:/001/encryptjava_deencryptjava.pdf"), "hui");
        // p.encryptPdf("D:/001/java.pdf", "D:/001/encryptjava2.pdf");
        // t.testBigPdfFileRead();

        // 测试 javsScript 语句
        // test.addScriptActionsExample(new File("D:\\scriptsample.pdf"));
        // t.addScriptExample(f4.getAbsolutePath(), f5.getAbsolutePath());
        //	PDFUtilsBase.setExpireDateWithJavaScript(f6, f7, "2011,02,01", 20, 30);

        // 非递归形式实现破解 pdf
        // PDFUtilsBase.decryptPdf2(new File("D:\\download\\ansi\\"), new File(
        // "D:\\download\\des\\"), new File("D:\\download\\bad\\"));

        //
        // t.getPageInfomation(f3);
        // t.addScriptExample("d:\\hello_reverse.pdf", "d:\\1.pdf");

        // 测试删除 js
        // t.removeJavaScript(new File("d:\\23"),new
        // File("d:\\333"),"expireCNS");
        // System.out.println(new File("d:\\01").getFreeSpace() /1000000.00);

        // 测试添加 js

        // t.setExpireDateWithJavaScript(new File("d:\\23"), new
        // File("d:\\333"),"");

        // 合并文件测试
        t.merge(ff, new File("d:\\merge.pdf"));

    }

    // http://www.adobe.com/content/dam/Adobe/en/devnet/acrobat/pdfs/js_api_reference.pdf
    // adobe 有 javaScript 参考文档

    /**
     * 优化 PdfReader 读取 pdf 文件，如果 pdf 体积过大，也可以读取，不会产生内存溢出
     *
     * @param pdfFile 待读取的 pdf 文件
     * @return PdfReader 实例
     * @throws IOException
     */
    protected static PdfReader getPdfReader(File pdfFile) throws IOException {

        FileInputStream fileStream = new FileInputStream(pdfFile);
        return new PdfReader(new RandomAccessFileOrArray(new FileChannelRandomAccessSource(fileStream.getChannel())), null);
    }


    /**
     * 优化 PdfReader 读取 pdf 文件，如果 pdf 体积过大，也可以读取
     *
     * @param pdfFile       待读取的 pdf 文件
     * @param ownerPassword 用户打开文件密码
     * @return
     * @throws IOException
     */
    protected static PdfReader getPdfReader(File pdfFile, String ownerPassword) throws IOException {

        FileInputStream fileStream = new FileInputStream(pdfFile);

        if (ownerPassword == null)
            return getPdfReader(pdfFile);

        return new PdfReader(new RandomAccessFileOrArray(new FileChannelRandomAccessSource(fileStream.getChannel())), ownerPassword.getBytes());
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
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public void addScriptExample(File src, File dest) throws IOException,
            DocumentException {
        // Create a reader
        PdfReader reader = getPdfReader(src);
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
     * 添加文件为背景，可作为添加水印方法。。
     * <p>
     * 此方法可以作为添加水印的方法。背景文件可以用 word 生成，格式和字体任意设置，之后可以作为新生成的文件的背景。
     * </p>
     * <p>
     * 源自 iText in Action 2nd Edition，Chapter 6: Working with existing PDFs
     * ，StampStationery
     * </p>
     *
     * @param src      the original PDF
     * @param template a PDF that will be added as background
     * @param dest     the resulting PDF
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public void addWatermarkWithTemplate(File src, File template,
                                         File dest) throws IOException, DocumentException {
        // Create readers
        PdfReader reader = getPdfReader(src);
        PdfReader s_reader = getPdfReader(template);
        // Create the stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // Add the stationery to each page
        PdfImportedPage page = stamper.getImportedPage(s_reader, 1);
        int n = reader.getNumberOfPages();
        PdfContentByte background;
        for (int i = 1; i <= n; i++) {
            background = stamper.getUnderContent(i);
            background.addTemplate(page, 0, 0);
        }
        // CLose the stamper
        stamper.close();
    }

    /**
     * 获取 pdf 文件属性信息
     *
     * @param srcPdfFile
     */
    public void getPageInfomation(File srcPdfFile) {

        try {

            PdfReader reader = getPdfReader(srcPdfFile);

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

    private void test() {
    }


}
