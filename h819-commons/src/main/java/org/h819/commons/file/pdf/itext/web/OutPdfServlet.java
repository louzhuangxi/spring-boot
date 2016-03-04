package org.h819.commons.file.pdf.itext.web;


import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @Title: OutPdfServlet.java
 * @Description: TODO(用来生成 pdf 文件流)
 * @author H819
 * @date 2010-3-14
 * @version V1.0
 */
public class OutPdfServlet extends HttpServlet {


    private static Logger logger = LoggerFactory.getLogger(OutPdfServlet.class);

    /**
	 * Constructor of the object.
	 */
	public OutPdfServlet() {
		super();
	}

	/**
	 * 把已经存在的文件，通过流的方式，发送到客户端
	 * 
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	public void createExistPdfOutPutStream(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {

			System.out.println("in ");
			// 获取传过来文件路径
			HttpSession session = request.getSession();
			logger.info("get parameter in servlet 'pdfpath' :"
					+ session.getAttribute("pdfpath"));
			String filePath = (String) session.getAttribute("pdfpath");

			filePath = "D:\\001\\write_sample.pdf";

			logger.info("get parameter in servlet 'pdfpath' :" + filePath);

			// 输出流
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 获得响应数据流
			ServletOutputStream out = response.getOutputStream();
			IOUtils.copy(FileUtils.openInputStream(new File(filePath)), baos);

			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("application/pdf");

			// 设置响应数据大小
			response.setContentLength(baos.size());

			// 将pdf数据流写入响应数据流中
			baos.writeTo(out);
			out.flush();

		} catch (Exception e2) {
			System.out.println("Error in " + getClass().getName() + "\n" + e2);
		}
	}

	/**
	 * 新建 pdf 文件，通过流的方式，发送到客户端
	 * 
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	public void createNewPdfOutPutStream(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {

			String msg = "your message";

			Document document = new Document();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			
			document.open();
			document.add(new Paragraph(msg));
			document.add(Chunk.NEWLINE);
			document.add(new Paragraph("a paragraph"));
			
			
			String javaScript = "function find() { if (event.ctrlKey) {alert(\"你按了Ctrl键\");} }";			
			writer.addJavaScript(javaScript);
			
			PdfAction action = PdfAction.javaScript("find ();", writer);
			writer.setPageAction(PdfWriter.PAGE_OPEN, action);
			document.close();
			
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");

			response.setContentType("application/pdf");

			response.setContentLength(baos.size());

			ServletOutputStream out = response.getOutputStream();
			baos.writeTo(out);
			out.flush();

		} catch (Exception e2) {
			System.out.println("Error in " + getClass().getName() + "\n" + e2);
		}
	}

	/**
	 * 包含 pdf 文件的流，通过流的方式，直接发送到客户端
	 * 

	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
//	public void fileStreamToOutPutStream(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		try {
//
//			Ftp4jUtils ftp = new Ftp4jUtils();
//			FTPClient client = ftp.getFTPClient("129.9.200.236", "read",
//					"readpdf");
//
//			HttpSession session = request.getSession();
//
//			// logger.info("get parameter in servlet 'pdfpath' :"
//			// + session.getAttribute("pdfpath"));
//
//			String filePath = (String) session.getAttribute("pdfpath");
//
//			filePath = "/" + StringUtils.substringAfter(filePath, "/");
//
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ServletOutputStream out = response.getOutputStream();
//
//			logger.info("file :" + filePath);
//
//			if (ftp.existsFile(client, filePath)) {
//
//				logger.info("existsFile :" + filePath);
//
//				logger.info("ftp currentDirectory :" + client.currentDirectory());
//
//				ftp.downLoadFile(client, filePath, baos);
//
//				response.setHeader("Expires", "0");
//				response.setHeader("Cache-Control",
//						"must-revalidate, post-check=0, pre-check=0");
//				response.setHeader("Pragma", "public");
//				response.setContentType("application/pdf");
//				response.setContentLength(baos.size());
//
//				client.disconnect(true);
//				baos.writeTo(out);
//				out.flush();
//				out.close();
//			} else {
//
//				logger.info("not existsFile :");
//				response.setContentType("text/html");
//
//				PrintWriter out2 = response.getWriter();
//
//				response.setContentType("text/html");
//
//				out2
//						.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
//				out2.println("<HTML>");
//				out2.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
//				out2.println("  <BODY>");
//				out2.print("    文件不存在 ");
//				out2.println("  </BODY>");
//				out2.println("</HTML>");
//				out2.flush();
//				out2.close();
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void init() throws ServletException {
		// Put your code here
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in logger
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		createNewPdfOutPutStream(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		createNewPdfOutPutStream(request, response);
	}

}
