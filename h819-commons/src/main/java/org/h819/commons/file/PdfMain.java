package org.h819.commons.file;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017-05-13
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
public class PdfMain {

    public static final String DEST = "D:\\itext7\\watermark1.pdf";
    public static final String DEST2 = "D:\\itext7\\watermark2.pdf";
    public static final String IMG = "D:\\itext7\\itext.png";
    public static final String SRC = "D:\\itext7\\source.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfMain().manipulatePdf(DEST);
    }


    void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        int n = pdfDoc.getNumberOfPages();
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        Paragraph p = new Paragraph("My watermark (text)").setFont(font).setFontSize(30);
        // image watermark
        ImageData img = ImageDataFactory.create(IMG);
        //  Implement transformation matrix usage in order to scale image
        float w = img.getWidth();
        float h = img.getHeight();
        // transparency
        PdfExtGState gs1 = new PdfExtGState();
        gs1.setFillOpacity(0.5f);
        // properties
        PdfCanvas over;
        Rectangle pagesize;
        float x, y;
        // loop over every page
        for (int i = 1; i <= n; i++) {
            PdfPage pdfPage = pdfDoc.getPage(i);
            pagesize = pdfPage.getPageSizeWithRotation();
            pdfPage.setIgnorePageRotationForContent(true);

            x = (pagesize.getLeft() + pagesize.getRight()) / 2;
            y = (pagesize.getTop() + pagesize.getBottom()) / 2;
            over = new PdfCanvas(pdfDoc.getPage(i));
            over.saveState();
            over.setExtGState(gs1);
            if (i % 2 == 1) {
                doc.showTextAligned(p, x, y, i, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            } else {
                over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2), false);
            }
            over.restoreState();
        }
        doc.close();
    }

    void manipulatePdf2(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        int n = pdfDoc.getNumberOfPages();
        PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(FontConstants.HELVETICA));
        Paragraph p = new Paragraph("My watermark (text)").setFont(font).setFontSize(30);
        // image watermark
        ImageData img = ImageDataFactory.create(IMG);
        //  Implement transformation matrix usage in order to scale image
        float w = img.getWidth();
        float h = img.getHeight();
        // transparency
        PdfExtGState gs1 = new PdfExtGState();
        gs1.setFillOpacity(0.5f);
        // properties
        PdfCanvas over;
        Rectangle pagesize;
        float x, y;
        // loop over every page
        for (int i = 1; i <= n; i++) {
            pagesize = pdfDoc.getPage(i).getPageSize();
            x = (pagesize.getLeft() + pagesize.getRight()) / 2;
            y = (pagesize.getTop() + pagesize.getBottom()) / 2;
            over = new PdfCanvas(pdfDoc.getPage(i));
            over.saveState();
            over.setExtGState(gs1);
            if (i % 2 == 1)
                doc.showTextAligned(p, x, y, i, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            else
                over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2), true);
            over.restoreState();
        }
        doc.close();
    }
}
