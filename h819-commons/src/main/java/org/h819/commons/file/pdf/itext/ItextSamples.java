package org.h819.commons.file.pdf.itext;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;

/**
 * 在这里测试 itext 各种方法
 */
public class ItextSamples {

    private static Logger logger = LoggerFactory.getLogger(ItextSamples.class);

	/**
	 * Reads and encrypts an existing PDF file.
	 *
	 *            no arguments needed
	 */

	void encrypt(String fileInput, String fileOutput) {

		try {
			
			PdfReader reader = new PdfReader(fileInput);
			// PdfEncryptor.encrypt(reader,
			// new FileOutputStream("encrypted2.pdf"),
			// "Hello".getBytes(),
			// "World".getBytes(),
			// PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY ,
			// false);

			// 不加打开密码，不允许打印和保存
			PdfEncryptor.encrypt(reader, new FileOutputStream(fileOutput),
					null, null, 0, false);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reads the pages of an existing PDF file, adds pagenumbers and a watermark.
	 * 测试加文字水印，注意解决汉字问题
	 * 还没有成功
	 */

	void waterMark(String fileInput, String fileOutput, String waterMark) {

		try {
			// we create a reader for a certain document
			PdfReader reader = new PdfReader(fileInput);
			int n = reader.getNumberOfPages();
			// we create a stamper that will copy the document to a new file
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					fileOutput));

			int i = 0;

			PdfContentByte over;

			BaseFont bf = BaseFont.createFont("FOND.TTC,0",
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

			while (i < n) {
				i++;
				// watermark under the existing page
				//under = stamp.getUnderContent(i);
				//under.addImage(img);
				// text over the existing page

				over = stamp.getOverContent(i);
				over.beginText();
				//文字一
				over.setFontAndSize(bf, 16);
				over.setTextMatrix(10, 10);
				over.showText("page " + i);

				//文字二
				String str = new String(waterMark.getBytes("GBK"), "GBK");
				over.setFontAndSize(bf, 24);
				over.showTextAligned(Element.ALIGN_BOTTOM, str, 30, 30, 90);
				over.endText();

			}
			stamp.close();
		} catch (Exception de) {
			de.printStackTrace();
		}
	}

	public static void main(String[] args) {

		ItextSamples itext = new ItextSamples();

		itext.waterMark("D:\\00\\d.pdf", "D:\\00\\d.1.pdf", "北京市质量技术监督信息研究所");

	}
}