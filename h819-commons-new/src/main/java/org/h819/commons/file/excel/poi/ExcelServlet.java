package org.h819.commons.file.excel.poi;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<?> excelContent = getPersonList();
		String[] columnNames = new String[] { "编号", "名称", "住址", "介绍", "出生日期",
				"月薪" };
		String titleName = "测试Excel工具类导出.xls";

		OutputStream fOut = response.getOutputStream();
		response.setHeader("Content-Disposition", "attachment; filename="
				+ new String(titleName.getBytes("GB2312"), "ISO8859-1"));

	//	ExcelUtil.create(request, excelContent, columnNames, titleName, fOut);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	/**
	 * 组装数据实体，实际上是内涵数组的集合，具体组装根据业务自行编写代码实现 数据体中的数字，小数点，日期等不用考虑直接使用
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List getPersonList() {
		List list = new ArrayList();
		String[] p1 = new String[] { "00001", "科研室主任", "海淀区", "北京大学研究生毕业",
				"1988-9-6", "4500.99" };
		String[] p2 = new String[] { "00002", "营销部经理", "朝阳区", "北京国际商务学院本科生",
				"1995-6-8", "6000.25" };
		String[] p3 = new String[] { "00003", "策划部专员", "石景山区", "清华大学应届毕业生",
				"1889-9-5", "10000" };
		list.add(p1);
		list.add(p2);
		list.add(p3);
		return list;
	}

}
