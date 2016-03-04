/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package org.h819.commons.file.pdf.itext.examples.iText_in_Action_2nd_Edition;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.RenderListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ParsingHelloWorld {

	public static final String RESULT = "d://C1068500.pdf";
	/** The resulting PDF. */
	public static final String PDF = "d://hello_reverse.pdf";
	/** A possible resulting after parsing the PDF. */
	public static final String TEXT1 = "d://result1.txt";
	/** A possible resulting after parsing the PDF. */
	public static final String TEXT2 = "d://result2.txt";
	/** A possible resulting after parsing the PDF. */
	public static final String TEXT3 = "d://result3.txt";

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @throws DocumentException
	 * @throws java.io.IOException
	 */
	public void createPdf(String filename) throws DocumentException,
			IOException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(filename));
		// step 3
		document.open();
		// step 4
		// we add the text to the direct content, but not in the right order
		PdfContentByte cb = writer.getDirectContent();
		BaseFont bf = BaseFont.createFont();
		cb.beginText();
		cb.setFontAndSize(bf, 12);
		cb.moveText(88.66f, 367);
		cb.showText("ld");
		cb.moveText(-22f, 0);
		cb.showText("Wor");
		cb.moveText(-15.33f, 0);
		cb.showText("llo");
		cb.moveText(-15.33f, 0);
		cb.showText("He");
		cb.endText();
		// we also add text in a form XObject
		PdfTemplate tmp = cb.createTemplate(250, 25);
		tmp.beginText();
		tmp.setFontAndSize(bf, 12);
		tmp.moveText(0, 7);
		tmp.showText("Hello People");
		tmp.endText();
		cb.addTemplate(tmp, 36, 343);
		// step 5
		document.close();
	}

	/**
	 * Parses the PDF using PRTokeniser
	 * 
	 * @param src
	 *            the path to the original PDF file
	 * @param dest
	 *            the path to the resulting text file
	 * @throws java.io.IOException
	 */
	public void parsePdf(String src, String dest) throws IOException {
		PdfReader reader = new PdfReader(src);
		// we can inspect the syntax of the imported page
		byte[] streamBytes = reader.getPageContent(1);
		PRTokeniser tokenizer = new PRTokeniser(null); // null 不对????
		PrintWriter out = new PrintWriter(new FileOutputStream(dest));
		while (tokenizer.nextToken()) {
			if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING) {
				out.println(tokenizer.getStringValue());
			}
		}
		out.flush();
		out.close();
	}

	/**
	 * Extracts text from a PDF document.
	 * 
	 * @param src
	 *            the original PDF document
	 * @param dest
	 *            the resulting text file
	 * @throws java.io.IOException
	 */
	public void extractText(String src, String dest) throws IOException {

        int pageNum = 6;

		PrintWriter out = new PrintWriter(new FileOutputStream(dest));
		PdfReader reader = new PdfReader(src);
		RenderListener listener = new MyTextRenderListener(out);
		PdfContentStreamProcessor processor = new PdfContentStreamProcessor(
				listener);
		PdfDictionary pageDic = reader.getPageN(pageNum);
		PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
		processor.processContent(
				ContentByteUtils.getContentBytesForPage(reader, pageNum),
				resourcesDic);
		out.flush();
		out.close();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            no arguments needed
	 * @throws DocumentException
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws DocumentException,
			IOException {
		ParsingHelloWorld example = new ParsingHelloWorld();
//		HelloWorld.main(args);
//		example.createPdf(PDF);
//		example.parsePdf(RESULT, TEXT1);
//		example.parsePdf(PDF, TEXT2);
		//example.extractText(PDF, TEXT3);
        example.extractText("d://GB 9685.pdf", "d://GB 9685.txt");


	}
}
