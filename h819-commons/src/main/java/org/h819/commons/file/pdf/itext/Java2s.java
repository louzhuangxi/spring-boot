package org.h819.commons.file.pdf.itext;

/**   
 * @Title: Java2s.java
 * @Description: TODO(添加描述)
 * @author H819
 * @date 2010-3-14 
 * @version V1.0   
 */

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

// http://www.java2s.com 例子测试
public class Java2s {

	// 会在根目录下面建立两个文件
	String witerSampleFile = "d:\\write_sample.pdf";
	
	PdfReader reader;


	/**
	 * 文档打印的时候，会弹出警告提示框，点击确认之后，才可以进行打印
	 */

	// http://www.adobe.com/devnet/actionscript/articles/atp_ria_guide.html
	public void printingWarn(String readSampleFile) {
		try {
			
			Document document = new Document();
			// 2.pdf 将要打印的文档
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(readSampleFile));
			document.open();
			document.add(new Paragraph("test."));
			// 警示语
			// PdfAction jAction =
			// PdfAction.javaScript("app.loadform(){alert(\"这是一个自动装载例子!\");}",writer);
			// PdfAction jAction =
			// PdfAction.javaScript("loadform(){alert(\"这是一个自动装载例子!\");};\r",writer);
			// String jActions
			// ="function loadform(){app.alert('temp!')}";loadform();
			String actionsScripe = "var work = true; function loadform() { if(work=true) app.alert(\"work!\");  }";
			writer.addJavaScript(actionsScripe);
			PdfAction action = PdfAction.javaScript("loadform();", writer);
			writer.setAdditionalAction(PdfWriter.WILL_PRINT, action);

			
			//
			// PdfWriter.setAdditionalAction() 接受的参数
			// The action is triggered just before closing the document.
			// PdfWriter.DOCUMENT_CLOSE

			// The action is triggered just before saving the document.
			//意思是，填写 pdf 表单之后，点击保存按钮唤起的动作，"另存为" 操作不响应
			// PdfWriter.WILL_SAVE

			// The action is triggered just after saving the document.
			// PdfWriter.DID_SAVE

			// The action is triggered just before printing (part of) the
			// document.
			// PdfWriter.WILL_PRINT

			// The action is triggered just after printing.
			// PdfWriter.DID_PRINT

			// PdfWriter.setPageAction 接受的参数
			// The action is triggered when you enter a certain page.
			// PdfWriter.PAGE_OPEN

			// The action is triggered when you leave a certain page.
			// PdfWriter.PAGE_CLOSE

			document.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Java2s ex = new Java2s();
		// ex.printingWarn();
		// ex.getPdfInfor();
		ex.printingWarn("d:\\read_sample.pdf");

	}

}
