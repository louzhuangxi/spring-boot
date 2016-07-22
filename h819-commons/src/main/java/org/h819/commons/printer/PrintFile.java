package org.h819.commons.printer;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apollo
 * 虚拟打印机不可用，只是生成打印队列，并不开始打印
 * 测试不同的虚拟机，看看可不可以(adobe)
 * 有时间测试一下真实打印机是否可行
 * 可以制作成批量打印的工具
 * 
 * 批量打印为的手工方法：
 * 打开打印机的查看打印队列窗口，直接把文件拖进去，即可打印多个文件。
 * 
 * pdffactory 可以设置不出现查看pdf文件窗口，直接保存为pdf
 * 需要设置一下自动保存的文件数目，在打印机的属性中
 * 
 * 
 * 
 * 
 * 
 */
public class PrintFile{

	public static void main(String args[]) {
		
	       try {

	            File f = new File("D:\\p\\url.txt");
	            //构建打印请求属性集
	            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	            pras.add(new Copies(1));
	            

	            //打印文档的属性集
	            DocAttributeSet das = new HashDocAttributeSet();

	            //设置打印格式，因为未确定文件类型，这里选择AUTOSENSE
	            DocFlavor flavor = DocFlavor.INPUT_STREAM.TEXT_HTML_HOST;
	            //DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
	            //查找所有的可用的打印机
	            PrintService printService[] = PrintServiceLookup.lookupPrintServices(null, null);//flavor, pras
	            //   PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
	            System.out.println(printService.length);
	            for (int i = 0; i < printService.length; i++) {
	                System.out.println(printService[i].getName());
	            }
	            
	            //定位默认的打印机
	            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

//	            defaultService.getName();
//	            System.out.println(defaultService.getName());
	            
	            
//	            PrinterJob myPrtJob = PrinterJob.getPrinterJob();
//	            PageFormat pageFormat = myPrtJob.defaultPage();
//	            myPrtJob.setPrintable("", pageFormat);
//	            if(myPrtJob.printDialog())
//	                try
//	                {
//	                    myPrtJob.print();
//	                }
//	                catch(PrinterException pe)
//	                {
//	                    pe.printStackTrace();
//	                }

	            //显示打印对话框
	            PrintService service = ServiceUI.printDialog(null, 50, 50, printService, defaultService, flavor, pras);
	           
	            System.out.println(service.getName());
	            
	            if (service != null) {
	                DocPrintJob job = service.createPrintJob();
      
	                FileInputStream fis = new FileInputStream(f); //构造待打印的文件流
	              //   DocFlavor flavor = DocFlavor.URL.GIF;
	                Doc doc = new SimpleDoc(fis, flavor, das);
	                
	                job.print(doc, pras);

	            }

	        } catch (PrintException ex) {
	            Logger.getLogger(PrintFile.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (IOException ex) {
	            Logger.getLogger(PrintFile.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (HeadlessException ex) {
	            Logger.getLogger(PrintFile.class.getName()).log(Level.SEVERE, null, ex);
	        }

	}
}
