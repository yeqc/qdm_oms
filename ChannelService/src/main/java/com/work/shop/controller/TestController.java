package com.work.shop.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ChannelApiGoods;
import com.work.shop.api.bean.LocalItemQuery;
import com.work.shop.api.service.ApiService;
import com.work.shop.vo.JsonResult;

@Controller
public class TestController extends BaseController {

	@Resource(name = "apiService")
	private ApiService apiService;

	@RequestMapping(value = "/upload.spmvc")
	public void uploadFile(@RequestParam MultipartFile myfile, HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		try {
			jsonResult.setMessage("文件上传成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("数据格式不正确，请检查文件!");
		}
		// response.setCharacterEncoding("UTF-8");
		// response.addHeader("CacheControl", "no-cache");
		// response.addHeader("Content-Type", "application/json");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			out = response.getWriter();
			String jsonStr = mapper.writeValueAsString(jsonResult);
			System.out.println(jsonStr);
			jsonStr = "{success:true,mess:'文件上传成功!'}";
			System.out.println(jsonStr);
			out.print(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
		}
	}

	@RequestMapping(value = "/api.spmvc")
	public void api(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRealPath("/") + "temp\\temp.txt";
		File temp = new File(path);
		if (!temp.exists()) {
			try {
				temp.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "export.spmvc")
	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {

		JsonResult jsonResult = new JsonResult();

		int pageSize = 20;

		String shopCode = "A02588S022";

		String channelCode = "JD_CHANNEL_CODE";

		LocalItemQuery itemQuery = new LocalItemQuery();
		itemQuery.setChannelCode(channelCode);
		itemQuery.setShopCode(shopCode);
		itemQuery.setItemNo(null);
		itemQuery.setPage(1);
		itemQuery.setPageSize(pageSize);
		itemQuery.setStatus("");
		itemQuery.setUserName("");
		ApiResultVO<List<ChannelApiGoods>> rapiResultVO = apiService.searchItemPage(itemQuery);

		List<ChannelApiGoods> flist = rapiResultVO.getApiGoods();

		if (!"0".equals(rapiResultVO.getCode())) {
			jsonResult.setMessage(rapiResultVO.getMessage());
			jsonResult.setIsok(false);
			outPrintJson(response, jsonResult);
			return;
		}

		Integer total = rapiResultVO.getTotal();
		int iTotal = Integer.valueOf(total);

		 int pageNum =0;
//		int pageNum = iTotal / pageSize;
//		if (iTotal % pageSize > 0) {
//			++pageNum;
//		}
		StringBuffer sb = new StringBuffer();

		sb.append("店铺编码" + "," + "店铺名称" + "," + "商品款号" + "," + "渠道商品款号" + "," + "店铺商品名称" + "," + "店铺商品价格" + "," + "上下架状态" + "\r\n");
		if (pageNum > 1) {

			for (int j = 1; j <= pageNum; j++) {
				if (pageNum == j) {
					if (iTotal % pageSize > 0) {
						pageSize = iTotal % pageSize;
					}
				}
				itemQuery.setPage(j);
				ApiResultVO<List<ChannelApiGoods>> apiResultVO = apiService.searchItemPage(itemQuery);
				List<ChannelApiGoods> list = apiResultVO.getApiGoods();

				if (!"0".equals(apiResultVO.getCode())) {
					jsonResult.setMessage(apiResultVO.getMessage());
					jsonResult.setIsok(false);
					outPrintJson(response, jsonResult);
					return;
				}

				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {

						sb.append(list.get(i).getShopCode() == null ? "" : list.get(i).getShopCode() + ",");
						sb.append(list.get(i).getShopName() == null ? "" : list.get(i).getShopName() + ",");
						sb.append(list.get(i).getGoodsSn() == null ? "" : list.get(i).getGoodsSn() + ",");
						sb.append(list.get(i).getChannelGoodsSn() == null ? "" : list.get(i).getChannelGoodsSn() + ",");
						sb.append(list.get(i).getGoodsName() == null ? "" : list.get(i).getGoodsName() + ",");
						sb.append(list.get(i).getPrice() == null ? "" : list.get(i).getPrice() + ",");
						sb.append(list.get(i).getStatus() == null ? "" : list.get(i).getStatus() + "\r\n");
					}
				}
			}

		} else {

			if (flist.size() > 0) {
				for (int i = 0; i < flist.size(); i++) {
					sb.append(flist.get(i).getShopCode() == null ? "" : flist.get(i).getShopCode() + ",");
					sb.append(flist.get(i).getShopName() == null ? "" : flist.get(i).getShopName() + ",");
					sb.append(flist.get(i).getGoodsSn() == null ? "" : flist.get(i).getGoodsSn() + ",");
					sb.append(flist.get(i).getChannelGoodsSn() == null ? "" : flist.get(i).getChannelGoodsSn() + ",");
					sb.append(flist.get(i).getGoodsName() == null ? "" : flist.get(i).getGoodsName() + ",");
					sb.append(flist.get(i).getPrice() == null ? "" : flist.get(i).getPrice() + ",");
					sb.append(flist.get(i).getStatus() == null ? "" : flist.get(i).getStatus() + "\r\n");
				}
			}
		}

		String rootpath = request.getSession().getServletContext().getRealPath("/");

		String fileName = "goodsInfo_" + shopCode + "_" + channelCode + ".csv";

		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		try {

			File CreateFile = new File(rootpath + "/export/");

			// 没有文件夹
			if (!CreateFile.exists()) {
				CreateFile.mkdirs();
			}

			File file = new File(rootpath + "/export/" + fileName);
			
			if(file.isFile() && !file.exists()){
				file.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.newLine();
			bw.write(sb.toString());
			bw.close();

			try {
				response.setContentType("application/x-excel");
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				response.setHeader("Content-Length", String.valueOf(file.length()));
				in = new BufferedInputStream(new FileInputStream(file));
				out = new BufferedOutputStream(response.getOutputStream());
				byte[] data = new byte[1024];
				int len = 0;
				while (-1 != (len = in.read(data, 0, data.length))) {
					out.write(data, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			}
		} catch (Exception e) {

		}
		writeObject(jsonResult, response);

	}

}
