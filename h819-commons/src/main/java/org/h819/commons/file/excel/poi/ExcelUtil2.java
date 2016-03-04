package org.h819.commons.file.excel.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

/**
 * 导出Excel的工具类，只需给相应参数即可
 * 
 * @author Administrator
 * 
 */
public class ExcelUtil2 {
	/**
	 * 导出Excel的方法
	 * 
	 * @param request
	 *            Web应用中的request对象
	 * @param excelContent
	 *            数据体集合，集合内放置String数组
	 * @param columnNames
	 *            数据列的头，使用数组
	 * @param titleName
	 *            要导出的文件名称
	 * @param fOut
	 *            导出流，Web中使用response得到
	 * @throws IOException
	 *             可能会爆出的异常
	 */
	@SuppressWarnings("deprecation")
	public static void create(HttpServletRequest request, List<?> excelContent,
			String[] columnNames, String titleName, OutputStream fOut)
			throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();// 建立新HSSFWorkbook对象
		HSSFSheet sheet = wb.createSheet("sheet1");// 建立新的sheet对象
		// ================合并标题列=========================
		Region region = new Region();
		region.setColumnFrom((short) 0);
		region.setColumnTo((short) (columnNames.length - 1));
		region.setRowFrom((short) 0);
		region.setRowTo((short) 0);
		sheet.addMergedRegion(region);
		// ===============================================
		// 标题列列的字体样式
		HSSFFont titleFont = wb.createFont();
		titleFont.setColor(HSSFFont.COLOR_RED);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleFont.setFontHeight((short) 300);

		// 选择列的字体样式
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setColor(HSSFFont.BOLDWEIGHT_NORMAL);
		HSSFPrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		sheet.createFreezePane(0, 1);
		sheet.setAutobreaks(true);
		sheet.setDefaultColumnWidth((short) 13.5);
		printSetup.setFitHeight((short) 100);
		printSetup.setFitWidth((short) 180);
		// 标题列样式
		HSSFCellStyle titlestyle = wb.createCellStyle();
		titlestyle.setFont(titleFont);
		titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titlestyle.setFillForegroundColor(HSSFColor.WHITE.index);
		titlestyle.setFillPattern(HSSFCellStyle.SQUARES);
		titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
		titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
		titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
		// titlestyle.setFillPattern((short)300);

		titlestyle.setWrapText(true);
		// 选择列样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(headerFont);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setHidden(true);
		// 内容列的样式
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 写入标题
		HSSFRow titleRow = null;

		if (!titleName.equals("")) {
			titleRow = sheet.createRow((short) 0);
			titleRow.setHeightInPoints(30.120f);
			HSSFCell titlecell = titleRow.createCell((short) 0);// 标题
			//titlecell.setEncoding(HSSFCell.ENCODING_UTF_16);//
			titlecell.setCellStyle(titlestyle);
			titlecell.setCellValue(titleName);
		}

		// 创建列名称
		HSSFRow headRow = sheet.createRow((short) 1);//
		headRow.setHeightInPoints(20.120f);

		for (int i = 0; i < columnNames.length; i++) {
			HSSFCell cell = headRow.createCell((short) i);
			cell.setCellStyle(style);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);// 设置cell编码解决中文高位字节截断
			cell.setCellValue(columnNames[i]);
		}
		// 创建内容
		for (int i = 0; i < excelContent.size(); i++) {
			// 建立新行
			HSSFRow row = sheet.createRow((short) i + 2);
			for (int j = 0; j < ((Object[]) excelContent.get(i)).length; j++) {
				// 新建一列
				HSSFCell cell = row.createCell((short) j);
				cell.setCellStyle(style2);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);// 设置cell编码解决中文高位字节截断
				if (j == 0) {
					cell.setCellValue((i + 1) + "");
					continue;
				}
				Object t = ((Object[]) excelContent.get(i))[j];
				if (t instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal) ((Object[]) excelContent
							.get(i))[j]).toString());
					continue;
				}
				if (t instanceof java.sql.Date) {
					cell.setCellValue(((java.sql.Date) ((Object[]) excelContent
							.get(i))[j]).toString());
					continue;
				}
				if (t instanceof java.util.Date) {
					cell
							.setCellValue(((java.util.Date) ((Object[]) excelContent
									.get(i))[j]).toString());
					continue;
				}
				cell.setCellValue((String) ((Object[]) excelContent.get(i))[j]);
			}
		}

		request.setCharacterEncoding("gbk");
		MessageFormat.format("{0}/{1}.xls", request.getRealPath("expExcel"),
				Long.toString(System.currentTimeMillis())); // filename是工作薄的存放位置
		wb.write(fOut);
		fOut.flush();
		fOut.close();
	}

}
