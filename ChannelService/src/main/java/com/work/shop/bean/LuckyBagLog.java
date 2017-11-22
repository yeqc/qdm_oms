package com.work.shop.bean;

import java.util.Date;

public class LuckyBagLog {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_log.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_log.oper_time
     *
     * @mbggenerated
     */
    private Date operTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_log.operator
     *
     * @mbggenerated
     */
    private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_log.operate_table
     *
     * @mbggenerated
     */
    private String operateTable;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_log.operate_type
     *
     * @mbggenerated
     */
    private Integer operateType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column lucky_bag_log.operation
     *
     * @mbggenerated
     */
    private String operation;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_log.id
     *
     * @return the value of lucky_bag_log.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_log.id
     *
     * @param id the value for lucky_bag_log.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_log.oper_time
     *
     * @return the value of lucky_bag_log.oper_time
     *
     * @mbggenerated
     */
    public Date getOperTime() {
        return operTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_log.oper_time
     *
     * @param operTime the value for lucky_bag_log.oper_time
     *
     * @mbggenerated
     */
    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_log.operator
     *
     * @return the value of lucky_bag_log.operator
     *
     * @mbggenerated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_log.operator
     *
     * @param operator the value for lucky_bag_log.operator
     *
     * @mbggenerated
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_log.operate_table
     *
     * @return the value of lucky_bag_log.operate_table
     *
     * @mbggenerated
     */
    public String getOperateTable() {
        return operateTable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_log.operate_table
     *
     * @param operateTable the value for lucky_bag_log.operate_table
     *
     * @mbggenerated
     */
    public void setOperateTable(String operateTable) {
        this.operateTable = operateTable == null ? null : operateTable.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_log.operate_type
     *
     * @return the value of lucky_bag_log.operate_type
     *
     * @mbggenerated
     */
    public Integer getOperateType() {
        return operateType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_log.operate_type
     *
     * @param operateType the value for lucky_bag_log.operate_type
     *
     * @mbggenerated
     */
    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column lucky_bag_log.operation
     *
     * @return the value of lucky_bag_log.operation
     *
     * @mbggenerated
     */
    public String getOperation() {
        return operation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column lucky_bag_log.operation
     *
     * @param operation the value for lucky_bag_log.operation
     *
     * @mbggenerated
     */
    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }
}