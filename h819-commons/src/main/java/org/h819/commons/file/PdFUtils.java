package org.h819.commons.file;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.FileChannelRandomAccessSource;
import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.crypto.BadPasswordException;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.dom4j.DocumentException;
import org.h819.commons.MyConstants;
import org.h819.commons.MyExecUtils;
import org.h819.commons.exe.ExecParameter;
import org.h819.commons.file.pdf.PdfBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * Description : TODO()
 * User: h819
 * Date: 2017-05-13
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */

//http://tutorials.jenkov.com/java-itext/index.html
//html->pdf
//http://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-creating-pdf-documents-with-wkhtmltopdf/
//http://wkhtmltopdf.org/

//关于不允许保存 pdf 的解释 http://developers.itextpdf.com/content/best-itext-questions-stackoverview/questions-about-pdf-general/itext7-how-disable-save-button-and-hide-menu-bar-adobe-reader

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

public class PdFUtils extends PdfBase {

    private static String src_text = "D:\\itext7\\text_src.pdf";
    private static String dest_2 = "D:\\itext7\\DEST2.pdf";
    private static String DEST3 = "D:\\itext7\\DEST3.pdf";
    private static String DEST_TEXT_Position = "D:\\itext7\\test_temp.pdf";
    private static String IMG = "D:\\itext7\\itext.png";
    private static String src_1 = "D:\\itext7\\source1.pdf";
    private static String src_2 = "D:\\itext7\\source2.pdf";
    private static String src_3 = "D:\\itext7\\source3.pdf";
    // 该变量专门为 countPages() 函数设立，由于使用了递归，故提到全局变量，否则每次的返回值不能累计
    private static int numberOfPagesOfDirectory;
    private static List tempList;
    private static Logger logger = LoggerFactory.getLogger(PdFUtils.class);
    private String dest_1 = "D:\\itext7\\DEST1.pdf";

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


    public static void main(String[] args) throws Exception {


        //测试添加水印文字的位置
        //  PdFUtils.testWaterMark();
        // PdFUtils.testEncryptPdf();
        //  PdFUtils.testExpireDate();
        PdFUtils.testDecryptPdf();
    }

    private static void testWaterMark() throws FileNotFoundException {
        addWaterMarkFile(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "TOP")), null, "中文测试 it is test", TextPosition.TOP);
        addWaterMarkFile(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "BOTTOM")), null, "中文测试 it is test", TextPosition.BOTTOM);
        addWaterMarkFile(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "CENTER")), null, "中文测试 it is test", TextPosition.CENTER);
        addWaterMarkFile(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "LEFT")), null, "中文测试 it is test", TextPosition.LEFT);
        addWaterMarkFile(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "RIGHT")), null, "中文测试 it is test", TextPosition.RIGHT);
    }

    private static void testEncryptPdf() throws IOException {
//        encryptPdf(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "notprint")), null, null, false, true);
//        encryptPdf(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "notcopy")), null, null, true, false);
//        encryptPdf(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "notp+c")), null, null, false, false);
//        encryptPdf(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "notuser")), "user", null, true, true);
//        encryptPdf(new File(src_text), new File(DEST_TEXT_Position.replace("temp", "notowner")), null, "user2", true, true);

        decryptFile(new File(DEST_TEXT_Position.replace("temp", "notuser")), new File(DEST_TEXT_Position.replace("temp", "notuser_decrypt")), "user");
    }

    private static void testDecryptPdf() throws IOException {

        decryptFile(new File("D:\\itext7\\text_src_encrypt.pdf"), new File("D:\\itext7\\text_src_encrypt_de.pdf"), new File("D:\\itext7\\bad"));
    }

    private static void testExpireDate() throws IOException {
//        FileUtils.forceDelete(new File("D:\\itext7\\test__ExpireDate.pdf"));
//      ///  Files.deleteIfExists(Paths.get("D:\\itext7\\test__ExpireDate.pdf"));
        //  addExpireDateWithJavaScriptFile(new File("D:\\itext7\\text_src.pdf"), new File("D:\\itext7\\text_src_no_warning.pdf"), "2017-06-05", 1, 2);
//        Optional<String> s = Optional.empty();
//        System.out.println(s.isPresent());

        removeJavaScript(new File("D:\\itext7\\text_src_no_warning.pdf"), new File("D:\\itext7\\text_src_no_warning_.pdf"), PdfName.OpenAction);
    }

    /**
     * 测试：添加虚拟机启动参数
     * -Xmx512m
     * -Xms512m
     * <p>
     * 经过此方法优化之后，可以读取大文件，不会溢出。
     * 但写文件不知道怎么优化，文件大了还要内存溢出。
     *
     * @param pdfFile
     * @return
     * @throws IOException
     */
    private static Optional<PdfReader> getPdfReader(File pdfFile) throws IOException {
        if (pdfFile == null || !pdfFile.exists() || !pdfFile.isFile() || Files.size(Paths.get(pdfFile.getAbsolutePath())) == 0) {
            logger.info("{} Illegal.", pdfFile.getAbsolutePath());
            return Optional.empty();
        }
        IRandomAccessSource source = new FileChannelRandomAccessSource(new FileInputStream(pdfFile).getChannel());
        return Optional.of((new PdfReader(source, new ReaderProperties())));

    }

    /**
     * @param pdfFile
     * @param userPassword 文件打开密码
     * @return
     * @throws IOException
     */
    private static Optional<PdfReader> getPdfReader(File pdfFile, String userPassword) throws IOException {
        IRandomAccessSource source = new FileChannelRandomAccessSource(new FileInputStream(pdfFile).getChannel());
        // .setUnethicalReading(true) 必须设置，才可以打开有用户密码的文件
        return Optional.of(new PdfReader(source, new ReaderProperties().setPassword(userPassword.getBytes())).setUnethicalReading(true));

    }

    /**
     * 获得自定义的空心字体 STCAIYUN.TTF，该字体已经制成为 jar，需要加入项目的 classpath
     * 经过测试，该空心字体作为 pdf 的水印，不会遮挡 pdf 原文，支持中文
     * 需要注意的是，空心字体不能太小，否则会看不清楚
     *
     * @return
     * @throws IOException
     */
    private static PdfFont getPdfFont() throws IOException {

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
        return PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
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
            Optional<PdfReader> reader = getPdfReader(f);
            // we retrieve the total number of pages
            // 对加密的文件会抛出异常
            if (!reader.isPresent()) {
                if (reader.get().isEncrypted()) {
                    System.err.println(getEncryptedErrorMessage(f));
                    continue;
                }

                PdfDocument pdfDoc = new PdfDocument(getPdfReader(f).get());
                numberOfPagesOfDirectory = numberOfPagesOfDirectory + pdfDoc.getNumberOfPages();
            }


        }
    }

    /**
     * 单个文件添加水印图片
     *
     * @param srcPdf         源文件
     * @param destPdf        目标文件
     * @param waterMarkImage 水印图片
     */
    public static void addWaterMarkFile(File srcPdf, File destPdf, File waterMarkImage) throws IOException, DocumentException {
        addWaterMarkFile(srcPdf, destPdf, waterMarkImage, null, null);
    }

    /**
     * 单个文件添加水印文字
     *
     * @param srcPdf        源文件
     * @param destPdf       目标文件
     * @param waterMarkText 水印文字
     */
    public static void addWaterMarkFile(File srcPdf, File destPdf, String waterMarkText, TextPosition textPosition) throws IOException, DocumentException {
        addWaterMarkFile(srcPdf, destPdf, null, waterMarkText, textPosition);
    }

    /**
     * 文件夹中所有文件添加水印图片
     *
     * @param srcPdfDirectory  源文件夹
     * @param descPdfDirectory 目标文件夹
     * @param waterMarkImage   水印图片
     */
    public static void addWaterMarkDerictory(File srcPdfDirectory, File descPdfDirectory, File waterMarkImage) throws IOException, DocumentException {

        if (descPdfDirectory.getAbsolutePath().contains(srcPdfDirectory.getAbsolutePath()))
            throw new IOException("目标文件夹不能在原文件夹中");


        if (!isEnoughSpace(srcPdfDirectory, descPdfDirectory))
            return;

        for (File file : srcPdfDirectory.listFiles()) {
            String destPath = descPdfDirectory.getAbsolutePath() + File.separator + file.getName();
            if (file.isFile()) {
                addWaterMarkFile(file, Paths.get(destPath).toFile(), waterMarkImage, null, null);
            } else if (file.isDirectory())
                addWaterMarkDerictory(file, Paths.get(destPath).toFile(), waterMarkImage);
            else return;

        }
    }

    /**
     * 文件夹中所有文件添加水印文字
     *
     * @param srcPdfFileDirectory  源文件夹
     * @param descPdfFileDirectory 目标文件夹
     * @param waterMarkText        水印文字
     */
    public static void addWaterMarkDerictory(File srcPdfFileDirectory, File descPdfFileDirectory, String waterMarkText, TextPosition textPosition) throws IOException, DocumentException {

        if (descPdfFileDirectory.getAbsolutePath().contains(srcPdfFileDirectory.getAbsolutePath()))
            throw new IOException("目标文件夹不能在原文件夹中");
        if (!isEnoughSpace(srcPdfFileDirectory, descPdfFileDirectory))
            return;

        for (File file : srcPdfFileDirectory.listFiles()) {
            String destPath = descPdfFileDirectory.getAbsolutePath() + File.separator + file.getName();
            if (file.isFile()) {
                addWaterMarkFile(file, Paths.get(destPath).toFile(), waterMarkText, textPosition);
            } else if (file.isDirectory())
                addWaterMarkDerictory(file, Paths.get(destPath).toFile(), waterMarkText, textPosition);
            else continue;

        }

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
            System.out.println(srcPdfFileDir.getAbsolutePath() + " not exist.");
            return false;
        }

        if (descPdfFileDir.getAbsolutePath().contains(srcPdfFileDir.getAbsolutePath())) {
            System.out.println(String.format("%s %s %s %s %s", "src= ", srcPdfFileDir.getAbsoluteFile(), " dest= ", descPdfFileDir.getAbsoluteFile(), " dest cannot in src Dir."));
            return false;
        }

        if (!descPdfFileDir.exists())
            try {
                Files.createDirectory(Paths.get(descPdfFileDir.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }


        // 检查磁盘空间是否充足
        // 目标盘剩余空间
        long prefixDiskFreeSize = descPdfFileDir.getFreeSpace();
        // 源文件夹大小
        long srcSize = FileUtils.sizeOfDirectory(srcPdfFileDir);

        if (prefixDiskFreeSize < srcSize) {
            System.err.println(descPdfFileDir.getParent() + " has not enoght disk size .");
            descPdfFileDir.delete();
            return false;
        }

        return true;

    }

    /**
     * 单个文件添加水印
     *
     * @param srcPdf         源文件
     * @param destPdf        目标文件
     * @param waterMarkText  水印文字
     * @param waterMarkImage 水印图片
     * @param textPosition   文字位置
     */
    public static void addWaterMarkFile(File srcPdf, File destPdf, File waterMarkImage, String waterMarkText, TextPosition textPosition) throws FileNotFoundException, IllegalFormatException {

        //加指定页
        if (waterMarkText == null && waterMarkImage == null)
            throw new FileNotFoundException(waterMarkText + " " + waterMarkImage + " all null.");


        if (srcPdf == null || !srcPdf.exists() || !srcPdf.isFile())
            throw new FileNotFoundException("pdf file :  '" + srcPdf + "' does not exsit.");


        if (!FilenameUtils.getExtension(srcPdf.getAbsolutePath()).toLowerCase().equals("pdf"))
            throw new FileNotFoundException("file :  '" + srcPdf + "' is not pdf.");

        if (waterMarkImage != null) {
            if (!waterMarkImage.exists() || !waterMarkImage.isFile())
                throw new FileNotFoundException("img file :  '" + srcPdf + "' does not exsit.");

            if (!FilenameUtils.getExtension(waterMarkImage.getAbsolutePath()).toLowerCase().equals("png"))
                throw new FileNotFoundException("image file '" + srcPdf + "'  not png.(必须为透明图片(png)格式，否则会遮挡 pdf 内容)");
        }

        destPdf.getParentFile().mkdirs();

        try {


            Optional<PdfReader> reader = getPdfReader(srcPdf);
            if (!reader.isPresent())
                return;

            if (reader.get().isEncrypted()) {
                System.err.println(getEncryptedErrorMessage(srcPdf));
                return;
            }


            PdfWriter writer = new PdfWriter(new FileOutputStream(destPdf));
            PdfDocument pdfDoc = new PdfDocument(reader.get(), writer);
            Document doc = new Document(pdfDoc);
            int n = pdfDoc.getNumberOfPages();
            PdfFont font = getPdfFont();//创建一个空心字体
            Paragraph p = new Paragraph("My watermark (text ?中文支持么)");
            p.setFont(font); //此处需要加载支持中文的字体
            p.setFontSize(18);
            p.setFontColor(Color.GRAY, 0.3f); //透明度


            // image watermark
            ImageData img = null;
            float w = 0, h = 0;
            if (waterMarkImage != null) {
                img = ImageDataFactory.create(waterMarkImage.getAbsolutePath());
                //  Implement transformation matrix usage in order to scale image
                w = img.getWidth();
                h = img.getHeight();
            }
            // transparency
            PdfExtGState gs1 = new PdfExtGState();
            gs1.setFillOpacity(0.5f);
            // properties
            PdfCanvas over;
            Rectangle pageSize;
            float x = 0; //横坐标
            float y = 0; //纵坐标
            float r = 0; //旋转角度
            // loop over every page
            for (int i = 1; i <= n; i++) {
                PdfPage pdfPage = pdfDoc.getPage(i);

                pageSize = pdfPage.getPageSizeWithRotation();
                pdfPage.setIgnorePageRotationForContent(true);

                //添加文字时，文字位置
                if (waterMarkText != null)
                    if (textPosition.equals(TextPosition.CENTER) || textPosition == null) {  // 居中对齐，旋转 45 度
                        x = (pageSize.getLeft() + pageSize.getRight()) / 2;
                        y = (pageSize.getTop() + pageSize.getBottom()) / 2;
                        r = 45;
                        p.setFontSize(35);
                    } else if (textPosition.equals(TextPosition.TOP)) {
                        x = (pageSize.getLeft() + pageSize.getRight()) / 2;
                        y = pageSize.getTop() - 10;
                        r = 0;

                    } else if (textPosition.equals(TextPosition.BOTTOM)) {
                        x = (pageSize.getLeft() + pageSize.getRight()) / 2;
                        y = pageSize.getBottom() + 30;
                        r = 0;

                    } else if (textPosition.equals(TextPosition.LEFT)) {
                        x = pageSize.getLeft() + 20;
                        y = (pageSize.getTop() + pageSize.getBottom()) / 2;
                        r = (float) Math.toRadians(90);  //注意 90 度的写法
                    } else if (textPosition.equals(TextPosition.RIGHT)) {
                        x = pageSize.getRight() - 20;
                        y = (pageSize.getTop() + pageSize.getBottom()) / 2;
                        r = (float) Math.toRadians(90); //注意 90 度的写法
                    }


                // 在 pdf 内容之上添加，如果加在底部，pdf 文件内容为图片，就会遮挡而看不见
                //over = stamper.getOverContent(i);
                over = new PdfCanvas(pdfDoc.getPage(i));
                over.saveState();
                over.setExtGState(gs1);

                if (waterMarkText != null && waterMarkImage != null) { // 文字图片隔页添加
                    if (i % 2 == 1) {
                        doc.showTextAligned(p, x, y, i, TextAlignment.CENTER, VerticalAlignment.MIDDLE, r);
                    } else {  //图片不用设置位置
                        over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2), false);
                    }
                } else if (waterMarkText != null && waterMarkImage == null) {  //每页添加文字
                    doc.showTextAligned(p, x, y, i, TextAlignment.CENTER, VerticalAlignment.MIDDLE, r);
                } else {   //每页添加图片
                    over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
                }
                over.restoreState();//恢复状态
                pdfDoc.setFlushUnusedObjects(true);

            }

            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getEncryptedErrorMessage(File f) {
        return "pdf file :  '" + f.getAbsolutePath() + "' is encrypted.";
    }

    /**
     * 检查文件夹内的文件是否被加密，可以递归
     *
     * @param srcPdfFileDir 待检查的文件夹
     * @throws java.io.IOException
     */
    public static void findEncryptPdf(File srcPdfFileDir) throws IOException {
        if (tempList == null)
            tempList = new ArrayList();

        tempList.clear();//重新初始化，否则静态变量全局不变
        findEncryptPdf0(srcPdfFileDir);

        if (tempList.size() != 0)
            FileUtils.writeLines(new File(srcPdfFileDir.getParent()
                    + File.separator + "encrypt.txt"), StandardCharsets.UTF_8.name(), tempList);
        logger.info(" finished!");

    }

    /**
     * 检查文件夹内的文件是否被加密，用到了递归方法，返回值被全局变量记录，通过 findEncryptPdf()函数调用
     *
     * @param srcPdfFileDir 待检查的文件夹
     * @throws java.io.IOException
     */
    private static void findEncryptPdf0(File srcPdfFileDir) throws IOException {

        if (!srcPdfFileDir.isDirectory()) {
            logger.info("srcPdfFileDir is not a Directory: " + srcPdfFileDir.getAbsolutePath());
            return;
        }

        File listFiles[] = srcPdfFileDir.listFiles();

        if (listFiles.length == 0) {
            logger.info("srcPdfFileDir has not file. " + srcPdfFileDir.getAbsolutePath());
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
                        tempList.add("size = 0 :" + f.getAbsolutePath());
                        continue;
                    }

                    Optional<PdfReader> reader;


                    try {
                        reader = getPdfReader(f);
                        if (!reader.isPresent())
                            return;
                    } catch (BadPasswordException e) {
                        // 有打开密码的文件，不能破解，直接登记
                        e.printStackTrace();
                        tempList.add("BadPassword,can not open :" + f.getAbsolutePath());
                        continue;
                    }
                    // logger.info("fileDesc name :" + fileDesc.getAbsolutePath());

                    if (reader.get().isEncrypted()) {// 未加密的文件，，直接登记
                        tempList.add("encrypted :" + f.getAbsolutePath());
                        reader.get().close();
                        continue;
                    } else {
                        tempList.add("not encrypted :" + f.getAbsoluteFile());
                        reader.get().close();
                        continue;
                    }

                }// end if f.isFile
                else
                    continue;
            } else if (f.isDirectory()) {
                findEncryptPdf0(f);
            }// end if f.isDirectory
            else
                continue;

        }// end for

    }

    /**
     * 加密 pdf 文件
     * -
     * 在 adobe reader x 以上的版本中，菜单栏和工具栏（悬浮工具栏）都不能在 pdf 文件中自定义是否显示。
     * 在  adobe reader 软件本身设置中可以关闭
     * (解释http://developers.itextpdf.com/content/best-itext-questions-stackoverview/questions-about-pdf-general/itext7-how-hide-adobe-floating-toolbar-when-showing-pdf-browser)
     * -
     *
     * @param srcPdfFile    源文件
     * @param descPdfFile   目标文件
     * @param userPassword  用户密码，授权打开文件 ,null 为没有密码
     * @param ownerPassword 拥有者密码，授权编辑文件 ,null 为没有密码
     * @param allowPrinting 是否允许打印
     * @param allowCopy     是否允许拷贝文字
     * @throws java.io.IOException
     * @throws DocumentException
     */
    public static void encryptPdf(File srcPdfFile, File descPdfFile,
                                  String userPassword, String ownerPassword,
                                  boolean allowPrinting, boolean allowCopy) throws IOException {

        if (srcPdfFile == null || !srcPdfFile.exists())
            throw new IOException("src pdf file '" + srcPdfFile
                    + "' does not exist.");

        try {

            Optional<PdfReader> reader = getPdfReader(srcPdfFile);
            if (!reader.isPresent())
                return;
            // PdfReader reader = new PdfReader(srcPdfFile, new ReaderProperties().setPassword("World".getBytes()));

            byte[] userPasswordByte = null;
            byte[] ownerPasswordByte = null;

            if (userPassword != null && !userPassword.isEmpty())
                userPasswordByte = userPassword.getBytes();

            if (ownerPassword != null && !ownerPassword.isEmpty())
                ownerPasswordByte = ownerPassword.getBytes();


            int permissions;
            // 设置 allow 属性
            if (allowPrinting && allowCopy)
                permissions = EncryptionConstants.ALLOW_PRINTING | EncryptionConstants.ALLOW_COPY;

            else if (allowPrinting)
                permissions = EncryptionConstants.ALLOW_PRINTING;

            else if (allowCopy)
                permissions = EncryptionConstants.ALLOW_COPY;

            else
                permissions = 0;

            PdfWriter writer = new PdfWriter(descPdfFile.getAbsolutePath(), new WriterProperties()
                    .setStandardEncryption(userPasswordByte, ownerPasswordByte, permissions,
                            EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA));

            PdfDocument pdfDoc = new PdfDocument(reader.get(), writer);
            pdfDoc.close();

            logger.info(srcPdfFile.getAbsoluteFile() + " encrypt finished.");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 解密有用户密码的文件
     *
     * @param srcPdfFile
     * @param descPdfFile
     * @param userPassword pdf 文件的 user or owner password.
     * @throws IOException
     */
    public static void decryptFile(File srcPdfFile, File descPdfFile,
                                   String userPassword) throws IOException, BadPasswordException {

        if (srcPdfFile == null || !srcPdfFile.exists())
            throw new IOException("src pdf file '" + srcPdfFile
                    + "' does not exist.");
        PdfReader reader = getPdfReader(srcPdfFile, userPassword).get();
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(descPdfFile.getAbsolutePath()));
        pdfDoc.close();
    }


    /**
     * 通过第三方提供的工具，破解没有设置打开密码的 pdf 文件。可以递归破解指定文件夹内的所有文件，并保留原来的目录结构
     *
     * @param srcPdfFileDirectory  存放待破解 pdf 文件的文件夹. 破解之后的文件，存放子默认的文件夹内。
     * @param descPdfFileDirectory 破解之后的文件存放的文件夹，保持和源文件同样的结构
     * @param badDirectory         存放有打开密码，不能破解的 pdf 的文件夹。
     * @throws java.io.IOException
     */
    public static void decryptFileDerictory(File srcPdfDirectory, File descPdfDirectory, File badDirectory) throws IOException, DocumentException {

        if (descPdfDirectory.getAbsolutePath().contains(srcPdfDirectory.getAbsolutePath()))
            throw new IOException("目标文件夹不能在原文件夹中");


        if (!isEnoughSpace(srcPdfDirectory, descPdfDirectory))
            return;

        for (File file : srcPdfDirectory.listFiles()) {
            String destPath = descPdfDirectory.getAbsolutePath() + File.separator + file.getName();
            if (file.isFile()) {
                decryptFile(file, Paths.get(destPath).toFile(), badDirectory);
            } else if (file.isDirectory())
                decryptFileDerictory(file, Paths.get(destPath).toFile(), badDirectory);
            else return;

        }
    }

    /**
     * 通过第三方提供的工具，破解没有设置打开密码的 pdf 文件。
     *
     * @param srcPdf
     * @param descPdf
     * @param badDirectory
     * @throws IOException
     */
    public static void decryptFile(File srcPdf, File descPdf, File badDirectory) throws IOException {

        decryptFile(srcPdf, descPdf, null, badDirectory);

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
    public static void decryptFile(File srcPdf, File descPdf, String ownerPassword, File badDirectory) throws IOException {

        if (!srcPdf.exists()) {
            logger.info("{} not exist.", srcPdf.getAbsoluteFile());
            return;
        }
        String extension = FilenameUtils.getExtension(srcPdf.getAbsolutePath());

        if (!extension.equalsIgnoreCase("pdf"))
            return;

        // 损坏的 0 字节文件，直接拷贝到统一的文件夹
        if (FileUtils.sizeOf(srcPdf) == 0) {
            logger.info("{} size =0 ,copy to {}", srcPdf.getAbsoluteFile(), badDirectory.getAbsoluteFile());
            FileUtils.copyDirectory(srcPdf, badDirectory, true);
            return;
        }

        try {

            PdfReader reader;
            if (ownerPassword != null)
                reader = getPdfReader(srcPdf, ownerPassword).get();
            else
                reader = getPdfReader(srcPdf).get();

            logger.info("{}", reader.isEncrypted());

            List<ExecParameter> list = new ArrayList(2);
            list.add(new ExecParameter("-i", srcPdf.getAbsolutePath())); // 只有 key ，没有 value
            list.add(new ExecParameter("-o", descPdf.getAbsolutePath()));
//            list.add(new ExecParameter("-u", ""));
//            list.add(new ExecParameter("-w", ""));


            String re =
                    MyExecUtils.exec(Paths.get(getPdfPdfdecryptExec()), list, 1);

            /**
             *  reader.isEncrypted() 判断不行，itext5 可以，itext7 不行，不知道为什么
             *  只好用其他方法判断。
             *  re 是命令返回的执行信息， pdfdecrypt.exe 如果没有加密，则会返回
             *  The 'D:\itext7\text_src_encrypt.pdf' file hasn't been encrypted.
             *  信息。
             */
            if (re.contains("file hasn't been encrypted")) {// 未加密的文件，直接拷贝 。
                FileUtils.copyFile(srcPdf, descPdf);
                logger.info("not encrypted,copy {} to {} ", srcPdf.getAbsolutePath(), descPdf.getAbsoluteFile());
                return;
            }

            logger.info("decrypted {} to {}", srcPdf.getAbsolutePath(), descPdf.getAbsolutePath());
            // 关闭处理完成的文件
            reader.close();

        } catch (BadPasswordException e) {
            // 有打开密码的文件，不能破解，统一拷贝至一个文件夹。
            logger.info("{} has user password ,copy to {}", srcPdf.getAbsoluteFile(), badDirectory.getAbsoluteFile());
            FileUtils.copyFileToDirectory(srcPdf, badDirectory, true);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 为已经存在的 pdf 文件添加使用日期限制，在警告期内可以查看，过期之后，直接关闭。
     * <p>
     * adobe acrobat 创建的 pdf 文件，可以执行自定义的 javaScript 语句，再利用 adobe reader 打开该 pdf 文件时，可以执行此 js 语句。
     * 此功能仅限于 adobe 的产品。
     * 内建的 js 函数见 http://help.adobe.com/livedocs/acrobat_sdk/10/Acrobat10_HTMLHelp/wwhelp/wwhimpl/common/html/wwhelp.htm?context=Acrobat10_SDK_HTMLHelp&file=JS_Dev_Overview.71.1.html
     * adobe reader 打开 pdf 时，可以禁止 javaScript 执行。
     * -
     * 本方法利用 js 函数限制 pdf 的打开，仅限于 adobe reader 产品。
     * -
     * adobe reader 可以禁止 javaScript， 或者调整客户端电脑的时间，均失效。
     * </p>
     *
     * @param srcPdfFile  源文件
     * @param descPdfFile 目标文件
     * @param startDate   开始日期，字符串格式需为 "2011-01-01"
     * @param alertDays   从开始日期计算，出现警告日期的天数。到达警告期后，出现警告信息，但可以查看文本
     * @param expiredDays 从开始日期计算，到达失效日期的天数。到达失效期后，出现警告信息后，直接关闭文本，不再允许查看文本。
     * @throws java.io.IOException
     */
    public static void addExpireDateWithJavaScriptFile(File srcPdfFile, File descPdfFile, String startDate, int alertDays, int expiredDays) throws IOException {

        if (!srcPdfFile.isFile())
            return;

        //   logger.info("{} , {} {}", alertDays,expiredDays,alertDays>expiredDays);
        // 简单判断开始日期格式
        if (alertDays >= expiredDays) {
            logger.info(" '警告天数 ' " + alertDays + " 大于 '过期天数' " + expiredDays);
            return;
        }

        // 简单判断开始日期格式，可以用 正则表达式
        if (!startDate.matches("(19|20)[0-9]{2}[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])")) {
            logger.info(startDate + " 开始日期格式录入不对，应该型如  'yyyy-MM-dd' 形式");
            return;
        }


        String extension = FilenameUtils.getExtension(srcPdfFile.getAbsolutePath()).toLowerCase();

        if (extension.equals("pdf")) {
            String js = expireDate.replace("startDateStr_replace", startDate)
                    .replace("alertDays_replace", String.valueOf(alertDays))
                    .replace("expiredDays_replace", String.valueOf(expiredDays));


            System.out.println(js);


            Optional<PdfReader> reader = getPdfReader(srcPdfFile);
            if (!reader.isPresent())
                return;
            PdfDocument pdfDocument = new PdfDocument(reader.get(), new PdfWriter(new FileOutputStream(descPdfFile)));
            /**
             * 执行 js 语句
             * 文件即将超出使用日期！\n\n The expiration date is drawing near!
             * 文件超出使用日期！\n\n The expiration date has already passed !
             */
            PdfAction printAction = new PdfAction();
            printAction.put(PdfName.S, PdfName.JavaScript);
            printAction.put(PdfName.JS, new PdfString(js));
            pdfDocument.getCatalog().setOpenAction(printAction); //打开 pdf 时，出现的提示框动作
            pdfDocument.close();
            logger.info(descPdfFile.getAbsolutePath() + " 已经添加了日期限制 !");

        } else {
            logger.info(srcPdfFile.getAbsolutePath() + " 不是 pdf 文件");
        }

    }

    /**
     * 文件夹
     *
     * @param srcPdfFileDir
     * @param descPdfFileDir
     * @param startDate
     * @param alertDays
     * @param expiredDays
     * @throws IOException
     */
    public static void addExpireDateWithJavaScriptDerictory(File srcPdfFileDir, File descPdfFileDir, String startDate, int alertDays, int expiredDays) throws IOException {

        if (!isEnoughSpace(srcPdfFileDir, descPdfFileDir))
            return;

        File listFiles[] = srcPdfFileDir.listFiles();

        if (listFiles.length == 0) {
            logger.info("srcPdfFileDir has not file. " + srcPdfFileDir.getAbsolutePath());
            return;
        }

        for (File f : listFiles) {
            if (f.isFile()) {
                addExpireDateWithJavaScriptFile(f,
                        new File(descPdfFileDir.getAbsolutePath()
                                + File.separator + f.getName()), startDate,
                        alertDays, expiredDays);
            }// end if f.isFile

            else if (f.isDirectory()) {
                addExpireDateWithJavaScriptDerictory(f, descPdfFileDir, startDate, alertDays, expiredDays);
            }// end if f.isDirectory
            else
                continue;

        }// end for
    }


    /**
     * 去掉 OpenAction javaScript
     *
     * @param srcPdfFile
     * @param descPdfFile
     * @throws IOException
     */
    public static void removeOpenActionJavaScript(File srcPdfFile,
                                                  File descPdfFile) throws IOException {
        removeJavaScript(srcPdfFile, descPdfFile, PdfName.OpenAction);

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
     * @param srcPdfFile  源文件夹
     * @param descPdfFile 目标文件夹
     * @param pdfName     添加的 pdf 属性
     * @throws java.io.IOException
     */
    public static void removeJavaScript(File srcPdfFile,
                                        File descPdfFile, PdfName pdfName) throws IOException {


        if (!srcPdfFile.isFile())
            return;

        String extension = FilenameUtils.getExtension(srcPdfFile.getAbsolutePath()).toLowerCase();

        if (extension.equals("pdf")) {

            Optional<PdfReader> reader = getPdfReader(srcPdfFile);
            if (!reader.isPresent())
                return;
            PdfDocument pdfDoc = new PdfDocument(reader.get(), new PdfWriter(descPdfFile.getAbsolutePath()));
            PdfDictionary root = pdfDoc.getCatalog().getPdfObject();

            /**
             * 查看所有的 key ，可以得知 js 加在 那个 PdfName 上，之后去掉即可。
             */
//            for (Map.Entry<PdfName, PdfObject> entry : root.entrySet()) {
//                System.out.println("Key : " + entry.getKey() + "\n Value : " + entry.getValue());
//            }

            // root.remove(PdfName.OpenAction); // 去掉 OpenAction，pdf 文件添加了 OpenAction 时
            root.remove(pdfName);
            pdfDoc.close();

            logger.info(descPdfFile.getAbsolutePath() + " 已经删除 javaScript  !");

        } else {
            logger.info(srcPdfFile.getAbsolutePath() + " 不是 pdf 文件");
        }
    }

    /**
     * 获取 pdfdecrypt.exe 文件路径
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

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfDictionary root = pdfDoc.getCatalog().getPdfObject();
        PdfDictionary names = root.getAsDictionary(PdfName.Names);
        names.remove(PdfName.JavaScript);
        pdfDoc.close();
    }

    /**
     * 检查文件夹内的文件是否被加密，用到了递归方法，返回值被全局变量记录，通过 findEncryptPdf()函数调用
     *
     * @param srcPdfFile 待检查的文件夹
     * @throws java.io.IOException
     */
    private static boolean isEcryptFile(File srcPdfFile) throws IOException {
             PdfReader reader = getPdfReader(srcPdfFile).get();
             return reader.isEncrypted();  // itext7 中不好用，itext5 可以，不知道为什么
    }

        /**
         * 合并 pdf
         *
         * @param srcPdfs
         * @param destPdf
         * @throws IOException
         */
    public void mergeFiles(List<File> srcPdfs, File destPdf) throws IOException {
        //Initialize PDF document with output intent
        PdfDocument pdf = new PdfDocument(new PdfWriter(destPdf.getAbsolutePath()));
        PdfMerger merger = new PdfMerger(pdf);

        for (File file : srcPdfs) {
            //Add pages from the first document
            Optional<PdfReader> reader = getPdfReader(file);
            if (!reader.isPresent())
                return;
            PdfDocument sourcePdf = new PdfDocument(reader.get());
            merger.merge(sourcePdf, 1, sourcePdf.getNumberOfPages());
            sourcePdf.setFlushUnusedObjects(true);
            sourcePdf.close();
        }
        pdf.setFlushUnusedObjects(true);
        pdf.close();
    }

    /**
     * text 在文本的位置
     */
    public enum TextPosition {

        TOP, // 页眉
        BOTTOM, //页脚
        CENTER, //居中
        LEFT,   //左边缘
        RIGHT   //右边缘
    }
}
