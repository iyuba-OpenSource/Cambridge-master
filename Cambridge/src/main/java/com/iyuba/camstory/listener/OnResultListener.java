package com.iyuba.camstory.listener;

/**
 * 
 * @author zqq
 * 
 *         功能：协议结果监听
 * 
 */
public interface OnResultListener {
	void OnSuccessListener(String msg);// 操作成功
	void OnFailureListener(String msg);// 操作失败
}
