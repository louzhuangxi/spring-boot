package org.h819.web;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @version
 * @author jianghui
 *
 *  配合 WEB-INF/commons/ValidateCodeShowSample.jsp 应用
 */


//可参考 http://www.oschina.net/code/snippet_99424_23429?p=1#comments
public class ValidateCodeServlet extends HttpServlet {

	public ValidateCodeServlet() {
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		drawImage(request, response);
	}

//	private Color getRandColor(int fc, int bc) {
//		Random random = new Random();
//		if (fc > 255)
//			fc = 255;
//		if (bc > 255)
//			bc = 255;
//		int r = fc + random.nextInt(bc - fc);
//		int g = fc + random.nextInt(bc - fc);
//		int b = fc + random.nextInt(bc - fc);
//		return new Color(r, g, b);
//	}

	private void drawImage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		ServletOutputStream outs = response.getOutputStream();
		
		//禁止缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0L);
		
		// 在页面中引用图片的时候，图片的宽和高要设置和此数值一样，否则图片会变形
		int width = 60;
		int height = 20;
		BufferedImage image = new BufferedImage(width, height, 1);
		Graphics g = image.getGraphics();
		//Random random = new Random();
		//g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", 0, 18));
		//g.setColor(getRandColor(160, 200));
//		for (int i = 0; i < 155; i++) {
//			int x = random.nextInt(width);
//			int y = random.nextInt(height);
//			int xl = random.nextInt(12);
//			int yl = random.nextInt(12);
//			g.drawLine(x, y, x + xl, y + yl);
//		}
		String validateCode = "";
		for (int i = 0; i < 4; i++) {
			//随机产生数字
			//String rand = String.valueOf(random.nextInt(10));

			//随机产生字母和数字，有大小写区分
			//String rand = RandomStringUtils.random(1, true, true);
			//validateCode = validateCode + rand;
//			g.setColor(new Color(20 + random.nextInt(110), 20 + random
//					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(Integer.toString(i), 13 * i + 6, 16);
		}

		//放入 session，可以随时提取来比较录入的数值是否正确
		request.getSession().setAttribute("validateCode", validateCode);
		g.dispose();
		ImageIO.write(image, "JPEG", outs);
		outs.close();
	}

	private static final long serialVersionUID = 0xf3d7830bc45d05d1L;

}
