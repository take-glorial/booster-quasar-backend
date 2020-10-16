package kr.co.takeit.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class TakeExcelTemplate {
	
	private HSSFWorkbook		workBook 	= null;
	private HSSFSheet	 		sheet 		= null;
	private HSSFCellStyle[][][] STYLE 		= null;	//정렬, 폰트, 배경
	
	public TakeExcelTemplate(HSSFWorkbook workBook){
		this.workBook = workBook;
	}
	
	public TakeExcelTemplate(HSSFWorkbook workBook, String sheetTitle){
		this.workBook = workBook;
		this.sheet = workBook.createSheet();
		workBook.setSheetName(0, sheetTitle);
	}
	
	public void setStyle(){
		HSSFCellStyle style = getCellStyle(
				true, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, 
				HSSFColor.CORNFLOWER_BLUE.index, HSSFCellStyle.BORDER_MEDIUM, 
				(short)10, HSSFColor.BLACK.index, false,
				false, (short)0, (short)0);
	}
	
	/**
	 * 셀 스타일
	 * @param isWrapText	자동줄바꿈여부(false로 할 경우 \n을 삽입 하면 줄바꿈이 됨)
	 * @param algin			텍스트 가로정렬
	 * @param vAlign		텍스트 세로정렬
	 * @param lineColor		선 색상
	 * @param lineBorder	선 굵기
	 * @param fontSize		폰트 크기
	 * @param fontColor		폰트 색상
	 * @param isBold		폰트 굵기여부
	 * @param bgColor		셀 배경
	 * @param bgPercent		셀 배경 강도
	 * @return
	 */
	public HSSFCellStyle getCellStyle(
			boolean isWrapText, short algin, short vAlign, short lineColor, short lineBorder, 
			short fontSize, short fontColor, boolean isBold, 
			boolean isBgColor, short bgColor, short bgPercent
		)
	{
		HSSFCellStyle style = workBook.createCellStyle();
		style.setWrapText(isWrapText);
		style.setAlignment(algin);
		style.setVerticalAlignment(vAlign);
		style.setBorderBottom(lineBorder);
		style.setBottomBorderColor(lineColor);
		style.setBorderLeft(lineBorder);
		style.setLeftBorderColor(lineColor);
		style.setBorderRight(lineBorder);
		style.setRightBorderColor(lineColor);
		style.setBorderTop(lineBorder);
		style.setTopBorderColor(lineColor);
		
		HSSFFont font = workBook.createFont();
		font.setFontHeightInPoints(fontSize);
		font.setColor(fontColor);
		if(isBold){
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		
		if(isBgColor){
			style.setFillForegroundColor(bgColor);
			style.setFillPattern(bgPercent);
		}
		
		return style;
	}
	
	public HSSFRow createRow(){
		HSSFRow row = sheet.createRow(0);
		return row;
	}
	
	public HSSFCell appendCell(HSSFRow row, HSSFCellStyle style, String value){
		HSSFCell cell  = row.createCell(row.getPhysicalNumberOfCells());
		cell.setCellStyle(style);
		cell.setCellValue(value);
		return cell;
	}
	
	public HSSFCell appendCell(HSSFRow row, HSSFCellStyle style, BigDecimal value){
		HSSFCell cell  = row.createCell(row.getPhysicalNumberOfCells());
		cell.setCellStyle(style);
		
		if(value==null){
			value=new BigDecimal("0");
		}
		
		cell.setCellValue(value.doubleValue());
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		return cell;
	}
	
	public HSSFCell appendCell(HSSFRow row, HSSFCellStyle style, Date value){
		HSSFCell cell  = row.createCell(row.getPhysicalNumberOfCells());
		cell.setCellStyle(style);
		cell.setCellValue(value);
		return cell;
	}
}