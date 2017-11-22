package com.work.shop.bean.mbproduct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductLibAttrValueExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    public ProductLibAttrValueExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_attr_value
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
     * This method corresponds to the database table product_lib_attr_value
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
     * This method corresponds to the database table product_lib_attr_value
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_lib_attr_value
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
     * This class corresponds to the database table product_lib_attr_value
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

        public Criteria andAttrValueIdIsNull() {
            addCriterion("attr_value_id is null");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdIsNotNull() {
            addCriterion("attr_value_id is not null");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdEqualTo(Integer value) {
            addCriterion("attr_value_id =", value, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdNotEqualTo(Integer value) {
            addCriterion("attr_value_id <>", value, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdGreaterThan(Integer value) {
            addCriterion("attr_value_id >", value, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("attr_value_id >=", value, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdLessThan(Integer value) {
            addCriterion("attr_value_id <", value, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdLessThanOrEqualTo(Integer value) {
            addCriterion("attr_value_id <=", value, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdIn(List<Integer> values) {
            addCriterion("attr_value_id in", values, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdNotIn(List<Integer> values) {
            addCriterion("attr_value_id not in", values, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdBetween(Integer value1, Integer value2) {
            addCriterion("attr_value_id between", value1, value2, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIdNotBetween(Integer value1, Integer value2) {
            addCriterion("attr_value_id not between", value1, value2, "attrValueId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdIsNull() {
            addCriterion("attr_key_id is null");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdIsNotNull() {
            addCriterion("attr_key_id is not null");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdEqualTo(Integer value) {
            addCriterion("attr_key_id =", value, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdNotEqualTo(Integer value) {
            addCriterion("attr_key_id <>", value, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdGreaterThan(Integer value) {
            addCriterion("attr_key_id >", value, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("attr_key_id >=", value, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdLessThan(Integer value) {
            addCriterion("attr_key_id <", value, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdLessThanOrEqualTo(Integer value) {
            addCriterion("attr_key_id <=", value, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdIn(List<Integer> values) {
            addCriterion("attr_key_id in", values, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdNotIn(List<Integer> values) {
            addCriterion("attr_key_id not in", values, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdBetween(Integer value1, Integer value2) {
            addCriterion("attr_key_id between", value1, value2, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrKeyIdNotBetween(Integer value1, Integer value2) {
            addCriterion("attr_key_id not between", value1, value2, "attrKeyId");
            return (Criteria) this;
        }

        public Criteria andAttrValueIsNull() {
            addCriterion("attr_value is null");
            return (Criteria) this;
        }

        public Criteria andAttrValueIsNotNull() {
            addCriterion("attr_value is not null");
            return (Criteria) this;
        }

        public Criteria andAttrValueEqualTo(String value) {
            addCriterion("attr_value =", value, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueNotEqualTo(String value) {
            addCriterion("attr_value <>", value, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueGreaterThan(String value) {
            addCriterion("attr_value >", value, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueGreaterThanOrEqualTo(String value) {
            addCriterion("attr_value >=", value, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueLessThan(String value) {
            addCriterion("attr_value <", value, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueLessThanOrEqualTo(String value) {
            addCriterion("attr_value <=", value, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueLike(String value) {
            addCriterion("attr_value like", value, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueNotLike(String value) {
            addCriterion("attr_value not like", value, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueIn(List<String> values) {
            addCriterion("attr_value in", values, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueNotIn(List<String> values) {
            addCriterion("attr_value not in", values, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueBetween(String value1, String value2) {
            addCriterion("attr_value between", value1, value2, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueNotBetween(String value1, String value2) {
            addCriterion("attr_value not between", value1, value2, "attrValue");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeIsNull() {
            addCriterion("attr_value_code is null");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeIsNotNull() {
            addCriterion("attr_value_code is not null");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeEqualTo(String value) {
            addCriterion("attr_value_code =", value, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeNotEqualTo(String value) {
            addCriterion("attr_value_code <>", value, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeGreaterThan(String value) {
            addCriterion("attr_value_code >", value, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeGreaterThanOrEqualTo(String value) {
            addCriterion("attr_value_code >=", value, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeLessThan(String value) {
            addCriterion("attr_value_code <", value, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeLessThanOrEqualTo(String value) {
            addCriterion("attr_value_code <=", value, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeLike(String value) {
            addCriterion("attr_value_code like", value, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeNotLike(String value) {
            addCriterion("attr_value_code not like", value, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeIn(List<String> values) {
            addCriterion("attr_value_code in", values, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeNotIn(List<String> values) {
            addCriterion("attr_value_code not in", values, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeBetween(String value1, String value2) {
            addCriterion("attr_value_code between", value1, value2, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andAttrValueCodeNotBetween(String value1, String value2) {
            addCriterion("attr_value_code not between", value1, value2, "attrValueCode");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrIsNull() {
            addCriterion("is_platform_attr is null");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrIsNotNull() {
            addCriterion("is_platform_attr is not null");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrEqualTo(Byte value) {
            addCriterion("is_platform_attr =", value, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrNotEqualTo(Byte value) {
            addCriterion("is_platform_attr <>", value, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrGreaterThan(Byte value) {
            addCriterion("is_platform_attr >", value, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_platform_attr >=", value, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrLessThan(Byte value) {
            addCriterion("is_platform_attr <", value, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrLessThanOrEqualTo(Byte value) {
            addCriterion("is_platform_attr <=", value, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrIn(List<Byte> values) {
            addCriterion("is_platform_attr in", values, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrNotIn(List<Byte> values) {
            addCriterion("is_platform_attr not in", values, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrBetween(Byte value1, Byte value2) {
            addCriterion("is_platform_attr between", value1, value2, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andIsPlatformAttrNotBetween(Byte value1, Byte value2) {
            addCriterion("is_platform_attr not between", value1, value2, "isPlatformAttr");
            return (Criteria) this;
        }

        public Criteria andSortIsNull() {
            addCriterion("sort is null");
            return (Criteria) this;
        }

        public Criteria andSortIsNotNull() {
            addCriterion("sort is not null");
            return (Criteria) this;
        }

        public Criteria andSortEqualTo(Integer value) {
            addCriterion("sort =", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotEqualTo(Integer value) {
            addCriterion("sort <>", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThan(Integer value) {
            addCriterion("sort >", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThanOrEqualTo(Integer value) {
            addCriterion("sort >=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThan(Integer value) {
            addCriterion("sort <", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThanOrEqualTo(Integer value) {
            addCriterion("sort <=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortIn(List<Integer> values) {
            addCriterion("sort in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotIn(List<Integer> values) {
            addCriterion("sort not in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortBetween(Integer value1, Integer value2) {
            addCriterion("sort between", value1, value2, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotBetween(Integer value1, Integer value2) {
            addCriterion("sort not between", value1, value2, "sort");
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

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(String value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(String value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(String value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(String value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(String value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLike(String value) {
            addCriterion("create_user like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotLike(String value) {
            addCriterion("create_user not like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<String> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<String> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(String value1, String value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(String value1, String value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("create_date is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("create_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("create_date =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("create_date <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("create_date >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("create_date >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("create_date <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("create_date <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("create_date in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("create_date not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("create_date between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("create_date not between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserIsNull() {
            addCriterion("last_update_user is null");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserIsNotNull() {
            addCriterion("last_update_user is not null");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserEqualTo(String value) {
            addCriterion("last_update_user =", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserNotEqualTo(String value) {
            addCriterion("last_update_user <>", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserGreaterThan(String value) {
            addCriterion("last_update_user >", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserGreaterThanOrEqualTo(String value) {
            addCriterion("last_update_user >=", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserLessThan(String value) {
            addCriterion("last_update_user <", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserLessThanOrEqualTo(String value) {
            addCriterion("last_update_user <=", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserLike(String value) {
            addCriterion("last_update_user like", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserNotLike(String value) {
            addCriterion("last_update_user not like", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserIn(List<String> values) {
            addCriterion("last_update_user in", values, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserNotIn(List<String> values) {
            addCriterion("last_update_user not in", values, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserBetween(String value1, String value2) {
            addCriterion("last_update_user between", value1, value2, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserNotBetween(String value1, String value2) {
            addCriterion("last_update_user not between", value1, value2, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateIsNull() {
            addCriterion("last_update_date is null");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateIsNotNull() {
            addCriterion("last_update_date is not null");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateEqualTo(Date value) {
            addCriterion("last_update_date =", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateNotEqualTo(Date value) {
            addCriterion("last_update_date <>", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateGreaterThan(Date value) {
            addCriterion("last_update_date >", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("last_update_date >=", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateLessThan(Date value) {
            addCriterion("last_update_date <", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateLessThanOrEqualTo(Date value) {
            addCriterion("last_update_date <=", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateIn(List<Date> values) {
            addCriterion("last_update_date in", values, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateNotIn(List<Date> values) {
            addCriterion("last_update_date not in", values, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateBetween(Date value1, Date value2) {
            addCriterion("last_update_date between", value1, value2, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateNotBetween(Date value1, Date value2) {
            addCriterion("last_update_date not between", value1, value2, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameIsNull() {
            addCriterion("last_controller_name is null");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameIsNotNull() {
            addCriterion("last_controller_name is not null");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameEqualTo(String value) {
            addCriterion("last_controller_name =", value, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameNotEqualTo(String value) {
            addCriterion("last_controller_name <>", value, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameGreaterThan(String value) {
            addCriterion("last_controller_name >", value, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameGreaterThanOrEqualTo(String value) {
            addCriterion("last_controller_name >=", value, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameLessThan(String value) {
            addCriterion("last_controller_name <", value, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameLessThanOrEqualTo(String value) {
            addCriterion("last_controller_name <=", value, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameLike(String value) {
            addCriterion("last_controller_name like", value, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameNotLike(String value) {
            addCriterion("last_controller_name not like", value, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameIn(List<String> values) {
            addCriterion("last_controller_name in", values, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameNotIn(List<String> values) {
            addCriterion("last_controller_name not in", values, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameBetween(String value1, String value2) {
            addCriterion("last_controller_name between", value1, value2, "lastControllerName");
            return (Criteria) this;
        }

        public Criteria andLastControllerNameNotBetween(String value1, String value2) {
            addCriterion("last_controller_name not between", value1, value2, "lastControllerName");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table product_lib_attr_value
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
     * This class corresponds to the database table product_lib_attr_value
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