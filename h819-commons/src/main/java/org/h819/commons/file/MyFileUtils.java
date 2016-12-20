package org.h819.commons.file;

import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.h819.commons.MyConstants;
import org.h819.commons.MyJsonUtils;
import org.h819.commons.file.base.FileUtilsBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
public class MyFileUtils extends FileUtilsBase {

    private static Logger logger = LoggerFactory.getLogger(MyFileUtils.class);

    /**
     * 静态方法调用，不需要生成实例
     */
    private MyFileUtils() {

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
     * 获取文件扩展名 FileFilter
     *
     * @param caseSensitivity 大小写敏感
     * @param extension       扩展名
     * @return
     */
    public static FileFilter getFileNameExtensionFilter(IOCase caseSensitivity, String... extension) {
        if (extension.length == 0)
            return FileFilterUtils.trueFileFilter();
        else
            return new SuffixFileFilter(extension, caseSensitivity);
    }

    /**
     * 获取文件名满足 pattern  FileFilter
     *
     * @param pattern
     * @param caseSensitivity
     * @return
     */
    public static FileFilter getFileNameRegexFilter(String pattern, IOCase caseSensitivity) {
        return new RegexFileFilter(pattern, caseSensitivity);
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



    public static void main(String[] args) throws Exception {

        File big = new File("F:\\gho\\win7_32.gho");

        String[] filter = {"pdf", "jpg"};

        File dir = new File("G:\\魏勃资料库");
        File dir2 = new File("H:\\00\\");
        File dir3 = new File("H:\\01\\");
        File dir4 = new File("D:\\01");
        File dir5 = new File("D:\\01\\01");

      //  System.out.println(Arrays.asList(dir.listFiles()));
       // System.out.println("file name = "+dir.getName());

        // System.out.println(new ArrayList<>(3).size());


        Map<String, List<String>> lists = findDuplicateFiles(Arrays.asList(dir4));
        MyJsonUtils.prettyPrint(lists);

        System.out.println(StringUtils.center("splite",80,"*"));

       deleteDuplicateFiles("D:\\01\\Ace - Responsive Admin Template 1.3.4\\", Arrays.asList(dir4));
//        long start1 = System.nanoTime();
//        String hash1 = DigestUtils.md5Hex(new FileInputStream(big));
//        System.out.println(hash1 + " : " + (System.nanoTime() - start1));
//
//        long start2 = System.nanoTime();
//        HashCode hc = Files.hash(big, Hashing.md5());
//        System.out.println(hc.toString() + " : " + (System.nanoTime() - start2));

    }
}
