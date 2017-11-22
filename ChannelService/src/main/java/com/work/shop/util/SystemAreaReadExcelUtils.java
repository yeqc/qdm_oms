package com.work.shop.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.work.shop.bean.SystemArea;
import com.work.shop.bean.TicketInfo;

public class SystemAreaReadExcelUtils {
	
	 /**
     * 读取xls文件内容
     * 
     * @return List<SystemArea>对象
     * @throws IOException
     *             输入/输出(i/o)异常
     */
    public static List<SystemArea> readXls(InputStream is) throws IOException {
      //  InputStream is = new FileInputStream(file);//读取excel名称
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        SystemArea systemArea = null;
        List<SystemArea> list = new ArrayList<SystemArea>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                systemArea = new SystemArea();
                // 循环列Cell
                //0地区编码 1地区类型 2地区名称3关联os地区编码 4OS地区名称
                // for (int cellNum = 0; cellNum <=4; cellNum++) {
                HSSFCell areaId = hssfRow.getCell((short) 0);
                if (areaId == null) {
                    continue;
                }
                String cs ="";
                if(getValue(areaId).indexOf(".")==-1){
                	cs = getValue(areaId);
                }else{
                	cs = getValue(areaId).substring(0, getValue(areaId).indexOf("."));
                }
                systemArea.setAreaId(cs);//地区编码
                HSSFCell areaType = hssfRow.getCell((short) 1);
                if (areaType == null) {
                    continue;
                }
                String at ="";
                if(getValue(areaType).indexOf(".")==-1){
                	at = getValue(areaType);
                }else{
                	at = getValue(areaType).substring(0, getValue(areaType).indexOf("."));
                }
                systemArea.setAreaType(at);//地区类型
                
                HSSFCell areaName = hssfRow.getCell((short) 2);//地区名称
                if (areaName == null) {
                    continue;
                }
                String an ="";
                if(getValue(areaName).indexOf(".")==-1){
                	an = getValue(areaName);
                }else{
                	an = getValue(areaName).substring(0, getValue(areaName).indexOf("."));
                }
                systemArea.setAreaName(an);
                
                HSSFCell osRegionId = hssfRow.getCell((short) 3);//关联os地区编码
                if(osRegionId ==null ){
                	continue;
                }
                    String or ="";
                    if(getValue(osRegionId).indexOf(".")==-1){
                    	or = getValue(osRegionId);
                    }else{
                    	or = getValue(osRegionId).substring(0, getValue(osRegionId).indexOf("."));
                    }
//                systemArea.setOsRegionId(Short.parseShort(or));
                systemArea.setOsRegionId(or);
                HSSFCell osRegionName = hssfRow.getCell((short) 4);//关联os地区名称
                if(osRegionName==null){
                	continue;
                }
                String orn ="";
                if(getValue(osRegionName).indexOf(".")==-1){
                	orn = getValue(osRegionName);
                }else{
                	orn = getValue(osRegionName).substring(0, getValue(osRegionName).indexOf("."));
                }
                systemArea.setOsRegionName(orn);
                
                HSSFCell pid = hssfRow.getCell((short) 5);//父id
                if(pid != null ){
                	 String pi ="";
                     if(getValue(pid).indexOf(".")==-1){
                     	pi = getValue(pid);
                     }else{
                     	pi = getValue(pid).substring(0, getValue(pid).indexOf("."));
                     }
//                	systemArea.setPid(( Integer.parseInt(pi)));
                }
                list.add(systemArea);
            }
        }
        return list;
    }
 
    /**
     * 得到Excel表中的值
     * 
     * @param hssfCell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        }else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        }else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
 
}


