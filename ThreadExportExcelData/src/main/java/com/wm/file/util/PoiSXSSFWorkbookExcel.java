package com.wm.file.util;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

/**
*

    @ClassName: PoiSXSSFWorkbookExcel.java
    @Description: 导出Excel 采用SXSSFWorkbook实现方式 excel为07之后版本，只支持.xlsx格式
    @author: l

*/
public class PoiSXSSFWorkbookExcel {

/**
 * 导出到单个sheet页中
 * @param title
 *            标题
 * @param List<List<String>> result
 *            导出数据内容
 * @return wb
 *            SXSSFWorkbook对象
 * @throws IOException
 * 
 */

public static SXSSFWorkbook getSxssfwbExcel(String sheetTitle,String[] title, List<List<String>> result) {
	SXSSFWorkbook wb = new SXSSFWorkbook();

	//int sheetNum = 0;// 记录额外创建的sheet数量
	Sheet sheet = wb.createSheet(sheetTitle);
	// wb.setSheetName(sheetNum, sheetTitle+sheetNum);
	int rownum = 0;
	Row row = sheet.createRow(rownum);
	
	// 设置并获取到需要的样式
    XSSFCellStyle xssfCellStyleHeader = getAndSetXSSFCellStyleHeader(wb);
	
	Cell cell;
	// 创建标题,此时row=0,即第一行
	for (int j = 0; j < title.length; j++) {
		cell = row.createCell(j);
		cell.setCellValue(title[j]);
		cell.setCellStyle(xssfCellStyleHeader);
	}

	// 遍历集合数据，创建excel内容,产生数据行
	if (result != null) {
		int index = 1;
		List<String> m = null;
		for (int i = 0; i < result.size(); i++) {
			row = sheet.createRow(index);
			int cellIndex = 0;
			m = result.get(i);
			for (String str : m) {
				row.createCell((short) cellIndex).setCellValue(str);
				cellIndex++;
			}
			
			index++;
		}
	}
	
	
	return wb;
}



/**
 * 每个sheet页导出不同内容到多个sheet页中，每个sheet页表头内容不相同
 * 
 * @param wb
 *            SXSSFWorkbook对象
 * @param sheetNum
 *            sheet页
 * @param sheetTitle
 *            sheet页名称
 * @param title
 *            标题
 * @param List<List<String>> result
 *            导出数据内容
 * @return wb
 *            SXSSFWorkbook对象
 * @throws IOException
 * 
 */

public static SXSSFWorkbook getSxssfwbManyDiffSheets(SXSSFWorkbook wb, int sheetNum, String sheetTitle,
		String[] title, List<List<String>> result) {
	
	Sheet sheet = wb.createSheet();
	wb.setSheetName(sheetNum, sheetTitle);
	int rownum = 0;
	Row row = sheet.createRow(rownum);
	
	// 设置并获取到需要的样式
    XSSFCellStyle xssfCellStyleHeader = getAndSetXSSFCellStyleHeader(wb);
	
	Cell cell;
	// 创建标题,此时row=0,即第一行
	for (int j = 0; j < title.length; j++) {
		cell = row.createCell(j);
		cell.setCellValue(title[j]);
		cell.setCellStyle(xssfCellStyleHeader);
	}

	// 遍历集合数据，创建excel内容,产生数据行
	if (result != null) {
		int index = 1;
		List<String> m = null;
		for (int i = 0; i < result.size(); i++) {
			row = sheet.createRow(index);
			int cellIndex = 0;
			m = result.get(i);
			for (String str : m) {
				row.createCell((short) cellIndex).setCellValue(str);
				cellIndex++;
			}
			
			index++;
		}
	}
	
	
	return wb;
}




/**
 * 每个sheet页导出相同内容到多个sheet页中，每个sheet页表头内容相同
 * 每个sheet页目前总数设置为n,这里设置n=60000
 * @param title
 *            标题
 * @param List<List<String>> result
 *            导出数据内容
 * @return wb
 *            SXSSFWorkbook对象
 * @throws IOException
 * 
 */

public static SXSSFWorkbook getSxssfwbManySameSheets(String sheetTitle,String[] title, List<List<String>> result) {
	SXSSFWorkbook wb = new SXSSFWorkbook();
	int sheetNum = 0;// 记录额外创建的sheet数量
	Sheet sheet = wb.createSheet(sheetTitle + sheetNum);
	// wb.setSheetName(sheetNum, sheetTitle+sheetNum);
	int rownum = 0;
	Row row = sheet.createRow(rownum);
	
	// 设置并获取到需要的样式
    XSSFCellStyle xssfCellStyleHeader = getAndSetXSSFCellStyleHeader(wb);
	
	Cell cell;
	// 创建标题,此时row=0,即第一行
	for (int j = 0; j < title.length; j++) {
		cell = row.createCell(j);
		cell.setCellValue(title[j]);
		cell.setCellStyle(xssfCellStyleHeader);
	}

	// 遍历集合数据，创建excel内容,产生数据行
	if (result != null) {
		List<String> m = null;
		for (int i = 0; i < result.size(); i++) {
			if ((i + 1) % 60000 == 0) {
				sheetNum++;
				sheet = wb.createSheet(sheetTitle + sheetNum);
				row = sheet.createRow(0);
				// 声明列对象，参数为列索引，可以是0～255之间的任何一个
				// 创建标题,此时row=0,即第一行
				for (int j = 0; j < title.length; j++) {
					cell = row.createCell(j);
					cell.setCellValue(title[j]);
					cell.setCellStyle(xssfCellStyleHeader);
				}
			}
			row = sheet.createRow((i + 1) - (sheetNum * 60000)+sheetNum);
			int cellIndex = 0;
			m = result.get(i);
			for (String str : m) {
				row.createCell((short) cellIndex).setCellValue(str);
				cellIndex++;
			}
		}
	}
	return wb;
}


/**
 * 获取并设置header样式
 */
private static XSSFCellStyle getAndSetXSSFCellStyleHeader(SXSSFWorkbook sxssfWorkbook) {
    XSSFCellStyle xssfCellStyle = (XSSFCellStyle) sxssfWorkbook.createCellStyle();
    Font font = sxssfWorkbook.createFont();
    // 字体大小
    font.setFontHeightInPoints((short) 14);
    // 字体粗细
    //font.setFontName("楷体");
    // 将字体应用到样式上面
    xssfCellStyle.setFont(font);
    // 是否自动换行
    xssfCellStyle.setWrapText(false);
    // 水平居中
    xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
    // 垂直居中
    xssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    return xssfCellStyle;
}

}