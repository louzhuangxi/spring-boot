package org.h819.commons.file;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.io.FileChannelRandomAccessSource;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.h819.commons.MyConstants;
import org.h819.commons.MyExecUtils;
import org.h819.commons.exe.ExecParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.List;

/**
 * @author H819
 * @version V1.0
 * @Title: PDFBaseUtils.java
 * @Description: TODO(基础pdf处理类)
 * @date 2015-3-1
 */

//http://tutorials.jenkov.com/java-itext/index.html

//html->pdf
//http://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-creating-pdf-documents-with-wkhtmltopdf/
//http://wkhtmltopdf.org/
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

public class MyPDFUtils {

    // 该变量专门为 countPages() 函数设立，由于使用了递归，故提到全局变量，否则每次的返回值不能累计
    // private static StringBuffer encryptPdfNamesTemp;
    private static List<String> tempList = new ArrayList<>();

    // 该变量专门为 countPages() 函数设立，由于使用了递归，故提到全局变量，否则每次的返回值不能累计
    private static int numberOfPagesOfDirectory;

    private static Logger logger = LoggerFactory.getLogger(MyPDFUtils.class);

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
     * 静态调用
     */
    private MyPDFUtils() {

    }


    /**
     * 单个文件添加水印
     *
     * @param srcPdf         源文件
     * @param destPdf        目标文件
     * @param waterMarkImage 水印图片
     */
    public static void addWaterMarkFile(File srcPdf, File destPdf, File waterMarkImage) throws IOException, DocumentException {
        addWaterMarkFile(srcPdf, destPdf, null, waterMarkImage);
    }


    /**
     * 单个文件添加水印
     *
     * @param srcPdf        源文件
     * @param destPdf       目标文件
     * @param waterMarkText 水印文字
     */
    public static void addWaterMarkFile(File srcPdf, File destPdf, String waterMarkText) throws IOException, DocumentException {
        addWaterMarkFile(srcPdf, destPdf, waterMarkText, null);
    }


    /**
     * 文件夹中所有文件添加水印
     *
     * @param srcPdfFileDirectory  源文件夹
     * @param descPdfFileDirectory 目标文件夹
     * @param waterMarkImage       水印图片
     */
    public static void addWaterMarkDerictory(File srcPdfFileDirectory, File descPdfFileDirectory, File waterMarkImage) throws IOException, DocumentException {

        if (descPdfFileDirectory.getAbsolutePath().contains(srcPdfFileDirectory.getAbsolutePath()))
            throw new IOException("目标文件夹不能在原文件夹中");


        if (!isEnoughSpace(srcPdfFileDirectory, descPdfFileDirectory))
            return;

        for (File file : srcPdfFileDirectory.listFiles()) {
            String path = descPdfFileDirectory.getAbsolutePath() + File.separator + file.getName();
            if (file.isFile()) {
                addWaterMarkFile(file, Paths.get(path).toFile(), null, waterMarkImage);
            } else if (file.isDirectory())
                addWaterMarkDerictory(file, Paths.get(path).toFile(), waterMarkImage);
            else return;

        }
    }


    /**
     * 文件夹中所有文件添加水印
     *
     * @param srcPdfFileDirectory  源文件夹
     * @param descPdfFileDirectory 目标文件夹
     * @param waterMarkText        水印文字
     */
    public static void addWaterMarkDerictory(File srcPdfFileDirectory, File descPdfFileDirectory, String waterMarkText) throws IOException, DocumentException {

        if (descPdfFileDirectory.getAbsolutePath().contains(srcPdfFileDirectory.getAbsolutePath()))
            throw new IOException("目标文件夹不能在原文件夹中");

        if (!isEnoughSpace(srcPdfFileDirectory, descPdfFileDirectory))
            return;

        for (File file : srcPdfFileDirectory.listFiles()) {
            String path = descPdfFileDirectory.getAbsolutePath() + File.separator + file.getName();
            if (file.isFile()) {
                addWaterMarkFile(file, Paths.get(path).toFile(), waterMarkText);
            } else if (file.isDirectory())
                addWaterMarkDerictory(file, Paths.get(path).toFile(), waterMarkText);
            else return;

        }

    }


    /**
     * 单个文件添加水印
     *
     * @param srcPdf         源文件
     * @param destPdf        目标文件
     * @param waterMarkText  水印文字
     * @param waterMarkImage 水印图片
     */
    public static void addWaterMarkFile(File srcPdf, File destPdf, String waterMarkText, File waterMarkImage) throws IOException, DocumentException {

        if (waterMarkText == null && waterMarkImage == null)
            throw new FileNotFoundException(waterMarkText + " " + waterMarkImage + " all null.");


        if (srcPdf == null || !srcPdf.exists() || !srcPdf.isFile())
            throw new FileNotFoundException("pdf file :  '" + srcPdf + "' does not exsit.");


        if (!FilenameUtils.getExtension(srcPdf.getAbsolutePath()).toLowerCase().equals("pdf"))
            return;


        if (waterMarkImage != null) {
            if (!waterMarkImage.exists() || !waterMarkImage.isFile())
                throw new FileNotFoundException("img file :  '" + srcPdf + "' does not exsit.");

            if (!FilenameUtils.getExtension(waterMarkImage.getAbsolutePath()).toLowerCase().equals("png"))
                throw new FileNotFoundException("image file '" + srcPdf + "'  not png.(必须为透明图片格式，否则会遮挡 pdf 内容)");
        }


        PdfReader reader = getPdfReader(srcPdf);

        int n = reader.getNumberOfPages();
        PdfStamper stamper = getPdfStamper(srcPdf, destPdf);

        //添加属性
        //        HashMap<String, String> moreInfo = new HashMap<String, String>();
//        moreInfo.put("Author", "H819 create");
//        moreInfo.put("Producer", "H819 Producer");
//        Key = CreationDate, Value = D:20070425182920
//        Key = Producer, Value = TH-OCR 2000 (C++/Win32)
//        Key = Author, Value = TH-OCR 2000
//        Key = Creator, Value = TH-OCR PDF Writer

        // stamp.setMoreInfo(moreInfo);

        // text
        Phrase text = null;
        if (waterMarkText != null) {
            //创建一个空心字体
            Font bfont = getPdfFont();
            bfont.setSize(35);
            bfont.setColor(new BaseColor(192, 192, 192));
            text = new Phrase(waterMarkText, bfont);
        }
        // image watermark
        Image img = null;
        float w = 0;
        float h = 0;
        if (waterMarkImage != null) {
            img = Image.getInstance(waterMarkImage.getAbsolutePath());
            w = img.getScaledWidth();
            h = img.getScaledHeight();
            //  img.
            img.setRotationDegrees(45);

        }


        // transparency
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.5f);
        // properties
        PdfContentByte over;
        Rectangle pageSize;
        float x, y;
        // loop over every page
        for (int i = 1; i <= n; i++) {
            pageSize = reader.getPageSizeWithRotation(i);
            x = (pageSize.getLeft() + pageSize.getRight()) / 2;
            y = (pageSize.getTop() + pageSize.getBottom()) / 2;
            // 在 pdf 内容之上添加，如果加在底部，pdf 文件内容为图片，就会遮挡而看不见
            over = stamper.getOverContent(i);
            // 不能加在底部
            // over = stamp.getUnderContent(i);
            // 不要加上 over.beginText(); over.endText(); 生成的文件会报错
            //第一行文字： 分别设置字体,大小,颜色:单位名称
            over.saveState(); //保存状态
            over.setGState(gs1);

            if (waterMarkText != null && waterMarkImage != null) { // 文字图片隔页添加
                if (i % 2 == 1) {
                    ColumnText.showTextAligned(over, Element.ALIGN_CENTER, text, x, y, 45);
                } else
                    over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
            } else if (waterMarkText != null) {  //每页添加文字

                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, text, x, y, 45);
                //可以接着添加第二行文字： 分别设置字体,大小,颜色 :发布单位名称
                // ...

            } else {   //每页添加图片
                over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
            }

            over.restoreState();//恢复状态
        }

        stamper.close();
        reader.close();


    }

    /**
     * 压缩已经存在的 pdf 文件。实例了处理已经存在的文件的方法
     *
     * @param srcPdfFile  the original PDF
     * @param descPdfFile the resulting PDF
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public static void compressPdf(File srcPdfFile, File descPdfFile) throws IOException, DocumentException {

        if (srcPdfFile == null || !srcPdfFile.exists())
            throw new IOException("src pdf file '" + srcPdfFile
                    + "' does not exsit.");

        PdfReader reader = getPdfReader(srcPdfFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                descPdfFile.getAbsoluteFile()), PdfWriter.VERSION_1_7);

        stamper.getWriter().setCompressionLevel(9);
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            reader.setPageContent(i, reader.getPageContent(i));
        }
        stamper.setFullCompression();
        stamper.close();
        reader.close();
    }

    /**
     * 通过压缩 pdf 文件中图片的方式，压缩 pdf
     *
     * @param srcPdf      the original PDF
     * @param destPdf     the resulting PDF
     * @param imageFactor The multiplication factor for the image (图片缩小比例 ，例如 imageFacto =0.5f)
     * @throws IOException
     * @throws DocumentException
     */
    public static void compressPdf(File srcPdf, File destPdf, float imageFactor) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(srcPdf.getAbsolutePath());
        int n = reader.getXrefSize();
        PdfObject object;
        PRStream stream;
        // Look for image and manipulate image stream
        for (int i = 0; i < n; i++) {
            object = reader.getPdfObject(i);
            if (object == null || !object.isStream())
                continue;
            stream = (PRStream) object;
            if (!PdfName.IMAGE.equals(stream.getAsName(PdfName.SUBTYPE)))
                continue;
            if (!PdfName.DCTDECODE.equals(stream.getAsName(PdfName.FILTER)))
                continue;
            PdfImageObject image = new PdfImageObject(stream);
            BufferedImage bi = image.getBufferedImage();
            if (bi == null)
                continue;
            int width = (int) (bi.getWidth() * imageFactor);
            int height = (int) (bi.getHeight() * imageFactor);
            if (width <= 0 || height <= 0)
                continue;
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            AffineTransform at = AffineTransform.getScaleInstance(imageFactor, imageFactor);
            Graphics2D g = img.createGraphics();
            g.drawRenderedImage(bi, at);
            ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
            ImageIO.write(img, "JPG", imgBytes);
            stream.clear();
            stream.setData(imgBytes.toByteArray(), false, PRStream.NO_COMPRESSION);
            stream.put(PdfName.TYPE, PdfName.XOBJECT);
            stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
            stream.put(PdfName.FILTER, PdfName.DCTDECODE);
            stream.put(PdfName.WIDTH, new PdfNumber(width));
            stream.put(PdfName.HEIGHT, new PdfNumber(height));
            stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
            stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
        }
        reader.removeUnusedObjects();
        // Save altered PDF
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destPdf.getAbsolutePath()));
        stamper.setFullCompression();
        stamper.close();
        reader.close();
    }

    /**
     * 创建一个不压缩的pdf
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
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(descPdfFile.getAbsoluteFile()));
        stamper.getWriter().setCompressionLevel(PdfStream.NO_COMPRESSION);
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            reader.setPageContent(i, reader.getPageContent(i));
        }
        stamper.close();
        reader.close();

    }

    /**
     * 通过第三方提供的工具，破解没有设置打开密码的 pdf 文件。可以递归破解指定文件夹内的所有文件，并保留原来的目录结构
     *
     * @param srcPdfFileDirectory  存放待破解 pdf 文件的文件夹. 破解之后的文件，存放子默认的文件夹内。
     * @param descPdfFileDirectory 破解之后的文件存放的文件夹，保持和源文件同样的结构
     * @param badDirectory         存放有打开密码，不能破解的 pdf 的文件夹。
     * @throws java.io.IOException
     */
    public static void decryptDirectory(File srcPdfFileDirectory, File descPdfFileDirectory, File badDirectory) throws IOException {

        if (!isEnoughSpace(srcPdfFileDirectory, descPdfFileDirectory))
            throw new IOException("目标文件夹磁盘控件不够");

        for (File file : srcPdfFileDirectory.listFiles()) {
            String path = descPdfFileDirectory.getAbsolutePath() + File.separator + file.getName();
            if (file.isFile()) {
                decryptFile(file, Paths.get(path).toFile(), badDirectory);
            } else if (file.isDirectory())
                decryptDirectory(file, Paths.get(path).toFile(), badDirectory);
            else return;

        }
    }

    /**
     * 通过第三方提供的工具，破解没有设置打开密码的 pdf 文件。
     * windows 下使用
     * "PDF Password Remover v4.0" 软件提供命令行破解，通过系统调用该命令行，进行破解
     *
     * @param srcPdf        存放待破解 pdf 文件
     * @param descPdf       存放破解之后的 pdf 文件
     * @param ownerPassword 用户打开文件密码。如果设置了打开密码，需要此参数，否则不能解密
     * @param badDirectory  存放有打开密码，不能破解的 pdf 的文件夹。
     * @throws java.io.IOException
     */
    public static void decryptFile(File srcPdf, File descPdf, String ownerPassword, File badDirectory) {

        String extension = FilenameUtils.getExtension(srcPdf.getAbsolutePath());

        if (!extension.equalsIgnoreCase("pdf"))
            return;

        // 损坏的 0 字节文件，直接拷贝到统一的文件夹
        if (FileUtils.sizeOf(srcPdf) == 0) {
            logger.info("{} size =0 ,copy to {}", srcPdf.getAbsoluteFile(), badDirectory.getAbsoluteFile());
            MyFileUtils.copyFileToDirectory(srcPdf, badDirectory);
            return;
        }

        try {

            PdfReader reader;
            if (ownerPassword != null)
                reader = getPdfReader(srcPdf, ownerPassword);
            else
                reader = getPdfReader(srcPdf);

            if (!reader.isEncrypted()) {// 未加密的文件，直接拷贝
                FileUtils.copyFile(srcPdf, descPdf);
                logger.info("not encrypted,copy {} to {} ", srcPdf.getAbsolutePath(), descPdf.getAbsoluteFile());
                return;
            }


            List<ExecParameter> list = new ArrayList(2);
            list.add(new ExecParameter("-i", srcPdf.getAbsolutePath())); // 只有 key ，没有 value
            list.add(new ExecParameter("-o", descPdf.getAbsolutePath()));
//            list.add(new ExecParameter("-u", ""));
//            list.add(new ExecParameter("-w", ""));
            MyExecUtils.exec(Paths.get(getPdfPdfdecryptExec()), list, 1);
            logger.info("decrypted {} to {}", srcPdf.getAbsolutePath(), descPdf.getAbsolutePath());
            // 关闭处理完成的文件
            reader.close();

        } catch (BadPasswordException e) {
            // 有打开密码的文件，不能破解，统一拷贝至一个文件夹。
            logger.info("{} has user password ,copy to {}", srcPdf.getAbsoluteFile(), badDirectory.getAbsoluteFile());
            MyFileUtils.copyFileToDirectory(srcPdf, badDirectory);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 破解没有用户打开密码的文件
     *
     * @param srcPdf       存放待破解 pdf 文件
     * @param descPdf      存放破解之后的 pdf 文件
     * @param badDirectory 存放有打开密码，不能破解的 pdf 的文件夹。
     * @throws java.io.IOException
     */
    public static void decryptFile(File srcPdf, File descPdf, File badDirectory) {
        decryptFile(srcPdf, descPdf, null, badDirectory);
    }

    /**
     * 加密 pdf 文件
     *
     * @param srcPdfFile       源文件
     * @param descPdfFile      目标文件
     * @param userPassword     用户密码，授权打开文件 ,null 为没有密码
     * @param ownerPassword    拥有者密码，授权编辑文件 ,null 为没有密码
     * @param allowHideMenuBar 是否显示菜单栏
     * @param allowHideToolbar 是否显示工具栏
     * @param allowPrinting    是否允许打印
     * @param allowCopy        是否允许拷贝文字
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public static void encryptPdf(File srcPdfFile, File descPdfFile,
                                  String userPassword, String ownerPassword,
                                  boolean allowHideMenuBar, boolean allowHideToolbar,
                                  boolean allowPrinting, boolean allowCopy) throws IOException {

        if (srcPdfFile == null || !srcPdfFile.exists())
            throw new IOException("src pdf file '" + srcPdfFile
                    + "' does not exist.");

        byte[] userByte = null;
        byte[] ownerByte = null;

        /** User password. */
        if (userPassword != null)
            userByte = userPassword.getBytes();
        /** Owner password. */
        if (ownerPassword != null)
            ownerByte = ownerPassword.getBytes();

        try {

            PdfReader reader = getPdfReader(srcPdfFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(descPdfFile.getAbsoluteFile()));

            // 设置显示属性
            // 参数含义参见 PdfWriter.setViewerPreferences 方法 和 addViewerPreference
            // 根据 api 提示，有的参数利用 addViewerPreference 方法比较合适
            // 参数可以按照位"|"(或)方式连续写, 但个别参数，如 PdfWriter.HideWindowUI
            // 加上之后，其他参数就不好用了, 实际使用的时候，要逐个测试

            // 默认属性(这几个属性用 addViewerPreference 方法设置)
            stamper.getWriter().addViewerPreference(PdfName.CENTERWINDOW, PdfBoolean.PDFTRUE);
            stamper.getWriter().addViewerPreference(PdfName.DISPLAYDOCTITLE, PdfBoolean.PDFTRUE);
            stamper.getWriter().addViewerPreference(PdfName.FITWINDOW, PdfBoolean.PDFTRUE);
            // writer.addViewerPreference(PdfName.HIDEWINDOWUI,
            // PdfBoolean.PDFTRUE);

            if (!allowHideToolbar)
                stamper.getWriter().addViewerPreference(PdfName.HIDETOOLBAR, PdfBoolean.PDFTRUE);

            if (!allowHideMenuBar)
                stamper.getWriter().addViewerPreference(PdfName.HIDEMENUBAR, PdfBoolean.PDFTRUE);

            // 设置 allow 属性
            if (allowPrinting && allowCopy)
                stamper.setEncryption(userByte, ownerByte, PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY, true);

            else if (allowPrinting)
                stamper.setEncryption(userByte, ownerByte, PdfWriter.ALLOW_PRINTING, true);

            else if (allowCopy)
                stamper.setEncryption(userByte, ownerByte, PdfWriter.ALLOW_COPY, true);

            else
                stamper.setEncryption(userByte, ownerByte, 0, true);

            stamper.close();
            reader.close();

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
    public static void findEncryptPdf(File srcPdfFileDir) throws IOException {

        tempList.clear();//重新初始化，否则静态变量全局不变
        testEcryptPdf(srcPdfFileDir);

        if (tempList.size() != 0)
            FileUtils.writeLines(new File(srcPdfFileDir.getParent()
                    + File.separator + "encrypt.txt"), StandardCharsets.UTF_8.name(), tempList);
        logger.info(" finished!");

    }


    /**
     * 计算指定目录中所有的pdf文本的页数，包括子目录。
     *
     * @param srcPdfFileDir 待计算的文件夹路径
     * @return
     * @throws IOException
     */
    public static int getNumberOfPages(File srcPdfFileDir) throws IOException {

        numberOfPagesOfDirectory = 0; //重新初始化，否则静态变量全局不变
        countNumberOfPagesOfDir(srcPdfFileDir);
        return numberOfPagesOfDirectory;
    }

    /**
     * 判断目标盘是否有足够的空间拷贝源文件夹
     *
     * @param srcPdfFileDir
     * @param descPdfFileDir
     * @return
     */
    private static boolean isEnoughSpace(File srcPdfFileDir, File descPdfFileDir) {


        if (!srcPdfFileDir.exists() && !srcPdfFileDir.isDirectory()) {
            logger.info(srcPdfFileDir.getAbsolutePath() + " not exist.");
            return false;
        }

        try {
            FileUtils.forceMkdir(descPdfFileDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // 检查磁盘空间是否充足

        // 目标盘剩余空间
        long prefixDiskFreeSize = descPdfFileDir.getFreeSpace();
        // 源文件夹大小
        long srcSize = FileUtils.sizeOfDirectory(srcPdfFileDir);

        // logger.info(descPdfFileDir.getAbsolutePath() + " 目标盘剩余空间："
        // + prefixDiskFreeSize / 1000000.00 + " M");
        // logger.info(srcPdfFileDir.getAbsolutePath() + " 原文件夹大小 :" + srcSize
        // / 1000000.00 + " M");


        if (prefixDiskFreeSize < srcSize) {
            logger.info(FilenameUtils.getPrefix(descPdfFileDir.getAbsolutePath()) + " has not enoght disk size .");
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
     * http://www.javabeat.net/javascript-in-pdf-documents-using-itext/
     * http://www.javabeat.net/javascript-communication-between-html-and-pdf-in-itext/
     * 去除附在 PDF 属性 field 上的 javaScript
     * <p>
     * pdf 文件可以添加 javaScript，来实现某种功能，例如激发对话框等动作。
     * <p>
     * javaScript 可以附加于某个 filed，此时去掉 这个 filed，使 javaScript 代码因缺少变量而无法执行
     * <p>
     * 即可以达到去除 javaScript 的目的。
     * <p>
     * 通过 itext 的 reader.getJavaScript()方法，可以查看 pdf 文件 JavaScript源代码(adobe pro
     * 控制台也可以看到源码)
     * <p>
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

        FileUtils.forceMkdir(descPdfFileDir);

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
        if (StringUtils.countMatches(startDate, ",") != 2) {
            logger.info(startDate + " 开始日期格式录入不对，应该型如  '2011,01,01' 形式");
            return;
        }

        if (!isEnoughSpace(srcPdfFileDir, descPdfFileDir))
            return;

        File listFiles[] = srcPdfFileDir.listFiles();

        if (listFiles.length == 0) {
            logger.info("srcPdfFileDir has not file. "
                    + srcPdfFileDir.getAbsolutePath());
            return;
        }

        // 拷贝资源文件到临时目录
        // String[] resources = new String[]{"/pdfexpiration.js"};
        File resPath = MyFileUtils.copyResourceFileFromJarLibToTmpDir("/pdfexpiration.js"
        );  //????
        // 得到资源文件内容
        String jsStr = FileUtils.readFileToString(resPath);

        /** 替换 js 文件中的字符串，作为输入条件 */
        // 替换开始日期
        jsStr = StringUtils.replace(jsStr, "2011,01,01", startDate);
        // 替换到达警告期的天数
        jsStr = StringUtils.replace(jsStr, "alertDays = 355", "alertDays = "
                + Integer.toString(alerDays));
        // 替换到达过期天数
        jsStr = StringUtils.replace(jsStr, "expiredDays = 365",
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
     * 检查文件夹内的文件是否被加密，用到了递归方法，返回值被全局变量记录，通过 findEncryptPdf()函数调用
     *
     * @param srcPdfFileDir 待检查的文件夹
     * @throws java.io.IOException
     */
    private static void testEcryptPdf(File srcPdfFileDir) throws IOException {

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
            String extension = FilenameUtils.getExtension(fileName).toLowerCase();

            if (f.isFile()) {

                if (extension.equals("pdf")) {

                    // 损坏的 0 字节文件，文件体积大于 40 M， 直接登记
                    if (FileUtils.sizeOf(f) == 0) {

                        tempList.add("size ==0 40M :" + f.getAbsolutePath());
                        continue;
                    }

                    PdfReader reader;
                    try {
                        reader = getPdfReader(f);
                    } catch (BadPasswordException e) {
                        // 有打开密码的文件，不能破解，直接登记
                        e.printStackTrace();
                        tempList.add("BadPassword,can not open :" + f.getAbsolutePath());
                        continue;
                    }
                    // logger.info("fileDesc name :" + fileDesc.getAbsolutePath());

                    if (reader.isEncrypted()) {// 未加密的文件，，直接登记
                        tempList.add("encrypted :" + f.getAbsolutePath());
                        reader.close();
                        continue;
                    } else {
                        tempList.add("not encrypted :" + f.getAbsoluteFile());
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

    /**
     * 简化代码编写
     *
     * @param srcPdf
     * @param destPdf
     * @return
     * @throws IOException
     * @throws DocumentException
     */

    public static PdfStamper getPdfStamper(File srcPdf, File destPdf) throws IOException, DocumentException {
        return new PdfStamper(getPdfReader(srcPdf), new FileOutputStream(destPdf), PdfWriter.VERSION_1_7);
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
    public static PdfReader getPdfReader(File pdfFile) throws IOException {

        Document.plainRandomAccess = true;
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
    public static PdfReader getPdfReader(File pdfFile, String ownerPassword) throws IOException {

        FileInputStream fileStream = new FileInputStream(pdfFile);

        if (ownerPassword == null)
            return getPdfReader(pdfFile);

        return new PdfReader(new RandomAccessFileOrArray(new FileChannelRandomAccessSource(fileStream.getChannel())), ownerPassword.getBytes());
    }


    /**
     * 获得自定义的空心字体 STCAIYUN.TTF，该字体已经制成为 jar，需要加入项目的 classpath
     * 经过测试，该空心字体作为 pdf 的水印，不会遮挡 pdf 原文，支持中文
     * 需要注意的是，空心字体不能太小，否则会看不清楚
     *
     * @return
     * @throws IOException
     */
    private static Font getPdfFont() {

        //空心字体
        String fontName = "/STCAIYUN.TTF";
        String fontPath =
                SystemUtils.getJavaIoTmpDir()
                        + File.separator + MyConstants.JarTempDir + File.separator
                        + fontName;

        //如果已经拷贝过，就不用再拷贝了
        if (!Files.exists(Paths.get(fontPath))) {
            MyFileUtils.copyResourceFileFromJarLibToTmpDir(fontName);
        }
        return FontFactory.getFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    }

    /**
     * 定义中文字体，项目需要加载 iTextAsian.jar
     * itext 5.5.9 以上，不用了，也可以支持中文
     *
     * @return
     */
    @Deprecated
    private static Font getChineseFont() {
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
     * 计算指定目录中所有的pdf文本的页数，包括子目录。用到了递归方法，返回值被全局变量记录，通过 getPageCount()函数调用
     *
     * @param srcPDFFileDir 待计算的文件夹路径
     * @return
     * @throws IOException
     */
    private static void countNumberOfPagesOfDir(File srcPDFFileDir) throws IOException {

        if (srcPDFFileDir == null || !srcPDFFileDir.isDirectory())
            throw new FileNotFoundException(srcPDFFileDir + "'is null or dose not exist.");


        Collection<File> listPDFs = FileUtils.listFiles(srcPDFFileDir, null, true);
        for (File f : listPDFs) {

            // 递归文件夹
            if (f.isDirectory())
                countNumberOfPagesOfDir(f);

            if (!FilenameUtils.getExtension(f.getName().toUpperCase()).equals("PDF"))
                continue;
            // System.out.println(f.getPath());
            // we create a reader for a certain document
            PdfReader reader = getPdfReader(f);
            // we retrieve the total number of pages
            // 对加密的文件会抛出异常
            if (reader.isEncrypted())
                continue;

            numberOfPagesOfDirectory = numberOfPagesOfDirectory + reader.getNumberOfPages();

        }
    }

    /**
     * pdfdecrypt.exe 文件路径
     *
     * @return
     * @throws IOException
     */
    private static String getPdfPdfdecryptExec() {

        //命令行模式，只需要两个文件即可
        String exec1 = "/pdfdecrypt.exe";
        String exec2 = "/license.dat";

        String tempPath =
                SystemUtils.getJavaIoTmpDir()
                        + File.separator + MyConstants.JarTempDir + File.separator;

        String exec1Path = tempPath + exec1;
        String exec2Path = tempPath + exec2;

        //如果已经拷贝过，就不用再拷贝了
        if (!Files.exists(Paths.get(exec1Path)))
            MyFileUtils.copyResourceFileFromJarLibToTmpDir(exec1);

        if (!Files.exists(Paths.get(exec2Path)))
            MyFileUtils.copyResourceFileFromJarLibToTmpDir(exec2);


        return exec1Path;
    }

}
