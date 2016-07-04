package org.h819.commons.printer;


import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
/*
 * 需要一个打印服务对象。这可通过三种方式实现：在jdk1.4之前的版本，必须要实现java.awt.print.Printable
 * 接口或通过Toolkit.getDefaultToolkit().getPrintJob来获取打印服务对象；在jdk1.4中则还可以通过javax.print.PrintSerivceLookup来查找定位一个打印服务对象。 
 *需要开始一个打印工作。这也有几种实现方法：在jdk1.4之前可以通过java.awt.print.PrintJob（jdk1.1提供的，现在已经很少用了）调用print或printAll方法开始打印工作；
 *也可以通过java.awt.print.PrinterJob的printDialog显示打印对话框，然后通过print方法开始打印；在jdk1.4中则可以通过javax.print.ServiceUI的printDialog显示
打印对话框，然后调用print方法开始一个打印工作。
 */
/**
 * java定位打印，把打印内容打到指定的地方。
 * 
 * @author lyb
 * 
 */
public class LocatePrint implements Printable {
   //总页数
	private int PAGES = 0;
    //打印字符串 
    private String printStr;
    float printX;
    float printY;
    //默认打印字体
    Font m_Printfont=new Font("宋体", Font.PLAIN, 24);
    /*
     * Graphic指明打印的图形环境；PageFormat指明打印页格式（页面大小以点为计量单位，
     * 1点为1英寸的1/72，1英寸为25.4毫米。A4纸大致为595×842点）；page指明页号
     */
    public int print(Graphics gp, PageFormat pf, int page)   throws PrinterException
    {
        Graphics2D g2 = (Graphics2D) gp;
        g2.setPaint(Color.black); // 设置打印颜色为黑色
        if (page >= PAGES) // 当打印页号大于需要打印的总页数时，打印工作结束
            return Printable.NO_SUCH_PAGE;
        g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界
            
        g2.setFont(m_Printfont);
        int i;
      
      this.PrintText(g2, printStr, m_Printfont, 0, 20,pf);
      this.PrintText(g2, printStr, m_Printfont, 0, 50,pf);
      this.PrintText(g2, printStr, m_Printfont, 0, 80,pf);
       // this.PrintText(g2, printStr, m_Printfont, 0, 90);
       
        return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作
    }
    //单位毫米
    public void PrintText(Graphics2D g2,String printStr,Font t,float printX,float printY,PageFormat pf)
    {
    	//英寸
    	float Printx=(float)(printX/25.4-(float)(pf.getWidth()-pf.getImageableWidth())/144)*72;
    	float Printy=(float)(printY/25.4-(float)(pf.getHeight()-pf.getImageableHeight())/144)*72;
    	if (Printx<0 )
    	{
    		Printx=0;
    	}
    
        g2.drawString(printStr,Printx,Printy);
      
    }
    /*
     设置打印字体
     */
    public void SetFont(Font f)
    {
    	//创建字体
    	m_Printfont= f;
    	return ;
  	
     }

    // 打印内容到指定位置
    public void printContent() {
        printStr = "打印测试内容";// 获取需要打印的目标文本
        if (printStr != null && printStr.length() > 0) // 当打印内容不为空时
        {
            PAGES = 1; // 获取打印总页数
            // 指定打印输出格式
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            // 定位默认的打印服务
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
           // PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
           // Toolkit.getDefaultToolkit().getPrintJob
            // 创建打印作业
            PrintService service = ServiceUI.printDialog(null, 200, 200, printService, defaultService, flavor, pras);
            //PrintService service = ServiceUI.printDialog(null, 200, 200, printService, printService , flavor, pras);
            //DocPrintJob job = printService.createPrintJob();
            DocPrintJob job = service.createPrintJob();
            // 设置打印属性
           // PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            // 设置纸张大小,也可以新建MediaSize类来自定义大小
            pras.add(MediaSizeName.ISO_A4);
            DocAttributeSet das = new HashDocAttributeSet();
            // 指定打印内容
            Doc doc = new SimpleDoc(this, flavor, das);
            // 不显示打印对话框，直接进行打印工作
            try {
            
                job.print(doc, pras); // 进行每一页的具体打印操作
            } catch (PrintException pe) {
                pe.printStackTrace();
            }
        } else {
            // 如果打印内容为空时，提示用户打印将取消
            JOptionPane.showConfirmDialog(null,"Sorry, Printer Job is Empty, Print Cancelled!", "Empty", JOptionPane.DEFAULT_OPTION,  JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        LocatePrint lp = new LocatePrint();
        //lp.printContent();
        lp.printFileAction();
    }
    /*打印指定的文件*/
    private void printFileAction()
    {
//    构造一个文件选择器，默认为当前目录
    JFileChooser fileChooser = new JFileChooser();

        int state = fileChooser.showOpenDialog(null);//弹出文件选择对话框
    
        if (state == fileChooser.APPROVE_OPTION)//如果用户选定了文件
        {
        	File file = fileChooser.getSelectedFile();//获取选择的文件
    		//构建打印请求属性集
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
    		//设置打印格式，因为未确定文件类型，这里选择AUTOSENSE
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
    		//查找所有的可用打印服务
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
    		//定位默认的打印服务
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            //显示打印对话框
            PrintService service = ServiceUI.printDialog(null, 200, 200, printService
                                               , defaultService, flavor, pras);
            if (service != null)
            {
            	try
                {
                	DocPrintJob job = service.createPrintJob();//创建打印作业
                    FileInputStream fis = new FileInputStream(file);//构造待打印的文件流
                    DocAttributeSet das = new HashDocAttributeSet();
                    Doc doc = new SimpleDoc(fis, flavor, das);//建立打印文件格式
                    job.print(doc, pras);//进行文件的打印
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                }
            }
        }
    }

}
