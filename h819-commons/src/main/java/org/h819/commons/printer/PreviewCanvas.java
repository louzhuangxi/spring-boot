

package org.h819.commons.printer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;


//            PrintTest, PrintPreviewDialog

class PreviewCanvas extends JPanel
{

    private String printStr;
    private int currentPage;
    private PrintTest preview;

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
        double px = pf.getWidth();
        double py = pf.getHeight();
        double sx = getWidth() - 1;
        double sy = getHeight() - 1;
        double xoff;
        double yoff;
        double scale;
        if(px / py < sx / sy)
        {
            scale = sy / py;
            xoff = 0.5D * (sx - scale * px);
            yoff = 0.0D;
        } else
        {
            scale = sx / px;
            xoff = 0.0D;
            yoff = 0.5D * (sy - scale * py);
        }
        g2.translate((float)xoff, (float)yoff);
        g2.scale((float)scale, (float)scale);
       // Rectangle2D page=new 
        Rectangle2D page = new Rectangle2D.Double(0.0D, 0.0D, px, py);
        g2.setPaint(Color.white);
        g2.fill(page);
        g2.setPaint(Color.black);
        g2.draw(page);
        try
        {
            preview.print(g2, pf, currentPage);
        }
        catch(PrinterException pe)
        {
            g2.draw(new Rectangle2D.Double(0.0D, 0.0D, px, py));
            g2.draw(new Rectangle2D.Double(0.0D, px, 0.0D, py));
        }
    }

    public void viewPage(int pos)
    {
        int newPage = currentPage + pos;
        if(newPage >= 0 && newPage < preview.getPagesCount(printStr))
        {
            currentPage = newPage;
            repaint();
        }
    }

    public PreviewCanvas(PrintPreviewDialog this$0, PrintTest pt, String str)
    {
        currentPage = 0;
        printStr = str;
        preview = pt;
    }
}
