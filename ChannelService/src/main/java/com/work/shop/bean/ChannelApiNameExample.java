package com.work.shop.bean;

import java.util.ArrayList;
import java.util.List;

public class ChannelApiNameExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    public ChannelApiNameExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_name
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
     * This method corresponds to the database table channel_api_name
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
     * This method corresponds to the database table channel_api_name
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_api_name
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
     * This class corresponds to the database table channel_api_name
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andChannelTitleIsNull() {
            addCriterion("channel_title is null");
            return (Criteria) this;
        }

        public Criteria andChannelTitleIsNotNull() {
            addCriterion("channel_title is not null");
            return (Criteria) this;
        }

        public Criteria andChannelTitleEqualTo(String value) {
            addCriterion("channel_title =", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleNotEqualTo(String value) {
            addCriterion("channel_title <>", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleGreaterThan(String value) {
            addCriterion("channel_title >", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleGreaterThanOrEqualTo(String value) {
            addCriterion("channel_title >=", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleLessThan(String value) {
            addCriterion("channel_title <", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleLessThanOrEqualTo(String value) {
            addCriterion("channel_title <=", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleLike(String value) {
            addCriterion("channel_title like", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleNotLike(String value) {
            addCriterion("channel_title not like", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleIn(List<String> values) {
            addCriterion("channel_title in", values, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleNotIn(List<String> values) {
            addCriterion("channel_title not in", values, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleBetween(String value1, String value2) {
            addCriterion("channel_title between", value1, value2, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleNotBetween(String value1, String value2) {
            addCriterion("channel_title not between", value1, value2, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andApiTypeIsNull() {
            addCriterion("api_type is null");
            return (Criteria) this;
        }

        public Criteria andApiTypeIsNotNull() {
            addCriterion("api_type is not null");
            return (Criteria) this;
        }

        public Criteria andApiTypeEqualTo(String value) {
            addCriterion("api_type =", value, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeNotEqualTo(String value) {
            addCriterion("api_type <>", value, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeGreaterThan(String value) {
            addCriterion("api_type >", value, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeGreaterThanOrEqualTo(String value) {
            addCriterion("api_type >=", value, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeLessThan(String value) {
            addCriterion("api_type <", value, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeLessThanOrEqualTo(String value) {
            addCriterion("api_type <=", value, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeLike(String value) {
            addCriterion("api_type like", value, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeNotLike(String value) {
            addCriterion("api_type not like", value, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeIn(List<String> values) {
            addCriterion("api_type in", values, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeNotIn(List<String> values) {
            addCriterion("api_type not in", values, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeBetween(String value1, String value2) {
            addCriterion("api_type between", value1, value2, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiTypeNotBetween(String value1, String value2) {
            addCriterion("api_type not between", value1, value2, "apiType");
            return (Criteria) this;
        }

        public Criteria andApiUrlIsNull() {
            addCriterion("api_url is null");
            return (Criteria) this;
        }

        public Criteria andApiUrlIsNotNull() {
            addCriterion("api_url is not null");
            return (Criteria) this;
        }

        public Criteria andApiUrlEqualTo(String value) {
            addCriterion("api_url =", value, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlNotEqualTo(String value) {
            addCriterion("api_url <>", value, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlGreaterThan(String value) {
            addCriterion("api_url >", value, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlGreaterThanOrEqualTo(String value) {
            addCriterion("api_url >=", value, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlLessThan(String value) {
            addCriterion("api_url <", value, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlLessThanOrEqualTo(String value) {
            addCriterion("api_url <=", value, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlLike(String value) {
            addCriterion("api_url like", value, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlNotLike(String value) {
            addCriterion("api_url not like", value, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlIn(List<String> values) {
            addCriterion("api_url in", values, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlNotIn(List<String> values) {
            addCriterion("api_url not in", values, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlBetween(String value1, String value2) {
            addCriterion("api_url between", value1, value2, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiUrlNotBetween(String value1, String value2) {
            addCriterion("api_url not between", value1, value2, "apiUrl");
            return (Criteria) this;
        }

        public Criteria andApiflagIsNull() {
            addCriterion("apiFlag is null");
            return (Criteria) this;
        }

        public Criteria andApiflagIsNotNull() {
            addCriterion("apiFlag is not null");
            return (Criteria) this;
        }

        public Criteria andApiflagEqualTo(Integer value) {
            addCriterion("apiFlag =", value, "apiflag");
            return (Criteria) this;
        }

        public Criteria andApiflagNotEqualTo(Integer value) {
            addCriterion("apiFlag <>", value, "apiflag");
            return (Criteria) this;
        }

        public Criteria andApiflagGreaterThan(Integer value) {
            addCriterion("apiFlag >", value, "apiflag");
            return (Criteria) this;
        }

        public Criteria andApiflagGreaterThanOrEqualTo(Integer value) {
            addCriterion("apiFlag >=", value, "apiflag");
            return (Criteria) this;
        }

        public Criteria andApiflagLessThan(Integer value) {
            addCriterion("apiFlag <", value, "apiflag");
            return (Criteria) this;
        }

        public Criteria andApiflagLessThanOrEqualTo(Integer value) {
            addCriterion("apiFlag <=", value, "apiflag");
            return (Criteria) this;
        }

        public Criteria andApiflagIn(List<Integer> values) {
            addCriterion("apiFlag in", values, "apiflag");
            return (Criteria) this;
        }

        public Criteria andApiflagNotIn(List<Integer> values) {
            addCriterion("apiFlag not in", values, "apiflag");
            return (Criteria) this;
        }

        public Criteria andApiflagBetween(Integer value1, Integer value2) {
            addCriterion("apiFlag between", value1, value2, "apiflag");
            return (Criteria) this;
        }

        public Criteria andApiflagNotBetween(Integer value1, Integer value2) {
            addCriterion("apiFlag not between", value1, value2, "apiflag");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table channel_api_name
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
     * This class corresponds to the database table channel_api_name
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