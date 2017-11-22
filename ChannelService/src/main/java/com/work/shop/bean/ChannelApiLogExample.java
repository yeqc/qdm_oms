package com.work.shop.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChannelApiLogExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public ChannelApiLogExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        /**  
         * limit 
         * 
         */ 
        public Criteria limit(int offset, int len) {
            if (len==0 )
              throw new RuntimeException("len is 0");
            addCriterion("limit",offset,len,"null");
            return (Criteria) this;
        }

        /**  
         * top 
         * 
         */ 
        public Criteria limit(int len) {
            if (len==0 )
              throw new RuntimeException("len is 0");
            addCriterion("limit", 0, len, "null");
            return (Criteria) this;
        }

        public Criteria getLimitValue() {
            return (Criteria)criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andLogIdIsNull() {
            addCriterion("log_id is null");
            return (Criteria) this;
        }

        public Criteria andLogIdIsNotNull() {
            addCriterion("log_id is not null");
            return (Criteria) this;
        }

        public Criteria andLogIdEqualTo(Integer value) {
            addCriterion("log_id =", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotEqualTo(Integer value) {
            addCriterion("log_id <>", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdGreaterThan(Integer value) {
            addCriterion("log_id >", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("log_id >=", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdLessThan(Integer value) {
            addCriterion("log_id <", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdLessThanOrEqualTo(Integer value) {
            addCriterion("log_id <=", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdIn(List<Integer> values) {
            addCriterion("log_id in", values, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotIn(List<Integer> values) {
            addCriterion("log_id not in", values, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdBetween(Integer value1, Integer value2) {
            addCriterion("log_id between", value1, value2, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotBetween(Integer value1, Integer value2) {
            addCriterion("log_id not between", value1, value2, "logId");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIsNull() {
            addCriterion("channel_code is null");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIsNotNull() {
            addCriterion("channel_code is not null");
            return (Criteria) this;
        }

        public Criteria andChannelCodeEqualTo(String value) {
            addCriterion("channel_code =", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotEqualTo(String value) {
            addCriterion("channel_code <>", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeGreaterThan(String value) {
            addCriterion("channel_code >", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeGreaterThanOrEqualTo(String value) {
            addCriterion("channel_code >=", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeLessThan(String value) {
            addCriterion("channel_code <", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeLessThanOrEqualTo(String value) {
            addCriterion("channel_code <=", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeLike(String value) {
            addCriterion("channel_code like", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotLike(String value) {
            addCriterion("channel_code not like", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIn(List<String> values) {
            addCriterion("channel_code in", values, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotIn(List<String> values) {
            addCriterion("channel_code not in", values, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeBetween(String value1, String value2) {
            addCriterion("channel_code between", value1, value2, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotBetween(String value1, String value2) {
            addCriterion("channel_code not between", value1, value2, "channelCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeIsNull() {
            addCriterion("shop_code is null");
            return (Criteria) this;
        }

        public Criteria andShopCodeIsNotNull() {
            addCriterion("shop_code is not null");
            return (Criteria) this;
        }

        public Criteria andShopCodeEqualTo(String value) {
            addCriterion("shop_code =", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotEqualTo(String value) {
            addCriterion("shop_code <>", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThan(String value) {
            addCriterion("shop_code >", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThanOrEqualTo(String value) {
            addCriterion("shop_code >=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThan(String value) {
            addCriterion("shop_code <", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThanOrEqualTo(String value) {
            addCriterion("shop_code <=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLike(String value) {
            addCriterion("shop_code like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotLike(String value) {
            addCriterion("shop_code not like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeIn(List<String> values) {
            addCriterion("shop_code in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotIn(List<String> values) {
            addCriterion("shop_code not in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeBetween(String value1, String value2) {
            addCriterion("shop_code between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotBetween(String value1, String value2) {
            addCriterion("shop_code not between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andMethodNameIsNull() {
            addCriterion("method_name is null");
            return (Criteria) this;
        }

        public Criteria andMethodNameIsNotNull() {
            addCriterion("method_name is not null");
            return (Criteria) this;
        }

        public Criteria andMethodNameEqualTo(String value) {
            addCriterion("method_name =", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotEqualTo(String value) {
            addCriterion("method_name <>", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameGreaterThan(String value) {
            addCriterion("method_name >", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameGreaterThanOrEqualTo(String value) {
            addCriterion("method_name >=", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLessThan(String value) {
            addCriterion("method_name <", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLessThanOrEqualTo(String value) {
            addCriterion("method_name <=", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLike(String value) {
            addCriterion("method_name like", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotLike(String value) {
            addCriterion("method_name not like", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameIn(List<String> values) {
            addCriterion("method_name in", values, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotIn(List<String> values) {
            addCriterion("method_name not in", values, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameBetween(String value1, String value2) {
            addCriterion("method_name between", value1, value2, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotBetween(String value1, String value2) {
            addCriterion("method_name not between", value1, value2, "methodName");
            return (Criteria) this;
        }

        public Criteria andParamInfoIsNull() {
            addCriterion("param_info is null");
            return (Criteria) this;
        }

        public Criteria andParamInfoIsNotNull() {
            addCriterion("param_info is not null");
            return (Criteria) this;
        }

        public Criteria andParamInfoEqualTo(String value) {
            addCriterion("param_info =", value, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoNotEqualTo(String value) {
            addCriterion("param_info <>", value, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoGreaterThan(String value) {
            addCriterion("param_info >", value, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoGreaterThanOrEqualTo(String value) {
            addCriterion("param_info >=", value, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoLessThan(String value) {
            addCriterion("param_info <", value, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoLessThanOrEqualTo(String value) {
            addCriterion("param_info <=", value, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoLike(String value) {
            addCriterion("param_info like", value, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoNotLike(String value) {
            addCriterion("param_info not like", value, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoIn(List<String> values) {
            addCriterion("param_info in", values, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoNotIn(List<String> values) {
            addCriterion("param_info not in", values, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoBetween(String value1, String value2) {
            addCriterion("param_info between", value1, value2, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andParamInfoNotBetween(String value1, String value2) {
            addCriterion("param_info not between", value1, value2, "paramInfo");
            return (Criteria) this;
        }

        public Criteria andReturnCodeIsNull() {
            addCriterion("return_code is null");
            return (Criteria) this;
        }

        public Criteria andReturnCodeIsNotNull() {
            addCriterion("return_code is not null");
            return (Criteria) this;
        }

        public Criteria andReturnCodeEqualTo(String value) {
            addCriterion("return_code =", value, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeNotEqualTo(String value) {
            addCriterion("return_code <>", value, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeGreaterThan(String value) {
            addCriterion("return_code >", value, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeGreaterThanOrEqualTo(String value) {
            addCriterion("return_code >=", value, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeLessThan(String value) {
            addCriterion("return_code <", value, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeLessThanOrEqualTo(String value) {
            addCriterion("return_code <=", value, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeLike(String value) {
            addCriterion("return_code like", value, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeNotLike(String value) {
            addCriterion("return_code not like", value, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeIn(List<String> values) {
            addCriterion("return_code in", values, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeNotIn(List<String> values) {
            addCriterion("return_code not in", values, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeBetween(String value1, String value2) {
            addCriterion("return_code between", value1, value2, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnCodeNotBetween(String value1, String value2) {
            addCriterion("return_code not between", value1, value2, "returnCode");
            return (Criteria) this;
        }

        public Criteria andReturnMessageIsNull() {
            addCriterion("return_Message is null");
            return (Criteria) this;
        }

        public Criteria andReturnMessageIsNotNull() {
            addCriterion("return_Message is not null");
            return (Criteria) this;
        }

        public Criteria andReturnMessageEqualTo(String value) {
            addCriterion("return_Message =", value, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageNotEqualTo(String value) {
            addCriterion("return_Message <>", value, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageGreaterThan(String value) {
            addCriterion("return_Message >", value, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageGreaterThanOrEqualTo(String value) {
            addCriterion("return_Message >=", value, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageLessThan(String value) {
            addCriterion("return_Message <", value, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageLessThanOrEqualTo(String value) {
            addCriterion("return_Message <=", value, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageLike(String value) {
            addCriterion("return_Message like", value, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageNotLike(String value) {
            addCriterion("return_Message not like", value, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageIn(List<String> values) {
            addCriterion("return_Message in", values, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageNotIn(List<String> values) {
            addCriterion("return_Message not in", values, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageBetween(String value1, String value2) {
            addCriterion("return_Message between", value1, value2, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andReturnMessageNotBetween(String value1, String value2) {
            addCriterion("return_Message not between", value1, value2, "returnMessage");
            return (Criteria) this;
        }

        public Criteria andIpAddressIsNull() {
            addCriterion("ip_address is null");
            return (Criteria) this;
        }

        public Criteria andIpAddressIsNotNull() {
            addCriterion("ip_address is not null");
            return (Criteria) this;
        }

        public Criteria andIpAddressEqualTo(String value) {
            addCriterion("ip_address =", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressNotEqualTo(String value) {
            addCriterion("ip_address <>", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressGreaterThan(String value) {
            addCriterion("ip_address >", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressGreaterThanOrEqualTo(String value) {
            addCriterion("ip_address >=", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressLessThan(String value) {
            addCriterion("ip_address <", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressLessThanOrEqualTo(String value) {
            addCriterion("ip_address <=", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressLike(String value) {
            addCriterion("ip_address like", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressNotLike(String value) {
            addCriterion("ip_address not like", value, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressIn(List<String> values) {
            addCriterion("ip_address in", values, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressNotIn(List<String> values) {
            addCriterion("ip_address not in", values, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressBetween(String value1, String value2) {
            addCriterion("ip_address between", value1, value2, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andIpAddressNotBetween(String value1, String value2) {
            addCriterion("ip_address not between", value1, value2, "ipAddress");
            return (Criteria) this;
        }

        public Criteria andUserIsNull() {
            addCriterion("user is null");
            return (Criteria) this;
        }

        public Criteria andUserIsNotNull() {
            addCriterion("user is not null");
            return (Criteria) this;
        }

        public Criteria andUserEqualTo(String value) {
            addCriterion("user =", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserNotEqualTo(String value) {
            addCriterion("user <>", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserGreaterThan(String value) {
            addCriterion("user >", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserGreaterThanOrEqualTo(String value) {
            addCriterion("user >=", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserLessThan(String value) {
            addCriterion("user <", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserLessThanOrEqualTo(String value) {
            addCriterion("user <=", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserLike(String value) {
            addCriterion("user like", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserNotLike(String value) {
            addCriterion("user not like", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserIn(List<String> values) {
            addCriterion("user in", values, "user");
            return (Criteria) this;
        }

        public Criteria andUserNotIn(List<String> values) {
            addCriterion("user not in", values, "user");
            return (Criteria) this;
        }

        public Criteria andUserBetween(String value1, String value2) {
            addCriterion("user between", value1, value2, "user");
            return (Criteria) this;
        }

        public Criteria andUserNotBetween(String value1, String value2) {
            addCriterion("user not between", value1, value2, "user");
            return (Criteria) this;
        }

        public Criteria andRequestTimeIsNull() {
            addCriterion("request_time is null");
            return (Criteria) this;
        }

        public Criteria andRequestTimeIsNotNull() {
            addCriterion("request_time is not null");
            return (Criteria) this;
        }

        public Criteria andRequestTimeEqualTo(Date value) {
            addCriterion("request_time =", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeNotEqualTo(Date value) {
            addCriterion("request_time <>", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeGreaterThan(Date value) {
            addCriterion("request_time >", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("request_time >=", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeLessThan(Date value) {
            addCriterion("request_time <", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeLessThanOrEqualTo(Date value) {
            addCriterion("request_time <=", value, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeIn(List<Date> values) {
            addCriterion("request_time in", values, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeNotIn(List<Date> values) {
            addCriterion("request_time not in", values, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeBetween(Date value1, Date value2) {
            addCriterion("request_time between", value1, value2, "requestTime");
            return (Criteria) this;
        }

        public Criteria andRequestTimeNotBetween(Date value1, Date value2) {
            addCriterion("request_time not between", value1, value2, "requestTime");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table channel_api_log
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table channel_api_log
     *
     * @mbggenerated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean limitValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isLimitValue() {
            return limitValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            if("limit".equals(condition))
              this.limitValue = true;
            else
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}