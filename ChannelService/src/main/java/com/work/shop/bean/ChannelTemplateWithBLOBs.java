package com.work.shop.bean;

public class ChannelTemplateWithBLOBs extends ChannelTemplate {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template.content
     *
     * @mbggenerated
     */
    private String content;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column channel_template.edit_template_content
     *
     * @mbggenerated
     */
    private String editTemplateContent;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template.content
     *
     * @return the value of channel_template.content
     *
     * @mbggenerated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template.content
     *
     * @param content the value for channel_template.content
     *
     * @mbggenerated
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column channel_template.edit_template_content
     *
     * @return the value of channel_template.edit_template_content
     *
     * @mbggenerated
     */
    public String getEditTemplateContent() {
        return editTemplateContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column channel_template.edit_template_content
     *
     * @param editTemplateContent the value for channel_template.edit_template_content
     *
     * @mbggenerated
     */
    public void setEditTemplateContent(String editTemplateContent) {
        this.editTemplateContent = editTemplateContent == null ? null : editTemplateContent.trim();
    }
}