package org.h819.commons.file;

import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.h819.commons.MyConstants;
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
     * Attempts to figure out the character set of the file using the excellent juniversalchardet library.
     * https://code.google.com/p/juniversalchardet/
     * that is the encoding detector library of Mozilla
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Charset getEncoding(File file) throws IOException {

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
     * jar 文件是必须用 java 命令打包的。
     *
     * @param resourceName 待拷贝的 jar 中的文件名称
     *                     参数写法
     *                     "/license.dat" 在 jar 根目录下
     *                     "/abc/license.dat" 在 jar /abc/ 目录下
     * @return 拷贝完成后的文件
     */
    public static File copyResourceFileFromJarLibToTmpDir(String resourceName) {
        // 拷贝资源文件到临时目录
        //  { "/license.dat", "/pdfdecrypt.exe", "/SkinMagic.dll" };
        // { "/STCAIYUN.TTF" };
        InputStream inputStream = MyFileUtils.class.getResourceAsStream(resourceName);

        if (inputStream == null) {
            logger.info(resourceName + " not exist in jar liberary.");
            return null;
        }
        //在系统临时目录下建立文件夹，存放拷贝后的文件
        //建立 java_jar_source_temp 文件夹，不用随机数，否则创建文件夹过多
        String tempfilepath =
                SystemUtils.getJavaIoTmpDir()
                        + File.separator + MyConstants.JarTempDir + File.separator
                        + resourceName;

        File resourceFile = new File(tempfilepath);

        logger.info("resource copy to :" + tempfilepath);

        try {
            // 拷贝资源文件到临时文件夹，每次都覆盖
            FileUtils.copyInputStreamToFile(inputStream, resourceFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            IOUtils.closeQuietly(inputStream);
            return null;
        }


        IOUtils.closeQuietly(inputStream);
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
     * 递归移动原文件夹中的所有文件到指定文件夹，目标文件夹不存在，则创建
     * -
     * 如目标文件已经存在，则改名，增加 _rename ，如果仍然存在，则抛出异常 org.apache.commons.io.FileExistsException
     *
     * @param srcFile
     * @param descDirectory
     * @param existsRename  目标文件存在，是否进行重命名
     */
    public static void moveFilesToDirectoryRecursively(File srcFile, File descDirectory, boolean existsRename) {

        if (srcFile.isFile())
            try {
                String desFileName = descDirectory.getAbsoluteFile() + File.separator + srcFile.getName();
                try {
                    FileUtils.moveFileToDirectory(srcFile, descDirectory, true);
                    System.out.println(String.format("move file %s to %s ", srcFile.getAbsoluteFile(), desFileName));
                } catch (FileExistsException exception) {  //文件存在（仅判断了文件名重复）

                    if (existsRename) {
                        String path = StringUtils.substringBeforeLast(desFileName, ".");
                        String extetion = StringUtils.substringAfterLast(desFileName, ".");
                        String rename = path + "_rename." + extetion;
                        File renamef = new File(rename);
                        if (renamef.exists()) //改名之后还重复，则抛出异常
                            throw new FileExistsException("已经存在 : " + rename);
                        else {
                            FileUtils.moveFile(srcFile, renamef);
                            System.out.println(String.format("exists , rename and move file %s to %s ", srcFile.getAbsoluteFile(), rename));
                        }
                    } else {
                        throw new FileExistsException("已经存在 : " + desFileName);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        else if (srcFile.isDirectory()) {
            File[] files = srcFile.listFiles();
            for (File f : files) {
                moveFilesToDirectoryRecursively(f, descDirectory, existsRename);
            }

        } else {
            if (!srcFile.exists())
                try {
                    throw new IOException(srcFile.getAbsolutePath() + " not exists");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
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
                    String encodingSrc = MyFileUtils.getEncoding(srcFile).name();
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
