package com.cz.flume.demoFlume.tools;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Ajax返回结果
 *
 * @author Haibo Qing
 */
@ApiModel(description = "请求结果")
public class AjaxResult<T> {

    /**
     * 返回状态码
     */
    @ApiModelProperty("是否成功")
    private boolean success = true;

    /**
     * 返回的中文消息
     */
    @ApiModelProperty(value = "结果消息", hidden = true)
    private String message;

    /**
     * 成功时携带的数据
     */
    @ApiModelProperty("成功时为 结果数据，失败时为 异常消息")
    private T data;

    public AjaxResult() {
        this.success = true;
    }

    public AjaxResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

	public String getMessage() {
        return message;
	}

	public AjaxResult setMessage(String message) {
		this.message = message;
		return this;
	}

    public T getData() {
        return data;
    }

    public AjaxResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public AjaxResult addSuccess(String message) {
        this.message = message;
        this.success = true;
        this.data = null;
        return this;
    }

    public AjaxResult addError(String message) {
        this.message = message;
        this.success = false;
        this.data = null;
        return this;
    }

    public AjaxResult addFail(String message) {
        this.message = message;
        this.success = false;
        this.data = null;
        return this;
    }

    public AjaxResult addWarn(String message) {
        this.message = message;
        this.success = false;
        this.data = null;
        return this;
    }

    public AjaxResult<T> success(T data) {
        this.message = "success";
        this.data = data;
        this.success = true;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return JsonKit.toJson(this);
    }
}
