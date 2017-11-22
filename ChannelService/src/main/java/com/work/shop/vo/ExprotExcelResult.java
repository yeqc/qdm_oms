package com.work.shop.vo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExprotExcelResult {

	/**
	 * excel的行;
	 ***/
	private int iRow;
	
	/**
	 * 返回信息;
	 ***/
    private String msg;
    
    
	/**
	 * csv字符串装载所有信息
	 ***/
    private StringBuffer sb;
    
    public StringBuffer getSb() {
		return sb;
	}
	public void setSb(StringBuffer sb) {
		this.sb = sb;
	}
	
	private HSSFWorkbook workbook;
	
	public HSSFWorkbook getWorkbook() {
		return workbook;
	}
	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}
	public int getiRow() {
		return iRow;
	}
	public void setiRow(int iRow) {
		this.iRow = iRow;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
    
}
