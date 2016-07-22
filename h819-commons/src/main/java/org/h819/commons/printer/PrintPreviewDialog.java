package org.h819.commons.printer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;


/*
 * 打印预览实现 
 */

public class PrintPreviewDialog extends JDialog
    implements ActionListener
{
    class PreviewCanvas extends JPanel
    {

        private String printStr;//打印文本
        private int currentPage;//当前页码
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
                g2.draw(new java.awt.geom.Line2D.Double(0.0D, 0.0D, px, py));
                g2.draw(new java.awt.geom.Line2D.Double(0.0D, px, 0.0D, py));
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

        public PreviewCanvas(PrintTest pt, String str)
        {
            currentPage = 0;
            printStr = str;
            preview = pt;
        }
    }


    private JButton nextButton;
    private JButton previousButton;
    private JButton closeButton;
    private JPanel buttonPanel;
    private PreviewCanvas canvas;
  /*
   * 
   */
    public PrintPreviewDialog(Frame parent, String title, boolean modal, PrintTest pt, String str)
    {
        super(parent, title, modal);
        nextButton = new JButton("Next");
        previousButton = new JButton("Previous");
        closeButton = new JButton("Close");
        buttonPanel = new JPanel();
        canvas = new PreviewCanvas(pt, str);
        setLayout();
    }
    /*
     * 
     */

    private void setLayout()
    {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(canvas, "Center");
        nextButton.setMnemonic('N');
        nextButton.addActionListener(this);
        buttonPanel.add(nextButton);
        previousButton.setMnemonic('N');
        previousButton.addActionListener(this);
        buttonPanel.add(previousButton);
        closeButton.setMnemonic('N');
        closeButton.addActionListener(this);
        buttonPanel.add(closeButton);
        getContentPane().add(buttonPanel, "South");
       // setBounds((int)((SystemProperties.SCREEN_WIDTH - (double)400) / (double)2), (int)((SystemProperties.SCREEN_HEIGHT - (double)400) / (double)2), 400, 400);
    }

    public void actionPerformed(ActionEvent evt)
    {
        Object src = evt.getSource();
        if(src == nextButton)
            nextAction();
        else
        if(src == previousButton)
            previousAction();
        else
        if(src == closeButton)
            closeAction();
    }

    private void closeAction()
    {
        setVisible(false);
        dispose();
    }

    private void nextAction()
    {
        canvas.viewPage(1);
    }

    private void previousAction()
    {
        canvas.viewPage(-1);
    }
}
