package org.h819.commons;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.ImageReader;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.io.FilenameUtils;
import org.h819.commons.file.image.ImageFormat;
import org.springframework.util.Assert;

import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Description : TODO(利用 zxing 生成 QRCode(二维码的一种) https://github.com/zxing/zxing )
 * User: h819
 * Date: 2015/2/12
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public class MyQRCodeUtils {


    public static void main(String[] args) {

        String filePath = "D://Backup3s.jpeg";
        Path file = Paths.get(filePath);
        OutputStream stream = null;
        URI uri = new File(filePath).toURI();


        try {
            //创建
            createQRCodeImageFile(file, ImageFormat.JPEG, "hello worldsfsf !", new Dimension(125, 125));
            //读取
            System.out.println(decodeQRCodeImage(uri));

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 创建 QRCode 图片，默认前景色为黑，背景色为白
     *
     * @param file
     * @param imageFormat
     * @param encodeContent
     * @param imageSize
     * @throws WriterException
     * @throws IOException
     */
    public static void createQRCodeImageFile(Path file, ImageFormat imageFormat, String encodeContent, Dimension imageSize) throws WriterException, IOException {
        createQRCodeImageFile(file, imageFormat, encodeContent, imageSize, Color.black, Color.white);
    }

    /**
     * 创建 QRCode 图片文件
     *
     * @param file            图片
     * @param imageFormat     图片编码格式
     * @param encodeContent   QRCode 内容
     * @param imageSize       图片大小
     * @param foreGroundColor 前景色
     * @param backGroundColor 背景色
     * @throws WriterException
     * @throws IOException
     */
    public static void createQRCodeImageFile(Path file, ImageFormat imageFormat, String encodeContent, Dimension imageSize, Color foreGroundColor, Color backGroundColor) throws WriterException, IOException {
        Assert.isTrue(FilenameUtils.getExtension(file.toString()).toUpperCase().equals(imageFormat.toString()), "文件扩展名和格式不一致");
        QRCodeWriter qrWrite = new QRCodeWriter();
        BitMatrix matrix = qrWrite.encode(encodeContent, BarcodeFormat.QR_CODE, imageSize.width, imageSize.height);
        MatrixToImageWriter.writeToPath(matrix, imageFormat.toString(), file, new MatrixToImageConfig(foreGroundColor.getRGB(), backGroundColor.getRGB()));
    }

    /**
     * 创建 QRCode 图片文件输出流，用于在线生成动态图片，默认前景色为黑，背景色为白
     *
     * @param stream
     * @param imageFormat
     * @param encodeContent
     * @param imageSize
     * @throws WriterException
     * @throws IOException
     */
    public static void createQRCodeImageStream(OutputStream stream, ImageFormat imageFormat, String encodeContent, Dimension imageSize) throws WriterException, IOException {

        createQRCodeImageStream(stream, imageFormat, encodeContent, imageSize);
    }

    /**
     * 创建 QRCode 图片文件输出流，用于在线生成动态图片
     *
     * @param stream
     * @param imageFormat
     * @param encodeContent
     * @param imageSize
     * @param foreGroundColor
     * @param backGroundColor
     * @throws WriterException
     * @throws IOException
     */
    public static void createQRCodeImageStream(OutputStream stream, ImageFormat imageFormat, String encodeContent, Dimension imageSize, Color foreGroundColor, Color backGroundColor) throws WriterException, IOException {

        QRCodeWriter qrWrite = new QRCodeWriter();
        BitMatrix matrix = qrWrite.encode(encodeContent, BarcodeFormat.QR_CODE, imageSize.width, imageSize.height);
        MatrixToImageWriter.writeToStream(matrix, imageFormat.toString(), stream, new MatrixToImageConfig(foreGroundColor.getRGB(), backGroundColor.getRGB()));
    }

    /**
     * 解码 QRCode 图片，解析出其内容
     *
     * @param imageURI QRCode 图片 URI
     * @return 解析后的内容
     * @throws IOException
     */
    public static String decodeQRCodeImage(URI imageURI) throws IOException {

        BufferedImage bufferedImage = ImageReader.readImage(imageURI);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result result = reader.decode(bitmap);
            return result.getText();
        } catch (ReaderException e) {
            e.printStackTrace();
        }
        return "";
    }


}
