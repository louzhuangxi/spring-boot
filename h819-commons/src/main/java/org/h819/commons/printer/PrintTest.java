

package org.h819.commons.printer;

//import com.szallcom.file.JavaFilter;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


// Referenced classes of package com.szallcom.tools:
//            PrintPreviewDialog

public class PrintTest extends JFrame
    implements ActionListener, Printable
{

    private JButton printTextButton;
    private JButton previewButton;
    private JButton printText2Button;
    private JButton printFileButton;
    private JButton printFrameButton;
    private JButton exitButton;
    private JLabel tipLabel;
    private JTextArea area;
    private JScrollPane scroll;
    private JPanel buttonPanel;
    private int PAGES;
    private String printStr;

    public PrintTest()
    {
        printTextButton = new JButton("Print Text");
        previewButton = new JButton("Print Preview");
        printText2Button = new JButton("Print Text2");
        printFileButton = new JButton("Print File");
        printFrameButton = new JButton("Print Frame");
        exitButton = new JButton("Exit");
        tipLabel = new JLabel("");
        area = new JTextArea();
        scroll = new JScrollPane(area);
        buttonPanel = new JPanel();
        PAGES = 0;
        setTitle("Print Test");
        setDefaultCloseOperation(3);
       // setBounds((int)((SystemProperties.SCREEN_WIDTH - (double)800) / (double)2), (int)((SystemProperties.SCREEN_HEIGHT - (double)600) / (double)2), 800, 600);
        initLayout();
    }

    private void initLayout()
    {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, "Center");
        printTextButton.setMnemonic('P');
        printTextButton.addActionListener(this);
        buttonPanel.add(printTextButton);
        previewButton.setMnemonic('v');
        previewButton.addActionListener(this);
        buttonPanel.add(previewButton);
        printText2Button.setMnemonic('e');
        printText2Button.addActionListener(this);
        buttonPanel.add(printText2Button);
        printFileButton.setMnemonic('i');
        printFileButton.addActionListener(this);
        buttonPanel.add(printFileButton);
        printFrameButton.setMnemonic('F');
        printFrameButton.addActionListener(this);
        buttonPanel.add(printFrameButton);
        exitButton.setMnemonic('x');
        exitButton.addActionListener(this);
        buttonPanel.add(exitButton);
        getContentPane().add(buttonPanel, "South");
    }

    public void actionPerformed(ActionEvent evt)
    {
        Object src = evt.getSource();
        if(src == printTextButton)
            printTextAction();
        else
        if(src == previewButton)
            previewAction();
        else
        if(src == printText2Button)
            printText2Action();
        else
        if(src == printFileButton)
            printFileAction();
        else
        if(src == printFrameButton)
            printFrameAction();
        else
        if(src == exitButton)
            exitApp();
    }

    public int print(Graphics g, PageFormat pf, int page)
        throws PrinterException
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(Color.black);
        if(page >= PAGES)
        {
            return 1;
        } else
        {
            g2.translate(pf.getImageableX(), pf.getImageableY());
            drawCurrentPageText(g2, pf, page);
            return 0;
        }
    }

    private void drawCurrentPageText(Graphics2D g2, PageFormat pf, int page)
    {
        Font f = area.getFont();
        String s = getDrawText(printStr)[page];
        float ascent = 16F;
        int i = f.getSize();
        int lines = 0;
        do
        {
            if(s.length() <= 0 || lines >= 54)
                break;
            int k = s.indexOf(10);
            if(k != -1)
            {
                lines++;
                String drawText = s.substring(0, k);
                g2.drawString(drawText, 0.0F, ascent);
                if(s.substring(k + 1).length() > 0)
                {
                    s = s.substring(k + 1);
                    ascent += i;
                }
            } else
            {
                lines++;
                String drawText = s;
                g2.drawString(drawText, 0.0F, ascent);
                s = "";
            }
        } while(true);
    }

    public String[] getDrawText(String s)
    {
        String drawText[] = new String[PAGES];
        for(int i = 0; i < PAGES; i++)
            drawText[i] = "";

        int suffix = 0;
        int lines = 0;
        do
        {
            if(s.length() <= 0)
                break;
            if(lines < 54)
            {
                int k = s.indexOf(10);
                if(k != -1)
                {
                    lines++;
                    drawText[suffix] = String.valueOf(drawText[suffix]) + String.valueOf(s.substring(0, k + 1));
                    if(s.substring(k + 1).length() > 0)
                        s = s.substring(k + 1);
                } else
                {
                    lines++;
                    drawText[suffix] = String.valueOf(drawText[suffix]) + String.valueOf(s);
                    s = "";
                }
            } else
            {
                lines = 0;
                suffix++;
            }
        } while(true);
        return drawText;
    }

    public int getPagesCount(String curStr)
    {
        int page = 0;
        int count = 0;
        for(String str = curStr; str.length() > 0;)
        {
            int position = str.indexOf(10);
            count++;
            if(position != -1)
                str = str.substring(position + 1);
            else
                str = "";
        }

        if(count > 0)
            page = count / 54 + 1;
        return page;
    }

    private void printTextAction()
    {
        printStr = area.getText().trim();
        if(printStr != null && printStr.length() > 0)
        {
            PAGES = getPagesCount(printStr);
            PrinterJob myPrtJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = myPrtJob.defaultPage();
            myPrtJob.setPrintable(this, pageFormat);
            if(myPrtJob.printDialog())
                try
                {
                    myPrtJob.print();
                }
                catch(PrinterException pe)
                {
                    pe.printStackTrace();
                }
        } else
        {
            JOptionPane.showConfirmDialog(null, "Sorry, Printer Job is Empty, Print Cancelled!", "Empty", -1, 2);
        }
    }

    private void previewAction()
    {
        printStr = area.getText().trim();
        PAGES = getPagesCount(printStr);
        (new PrintPreviewDialog(this, "Print Preview", true, this, printStr)).setVisible(true);
    }

    private void printText2Action()
    {
        printStr = area.getText().trim();
        if(printStr != null && printStr.length() > 0)
        {
            PAGES = getPagesCount(printStr);
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            DocPrintJob job = printService.createPrintJob();
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocAttributeSet das = new HashDocAttributeSet();
            Doc doc = new SimpleDoc(this, flavor, das);
            try
            {
                job.print(doc, pras);
            }
            catch(PrintException pe)
            {
                pe.printStackTrace();
            }
        } else
        {
            JOptionPane.showConfirmDialog(null, "Sorry, Printer Job is Empty, Print Cancelled!", "Empty", -1, 2);
        }
    }

    private void printFileAction()
    {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("USER_DIR"));
     //   fileChooser.setFileFilter(new JavaFilter());
        int state = fileChooser.showOpenDialog(this);
        if(state == 0)
        {
            File file = fileChooser.getSelectedFile();
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            PrintService service = ServiceUI.printDialog(null, 200, 200, printService, defaultService, flavor, pras);
            if(service != null)
                try
                {
                    DocPrintJob job = service.createPrintJob();
                    FileInputStream fis = new FileInputStream(file);
                    DocAttributeSet das = new HashDocAttributeSet();
                    Doc doc = new SimpleDoc(fis, flavor, das);
                    job.print(doc, pras);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
        }
    }

    private void printFrameAction()
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Properties props = new Properties();
        props.put("awt.print.printer", "durango");
        props.put("awt.print.numCopies", "2");
        if(kit != null)
        {
            PrintJob printJob = kit.getPrintJob(this, "Print Frame", props);
            if(printJob != null)
            {
                Graphics pg = printJob.getGraphics();
                if(pg != null)
                    try
                    {
                        printAll(pg);
                    }
                    finally
                    {
                        pg.dispose();
                    }
                printJob.end();
            }
        }
    }

    private void exitApp()
    {
        setVisible(false);
        dispose();
        System.exit(0);
    }

    public static void main(String args[])
    {
        (new PrintTest()).setVisible(true);
    }
}
