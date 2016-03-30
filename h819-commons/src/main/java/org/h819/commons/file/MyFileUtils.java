package org.h819.commons.file;

import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.SystemUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author h819
 * @version V1.0 作为 commons io 类的补充，平时应用的时候，主要参考 commons io，其中没有解决方法的时候，再参考本类。另外
 *          jdk 6 添加了一些新的 io 操作方法，可以再次查看
 * @ClassName: MyFileUtils
 * @Description: TODO(自定义的常用工具类)
 * @date May 14, 2009 10:15:24 AM
 */

// org.apache.commons.io.FilenameUtils 中有一些文件名分隔符的有用的类，如separatorsToSystem 方法等
// java.nio.file.Files ,java.nio.file.Paths 提供很多相应的工具
public class MyFileUtils {

    private static Logger logger = LoggerFactory.getLogger(MyFileUtils.class);

    private static ArrayList<String> fileNames = null;

    /**
     * 静态方法调用，不需要生成实例
     */
    public MyFileUtils() {

    }


    /**
     * windows 系统中，\ , / , : , * , ? , " , < , > , | 九个字符不能出现在文件夹中，用指定的字符代替。
     *
     * @param filename    文件名称
     * @param replacement 替代的字符串
     * @return
     */
    public static String getWindowsLegalFileName(String filename,
                                                 String replacement) {

        return filename.
                replace("\\", replacement).
                replace("/", replacement).
                replace(":", replacement).
                replace("*", replacement).
                replace("?", replacement).
                replace("\"", replacement).
                replace(">", replacement).
                replace("|", replacement).
                replace("<", replacement).trim();


    }

    /**
     * Attempts to figure out the character set of the file using the excellent juniversalchardet library.
     * https://code.google.com/p/juniversalchardet/
     * that is the encoding detector library of Mozilla
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Charset getDetectedEncoding(File file) throws IOException {

        byte[] buf = new byte[4096];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        final UniversalDetector universalDetector = new UniversalDetector(null);
        int numberOfBytesRead;
        while ((numberOfBytesRead = bufferedInputStream.read(buf)) > 0 && !universalDetector.isDone()) {
            universalDetector.handleData(buf, 0, numberOfBytesRead);
        }
        universalDetector.dataEnd();
        String encoding = universalDetector.getDetectedCharset();
        universalDetector.reset();
        bufferedInputStream.close();

        if (encoding != null) {  //解析不出来，默认为 ISO_8859_1
            logger.debug("Detected encoding for {} is {}.", file.getAbsolutePath(), encoding);
            try {
                return Charset.forName(encoding);
            } catch (IllegalCharsetNameException err) {
                logger.debug("Invalid detected charset name '" + encoding + "': " + err);
                return StandardCharsets.ISO_8859_1;
            } catch (UnsupportedCharsetException err) {
                logger.error("Detected charset '" + encoding + "' not supported: " + err);
                return StandardCharsets.ISO_8859_1;
            }
        } else {
            logger.info("encodeing is null, will use 'ISO_8859_1'  : " + file.getAbsolutePath() + " , " + encoding);
            return StandardCharsets.ISO_8859_1;

        }

    }

    /**
     * 拷贝 classpath 中依赖库 jar 资源中的文件到系统临时目录下
     * jar 必须用 java 命令打包
     *
     * @param resourceName 待拷贝的 jar 中的文件名称
     *                     参数写法
     *                     "/license.dat" 在 jar 根目录下
     *                     "/abc/license.dat" 在 jar /abc/ 目录下
     * @return 拷贝完成后，返回值中每个资源文件在数组中的位置顺序，和参数中该文件在数组中的位置顺序一致。
     */
    public static File copyResourceFileFromJarLibToTmpDir(String resourceName) {
        // 拷贝资源文件到临时目录
        // resources = new String[] { "/license.dat", "/pdfdecrypt.exe", "/SkinMagic.dll" };
        InputStream is = MyFileUtils.class.getResourceAsStream(resourceName);

        if (is == null) {
            logger.info(resourceName + " not exist,has not jar liberary.");
            return null;
        }

        //在系统临时目录下建立文件夹，存放拷贝后的文件
        String tempfilepath =
                SystemUtils.getJavaIoTmpDir()
                        + File.separator + RandomUtils.nextInt(0, 10000) + File.separator
                        + resourceName;

        File resourceFile = new File(tempfilepath);

        logger.info("resource copy to :" + tempfilepath);

        try {
            // 拷贝资源文件到临时文件夹
            FileUtils.copyInputStreamToFile(is, resourceFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            IOUtils.closeQuietly(is);
            return null;
        }


        IOUtils.closeQuietly(is);
        return resourceFile;
    }

    /**
     * Finds files within a given directory. All files found are filtered by an name filter.
     * (重新定义 FileUtils.listFiles 方法，使之更加符合使用习惯)
     *
     * @param directory       the directory to search in
     * @param fileNames       file names to apply when finding files.
     * @param recursive       if true all subdirectories are searched as well
     * @param caseSensitivity how to handle case sensitivity, null means case-sensitive (IOCase.SENSITIVE ,  IOCase.INSENSITIVE)
     * @return
     */
    public static Collection<File> listFiles(File directory, String[] fileNames, boolean recursive, IOCase caseSensitivity) {

        if (recursive)
            return FileUtils.listFiles(directory, new NameFileFilter(fileNames, caseSensitivity), TrueFileFilter.INSTANCE);
        else return FileUtils.listFiles(directory, new NameFileFilter(fileNames, caseSensitivity), null);

    }


    /**
     * 读取大文件，不能一次性读取，这样会占用大量内存，应逐条读取
     */
    private static void exampleReadLargeFile() {

        File theFile = new File("");
        LineIterator it = null;

        try {

            it = FileUtils.lineIterator(theFile, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                // do something with line
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LineIterator.closeQuietly(it);
        }
    }

    /**
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        // Windows下路径分隔符是"\"
        // Unix与Linux下路径分隔符是"/"
        // *****如果路径分隔符用File.separator表示，则可移植性更强。

        File f = new File("D:\\download\\gz-tony-spring-authority-master\\spring-authority\\src\\java\\com\\authority\\");
        File f2 = new File("D:\\download\\jiaojie\\2");
        File f3 = new File("F:\\decryptPdfDir\\");
        Collection<File> sf = MyFileUtils.listFiles(f3, new String[]{"AATCC112Y2014.PDF", "ASTMA239Y2014.PDF"}, true, IOCase.INSENSITIVE);
        for (File ss : sf)
            System.out.println("ss =" + ss.getAbsolutePath());
        //MyFileUtils.convertEncoding(f, f2, StandardCharsets.UTF_8);
        //System.out.println(f.getName());
    }

    /**
     * 格式化文件大小显示，便于阅读
     *
     * @param fileSize
     * @return
     */
    public static String getHumanReadableSize(long fileSize) {

        String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int unitIndex = (int) (Math.log10(fileSize) / 3);
        double unitValue = 1 << (unitIndex * 10);
        return new DecimalFormat("#,##0.#").format(fileSize / unitValue) + " " + units[unitIndex];

    }

    /**
     * 为文件重新编码。
     * 仅对文本文件进行编码。
     *
     * @param srcFile       源文件（也可以是文件夹）
     * @param descDirectory 转码之后的文件夹
     * @param charset       指定的转换完成的编码格式，如 StandardCharsets.UTF_8
     */
    // 该方法还不成熟，utf-8 编码变成 asc 时会乱码。
    // gb2312 到 utf-8 不出现乱码

    // 所以该方法还不能实现在 "windows 的文本编辑器"中，选择编码之后，文本“另存”的功能
    public static void convertEncoding(File srcFile, File descDirectory, Charset charset) {

        // 待编码的文本文件类型
        String[] extension = {"java", "html", "htm", "php", "ini", "bat", "css", "txt", "js", "jsp", "xml", "sql", "properties"};
        if (!descDirectory.exists()) {
            descDirectory.mkdir();
        }

        if (srcFile.isFile()) {
            if (FilenameUtils.isExtension(srcFile.getName().toLowerCase(), extension)) {
                try {
                    String encodingSrc = MyFileUtils.getDetectedEncoding(srcFile).name();
                    // logger.info(encodingSrc);
                    InputStreamReader in = new InputStreamReader(new FileInputStream(srcFile), encodingSrc);
                    File f = new File(descDirectory + File.separator + srcFile.getName());
                    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f), charset.name());
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                    // logger.info(MyFileUtils.getDetectedEncoding(f).name());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (srcFile.isDirectory()) {

            File fs[] = srcFile.listFiles();
            for (File f : fs)
                convertEncoding(f, new File(descDirectory + File.separator + srcFile.getName()), charset);
        } else {
            logger.info("wrong file type :" + srcFile.getAbsolutePath());
        }

    }


}
