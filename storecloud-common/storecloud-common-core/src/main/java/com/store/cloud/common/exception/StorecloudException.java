package com.store.cloud.common.exception;

import com.store.cloud.common.response.ResponseEnum;

/**
 * @author FrozenWatermelon
 * @date 2020/7/11
 */
public class StorecloudException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Object object;

	private ResponseEnum responseEnum;

	public StorecloudException(String msg) {
		super(msg);
	}

	public StorecloudException(String msg, Object object) {
		super(msg);
		this.object = object;
	}

	public StorecloudException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public StorecloudException(ResponseEnum responseEnum) {
		super(responseEnum.getMsg());
		this.responseEnum = responseEnum;
	}

	public StorecloudException(ResponseEnum responseEnum, Object object) {
		super(responseEnum.getMsg());
		this.responseEnum = responseEnum;
		this.object = object;
	}


	public Object getObject() {
		return object;
	}

	public ResponseEnum getResponseEnum() {
		return responseEnum;
	}

}
