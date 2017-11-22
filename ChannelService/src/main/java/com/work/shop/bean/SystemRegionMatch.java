package com.work.shop.bean;

public class SystemRegionMatch {
	
	private Integer id;
	
	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_region_mapper.old_region_id
     *
     * @mbggenerated
     */
    private String oldRegionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_region_mapper.old_region_name
     *
     * @mbggenerated
     */
    private String oldRegionName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_region_mapper.old_region_type
     *
     * @mbggenerated
     */
    private String oldRegionType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_region_mapper.new_area_id
     *
     * @mbggenerated
     */
    private String newAreaId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_region_mapper.new_area_name
     *
     * @mbggenerated
     */
    private String newAreaName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_region_mapper.match_type
     *
     * @mbggenerated
     */
    private String matchType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_region_mapper.parea_code
     *
     * @mbggenerated
     */
    private String pareaCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column system_region_mapper.relate_os_area
     *
     * @mbggenerated
     */
    private String relateOsArea;
    
    private String parentName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOldRegionId() {
		return oldRegionId;
	}

	public void setOldRegionId(String oldRegionId) {
		this.oldRegionId = oldRegionId;
	}

	public String getOldRegionName() {
		return oldRegionName;
	}

	public void setOldRegionName(String oldRegionName) {
		this.oldRegionName = oldRegionName;
	}

	public String getOldRegionType() {
		return oldRegionType;
	}

	public void setOldRegionType(String oldRegionType) {
		this.oldRegionType = oldRegionType;
	}

	public String getNewAreaId() {
		return newAreaId;
	}

	public void setNewAreaId(String newAreaId) {
		this.newAreaId = newAreaId;
	}

	public String getNewAreaName() {
		return newAreaName;
	}

	public void setNewAreaName(String newAreaName) {
		this.newAreaName = newAreaName;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getPareaCode() {
		return pareaCode;
	}

	public void setPareaCode(String pareaCode) {
		this.pareaCode = pareaCode;
	}

	public String getRelateOsArea() {
		return relateOsArea;
	}

	public void setRelateOsArea(String relateOsArea) {
		this.relateOsArea = relateOsArea;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
    
}
