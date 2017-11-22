package com.work.shop.util;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;

import com.work.shop.bean.GoodsProperty;
import com.work.shop.bean.ProductGoodsGalleryVo;
import com.work.shop.bean.bgcontentdb.BGproductAttrValues;

public class VelocityUtil {
	
	static {
		Properties p = new Properties();
		p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		p.setProperty("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		try {
			Velocity.init(p);
		} catch (Exception e) {
			System.out.println("初始化Velocity 时出错: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static String getParserHtml(String templateContent, GoodsProperty goodsProperty, Map<String, Object> map)throws Exception{
		VelocityContext context = new VelocityContext();
		if (null != goodsProperty && StringUtil.isNotNull(goodsProperty.getChannelCode())) {
			String channelCode = goodsProperty.getChannelCode();
			if (StringUtil.isTaoBaoChannel(channelCode)) {
				context.put("goodsInfoImg", Constants.TB_GOODS_INFO_URL);
				context.put("modelShowImg", Constants.TB_GOODS_MODEL_SHOW_URL);
				context.put("goodsEditImg", Constants.TB_GOODS_EDIT_URL);
				context.put("modelInfoImg", Constants.TB_GOODS_MODEL_INFO_URL);
				context.put("detailShowImg", Constants.TB_GOODS_DETAIL_SHOW_URL);
				context.put("colorSelectImg", Constants.TB_GOODS_COLOR_SELECT_URL);
				context.put("goodsSizeImg", Constants.TB_GOODS_SIZE_URL);
				context.put("goodsInfoSubImg", Constants.TB_GOODS_INFO_SUB_URL);
			} else if (Constants.JD_CHANNEL_CODE.equals(channelCode)) {
				context.put("goodsInfoImg", Constants.JD_GOODS_INFO_URL);
				context.put("modelShowImg", Constants.JD_GOODS_MODEL_SHOW_URL);
				context.put("goodsEditImg", Constants.JD_GOODS_EDIT_URL);
				context.put("modelInfoImg", Constants.JD_GOODS_MODEL_INFO_URL);
				context.put("detailShowImg", Constants.JD_GOODS_DETAIL_SHOW_URL);
				context.put("colorSelectImg", Constants.JD_GOODS_COLOR_SELECT_URL);
				context.put("goodsSizeImg", Constants.JD_GOODS_SIZE_URL);
				context.put("goodsInfoSubImg", Constants.JD_GOODS_INFO_SUB_URL);
			} else if (Constants.YHD_CHANNEL_CODE.equals(channelCode)) {
				context.put("goodsInfoImg", Constants.YHD_GOODS_INFO_URL);
				context.put("modelShowImg", Constants.YHD_GOODS_MODEL_SHOW_URL);
				context.put("goodsEditImg", Constants.YHD_GOODS_EDIT_URL);
				context.put("modelInfoImg", Constants.YHD_GOODS_MODEL_INFO_URL);
				context.put("detailShowImg", Constants.YHD_GOODS_DETAIL_SHOW_URL);
				context.put("colorSelectImg", Constants.YHD_GOODS_COLOR_SELECT_URL);
				context.put("goodsSizeImg", Constants.YHD_GOODS_SIZE_URL);
				context.put("goodsInfoSubImg", Constants.YHD_GOODS_INFO_SUB_URL);
			} else {
				context.put("goodsInfoImg", Constants.MB_GOODS_INFO_URL);
				context.put("modelShowImg", Constants.MB_GOODS_MODEL_SHOW_URL);
				context.put("goodsEditImg", Constants.MB_GOODS_EDIT_URL);
				context.put("modelInfoImg", Constants.MB_GOODS_MODEL_INFO_URL);
				context.put("detailShowImg", Constants.MB_GOODS_DETAIL_SHOW_URL);
				context.put("colorSelectImg", Constants.MB_GOODS_COLOR_SELECT_URL);
				context.put("goodsSizeImg", Constants.MB_GOODS_SIZE_URL);
				context.put("goodsInfoSubImg", Constants.MB_GOODS_INFO_SUB_URL);
			}
		}
		// 商品属性
		context.put("goods", goodsProperty);
		// 商品图片
		if (null != map && !map.isEmpty()) {
			context.put("modelImgs", (List<ProductGoodsGalleryVo>)map.get("modelImgs"));
			context.put("detailImgs", (List<ProductGoodsGalleryVo>)map.get("detailImgs"));
			context.put("detailPoNeImgs", (List<ProductGoodsGalleryVo>)map.get("detailPoNeImgs"));
			context.put("colorImgs", (List<ProductGoodsGalleryVo>)map.get("colorImgs"));
			context.put("attrValues", (List<BGproductAttrValues>)map.get("attrValues"));
			// 以下为：渠道版模板图片列表占位符 使用图片数据
			context.put("modelImgsCh", (List<ProductGoodsGalleryVo>)map.get("modelImgsCh"));
			context.put("detailPoNeImgsCh", (List<ProductGoodsGalleryVo>)map.get("detailPoNeImgsCh"));
			context.put("editModelImg", (ProductGoodsGalleryVo)map.get("editModelImg"));
		}
		// 图片服务器地址
		context.put("imgServer", Constants.PICTURE_DOMAIN);
		StringWriter result = new StringWriter();
		try {
			Velocity.evaluate(context, result, "mystring", templateContent);
		} catch (ParseErrorException pee) {
			pee.printStackTrace();
			System.out.println("ParseErrorException : " + pee);
			throw pee;
		} catch (MethodInvocationException mee) {
			System.out.println("MethodInvocationException : " + mee);
			throw mee;
		} catch (Exception e) {
			System.out.println("Exception : " + e);
			throw e;
		}
		
		return result.toString();
	}
}
