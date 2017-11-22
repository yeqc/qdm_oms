package com.work.shop.vo;

import java.util.List;

public class Level4AreaVO {

    private boolean success;

    private List<List<String>> result;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<List<String>> getResult() {
		return result;
	}

	public void setResult(List<List<String>> result) {
		this.result = result;
	}
}