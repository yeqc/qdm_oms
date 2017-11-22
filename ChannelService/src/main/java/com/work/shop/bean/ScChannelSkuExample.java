package com.work.shop.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScChannelSkuExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    public ScChannelSkuExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sc_channel_sku
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
     * This method corresponds to the database table sc_channel_sku
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
     * This method corresponds to the database table sc_channel_sku
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sc_channel_sku
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
     * This class corresponds to the database table sc_channel_sku
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andGoodsSnIsNull() {
            addCriterion("goods_sn is null");
            return (Criteria) this;
        }

        public Criteria andGoodsSnIsNotNull() {
            addCriterion("goods_sn is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsSnEqualTo(String value) {
            addCriterion("goods_sn =", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnNotEqualTo(String value) {
            addCriterion("goods_sn <>", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnGreaterThan(String value) {
            addCriterion("goods_sn >", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnGreaterThanOrEqualTo(String value) {
            addCriterion("goods_sn >=", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnLessThan(String value) {
            addCriterion("goods_sn <", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnLessThanOrEqualTo(String value) {
            addCriterion("goods_sn <=", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnLike(String value) {
            addCriterion("goods_sn like", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnNotLike(String value) {
            addCriterion("goods_sn not like", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnIn(List<String> values) {
            addCriterion("goods_sn in", values, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnNotIn(List<String> values) {
            addCriterion("goods_sn not in", values, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnBetween(String value1, String value2) {
            addCriterion("goods_sn between", value1, value2, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnNotBetween(String value1, String value2) {
            addCriterion("goods_sn not between", value1, value2, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andSkuIsNull() {
            addCriterion("sku is null");
            return (Criteria) this;
        }

        public Criteria andSkuIsNotNull() {
            addCriterion("sku is not null");
            return (Criteria) this;
        }

        public Criteria andSkuEqualTo(String value) {
            addCriterion("sku =", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotEqualTo(String value) {
            addCriterion("sku <>", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuGreaterThan(String value) {
            addCriterion("sku >", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuGreaterThanOrEqualTo(String value) {
            addCriterion("sku >=", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuLessThan(String value) {
            addCriterion("sku <", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuLessThanOrEqualTo(String value) {
            addCriterion("sku <=", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuLike(String value) {
            addCriterion("sku like", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotLike(String value) {
            addCriterion("sku not like", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuIn(List<String> values) {
            addCriterion("sku in", values, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotIn(List<String> values) {
            addCriterion("sku not in", values, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuBetween(String value1, String value2) {
            addCriterion("sku between", value1, value2, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotBetween(String value1, String value2) {
            addCriterion("sku not between", value1, value2, "sku");
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

        public Criteria andLastSyncStockIsNull() {
            addCriterion("last_sync_stock is null");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockIsNotNull() {
            addCriterion("last_sync_stock is not null");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockEqualTo(Integer value) {
            addCriterion("last_sync_stock =", value, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockNotEqualTo(Integer value) {
            addCriterion("last_sync_stock <>", value, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockGreaterThan(Integer value) {
            addCriterion("last_sync_stock >", value, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockGreaterThanOrEqualTo(Integer value) {
            addCriterion("last_sync_stock >=", value, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockLessThan(Integer value) {
            addCriterion("last_sync_stock <", value, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockLessThanOrEqualTo(Integer value) {
            addCriterion("last_sync_stock <=", value, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockIn(List<Integer> values) {
            addCriterion("last_sync_stock in", values, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockNotIn(List<Integer> values) {
            addCriterion("last_sync_stock not in", values, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockBetween(Integer value1, Integer value2) {
            addCriterion("last_sync_stock between", value1, value2, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncStockNotBetween(Integer value1, Integer value2) {
            addCriterion("last_sync_stock not between", value1, value2, "lastSyncStock");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateIsNull() {
            addCriterion("last_sync_date is null");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateIsNotNull() {
            addCriterion("last_sync_date is not null");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateEqualTo(Long value) {
            addCriterion("last_sync_date =", value, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateNotEqualTo(Long value) {
            addCriterion("last_sync_date <>", value, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateGreaterThan(Long value) {
            addCriterion("last_sync_date >", value, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateGreaterThanOrEqualTo(Long value) {
            addCriterion("last_sync_date >=", value, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateLessThan(Long value) {
            addCriterion("last_sync_date <", value, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateLessThanOrEqualTo(Long value) {
            addCriterion("last_sync_date <=", value, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateIn(List<Long> values) {
            addCriterion("last_sync_date in", values, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateNotIn(List<Long> values) {
            addCriterion("last_sync_date not in", values, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateBetween(Long value1, Long value2) {
            addCriterion("last_sync_date between", value1, value2, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andLastSyncDateNotBetween(Long value1, Long value2) {
            addCriterion("last_sync_date not between", value1, value2, "lastSyncDate");
            return (Criteria) this;
        }

        public Criteria andSyncFlagIsNull() {
            addCriterion("sync_flag is null");
            return (Criteria) this;
        }

        public Criteria andSyncFlagIsNotNull() {
            addCriterion("sync_flag is not null");
            return (Criteria) this;
        }

        public Criteria andSyncFlagEqualTo(Byte value) {
            addCriterion("sync_flag =", value, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andSyncFlagNotEqualTo(Byte value) {
            addCriterion("sync_flag <>", value, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andSyncFlagGreaterThan(Byte value) {
            addCriterion("sync_flag >", value, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andSyncFlagGreaterThanOrEqualTo(Byte value) {
            addCriterion("sync_flag >=", value, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andSyncFlagLessThan(Byte value) {
            addCriterion("sync_flag <", value, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andSyncFlagLessThanOrEqualTo(Byte value) {
            addCriterion("sync_flag <=", value, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andSyncFlagIn(List<Byte> values) {
            addCriterion("sync_flag in", values, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andSyncFlagNotIn(List<Byte> values) {
            addCriterion("sync_flag not in", values, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andSyncFlagBetween(Byte value1, Byte value2) {
            addCriterion("sync_flag between", value1, value2, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andSyncFlagNotBetween(Byte value1, Byte value2) {
            addCriterion("sync_flag not between", value1, value2, "syncFlag");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andDuptDateIsNull() {
            addCriterion("dupt_date is null");
            return (Criteria) this;
        }

        public Criteria andDuptDateIsNotNull() {
            addCriterion("dupt_date is not null");
            return (Criteria) this;
        }

        public Criteria andDuptDateEqualTo(Date value) {
            addCriterion("dupt_date =", value, "duptDate");
            return (Criteria) this;
        }

        public Criteria andDuptDateNotEqualTo(Date value) {
            addCriterion("dupt_date <>", value, "duptDate");
            return (Criteria) this;
        }

        public Criteria andDuptDateGreaterThan(Date value) {
            addCriterion("dupt_date >", value, "duptDate");
            return (Criteria) this;
        }

        public Criteria andDuptDateGreaterThanOrEqualTo(Date value) {
            addCriterion("dupt_date >=", value, "duptDate");
            return (Criteria) this;
        }

        public Criteria andDuptDateLessThan(Date value) {
            addCriterion("dupt_date <", value, "duptDate");
            return (Criteria) this;
        }

        public Criteria andDuptDateLessThanOrEqualTo(Date value) {
            addCriterion("dupt_date <=", value, "duptDate");
            return (Criteria) this;
        }

        public Criteria andDuptDateIn(List<Date> values) {
            addCriterion("dupt_date in", values, "duptDate");
            return (Criteria) this;
        }

        public Criteria andDuptDateNotIn(List<Date> values) {
            addCriterion("dupt_date not in", values, "duptDate");
            return (Criteria) this;
        }

        public Criteria andDuptDateBetween(Date value1, Date value2) {
            addCriterion("dupt_date between", value1, value2, "duptDate");
            return (Criteria) this;
        }

        public Criteria andDuptDateNotBetween(Date value1, Date value2) {
            addCriterion("dupt_date not between", value1, value2, "duptDate");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table sc_channel_sku
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
     * This class corresponds to the database table sc_channel_sku
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