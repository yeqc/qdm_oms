package com.work.shop.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.work.shop.bean.ChannelGoods;
import com.work.shop.util.FileUtil;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;


@Controller
@RequestMapping(value= "fileUpload")
public class FIleUploadController extends BaseController{
	
	
	@RequestMapping(value= "channelGoodsInfoList.spmvc")
	public void channelGoodsInfoPage(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		
	}
	
	@RequestMapping(value = "upload.spmvc")
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, 
			HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("upload");
		String fileName = file.getOriginalFilename();
//		String fileName = new Date().getTime()+".jpg";
		System.out.println(path);
		File targetFile = new File(path, fileName);
		if(!targetFile.exists()){
			targetFile.mkdirs();
		}

		//保存
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<ChannelGoods> list = new ArrayList<ChannelGoods>();
		ChannelGoods channelGoods = new ChannelGoods();
		channelGoods.setGoodsName("11111");
		list.add(channelGoods);
		Paging paging = new Paging(1, list);

		writeObject(paging, response);
		return null ;
	}
	
	@RequestMapping("/fileDownloadCsv.spmvc")
	public void fileDownloadCSV(HttpServletRequest request, HttpServletResponse response, String filePath) throws Exception{
		InputStream fis = null;
		try {
			String path = filePath.trim();
			if (StringUtil.isEmpty(path)) {
				return;
			}
			String serverPath = FileUtil.getDeployPath() + "/page/csvModel/" + path;

			File file = new File(serverPath);
			String fileName = filePath;
			
			fis = new BufferedInputStream(new FileInputStream(serverPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream;charset=GBK;");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fis) {
				fis.close();
				fis = null;
			}
		}
	}
}
