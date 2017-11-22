package com.work.shop.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AlbbTrade {

	/*
	 * 订单状态。CANCEL交易关闭，SUCCESS交易成功，WAIT_BUYER_PAY等待买家付款，WAIT_SELLER_SEND等待卖家发货，WAIT_BUYER_RECEIVE等待买家确认收货，WAIT_SELLER_ACT分阶段等待卖家操作，WAIT_BUYER_CONFIRM_ACTION分阶段等待买家确认卖家操作，WAIT_SELLER_PUSH分阶段等待卖家推进，WAIT_LOGISTICS_TAKE_IN等待物流公司揽件COD，WAIT_BUYER_SIGN等待买家签收COD，SIGN_IN_SUCCESS买家已签收COD，SIGN_IN_FAILED签收失败COD
	*/
	private String status;

	/*
	 * 修改时间
	*/
	private Date gmtModified;

	/*
	 * 创建时间
	*/
	private Date gmtCreate;

	/*
	 * 退款状态。WAIT_SELLER_AGREE等待卖家同意退款协议，REFUND_SUCCESS退款成功，REFUND_CLOSED退款关闭，
	 * WAIT_BUYER_MODIFY等待买家修改，WAIT_BUYER_SEND等待买家退货，WAIT_SELLER_RECEIVE等待卖家确认收货
	*/
	private String refundStatus;

	/*
	 * 买家评价状态。5未评价，4已评价
	*/
	private Integer buyerRateStatus;

	/*
	 * 卖家评价状态。5未评价，4已评价
	*/
	private Integer sellerRateStatus;

	/*
	 * 支付时间
	*/

	private Date gmtPayment;

	/*
	 * 发货时间
	*/

	private Date gmtGoodsSend;

	/*
	 * 确认时间
	*/

	private Date gmtConfirmed;

	/*
	 * 折扣，单位分
	*/
	private Long discount;

	/*
	 * 运费，单位分
	*/
	private Long carriage;

	/*
	 * 退款金额，单位分
	*/
	private Long refundPayment;

	/*
	 * 付款总金额，单位（分）订单需要支付的总金额=产品总金额+运费-折扣金额-抵价券（如果有的话），如果是COD订单，不包括COD服务费
	*/
	private Long sumPayment;

	/*
	 * 关闭原因
	*/
	private String closeReason;

	/*
	 * 买家留言
	*/
	private String buyerFeedback;

	/*
	 * 收货人邮编
	*/
	private String toPost;

	/*
	 * 收货人地址
	*/
	private String toArea;

	/*
	 * 支付宝交易号
	*/
	private String alipayTradeId;

	/*
	 * 卖家公司名称
	*/
	private String sellerCompanyName;

	/*
	 * 卖家email
	*/
	private String sellerEmail;

	/*
	 * 买家公司名称
	*/
	private String buyerCompanyName;

	/*
	 * 买家email
	*/
	private String buyerEmail;

	/*
	 * 总货品金额
	*/
	private Long sumProductPayment;

	/*
	 * 订单明细
	*/
//	private List<AlbbOrder> orderEntries;

	/*
	 * 物流单列表
	*/
//	private AlbbLogisticsOrderModel[] logisticsOrderList;

	/*
	 * 备注列表
	 *  (由买家备注和卖家备注构成 )
	*/
//	private AlbbOrderMemoModel[] orderMemoList;

	/*
	 * 发票信息
	*/
//	private AlbbOrderInvoiceModel orderInvoiceModel;

	/*
	 * 子支付单
	*/
	private List stepOrderList;

	/*
	 * 是否一次性付款
	*/
	private boolean stepPayAll;

	/*
	 * 支付状态。1:等待买家付款，2:已付款，4:交易关闭，6:交易成功，8:交易被系统关闭
	*/
	private int payStatus;

	/*
	 * 物流状态。8:还未创建物流订单；1:未发货；2:已发货；3:已收货，交易成功；4:已经退货，交易失败；5:部分发货，交易成功
	*/
	private int logisticsStatus;

	/*
	 * 分阶段协议地址
	*/
	private String stepAgreementPath;

	/*
	 * cod状态。0:初始值，20:接单，-20:不接单，2:接单超时，30:揽收成功，-30:揽收失败，3:揽收超时，100:签收成功，-100:签收失败，10:订单等候发送给物流公司，-1:用户取消物流订单
	*/
	private int codStatus;

	/*
	 * 是否COD订单并且清算成功
	*/
	private boolean codAudit;

	/*
	 * cod服务费
	*/
	private Long codFee;

	/*
	 * 买家承担的服务费
	*/
	private Long codBuyerFee;

	/*
	 * 卖家承担的服务费
	*/
	private Long codSellerFee;

	/*
	 * cod交易的实付款（买家实际支付给物流的费用）
	*/
	private Long codActualFee;

	/*
	 * 买家签收时间(COD)
	*/

	private Date gmtSign;

	/*
	 * cod三家分润
	*/
	private String codFeeDividend;

	/*
	 * cod服务费初始值
	*/
	private Long codInitFee;

	/*
	 * 买家承担的服务费初始值
	*/
	private Long codBuyerInitFee;

	/*
	 * 买家备注
	*/
//	private AlbbOrderMemoModel buyerOrderMemo;

	/**
	 * new add, as a foreign key
	 */
	private Integer buyerOrderMemoId;

	/*
	 * 卖家备注
	*/
//	private AlbbOrderMemoModel sellerOrderMemo;

	/**
	 * new add, as a foreign key
	 */
	private Integer sellerOrderMemoId;

	/*
	 * 订单号
	*/
	private Long id;

	/*
	 * 买家id
	*/
	private String buyerMemberId;

	/*
	 * 卖家id
	*/
	private String sellerMemberId;

	/*
	 * 卖家数字id
	*/
	private Long sellerUserId;

	/*
	 * 买家数字id
	*/
	private Long buyerUserId;

	/*
	 * 买家支付宝id
	*/
	private String buyerAlipayId;

	/*
	 * 卖家支付宝id
	*/
	private String sellerAlipayId;

	/*
	 * 卖家登录id
	*/
	private String sellerLoginId;

	/*
	 * 买家登录id
	*/
	private String buyerLoginId;

	/*
	 * 交易类型。UNIFY统一交易流程，STEPPAY分阶段交易，COD货到付款交易，CERTIFICATE信用凭证支付交易（注：该字段已不再维护，替换为tradeTypeStr，请新业务使用新字段）
	*/
	private String tradeType;

	/*
	 * 交易类型string。6统一交易流程，7分阶段交易，8货到付款交易，9信用凭证支付交易，10帐期支付交易
	*/
	private String tradeTypeStr;

	/*
	 * 收货人手机
	*/
	private String toMobile;

	/*
	 * 收货人电话
	*/
	private String toPhone;

	/*
	 * 收货人姓名
	*/
	private String toFullName;

	private String outerOrderSn;

	private String orderErrorMsg;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Integer getBuyerRateStatus() {
		return buyerRateStatus;
	}

	public void setBuyerRateStatus(Integer buyerRateStatus) {
		this.buyerRateStatus = buyerRateStatus;
	}

	public Integer getSellerRateStatus() {
		return sellerRateStatus;
	}

	public void setSellerRateStatus(Integer sellerRateStatus) {
		this.sellerRateStatus = sellerRateStatus;
	}

	public Date getGmtPayment() {
		return gmtPayment;
	}

	public void setGmtPayment(Date gmtPayment) {
		this.gmtPayment = gmtPayment;
	}

	public Date getGmtGoodsSend() {
		return gmtGoodsSend;
	}

	public void setGmtGoodsSend(Date gmtGoodsSend) {
		this.gmtGoodsSend = gmtGoodsSend;
	}

	public Date getGmtConfirmed() {
		return gmtConfirmed;
	}

	public void setGmtConfirmed(Date gmtConfirmed) {
		this.gmtConfirmed = gmtConfirmed;
	}

	public Long getDiscount() {
		return discount;
	}

	public void setDiscount(Long discount) {
		this.discount = discount;
	}

	public Long getCarriage() {
		return carriage;
	}

	public void setCarriage(Long carriage) {
		this.carriage = carriage;
	}

	public Long getRefundPayment() {
		return refundPayment;
	}

	public void setRefundPayment(Long refundPayment) {
		this.refundPayment = refundPayment;
	}

	public Long getSumPayment() {
		return sumPayment;
	}

	public void setSumPayment(Long sumPayment) {
		this.sumPayment = sumPayment;
	}

	public String getCloseReason() {
		return closeReason;
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	public String getBuyerFeedback() {
		return buyerFeedback;
	}

	public void setBuyerFeedback(String buyerFeedback) {
		this.buyerFeedback = buyerFeedback;
	}

	public String getToPost() {
		return toPost;
	}

	public void setToPost(String toPost) {
		this.toPost = toPost;
	}

	public String getToArea() {
		return toArea;
	}

	public void setToArea(String toArea) {
		this.toArea = toArea;
	}

	public String getAlipayTradeId() {
		return alipayTradeId;
	}

	public void setAlipayTradeId(String alipayTradeId) {
		this.alipayTradeId = alipayTradeId;
	}

	public String getSellerCompanyName() {
		return sellerCompanyName;
	}

	public void setSellerCompanyName(String sellerCompanyName) {
		this.sellerCompanyName = sellerCompanyName;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getBuyerCompanyName() {
		return buyerCompanyName;
	}

	public void setBuyerCompanyName(String buyerCompanyName) {
		this.buyerCompanyName = buyerCompanyName;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public Long getSumProductPayment() {
		return sumProductPayment;
	}

	public void setSumProductPayment(Long sumProductPayment) {
		this.sumProductPayment = sumProductPayment;
	}

	public List getStepOrderList() {
		return stepOrderList;
	}

	public void setStepOrderList(List stepOrderList) {
		this.stepOrderList = stepOrderList;
	}

	public boolean isStepPayAll() {
		return stepPayAll;
	}

	public void setStepPayAll(boolean stepPayAll) {
		this.stepPayAll = stepPayAll;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getLogisticsStatus() {
		return logisticsStatus;
	}

	public void setLogisticsStatus(int logisticsStatus) {
		this.logisticsStatus = logisticsStatus;
	}

	public String getStepAgreementPath() {
		return stepAgreementPath;
	}

	public void setStepAgreementPath(String stepAgreementPath) {
		this.stepAgreementPath = stepAgreementPath;
	}

	public int getCodStatus() {
		return codStatus;
	}

	public void setCodStatus(int codStatus) {
		this.codStatus = codStatus;
	}

	public boolean isCodAudit() {
		return codAudit;
	}

	public void setCodAudit(boolean codAudit) {
		this.codAudit = codAudit;
	}

	public Long getCodFee() {
		return codFee;
	}

	public void setCodFee(Long codFee) {
		this.codFee = codFee;
	}

	public Long getCodBuyerFee() {
		return codBuyerFee;
	}

	public void setCodBuyerFee(Long codBuyerFee) {
		this.codBuyerFee = codBuyerFee;
	}

	public Long getCodSellerFee() {
		return codSellerFee;
	}

	public void setCodSellerFee(Long codSellerFee) {
		this.codSellerFee = codSellerFee;
	}

	public Long getCodActualFee() {
		return codActualFee;
	}

	public void setCodActualFee(Long codActualFee) {
		this.codActualFee = codActualFee;
	}

	public Date getGmtSign() {
		return gmtSign;
	}

	public void setGmtSign(Date gmtSign) {
		this.gmtSign = gmtSign;
	}

	public String getCodFeeDividend() {
		return codFeeDividend;
	}

	public void setCodFeeDividend(String codFeeDividend) {
		this.codFeeDividend = codFeeDividend;
	}

	public Long getCodInitFee() {
		return codInitFee;
	}

	public void setCodInitFee(Long codInitFee) {
		this.codInitFee = codInitFee;
	}

	public Long getCodBuyerInitFee() {
		return codBuyerInitFee;
	}

	public void setCodBuyerInitFee(Long codBuyerInitFee) {
		this.codBuyerInitFee = codBuyerInitFee;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBuyerMemberId() {
		return buyerMemberId;
	}

	public void setBuyerMemberId(String buyerMemberId) {
		this.buyerMemberId = buyerMemberId;
	}

	public String getSellerMemberId() {
		return sellerMemberId;
	}

	public void setSellerMemberId(String sellerMemberId) {
		this.sellerMemberId = sellerMemberId;
	}

	public Long getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(Long sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

	public Long getBuyerUserId() {
		return buyerUserId;
	}

	public void setBuyerUserId(Long buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	public String getBuyerAlipayId() {
		return buyerAlipayId;
	}

	public void setBuyerAlipayId(String buyerAlipayId) {
		this.buyerAlipayId = buyerAlipayId;
	}

	public String getSellerAlipayId() {
		return sellerAlipayId;
	}

	public void setSellerAlipayId(String sellerAlipayId) {
		this.sellerAlipayId = sellerAlipayId;
	}

	public String getSellerLoginId() {
		return sellerLoginId;
	}

	public void setSellerLoginId(String sellerLoginId) {
		this.sellerLoginId = sellerLoginId;
	}

	public String getBuyerLoginId() {
		return buyerLoginId;
	}

	public void setBuyerLoginId(String buyerLoginId) {
		this.buyerLoginId = buyerLoginId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeTypeStr() {
		return tradeTypeStr;
	}

	public void setTradeTypeStr(String tradeTypeStr) {
		this.tradeTypeStr = tradeTypeStr;
	}

	public String getToMobile() {
		return toMobile;
	}

	public void setToMobile(String toMobile) {
		this.toMobile = toMobile;
	}

	public String getToPhone() {
		return toPhone;
	}

	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}

	public String getToFullName() {
		return toFullName;
	}

	public void setToFullName(String toFullName) {
		this.toFullName = toFullName;
	}

	//

	public Integer getBuyerOrderMemoId() {
		return buyerOrderMemoId;
	}

	public void setBuyerOrderMemoId(Integer buyerOrderMemoId) {
		this.buyerOrderMemoId = buyerOrderMemoId;
	}

	public Integer getSellerOrderMemoId() {
		return sellerOrderMemoId;
	}

	public void setSellerOrderMemoId(Integer sellerOrderMemoId) {
		this.sellerOrderMemoId = sellerOrderMemoId;
	}

	public String getOuterOrderSn() {
		return outerOrderSn;
	}

	public void setOuterOrderSn(String outerOrderSn) {
		this.outerOrderSn = outerOrderSn;
	}

	public String getOrderErrorMsg() {
		return orderErrorMsg;
	}

	public void setOrderErrorMsg(String orderErrorMsg) {
		this.orderErrorMsg = orderErrorMsg;
	}
}
