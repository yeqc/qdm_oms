package com.work.shop.bean.mbproduct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MBProductSellerGoodsAttrVOExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    public MBProductSellerGoodsAttrVOExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
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
     * This method corresponds to the database table product_seller_goods_attr
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
     * This method corresponds to the database table product_seller_goods_attr
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_seller_goods_attr
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
     * This class corresponds to the database table product_seller_goods_attr
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

        public Criteria andSellerCodeIsNull() {
            addCriterion("seller_code is null");
            return (Criteria) this;
        }

        public Criteria andSellerCodeIsNotNull() {
            addCriterion("seller_code is not null");
            return (Criteria) this;
        }

        public Criteria andSellerCodeEqualTo(String value) {
            addCriterion("seller_code =", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeNotEqualTo(String value) {
            addCriterion("seller_code <>", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeGreaterThan(String value) {
            addCriterion("seller_code >", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeGreaterThanOrEqualTo(String value) {
            addCriterion("seller_code >=", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeLessThan(String value) {
            addCriterion("seller_code <", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeLessThanOrEqualTo(String value) {
            addCriterion("seller_code <=", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeLike(String value) {
            addCriterion("seller_code like", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeNotLike(String value) {
            addCriterion("seller_code not like", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeIn(List<String> values) {
            addCriterion("seller_code in", values, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeNotIn(List<String> values) {
            addCriterion("seller_code not in", values, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeBetween(String value1, String value2) {
            addCriterion("seller_code between", value1, value2, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeNotBetween(String value1, String value2) {
            addCriterion("seller_code not between", value1, value2, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeIsNull() {
            addCriterion("product_sys_code is null");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeIsNotNull() {
            addCriterion("product_sys_code is not null");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeEqualTo(String value) {
            addCriterion("product_sys_code =", value, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeNotEqualTo(String value) {
            addCriterion("product_sys_code <>", value, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeGreaterThan(String value) {
            addCriterion("product_sys_code >", value, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeGreaterThanOrEqualTo(String value) {
            addCriterion("product_sys_code >=", value, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeLessThan(String value) {
            addCriterion("product_sys_code <", value, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeLessThanOrEqualTo(String value) {
            addCriterion("product_sys_code <=", value, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeLike(String value) {
            addCriterion("product_sys_code like", value, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeNotLike(String value) {
            addCriterion("product_sys_code not like", value, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeIn(List<String> values) {
            addCriterion("product_sys_code in", values, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeNotIn(List<String> values) {
            addCriterion("product_sys_code not in", values, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeBetween(String value1, String value2) {
            addCriterion("product_sys_code between", value1, value2, "productSysCode");
            return (Criteria) this;
        }

        public Criteria andProductSysCodeNotBetween(String value1, String value2) {
            addCriterion("product_sys_code not between", value1, value2, "productSysCode");
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

        public Criteria andValueIsNull() {
            addCriterion("value is null");
            return (Criteria) this;
        }

        public Criteria andValueIsNotNull() {
            addCriterion("value is not null");
            return (Criteria) this;
        }

        public Criteria andValueEqualTo(String value) {
            addCriterion("value =", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotEqualTo(String value) {
            addCriterion("value <>", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThan(String value) {
            addCriterion("value >", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThanOrEqualTo(String value) {
            addCriterion("value >=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThan(String value) {
            addCriterion("value <", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThanOrEqualTo(String value) {
            addCriterion("value <=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLike(String value) {
            addCriterion("value like", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotLike(String value) {
            addCriterion("value not like", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueIn(List<String> values) {
            addCriterion("value in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotIn(List<String> values) {
            addCriterion("value not in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueBetween(String value1, String value2) {
            addCriterion("value between", value1, value2, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotBetween(String value1, String value2) {
            addCriterion("value not between", value1, value2, "value");
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

        public Criteria andSortEqualTo(Byte value) {
            addCriterion("sort =", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotEqualTo(Byte value) {
            addCriterion("sort <>", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThan(Byte value) {
            addCriterion("sort >", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortGreaterThanOrEqualTo(Byte value) {
            addCriterion("sort >=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThan(Byte value) {
            addCriterion("sort <", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortLessThanOrEqualTo(Byte value) {
            addCriterion("sort <=", value, "sort");
            return (Criteria) this;
        }

        public Criteria andSortIn(List<Byte> values) {
            addCriterion("sort in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotIn(List<Byte> values) {
            addCriterion("sort not in", values, "sort");
            return (Criteria) this;
        }

        public Criteria andSortBetween(Byte value1, Byte value2) {
            addCriterion("sort between", value1, value2, "sort");
            return (Criteria) this;
        }

        public Criteria andSortNotBetween(Byte value1, Byte value2) {
            addCriterion("sort not between", value1, value2, "sort");
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
            addCriterion("psga.last_update_user is null");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserIsNotNull() {
            addCriterion("psga.last_update_user is not null");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserEqualTo(String value) {
            addCriterion("psga.last_update_user =", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserNotEqualTo(String value) {
            addCriterion("psga.last_update_user <>", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserGreaterThan(String value) {
            addCriterion("psga.last_update_user >", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserGreaterThanOrEqualTo(String value) {
            addCriterion("psga.last_update_user >=", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserLessThan(String value) {
            addCriterion("psga.last_update_user <", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserLessThanOrEqualTo(String value) {
            addCriterion("psga.last_update_user <=", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserLike(String value) {
            addCriterion("psga.last_update_user like", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserNotLike(String value) {
            addCriterion("psga.last_update_user not like", value, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserIn(List<String> values) {
            addCriterion("psga.last_update_user in", values, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserNotIn(List<String> values) {
            addCriterion("psga.last_update_user not in", values, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserBetween(String value1, String value2) {
            addCriterion("psga.last_update_user between", value1, value2, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateUserNotBetween(String value1, String value2) {
            addCriterion("psga.last_update_user not between", value1, value2, "lastUpdateUser");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateIsNull() {
            addCriterion("psga.last_update_date is null");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateIsNotNull() {
            addCriterion("psga.last_update_date is not null");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateEqualTo(Date value) {
            addCriterion("psga.last_update_date =", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateNotEqualTo(Date value) {
            addCriterion("psga.last_update_date <>", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateGreaterThan(Date value) {
            addCriterion("psga.last_update_date >", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("psga.last_update_date >=", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateLessThan(Date value) {
            addCriterion("psga.last_update_date <", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateLessThanOrEqualTo(Date value) {
            addCriterion("psga.last_update_date <=", value, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateIn(List<Date> values) {
            addCriterion("psga.last_update_date in", values, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateNotIn(List<Date> values) {
            addCriterion("psga.last_update_date not in", values, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateBetween(Date value1, Date value2) {
            addCriterion("psga.last_update_date between", value1, value2, "lastUpdateDate");
            return (Criteria) this;
        }

        public Criteria andLastUpdateDateNotBetween(Date value1, Date value2) {
            addCriterion("psga.last_update_date not between", value1, value2, "lastUpdateDate");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table product_seller_goods_attr
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
     * This class corresponds to the database table product_seller_goods_attr
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