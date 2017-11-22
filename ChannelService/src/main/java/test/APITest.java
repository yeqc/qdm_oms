package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import au.com.bytecode.opencsv.CSVReader;

import com.alibaba.fastjson.JSON;
import com.banggo.scheduler.exception.FatalException;
import com.banggo.scheduler.exception.WarnningException;
import com.banggo.scheduler.task.TaskExecuteRequest;
import com.caucho.hessian.client.HessianProxyFactory;
import com.work.shop.api.ConstantValues;
import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.LocalItemQuery;
import com.work.shop.api.handler.ChannelApiHandler;
import com.work.shop.api.handler.JDApiHandler;
import com.work.shop.api.service.ApiService;
import com.work.shop.api.service.ChannelApiService;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.ChannelShopExample.Criteria;
import com.work.shop.scheduler.ExportOnSaleDataTimerTask;
import com.work.shop.scheduler.service.BaseScheduleService;
import com.work.shop.service.ShopService;
import com.work.shop.util.ActiveMqUtils;
import com.work.shop.util.Constants;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.FtpUtil;
import com.work.shop.util.HttpClientUtil;
import com.work.shop.util.PropertieFileReader;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.vo.ChannelShopVo;
import com.work.shop.vo.SyncStockParam;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" }) 
public class APITest {

	private ChannelApiHandler service;

	@Test
	public void dataCountTest() {
		ApplicationContext contex = new ClassPathXmlApplicationContext("applicationContext.xml");
		ShopService shopService = (ShopService) contex.getBean("shopService");
		ChannelShopExample example = new ChannelShopExample();
		Criteria criteria = example.or();
		criteria.andShopStatusEqualTo(Byte.valueOf("1")); // 默认加载已激活状态的店铺
		criteria.andShopChannelEqualTo(Byte.valueOf("1"));
		List<ChannelShopVo> list = shopService.getChannelShopList(example, false);
		if (list != null && list.size() > 0) {
			StringBuffer sb = new StringBuffer("结果：" + "\n");
			for (ChannelShopVo channelShopVo : list) {
				if (StringUtil.isNotNull(channelShopVo.getChannelCode()) && StringUtil.isNotNull(channelShopVo.getShopCode())) {
					String channelCode = channelShopVo.getChannelCode();
					String shopCode = channelShopVo.getShopCode();
					ApiService apiService = (ApiService) contex.getBean("apiService");
					LocalItemQuery itemQuery = new LocalItemQuery();
					itemQuery.setChannelCode(channelCode);
					itemQuery.setShopCode(shopCode);
					itemQuery.setItemNo(null);
					itemQuery.setPage(1);
					itemQuery.setPageSize(10);
					itemQuery.setStatus("1");
					itemQuery.setUserName("test");
					ApiResultVO upvo = apiService.searchItemPage(itemQuery);
					if (upvo == null) {
						continue;
					}
					int up = Integer.valueOf(upvo.getTotal() == null ? 0 : upvo.getTotal());
					itemQuery.setStatus("2");
					ApiResultVO downvo = apiService.searchItemPage(itemQuery);
					if (downvo == null) {
						continue;
					}
					int down = Integer.valueOf(downvo.getTotal() == null ? 0 : downvo.getTotal());
					sb.append(shopCode + " : " + up + "+" + down + "=" + (up + down)).append("\n");
				}
			}
			System.out.println(sb.toString());
		}
	}

	private void init(String key) {
		Map<String, String> secretInforMap = new HashMap<String, String>();
		service.buildApiClient(secretInforMap);
	}

	/**
	 * Test ok
	 */
	@Test
	public void SearchItemPageTest() {
		// init("YHD");
		String goodsSn = "";
		// goodsSn = "207948";
		// goodsSn = "561576";
		// goodsSn = "242626";
		// goodsSn = "238285";

		// ApiResultVO result = service.SearchItemPage(null, null, 1, 15, "");
		for (int i = 10; i < 50; i++) {
			init("YHD");
			System.out.println(new Date());
			ApiResultVO result = service.getBaseData(null);

			System.out.println("第" + i + "页" + result.getCode() + " --------- " + result.getMessage() + " --------- " + (result.getApiGoods() == null ? 0 : result.getApiGoods()));
		}
	}

	@Test
	public void testScheduler() throws Exception {
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setOverloadEnabled(true);
		String url = "http://127.0.0.1/ChannelService_dev/task";
		// String url = "http://10.100.200.64/ChannelService/task";
		// String url = "http://10.100.200.51:9071/BGQuartzService/task";
		BaseScheduleService basic = (BaseScheduleService) factory.create(BaseScheduleService.class, url);
		String name = "derek";
		basic.doScheduler();

		// ApplicationContext contex = new
		// ClassPathXmlApplicationContext("applicationContext.xml");
		// JobSynchronousStatus jobSynchronousStatus = (JobSynchronousStatus)
		// contex.getBean("jobSynchronousStatus");
		// jobSynchronousStatus.doScheduler();

	}

	@Test
	public void updatePriceTest() {
		init("JD_MB");
		// ApiResultVO result = service.updatePrice("263823", 29);
		ApiResultVO result = service.updatePrice(null);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}

	@Test
	public void QQUpdatePriceTest() {
		init("PP");
		// ApiResultVO result = service.updatePrice("214327", 35);
		ApiResultVO result = service.updatePrice(null);
		// ApiResultVO result = service.updatePrice("238286", 109);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}

	@Test
	public void JD_MBUpdatePriceTest() {
		init("JD_MB");
		// ApiResultVO result = service.updatePrice("205558", 55);
		ApiResultVO result = service.updatePrice(null);
		// ApiResultVO result = service.updatePrice("270282", 55);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}

	@Test
	public void JD_MBUpdateInfoTest() {
		init("JD_MB");
		// String detail =
		// "<div class='gridly' style='width: 740px;position: relative;font-family: Tahoma, Geneva, sans-serif; font-size: 12px; overflow: hidden;'><div  id=\"20140806165744_166\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><h4 style='border-bottom: #999 1px solid; margin: 0px 0px 10px; clear: both; font-size: 12px; font-weight: normal; padding-top: 10px'>    编辑推荐</h4><div style='padding-bottom: 10px'>    羽绒服秉承简约的设计理念，鸭绒填充非常保暖有型，用色靓丽美观，短款时尚利落冬季穿出你的潇洒和昂扬！</div><div>    <div style='width: 462px; float: left;'>                <img width='462px' src='http://img10.360buyimg.com/imgzone/jfs/t457/327/265696738/83950/671e58bf/5459da6cN8107abd3.jpg' alt=''>            </div>    <div style='width: 258px; float: right' class='model_infor'>        <h3 style='padding-bottom: 5px; margin: 0px; padding-left: 0px; padding-right: 0px; font-family: '>            <img width='217' height='41' src='http://img10.360buyimg.com/imgzone/jfs/t205/91/1747321578/1465/39ce368f/53b67cbcNd947fb25.png' alt='商品信息'>        </h3>        <ul style='padding-bottom: 0px; list-style-type: none; margin: 0px; padding-left: 5px; width: 258px; padding-right: 0px; padding-top: 0px'        class='prod_attr'>            <li style='padding:2px 0;clear:left;list-style: none;'>                <b style='float:left;line-height:120%;'>                    款号:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;'>                    238494                </span>            </li>            <li style='padding:2px 0;clear:left;list-style: none;'>                <b style='float:left;line-height:120%;'>                    款名:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;list-style: none;'>                    男面料拼接立领羽绒服                </span>            </li>            <li style='padding:2px 0;clear:left;list-style: none;'>                <b style='float:left;line-height:120%;'>                    吊牌价:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;list-style: none;'>                    499.00                </span>            </li>                    </ul>    </div></div></div><div  id=\"20140806165745_167\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><div><h4 style='border-bottom:#999 1px solid;margin:0px 0px 10px;clear:both;font-size:12px;font-weight:normal;padding-top:10px;'>模特展示</h4></div><div class='big_show' style='padding:10px 0 0; overflow:hidden; text-align:center;'><img src='http://img10.360buyimg.com/imgzone/jfs/t619/189/255750056/116074/ba1b3350/5459da6eNa172ac47.jpg' alt='男面料拼接立领羽绒服模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t655/141/266546786/107916/7745eb40/5459da6fN586bc773.jpg' alt='男面料拼接立领羽绒服模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t517/197/252562098/108250/39f450df/5459da71Nf53c94a9.jpg' alt='男面料拼接立领羽绒服模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t574/100/245223702/78380/d6a037b2/5459da73Nfc07afd0.jpg' alt='男面料拼接立领羽绒服模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t568/200/250697764/81639/cdf389d6/5459da74N1ece1961.jpg' alt='男面料拼接立领羽绒服模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t694/336/250270055/88274/9063f31b/5459da76N1ae08df0.jpg' alt='男面料拼接立领羽绒服模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t619/197/259521319/88757/63fa1860/5459da78Naffd6c8f.jpg' alt='男面料拼接立领羽绒服模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t565/223/265392207/81163/f12ea18e/5459da79N10e257d8.jpg' alt='男面料拼接立领羽绒服模特展示' width='730'  style='margin-bottom:10px;'> </div> <img src='http://img10.360buyimg.com/imgzone/jfs/t571/224/267435682/116651/345705da/5459da7bN7651dcc1.jpg' width='730' alt='男面料拼接立领羽绒服平面展示'  style='padding:5px 0;'></div><div  id=\"20140806165745_168\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"></div><div  id=\"20140806165746_169\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"></div><div  id=\"20140806165746_170\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><div style='width:740px'><h4 style='border-bottom:#999 1px solid;margin:0px 0px 10px;clear:both;font-size:12px;font-weight:normal;padding-top:10px;'>颜色选择</h4></div><div class='color_show' style='padding:10px 0;width:740px'>    <ul style='padding:0 0 10px 10px; overflow:hidden;'>                        <li style='width:170px; padding-right:12px; float:left;'>                <img src='http://img10.360buyimg.com/imgzone/jfs/t652/176/254612855/10656/fab34897/5459da7dNedb64dbe.jpg' width='170' alt='男面料拼接立领羽绒服颜色：姜饼棕' style='padding-top:10px; vertical-align:top;'>                <h5 style='font-size:12px; font-weight:normal; height:20px; overflow:hidden; text-align:center; padding-bottom:10px;'>                    姜饼棕                </h5>        </li>                                <li style='width:170px; padding-right:12px; float:left;'>                <img src='http://img10.360buyimg.com/imgzone/jfs/t586/306/269034871/10546/d8ea94e4/5459da7fNcdd83dbe.jpg' width='170' alt='男面料拼接立领羽绒服颜色：石头灰' style='padding-top:10px; vertical-align:top;'>                <h5 style='font-size:12px; font-weight:normal; height:20px; overflow:hidden; text-align:center; padding-bottom:10px;'>                    石头灰                </h5>        </li>                                <li style='width:170px; padding-right:12px; float:left;'>                <img src='http://img10.360buyimg.com/imgzone/jfs/t505/340/265610587/10161/1b27061a/5459da82N5e64a2e6.jpg' width='170' alt='男面料拼接立领羽绒服颜色：红木褐' style='padding-top:10px; vertical-align:top;'>                <h5 style='font-size:12px; font-weight:normal; height:20px; overflow:hidden; text-align:center; padding-bottom:10px;'>                    红木褐                </h5>        </li>                                <li style='width:170px; padding-right:12px; float:left;'>                <img src='http://img10.360buyimg.com/imgzone/jfs/t604/347/264813385/8896/e49b0ff2/5459da84Nf0b32e6a.jpg' width='170' alt='男面料拼接立领羽绒服颜色：影黑' style='padding-top:10px; vertical-align:top;'>                <h5 style='font-size:12px; font-weight:normal; height:20px; overflow:hidden; text-align:center; padding-bottom:10px;'>                    影黑                </h5>        </li>                                <li style='width:170px; padding-right:12px; float:left;'>                <img src='http://img10.360buyimg.com/imgzone/jfs/t595/347/256497447/10948/3aee40dc/5459da86Ndc2abd4d.jpg' width='170' alt='男面料拼接立领羽绒服颜色：貂棕' style='padding-top:10px; vertical-align:top;'>                <h5 style='font-size:12px; font-weight:normal; height:20px; overflow:hidden; text-align:center; padding-bottom:10px;'>                    貂棕                </h5>        </li>                                <li style='width:170px; padding-right:12px; float:left;'>                <img src='http://img10.360buyimg.com/imgzone/jfs/t634/327/256774376/10160/eb92796c/5459da87N8f11229f.jpg' width='170' alt='男面料拼接立领羽绒服颜色：深湖蓝' style='padding-top:10px; vertical-align:top;'>                <h5 style='font-size:12px; font-weight:normal; height:20px; overflow:hidden; text-align:center; padding-bottom:10px;'>                    深湖蓝                </h5>        </li>                    </ul></div></div><div  id=\"20140806165746_171\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><div style='width:740px'><h4 style='border-bottom:#999 1px solid;margin:0px 0px 10px;clear:both;font-size:12px;font-weight:normal;padding-top:10px;'>尺码规格</h4></div><div style='width:740px'>  <img src=http://img10.360buyimg.com/imgzone/jfs/t454/149/263860344/27127/fb8f0f00/5459da89N9d06f2e6.jpg  alt='尺码规格说明' /></div><div style='width:740px'>    <table cellspacing='0' cellpadding='0' class='rule' style='width:100%;border-collapse:collapse; margin:15px 0;'>        <tbody>          <tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>基本号型</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>-</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>衣长</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>肩宽</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>胸围</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>袖长</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>150/76A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>XXXS</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>61.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>42.5</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>49.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>60.6</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>155/80A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>XXS</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>62.5</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>43.3</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>50.5</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>61.1</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>160/84A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>XS</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>64.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>44.1</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>52.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>62.4</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>165/88A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>S</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>66.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>45.3</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>54.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>63.7</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>170/92A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>M</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>68.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>46.5</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>56.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>65.0</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>175/96A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>L</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>70.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>47.7</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>58.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>66.3</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>180/100A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>XL</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>72.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>48.9</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>60.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>67.6</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>185/104B</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>XXL</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>74.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>50.4</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>63.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>68.9</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>190/108B</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>XXXL</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>76.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>51.9</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>66.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>69.4</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>190/112C</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>-</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>78.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>53.4</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>69.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>69.9</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>190/120C</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>-</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>79.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>54.6</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>71.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>70.9</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>190/128C</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>-</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>80.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>55.8</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>73.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>-</td></tr>        </tbody>    </table></div></div></div>";
		String detail = "<div class='gridly' style='width: 740px;position: relative;font-family: Tahoma, Geneva, sans-serif; font-size: 12px; overflow: hidden;'><div  id=\"20140806165744_166\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><h4 style='border-bottom: #999 1px solid; margin: 0px 0px 10px; clear: both; font-size: 12px; font-weight: normal; padding-top: 10px'>    编辑推荐</h4><div style='padding-bottom: 10px'>    独特的AB纱线，将3种颜色的纱线进行糅合，在经过独特的针织工艺打造，呈现出令人眼前一亮的视觉效果。这样的针织衫肌理附有艺术感，而且手感非常厚实，具有不错的保暖效果。大身呈现自然的纵向条纹，看起来更加显瘦。领子和下摆处以深色线条加以勾勒，前短后长的设计层次感十足，超级有范。不同的色组可供选择，建议搭配略微紧身的裤装，打造出玲珑有致的曲线型身材。</div><div>    <div style='width: 462px; float: left;'>                <img width='462px' src='http://img10.360buyimg.com/imgzone/jfs/t517/265/271280126/56234/becb4f91/5459df42Nd1fe6d65.jpg' alt=''>            </div>    <div style='width: 258px; float: right' class='model_infor'>        <h3 style='padding-bottom: 5px; margin: 0px; padding-left: 0px; padding-right: 0px; font-family: '>            <img width='217' height='41' src='http://img10.360buyimg.com/imgzone/jfs/t205/91/1747321578/1465/39ce368f/53b67cbcNd947fb25.png' alt='商品信息'>        </h3>        <ul style='padding-bottom: 0px; list-style-type: none; margin: 0px; padding-left: 5px; width: 258px; padding-right: 0px; padding-top: 0px'        class='prod_attr'>            <li style='padding:2px 0;clear:left;list-style: none;'>                <b style='float:left;line-height:120%;'>                    款号:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;'>                    218764                </span>            </li>            <li style='padding:2px 0;clear:left;list-style: none;'>                <b style='float:left;line-height:120%;'>                    款名:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;list-style: none;'>                    女AB纱线套头毛衫                </span>            </li>            <li style='padding:2px 0;clear:left;list-style: none;'>                <b style='float:left;line-height:120%;'>                    吊牌价:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;list-style: none;'>                    199.00                </span>            </li>                                                <li style='padding:2px 0;clear:left;list-style:none;'>                <b style='float:left;line-height:120%;'>                    材质:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;'>                    纱线：58%棉      42%腈纶                </span>            </li>                                     <li style='padding:2px 0;clear:left;list-style:none;'>                <b style='float:left;line-height:120%;'>                    衣长:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;'>                    合体                </span>            </li>                                     <li style='padding:2px 0;clear:left;list-style:none;'>                <b style='float:left;line-height:120%;'>                    季节:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;'>                    冬                </span>            </li>                                     <li style='padding:2px 0;clear:left;list-style:none;'>                <b style='float:left;line-height:120%;'>                    人群:                </b>                <span style='float:left;line-height:120%;padding-left:3px;width:180px;'>                    女                </span>            </li>                                   </ul>    </div></div></div><div  id=\"20140806165745_167\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><div><h4 style='border-bottom:#999 1px solid;margin:0px 0px 10px;clear:both;font-size:12px;font-weight:normal;padding-top:10px;'>模特展示</h4></div><div class='big_show' style='padding:10px 0 0; overflow:hidden; text-align:center;'><img src='http://img10.360buyimg.com/imgzone/jfs/t664/65/256043539/93266/375b3635/5459df44N12363db3.jpg' alt='女AB纱线套头毛衫模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t481/161/258469895/72600/e22427ff/5459df47N59bab241.jpg' alt='女AB纱线套头毛衫模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t583/361/255329956/93439/108cb35a/5459df48Ne75afafe.jpg' alt='女AB纱线套头毛衫模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t463/37/264055295/59759/1114b942/5459df4aN4dc8c689.jpg' alt='女AB纱线套头毛衫模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t508/26/260325779/57322/f806c977/5459df4bN300e54a9.jpg' alt='女AB纱线套头毛衫模特展示' width='730'  style='margin-bottom:10px;'> <img src='http://img10.360buyimg.com/imgzone/jfs/t682/18/268631336/53619/8ec9fa00/5459df4eN8ae40352.jpg' alt='女AB纱线套头毛衫模特展示' width='730'  style='margin-bottom:10px;'> </div> <img src='http://img10.360buyimg.com/imgzone/jfs/t625/163/260128567/294859/a64982e9/5459df50Nf557f250.jpg' width='730' alt='女AB纱线套头毛衫平面展示'  style='padding:5px 0;'><img src='http://img10.360buyimg.com/imgzone/jfs/t694/49/249240040/306592/71dd47c9/5459df52N99b8e758.jpg' width='730' alt='女AB纱线套头毛衫平面展示'  style='padding:5px 0;'></div><div  id=\"20140806165745_168\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><div style='width:740px'><h4 style='border-bottom:#999 1px solid;margin:0px 0px 10px;clear:both;font-size:12px;font-weight:normal;padding-top:10px;'>模特信息</h4></div><div style='width:740px'>    <img alt='女AB纱线套头毛衫模特信息' width='730' src='http://img10.360buyimg.com/imgzone/jfs/t460/171/258149983/15452/70d32158/5459df53Nc943e7fe.jpg'></div></div><div  id=\"20140806165746_169\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><div style='width:740px'><h4 style='border-bottom:#999 1px solid;margin:0px 0px 10px;clear:both;font-size:12px;font-weight:normal;padding-top:10px;'>细节展示</h4></div><div class='little_show' style='padding:10px 0 10px 10px; overflow:hidden;width:740px'><div class='dl_padding' style='padding-left: 10px; width:350px; float:left;'>    <div style='height:350px;'>        <img src='http://img10.360buyimg.com/imgzone/jfs/t487/29/259665905/68829/14bfaa1/5459df55N5d71e5bf.jpg' width='350' height='350' alt='女AB纱线套头毛衫'>     </div>     <div style='height:20px; overflow:hidden; padding:5px 5px 15px; text-align:center;'>               </div>     </div><div class='dl_padding' style='padding-left: 10px; width:350px; float:left;'>    <div style='height:350px;'>        <img src='http://img10.360buyimg.com/imgzone/jfs/t505/46/253615269/21323/93e31c0f/5459df56N892979a5.jpg' width='350' height='350' alt='女AB纱线套头毛衫'>     </div>     <div style='height:20px; overflow:hidden; padding:5px 5px 15px; text-align:center;'>               </div>     </div><div class='dl_padding' style='padding-left: 10px; width:350px; float:left;'>    <div style='height:350px;'>        <img src='http://img10.360buyimg.com/imgzone/jfs/t670/123/249099652/13279/d87f634a/5459df58N81381cb6.jpg' width='350' height='350' alt='女AB纱线套头毛衫'>     </div>     <div style='height:20px; overflow:hidden; padding:5px 5px 15px; text-align:center;'>               </div>     </div><div class='dl_padding' style='padding-left: 10px; width:350px; float:left;'>    <div style='height:350px;'>        <img src='http://img10.360buyimg.com/imgzone/jfs/t496/53/251980308/82380/7829d84e/5459df59N11253509.jpg' width='350' height='350' alt='女AB纱线套头毛衫领部的针织纹理富有变化感，十分华丽'>     </div>     <div style='height:20px; overflow:hidden; padding:5px 5px 15px; text-align:center;'>         领部的针织纹理富有变化感，十分华丽      </div>     </div><div class='dl_padding' style='padding-left: 10px; width:350px; float:left;'>    <div style='height:350px;'>        <img src='http://img10.360buyimg.com/imgzone/jfs/t694/59/254800826/99810/7d0893d0/5459df5bN43a865db.jpg' width='350' height='350' alt='女AB纱线套头毛衫将3种颜色的纱线进行糅合，呈现出令人眼前一亮的视觉效果'>     </div>     <div style='height:20px; overflow:hidden; padding:5px 5px 15px; text-align:center;'>         将3种颜色的纱线进行糅合，呈现出令人眼前一亮的视觉效果      </div>     </div><div class='dl_padding' style='padding-left: 10px; width:350px; float:left;'>    <div style='height:350px;'>        <img src='http://img10.360buyimg.com/imgzone/jfs/t586/26/257814318/86665/7b94d0f2/5459df5eN70bfdc22.jpg' width='350' height='350' alt='女AB纱线套头毛衫袖子的拼合处做工平整服帖'>     </div>     <div style='height:20px; overflow:hidden; padding:5px 5px 15px; text-align:center;'>         袖子的拼合处做工平整服帖      </div>     </div><div class='dl_padding' style='padding-left: 10px; width:350px; float:left;'>    <div style='height:350px;'>        <img src='http://img10.360buyimg.com/imgzone/jfs/t496/66/266423169/98254/8b56c05d/5459df61Ne1a6fc77.jpg' width='350' height='350' alt='女AB纱线套头毛衫袖口的针织纹理富有变化感'>     </div>     <div style='height:20px; overflow:hidden; padding:5px 5px 15px; text-align:center;'>         袖口的针织纹理富有变化感      </div>     </div><div class='dl_padding' style='padding-left: 10px; width:350px; float:left;'>    <div style='height:350px;'>        <img src='http://img10.360buyimg.com/imgzone/jfs/t631/186/254479324/97025/f093bc91/5459df63Ne9d1f081.jpg' width='350' height='350' alt='女AB纱线套头毛衫下摆略微收口对廓形起到修饰作用'>     </div>     <div style='height:20px; overflow:hidden; padding:5px 5px 15px; text-align:center;'>         下摆略微收口对廓形起到修饰作用      </div>     </div><div class='dl_padding' style='padding-left: 10px; width:350px; float:left;'>    <div style='height:350px;'>        <img src='http://img10.360buyimg.com/imgzone/jfs/t646/63/255716265/83652/1115c495/5459df66N5b8103d7.jpg' width='350' height='350' alt='女AB纱线套头毛衫前短后长的设计层次感十足，超级有范'>     </div>     <div style='height:20px; overflow:hidden; padding:5px 5px 15px; text-align:center;'>         前短后长的设计层次感十足，超级有范      </div>     </div></div></div><div  id=\"20140806165746_170\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><div style='width:740px'><h4 style='border-bottom:#999 1px solid;margin:0px 0px 10px;clear:both;font-size:12px;font-weight:normal;padding-top:10px;'>颜色选择</h4></div><div class='color_show' style='padding:10px 0;width:740px'>    <ul style='padding:0 0 10px 10px; overflow:hidden;'>                        <li style='width:170px; padding-right:12px; float:left;'>                <img src='http://img10.360buyimg.com/imgzone/jfs/t562/91/256999186/15563/9b5b9c62/5459df67N28294609.jpg' width='170' alt='女AB纱线套头毛衫颜色：红色组' style='padding-top:10px; vertical-align:top;'>                <h5 style='font-size:12px; font-weight:normal; height:20px; overflow:hidden; text-align:center; padding-bottom:10px;'>                    红色组                </h5>        </li>                                <li style='width:170px; padding-right:12px; float:left;'>                <img src='http://img10.360buyimg.com/imgzone/jfs/t493/80/266139786/10169/6f6532e0/5459df68N5b9dcb58.jpg' width='170' alt='女AB纱线套头毛衫颜色：灰色组' style='padding-top:10px; vertical-align:top;'>                <h5 style='font-size:12px; font-weight:normal; height:20px; overflow:hidden; text-align:center; padding-bottom:10px;'>                    灰色组                </h5>        </li>                                <li style='width:170px; padding-right:12px; float:left;'>                <img src='http://img10.360buyimg.com/imgzone/jfs/t466/306/256664163/14467/e2cd9b1b/5459df69N8b5d05cb.jpg' width='170' alt='女AB纱线套头毛衫颜色：黑色组' style='padding-top:10px; vertical-align:top;'>                <h5 style='font-size:12px; font-weight:normal; height:20px; overflow:hidden; text-align:center; padding-bottom:10px;'>                    黑色组                </h5>        </li>                    </ul></div></div><div  id=\"20140806165746_171\" style=\"width: 740px; font-family: Tahoma, Geneva; font-size: 12px; overflow: hidden;\"><div style='width:740px'><h4 style='border-bottom:#999 1px solid;margin:0px 0px 10px;clear:both;font-size:12px;font-weight:normal;padding-top:10px;'>尺码规格</h4></div><div style='width:740px'>  <img src=http://img10.360buyimg.com/imgzone/jfs/t694/79/270015310/27127/fb8f0f00/5459df6aN48f400bd.jpg  alt='尺码规格说明' /></div><div style='width:740px'>    <table cellspacing='0' cellpadding='0' class='rule' style='width:100%;border-collapse:collapse; margin:15px 0;'>        <tbody>          <tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>基本号型</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>尺码</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>肩宽</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>胸围</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>衣长</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>袖长</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>150/76A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>XS</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>33.6</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>44.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>48.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>50.0</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>155/80A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>S</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>34.8</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>46.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>50.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>51.5</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>160/84A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>M</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>36.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>48.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>52.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>53.0</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>165/88A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>L</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>37.2</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>50.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>54.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>54.5</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>170/92A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>XL</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>38.4</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>52.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>56.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>56.0</td></tr><tr><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>175/96A</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>XXL</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>39.6</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>54.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>58.0</td><td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>57.5</td></tr>        </tbody>    </table></div></div></div>";
		ApiResultVO result = service.updateGoodsName(null);
		// ApiResultVO result = service.updatePrice("270282", 55);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}

	@Test
	public void PPUpdatePriceTest() {
		init("PP");
		ApiResultVO result = service.getBaseData(null);
		System.out.println("ALL --------- " + result.getTotal());
	}



	@Test
	public void ddUpdateGoodsInfoTest() {
		init("DD");
		String goodsSn = "248513";
		ApiResultVO result = service.updateGoodsName(null);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}


	@Test
	public void ddParseClassTest() throws Exception {
//		TestBean bean = new TestBean();
//		bean.setName("derek");
//		bean.setAge(26);
//		bean.setBirthday(new Date());
//		List<String> cj = new ArrayList<String>();
//		cj.add("10");
//		cj.add("20");
//		cj.add("30");
//		bean.setCj(cj);
//		List<Address> adds = new ArrayList<Address>();
//		Address address = null;
//		for (int i = 0; i < 3; i++) {
//			address = new Address();
//			address.setProvince("province_" + i);
//			address.setCity("city_" + i);
//			address.setCountry("country_" + i);
//			adds.add(address);
//		}
//		bean.setAddressList(adds);
//		System.out.println(Fox.J2Xjoint("<?xml version=\"1.0\" encoding=\"GBK\"?>\n", bean));
		// System.out.println(Parse.parseBeanToXml(bean));
	}

	@Test
	public void ddShelvesDownTest() {
		init("AQY");
		String goodsSn = "471524";
		List<String> skus = new ArrayList<String>();
		skus.add(goodsSn);
		ApiResultVO result = service.shelvesUpOrDown(null);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}

	@Test
	public void ddShelvesUpTest() {
		init("AQY");
		String goodsSn = "471524";
		List<String> skus = new ArrayList<String>();
		skus.add(goodsSn);
		ApiResultVO result = service.shelvesUpOrDown(null);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}
	
	@Test
	public void pReadCSVTest() {
		init("PP");
		CSVReader reader = null;
		StringBuffer last = new StringBuffer();
		try {
			String filePath = "G:/price (52).csv";
			File file = new File(filePath);
			reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "GBK"));
			String[] nextLine;
			int i = 0;
			while ((nextLine = reader.readNext()) != null) {
				if (i != 0) {
					String goodsSn = nextLine[0];
					double price = Double.parseDouble(nextLine[1]);
					ApiResultVO result = service.updatePrice(null);
					boolean flag = false;
					for (int k = 0; k < 3; k++) {
						result = service.updatePrice(null);
						System.out.println(goodsSn + " ---- " + price + " ---- " + result.getCode() + " ---- " + result.getMessage());
						if ("0".equals(result.getCode())) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						last.append(goodsSn).append(",").append(price).append("\n");
					}
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(last.toString());
	}

	@Test
	public void TestUUID() {
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
		System.out.println(uuid.length());
	}

	@Test
	public void JD_MC_1_TESTPIECE() {
		init("JD_MC_1");
		// 573549 49
		ApiResultVO result = service.updatePrice(null);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}

	@Test
	public void JD_MC_TESTPIECE() {
		init("JD_MC");
		ApiResultVO result = service.updatePrice(null);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}

	@Test
	public void JD_MB_TESTPIECE() {
		init("JD_MB");
		ApiResultVO result = service.updatePrice(null);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}

	@Test
	public void YHD_MB_TESTPIECE() {
		init("YHD");
		ApiResultVO result = service.updatePrice(null);
		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}

	@Test
	public void Test_Time() {
		long t = 1418301861112L;
		long e = t + (31535999 * 1000);
		Date t1 = new Date(t);
		Date t2 = new Date(e);
		System.out.println(DateTimeUtils.format(t1, DateTimeUtils.YYYY_MM_DD_HH_mm_ss_SSS));
		System.out.println(DateTimeUtils.format(t2, DateTimeUtils.YYYY_MM_DD_HH_mm_ss_SSS));
	}

	@Test
	public void put_mq_test() throws Exception {
		ApplicationContext contex = new ClassPathXmlApplicationContext("applicationContext.xml");
		ActiveMqUtils mqUtils = contex.getBean(ActiveMqUtils.class);
		SyncStockParam param = new SyncStockParam();
		param.setShopCode("HQ01S160");
		param.setSku("111111");
		param.setStockCount(10);
		param.setType("1");
		String message = JSON.toJSONString(param);
		mqUtils.sendMessage(Constants.QUEUE_FIX_CS_SYNC_STOCK + "HQ01S160", message);
	}

	@Test
	public void searchItemPageTest() throws Exception {
		ApplicationContext contex = new ClassPathXmlApplicationContext("applicationContext.xml");
		ChannelApiService apiService = contex.getBean(ChannelApiService.class);
//		SyncStockParam param = new SyncStockParam();
//		param.setShopCode("HQ01S173");
//		param.setSku("27654699130");
//		param.setStockCount(4);
//		param.setType("1");
		LocalItemQuery itemQuery = new LocalItemQuery();
		itemQuery.setChannelCode("AQY_CHANNEL_CODE");
		itemQuery.setShopCode("HQ01S173");
		itemQuery.setItemNo(null);
		itemQuery.setPage(1);
		itemQuery.setPageSize(10);
		itemQuery.setStatus("1");
		itemQuery.setUserName("test");
		ApiResultVO result = apiService.searchItemPage(itemQuery);
//		ApiResultVO result = apiService.searchItemPage("PP_CHANNEL_CODE", "HQ01S112",
//				null, null, 1, 10, "1", "admin");
		

		System.out.println(result.getCode() + " --------- " + result.getMessage());
	}
	
	/**
	 * 测试库存同步
	 * @throws FatalException 
	 * @throws WarnningException 
	 */
	@Test
	public void testcsv() throws WarnningException, FatalException{
		ApplicationContext contex = new ClassPathXmlApplicationContext("applicationContext.xml");
		ExportOnSaleDataTimerTask service = (ExportOnSaleDataTimerTask) contex.getBean("exportOnSaleDataTimerTask");
		TaskExecuteRequest request=new TaskExecuteRequest();
		Map<String,String> params=new HashMap<String, String>();
		params.put("shopCodes", "HQ01S114");
		request.setParams(params);
		service.execute(request);
		List<String> list=FtpUtil.getFileNameListByPath(PropertieFileReader.getString("ftp_path") + "/"+ TimeUtil.format2Date(new Date())+"/"+"HQ01S114"+"/goods/");
		System.out.println(list.size());
	}
	
	@Test
	public void testurlpost() throws Exception{
		String data1="{\"Detail\":[{\"num\":1,\"returnPrice\":479,\"skuSn\":\"54204290142\"}],\"freight\":0,\"order_type\":1,\"os_order_no\":\"140428707647\",\"out_order_no\":\"15031314114410073935\",\"total_amount\":479,\"total_count\":1}";
		String data="{'Detail':[{'num':1,'returnPrice':479,'skuSn':'54204290142'}],'freight':0,'order_type':1,'os_order_no':'140428707647','out_order_no':'15031314114410073935','total_amount':479,'total_count':1}";
		Map<String, Object> paramModel = new HashMap<String, Object>();
		paramModel.put("formDto",data);
		ObjectMapper objectMapper = new ObjectMapper();
		final String requestBody = objectMapper
				.writeValueAsString(paramModel);
		System.out.println(requestBody);
		String response=post("http://10.100.5.12:8110/IInterface_OSReturnChange.svc/FinalReturnRefundForm", requestBody);
		System.out.println(response);
	}
	
	@Test
	public void testurlget() throws Exception{
		String data="{'Detail':[{'num':1,'returnPrice':479,'skuSn':'54204290142'}],'freight':0,'order_type':1,'os_order_no':'140428707647','out_order_no':'15031314114410073935','total_amount':479,'total_count':1}";
		String response=get("http://10.100.5.12:8110/IInterface_OSReturnChange.svc/FinalReturnRefundForm?formDto="+data);
		System.out.println(response);
	}
	
	@Test
	public void testurlpost1() throws Exception{
		String data1="{\"Detail\":[{\"num\":1,\"returnPrice\":479,\"skuSn\":\"54204290142\"}],\"freight\":0,\"order_type\":1,\"os_order_no\":\"140428707647\",\"out_order_no\":\"15031314114410073935\",\"total_amount\":479,\"total_count\":1}";
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("formDto", data1));
		String response=HttpClientUtil.post("http://10.100.5.12:8110/IInterface_OSReturnChange.svc/FinalReturnRefundForm", valuePairs);
		System.out.println(response);
	}
	
	
	
	private String post(String url, String params) throws Exception {
		String responseBody = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		try {
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(params, "UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			HttpResponse resp = httpClient.execute(post);
			responseBody = EntityUtils.toString(resp.getEntity(), "UTF-8");
		} catch (Exception e) {
			throw e;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return responseBody;
	}
	
	private String get(String url) {
		String responseBody = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
		managerParams.setConnectionTimeout(100000);
		managerParams.setSoTimeout(120000);
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				responseBody = getMethod.getResponseBodyAsString();
			} else {
				System.out.println("Eorr occus");
				responseBody = "跳转失败，错误编码：" + statusCode;
			}
		} catch (Exception e) {
			responseBody = "网关限制无法跳转!";
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return responseBody;
	}
	
	
	
	/**
	 * 测试库存同步
	 * @throws FatalException 
	 * @throws WarnningException 
	 */
	@Test
	public void testOmsOrder() throws WarnningException, FatalException{
		try {
			String userName = "屈磊明";
//			userName = URLEncoder.encode(userName, "UTF-8");
			String json = "{\"addTime\":\"2015-09-29 11:11:20\",\"bonus\":0,\"consignee\":\""+ userName+"\",\"discount\":0,\"email\":\"\",\"goodsAmount\":488,\"goodsCount\":17,\"invType\":\"\\u90a6\\u8d2d\",\"mobile\":\"18900922223\",\"orderFrom\":\"HQ01S116\",\"orderStatus\":0,\"orderType\":0,\"payList\":[{\"bonusId\":\"\",\"payCode\":\"alipay\",\"payId\":\"1\",\"payStatus\":\"0\",\"payTotalFee\":\"488.0\",\"surplus\":\"0\"}],\"payStatus\":0,\"prName\":\"\",\"referer\":\"\\u90a6\\u8d2dd3\",\"shipList\":[{\"address\":\"\\u5eb7\\u6865\\u4e1c\\u8def700\\u53f7\",\"chargeType\":0,\"city\":\"310100\",\"consignee\":\"??1\",\"country\":\"1\",\"district\":\"310108\",\"email\":\"\",\"goodsCount\":17,\"goodsList\":[{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"23873200146\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"15\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"53713390148\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"34\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"23784490144\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"5\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"27338934130\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"24\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"21487240144\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"21\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"53965126146\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"39\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"22238830150\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"50\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"23353280148\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"25\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"23100671150\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"23\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"53904947148\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"43\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"20640630150\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"1\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"24833752136\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"27\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"25348538132\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"47\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"23871380144\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"17\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"23353090144\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"48\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"23100671150\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"23\"},{\"extensionCode\":\"common\",\"goods_price\":\"69\",\"extensionId\":\"1\",\"shareBonus\":\"0\",\"useCard\":\"\",\"is_real\":\"1\",\"goodsNumber\":\"1\",\"promotionDesc\":\"\",\"goods_name\":\"\\u7537\\u4e0d\\u540c\\u9762\\u6599\\u5757\\u9762\\u5206\\u5272POLO\\u6064\",\"customCode\":\"25378090128\",\"goodsThumb\":\"\\/sources\\/images\\/goods\\/MC\\/512271\\/512271_90_09.jpg\",\"transactionPrice\":\"46\"}],\"mobile\":\"18900922223\",\"province\":\"310000\",\"shippingCode\":\"99\",\"shippingFee\":0,\"tel\":\"\"}],\"shippingTotalFee\":0,\"sourceCode\":\"\",\"tel\":\"\",\"totalFee\":488,\"totalPayable\":\"488.0\",\"transType\":1,\"userId\":\"lujuan\"}";
		
//			json = URLEncoder.encode(json, "UTF-8");
//			json = URLEncoder.encode(json, "UTF-8");
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("orderInfo", json));
			System.out.println(json);
			String result = HttpClientUtil.post("http://localhost:8088/Oms/api/createOrderPost", valuePairs);
			String str2 = URLDecoder.decode(result, "UTF-8");
//			str2 = new String(str2.getBytes("iso8859-1"), "UTF-8");
			System.out.println(str2);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateOms() throws WarnningException, FatalException{
		try {
			CSVReader parentReader = null;
			// 读取区域文件
			parentReader = new CSVReader(new InputStreamReader(new FileInputStream("F:\\手机号码.csv"), "GBK"));
			String[] parentNextLine;
			int i = 0;
			List<String> list =new ArrayList<String>();
			while ((parentNextLine = parentReader.readNext()) != null) {
				if (i != 0) {
					// 循环行Row
					String decodeMobel = parentNextLine[0];
					if (StringUtil.isEmpty(decodeMobel)) {
						continue;
					}
					/*String url = "http://10.100.22.206:8080/Oms/dubbo/com.work.shop.oms.api.rsa.service.RSAService?method=decode&param=[\'" + decodeMobel + "\']";
					List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
					valuePairs.add(new BasicNameValuePair("method", "encode"));
					valuePairs.add(new BasicNameValuePair("param", "[" + decodeMobel + "]"));
					String encodeMobel = HttpClientUtil.post(url, null);
					sb.append(encodeMobel);*/
					list.add(decodeMobel);
				}
				i++;
			}
			List<Object> objects = new ArrayList<Object>();
			objects.add(list);
			System.out.println("总单数" + list.size());
			parentReader.close();
//			String url = "http://10.100.22.206:8080/Oms/dubbo/com.work.shop.oms.api.rsa.service.RSAService?method=decode_array&param=" + JSON.toJSONString(objects);

			String url = "http://10.100.22.201:8080/Oms/dubbo/com.work.shop.oms.api.rsa.service.RSAService";
			System.out.println(url);
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("method", "decode_array"));
			valuePairs.add(new BasicNameValuePair("param", "[" + JSON.toJSONString(list) + "]"));
			String encodeMobel = HttpClientUtil.post(url, valuePairs);
			Map<String, String> map = new HashMap<String, String>();
			map = JSON.parseObject(encodeMobel, Map.class);
			StringBuffer sb = new StringBuffer("加密号码,号码\n");
			System.out.println(map.size());
			for (String enmobile : list) {
				String mobile = map.get(enmobile);
				sb.append(enmobile + ",");
				sb.append(mobile);
				sb.append("\n");
			}
			/*for (String key: map.keySet()) {
				String mobile = map.get(key);
				sb.append(key + ",");
				sb.append(mobile);
				sb.append("\n");
			}*/
			try {
				File file = new File("F:\\decodemobil.csv");
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
				bw.write(sb.toString());
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRSAMobile() throws WarnningException, FatalException{
		try {
			CSVReader parentReader = null;
			// 读取区域文件
			parentReader = new CSVReader(new InputStreamReader(new FileInputStream("F:\\手机号码.csv"), "GBK"));
			String[] parentNextLine;
			int i = 0;
			List<String> list =new ArrayList<String>();
			StringBuffer sb = new StringBuffer("号码\n");
			while ((parentNextLine = parentReader.readNext()) != null) {
				if (i != 0) {
					// 循环行Row
					String decodeMobel = parentNextLine[0];
					if (StringUtil.isEmpty(decodeMobel)) {
						continue;
					}
					String url = "http://10.100.22.206:8080/Oms/dubbo/com.work.shop.oms.api.rsa.service.RSAService?method=decode&param=[\'" + decodeMobel + "\']";
					List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
					valuePairs.add(new BasicNameValuePair("method", "encode"));
					valuePairs.add(new BasicNameValuePair("param", "[" + decodeMobel + "]"));
					String encodeMobel = HttpClientUtil.post(url, null);
					sb.append(encodeMobel);
					sb.append("\n");
					list.add(decodeMobel);
				}
				i++;
			}
			try {
				File file = new File("F:\\decodemobil.csv");
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
				bw.write(sb.toString());
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void testOmsStock() throws WarnningException, FatalException{
		try {
			CSVReader parentReader = null;
			// 读取区域文件
			parentReader = new CSVReader(new InputStreamReader(new FileInputStream("F:\\updown.csv"), "GBK"));
			String[] parentNextLine;
			int i = 0;
			while ((parentNextLine = parentReader.readNext()) != null) {
				if (i != 0) {
					// 循环行Row
					String goodsSn = parentNextLine[0];
					if (StringUtil.isEmpty(goodsSn)) {
						continue;
					}
					String url = "http://10.80.12.1:8081/ChannelService/custom/channelShopApi/goodsUpDown";
					List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
					valuePairs.add(new BasicNameValuePair("status", "0"));
					valuePairs.add(new BasicNameValuePair("shopCode", "HQ01S116"));
					valuePairs.add(new BasicNameValuePair("goodsSns", goodsSn));
					String encodeMobel = HttpClientUtil.post(url, valuePairs);
					System.out.println(encodeMobel);
				}
				i++;
			}
			parentReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testCreateOrder() throws WarnningException, FatalException{
		try {
			
			String url = "http://10.100.22.206:8080/Oms/api/createOrderPost";
			String param = "{\"actionUser\":\"\",\"addTime\":1461378363000,\"beneficiaryId\":0,\"bonus\":0,\"cardTotalFee\":0,\"consignee\":\"周甜\",\"discount\":0,\"email\":\"845236126@qq.com\",\"extensionCode\":\"\",\"extensionId\":0,\"fromAd\":0,\"goodsAmount\":328,\"goodsCount\":11,\"howOss\":\"\",\"insureTotalFee\":0,\"integral\":0,\"integralMoney\":0,\"invContent\":\"\",\"invPayee\":\"\",\"invType\":\"普通发票\",\"invoicesOrganization\":\"\",\"isAdvance\":0,\"isGroup\":0,\"isNow\":0,\"isOrderPrint\":0,\"isRestricted\":0,\"mergeFrom\":\"\",\"mobile\":\"13720309628\",\"moneyPaid\":0,\"orderCategory\":1,\"orderFrom\":\"HQ01S116\",\"orderOutSn\":\"\",\"orderStatus\":0,\"orderType\":0,\"outletType\":0,\"packTotalFee\":0,\"parentId\":0,\"payList\":[{\"bonusId\":\"\",\"howSurplus\":\"\",\"integral\":0,\"integralMoney\":0,\"payCode\":\"alipay\",\"payFee\":0,\"payId\":1,\"payNote\":\"\",\"payStatus\":0,\"payTotalFee\":328,\"surplus\":0}],\"payStatus\":0,\"payTotalFee\":0,\"posGroup\":0,\"postscript\":\"\",\"prName\":\"\",\"promotionSummry\":[],\"reason\":\"S\",\"referer\":\"邦购\",\"shipList\":[{\"address\":\"嘉定新城云谷路1388弄9号\",\"bestTime\":\"\",\"cacCode\":\"\",\"cardId\":0,\"cardMessage\":\"\",\"chargeType\":0,\"city\":\"310100\",\"consignee\":\"周甜\",\"country\":\"1\",\"deliveryType\":0,\"directRange\":0,\"district\":\"310114\",\"email\":\"845236126@qq.com\",\"goodsCount\":11,\"goodsList\":[{\"barCode\":\"\",\"customCode\":\"55895960126\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MC/558959/558959_60_09.jpg\",\"goods_name\":\"女染整半裙\",\"goods_price\":59,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"5e422ceb-07f1-456d-baf1-77f086028d87\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":59,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"52089670144\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MC/520896/520896_70_09.jpg\",\"goods_name\":\"女格子印花长袖衬衫\",\"goods_price\":69,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"099a1ffd-984b-4c78-9ad1-d9d2032c6cbf\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":69,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"57188925137\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MC/571889/571889_25_10.jpg\",\"goods_name\":\"女时尚绕带高跟鞋\",\"goods_price\":39,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"818c2f42-9618-4f29-8326-c555ff0ec736\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":39,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"55680930126\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MC/556809/556809_30_10.jpg\",\"goods_name\":\"女破洞细节牛仔半裙\",\"goods_price\":19,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"72cfff85-f16a-4774-bc04-cb8d87b01492\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":19,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"24490093130\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MB/244900/244900_93_10.jpg\",\"goods_name\":\"女时尚多色斜挎包\",\"goods_price\":39,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"1cb78329-4415-49fa-b1e0-f2b18fa28191\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":39,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"23382171144\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MB/233821/233821_71_09.jpg\",\"goods_name\":\"女针梭拼接漏肩细节连衣裙\",\"goods_price\":39,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"d23982e9-8e74-4af1-a406-79dd6a170103\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":39,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"24167970144\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MB/241679/241679_70_09.jpg\",\"goods_name\":\"女前胸抽橡筋长款连衣裙\",\"goods_price\":15,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"8aeb81b1-62da-4ac9-9737-37db51b6f2f2\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":15,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"25880940126\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MB/258809/258809_40_09.jpg\",\"goods_name\":\"女印花牛仔半裙\",\"goods_price\":21,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"3a425868-24ca-4ac1-b99f-60b1f9221e43\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":21,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"25158090126\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MB/251580/251580_90_09.jpg\",\"goods_name\":\"女多分割脚口翻边洗水短裤\",\"goods_price\":10,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"e0e07b21-a9f4-4d92-baf6-0177cde6ae2c\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":10,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"57361132130\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MC/573611/573611_32_11.jpg\",\"goods_name\":\"女菱形图案丝袜\",\"goods_price\":9,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"78be8f72-c2d4-43ba-b54e-49e34bf2fd0a\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":9,\"useCard\":\"\"},{\"barCode\":\"\",\"customCode\":\"25885291126\",\"extensionCode\":\"common\",\"extensionId\":\"1\",\"goodsNumber\":1,\"goodsThumb\":\"/sources/images/goods/MB/258852/258852_91_09.jpg\",\"goods_name\":\"女多方案包臀半裙\",\"goods_price\":9,\"integral\":0,\"integralMoney\":0,\"isRestricted\":0,\"isShare\":0,\"itemId\":\"b59555bc-eb42-410b-ade8-76e5ac673600\",\"priceVal\":0,\"promotionDesc\":\"\",\"shareBonus\":0,\"skuSn\":\"\",\"supplierCode\":\"\",\"transactionPrice\":9,\"useCard\":\"\"}],\"insureFee\":0,\"mobile\":\"13720309628\",\"packId\":0,\"province\":\"310000\",\"relatingOrderSn\":\"\",\"shippingCode\":\"99\",\"shippingDays\":0,\"shippingFee\":0,\"shippingRequire\":\"\",\"signBuilding\":\"\",\"tel\":\"\",\"zipcode\":\"\"}],\"shippingTotalFee\":0,\"smsCode\":\"\",\"smsFlag\":\"\",\"source\":3,\"sourceCode\":\"bsmv||\",\"splitFrom\":\"\",\"surplus\":0,\"tax\":0,\"tel\":\"\",\"toBuyer\":\"\",\"totalFee\":328,\"totalPayable\":328,\"transType\":1,\"userId\":\"QQ_88D5668B9539AAD6D3ADB909A9AEB3BD\"}";
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("orderInfo", param));
			String encodeMobel = HttpClientUtil.post(url, null);
			System.out.println(URLDecoder.decode(encodeMobel, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOMSCreateOrder() throws WarnningException, FatalException{
		try {
			
			String url = "http://10.100.22.206:8080/Oms/api/createOrderPost";
			String param = "%7B%22shippingTotalFee%22%3A0%2C%22totalFee%22%3A179%2C%22bonus%22%3A0%2C%22integral%22%3A1%2C%22integralMoney%22%3A0.01%2C%22prName%22%3A%22%22%2C%22addTime%22%3A%222016-05-16+10%3A30%3A47%22%2C%22goodsAmount%22%3A179%2C%22isGroup%22%3A0%2C%22invType%22%3A%22%5Cu666e%5Cu901a%5Cu53d1%5Cu7968%22%2C%22transType%22%3A1%2C%22userId%22%3A%22test%22%2C%22goodsCount%22%3A1%2C%22discount%22%3A%220.00%22%2C%22orderType%22%3A0%2C%22source%22%3A3%2C%22orderStatus%22%3A0%2C%22payStatus%22%3A0%2C%22sourceCode%22%3A%22%22%2C%22referer%22%3A%22%5Cu90a6%5Cu8d2d%22%2C%22orderFrom%22%3A%22HQ01S116%22%2C%22consignee%22%3A%22jackiess%22%2C%22tel%22%3A%22%22%2C%22mobile%22%3A%2218616328527%22%2C%22email%22%3A%22%22%2C%22totalPayable%22%3A%22178.99%22%2C%22shipList%22%3A%5B%7B%22shippingCode%22%3A%2299%22%2C%22consignee%22%3A%22jackiess%22%2C%22country%22%3A%221%22%2C%22province%22%3A%22130000%22%2C%22city%22%3A%22130100%22%2C%22district%22%3A%22130102%22%2C%22address%22%3A%22shanghaipudong%22%2C%22tel%22%3A%22%22%2C%22mobile%22%3A%2218616328527%22%2C%22email%22%3A%22%22%2C%22chargeType%22%3A0%2C%22shippingFee%22%3A0%2C%22goodsCount%22%3A1%2C%22goodsList%22%3A%5B%7B%22itemId%22%3A%2216ada341-136a-438d-8e70-ca82eebeee0a%22%2C%22customCode%22%3A%2250169370199%22%2C%22goods_name%22%3A%22%5Cu7ae5%5Cu88c52016%5Cu6625%5Cu88c5%5Cu5973%5Cu7ae5%5Cu53ef%5Cu7231%5Cu897f%5Cu74dc%5Cu5305501693%22%2C%22goodsThumb%22%3A%22%5C%2Fsources%5C%2Fimages%5C%2Fgoods%5C%2FMC%5C%2F501693%5C%2F501693_70_00.jpg%22%2C%22goods_price%22%3A179%2C%22is_real%22%3A1%2C%22transactionPrice%22%3A179%2C%22shareBonus%22%3A0%2C%22integral%22%3A1%2C%22integralMoney%22%3A0.01%2C%22goodsNumber%22%3A1%2C%22useCard%22%3A%22%22%2C%22extensionId%22%3A1%2C%22promotionDesc%22%3A%22%22%2C%22extensionCode%22%3A%22common%22%7D%5D%7D%5D%2C%22payList%22%3A%5B%7B%22integralMoney%22%3A0.01%2C%22surplus%22%3A0%2C%22payId%22%3A%221%22%2C%22payCode%22%3A%22alipay%22%2C%22payStatus%22%3A0%2C%22payTotalFee%22%3A%22178.99%22%2C%22bonusId%22%3A%22%22%7D%5D%2C%22promotionSummry%22%3A%5B%5D%7D";
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("orderInfo", param));
			String encodeMobel = HttpClientUtil.post(url, null);
			System.out.println(URLDecoder.decode(encodeMobel, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}