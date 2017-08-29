package org.h819.commons;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author h819
 * @Description: TODO(编码工具类)
 * @date May 14, 2016 10:15:24
 */
@Slf4j
public class MyCharsetUtils {

    //private static final Logger log = LoggerFactory.getLogger(MyCharsetUtils.class);

    /**
     * 静态方法调用，不需要生成实例
     */
    private MyCharsetUtils() {
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
                    String encodingSrc = MyCharsetUtils.detectEncoding(srcFile).name();
                    // log.info(encodingSrc);
                    InputStreamReader in = new InputStreamReader(new FileInputStream(srcFile), encodingSrc);
                    File f = new File(descDirectory + File.separator + srcFile.getName());
                    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f), charset.name());
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                    // log.info(MyFileUtils.getDetectedEncoding(f).name());
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
            log.info("wrong file type :" + srcFile.getAbsolutePath());
        }
    }

    public static Charset detectEncoding(File file) throws IOException {
        //必须用 BufferedInputStream 包装
        return detectEncoding(new BufferedInputStream(new FileInputStream(file)));
    }

    public static Charset detectEncoding(String string) throws IOException {
        return detectEncoding(string.getBytes());
    }

    public static Charset detectEncoding(byte[] bytes) throws IOException {
        return detectEncoding(new ByteArrayInputStream(bytes));
    }

    /**
     * 利用 icu4j 探测输入流编码，只能探测文本类型的输入流
     * -
     * 抛弃 juniversalchardet
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static Charset detectEncoding(InputStream in) throws IOException {
        final CharsetDetector detector = new CharsetDetector();
        detector.setText(in);

        final CharsetMatch charsetMatch = detector.detect();
        if (charsetMatch == null) {
            log.info("Cannot detect source charset.");
            return null;
        }
        //This is an integer from 0 to 100. The higher the value, the more confidence
        //探测的相似度在 1~100 之间，相似度越高结果越准确。
        int confidence = charsetMatch.getConfidence();
        final String name = charsetMatch.getName();
        log.info("CharsetMatch: {} ({}% 相似度，相似度小于 50% 时，可能编码无法判断。)", name, confidence);
        //打印该文本编码，所有可能性
//        CharsetMatch[] matches = detector.detectAll();
//        System.out.println("All possibilities : " + Arrays.asList(matches));
        return Charset.forName(name);
    }

    public static void main(String[] args) throws Exception {

        File big = new File("F:\\gho\\win7_32.gho");
        String[] filter = {"pdf", "jpg"};

        File dir = new File("G:\\mypic\\");
        File dir2 = new File("H:\\00\\");
        File dir3 = new File("H:\\01\\");
        File dir4 = new File("D:\\01\\00");
        File dir5 = new File("D:\\01\\01");

        if (!dir.isDirectory()) {
            System.out.println("Supplied directory does not exist.");
            return;
        }

    }
}