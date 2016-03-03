package org.h819.commons.file;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * 根据文本生成图片的工具
 *
 * @author Winter Lau
 * @date 2009-7-30 下午12:58:26
 */
public class MyImageUtils {

    private final static IndexColorModel icm = createIndexColorModel();

    /**
     * 生成电子邮件图片
     *
     * @param email
     * @param out
     * @throws java.io.IOException
     */
    public static void MakeEmailImage(String email, OutputStream out)
            throws IOException {
        int height = 22;
        BufferedImage bi = new BufferedImage(255, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        Font mFont = new Font("Verdana", Font.PLAIN, 14);
        g.setFont(mFont);
        g.drawString(email, 2, 19);
        FontMetrics fm = g.getFontMetrics();
        int new_width = fm.charsWidth(email.toCharArray(), 0, email.length()) + 4;
        int new_height = fm.getHeight();
        BufferedImage nbi = new BufferedImage(new_width, new_height,
                BufferedImage.TYPE_BYTE_INDEXED, icm);
        Graphics2D g2 = (Graphics2D) nbi.getGraphics();
        g2.setColor(new Color(0, 0, 0, 0));// 透明
        g2.fillRect(0, 0, new_width, new_height);
        g2.setFont(mFont);
        g2.setColor(new Color(200, 0, 0));
        g2.drawString(email, 2, new_height - 4);

        ImageIO.write(nbi, "gif", out);
    }

    /**
     * 生成电话号码图片
     *
     * @param phone
     * @param out
     * @throws java.io.IOException
     */
    public static void MakePhoneImage(String phone, OutputStream out)
            throws IOException {
        int height = 22;
        BufferedImage bi = new BufferedImage(255, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        Font mFont = new Font("Verdana", Font.BOLD, 20);
        g.setFont(mFont);
        g.drawString(phone, 2, 19);
        FontMetrics fm = g.getFontMetrics();
        int new_width = fm.charsWidth(phone.toCharArray(), 0, phone.length()) + 4;
        int new_height = fm.getHeight();
        BufferedImage nbi = new BufferedImage(new_width, new_height,
                BufferedImage.TYPE_BYTE_INDEXED, icm);
        Graphics2D g2 = (Graphics2D) nbi.getGraphics();
        g2.setColor(new Color(0, 0, 0, 0));// 透明
        g2.fillRect(0, 0, new_width, new_height);
        g2.setFont(mFont);
        g2.setColor(new Color(200, 0, 0));
        g2.drawString(phone, 2, new_height - 4);
        ImageIO.write(nbi, "gif", out);
    }

    /**
     * 生成产品关键特征
     *
     * @param attribute
     * @param out
     * @throws java.io.IOException
     */
    public static void MakeProductAttribute(String attribute, OutputStream out)
            throws IOException {
        int height = 22;
        BufferedImage bi = new BufferedImage(255, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        Font mFont = new Font("宋体", Font.BOLD, 13);
        g.setFont(mFont);
        g.drawString(new String(attribute), 2, 19);
        FontMetrics fm = g.getFontMetrics();
        int new_width = fm.charsWidth(attribute.toCharArray(), 0,
                attribute.length()) + 4;
        int new_height = fm.getHeight();
        BufferedImage nbi = new BufferedImage(new_width, new_height,
                BufferedImage.TYPE_BYTE_INDEXED, icm);
        Graphics2D g2 = (Graphics2D) nbi.getGraphics();
        g2.setColor(new Color(0, 0, 0, 0));// 透明
        g2.fillRect(0, 0, new_width, new_height);
        g2.setFont(mFont);
        g2.setColor(new Color(200, 0, 0));
        g2.drawString(attribute, 2, new_height - 4);
        ImageIO.write(nbi, "gif", out);
    }

    /**
     * 图片放大缩小,以及压缩
     * 追求最好的效果
     * 缩放 :开源项目 : imgscalr
     * @param src
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    private static BufferedImage resizeImage(BufferedImage src, int width, int height) throws IOException {
        // Setup the rendering resources to match the source image's
        BufferedImage dist = new BufferedImage(width, height, (src.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB));
        Image image = src.getScaledInstance( width, height, Image.SCALE_SMOOTH );

        Graphics2D g2 = dist.createGraphics();

        // Scale the image to the new buffer using the specified rendering hint.
        g2.drawImage(image, 0, 0, width, height, null);

        // Scale the image to the new buffer using the specified rendering hint.
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Just to be clean, explicitly dispose our temporary graphics object
        g2.dispose();
        // Return the scaled image to the caller.
        return dist;
    }

    /**
     *
     * 图片缩小,以及压缩，取宽度即可
     * 对于gif和图片本身小于限定大小的略过resize
     * 因为放大后的图片太丑
     *
     * @param src
     * @param dist
     * @param width
     * @throws IOException
     */
    public static void resize(File src, File dist, int width) throws IOException {
        // 先判断文件夹是否存在，不存在时则创建整个目录
        File parentDirectory = dist.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
        // 拒绝resize、GIF，直接copy
        if (src.getName().toLowerCase().endsWith(".gif")) {
            FileUtils.copyFile(src, dist);
            return;
        }
        // 读取图片
        BufferedImage bufferedImage = ImageIO.read(src);
        int w = bufferedImage.getWidth(null);
        int h = bufferedImage.getHeight(null);
        // 比例，取宽度比，转换double，更精确的比例
        double rate = (double)width / w;
        if (rate >= 1) {
            FileUtils.copyFile(src, dist);
            return;
        }
        // 图片缩放
        BufferedImage out = resizeImage(bufferedImage, width, (int)(h * rate));
        // 生成图片
        ImageIO.write(out, "JPEG", dist);
    }

    public static void main(String[] args) throws IOException {
        String num = "020-85551111";
        FileOutputStream fos = new FileOutputStream("D:/phone.gif");
        try {
            MakePhoneImage(num, fos);
        } finally {
            fos.close();
        }
        String email = "xxxxx@oschina.net";
        FileOutputStream fos2 = new FileOutputStream("D:/email.gif");
        try {
            MakeEmailImage(email, fos2);
        } finally {
            fos2.close();
        }
    }

    static IndexColorModel createIndexColorModel() {
        BufferedImage ex = new BufferedImage(1, 1,
                BufferedImage.TYPE_BYTE_INDEXED);
        IndexColorModel icm = (IndexColorModel) ex.getColorModel();
        int SIZE = 256;
        byte[] r = new byte[SIZE];
        byte[] g = new byte[SIZE];
        byte[] b = new byte[SIZE];
        byte[] a = new byte[SIZE];
        icm.getReds(r);
        icm.getGreens(g);
        icm.getBlues(b);
        java.util.Arrays.fill(a, (byte) 255);
        r[0] = g[0] = b[0] = a[0] = 0; // transparent
        return new IndexColorModel(8, SIZE, r, g, b, a);
    }
}
