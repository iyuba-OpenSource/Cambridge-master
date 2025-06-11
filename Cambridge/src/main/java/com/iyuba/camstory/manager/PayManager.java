package com.iyuba.camstory.manager;

import java.util.Date;

import com.android.volley.Request;
import com.iyuba.camstory.frame.CrashApplication;
import com.iyuba.camstory.listener.CheckAmountRequest;
import com.iyuba.camstory.listener.OnResultListener;
import com.iyuba.camstory.listener.PayRequest;
import com.iyuba.camstory.listener.RequestCallBack;

import android.content.Context;

/**
 * 爱语币管理
 * 
 * @author chentong
 * 
 */
public class PayManager {
	private static PayManager instance;
	private int pay_amount;
	private Date lasttime;

	public static PayManager getInstance(Context mContext) {
		if (instance == null) {
			instance = new PayManager(mContext);
		}
		return instance;
	}

	private PayManager(Context mContext) {
	}

	/**
	 * 
	 * @param userId
	 * @param resultListener
	 *          功能：查询用户余额
	 */
	public void checkAmount(final int userId,
			final OnResultListener resultListener) {
		CheckAmountRequest checkAmountRequest = new CheckAmountRequest(userId,
				new RequestCallBack() {

					@Override
					public void requestResult(Request result) {
						CheckAmountRequest checkAmountResponse = (CheckAmountRequest) result;
						if (checkAmountResponse.isCheckSuccess())// 查询失败，提示错误信息
						{
							resultListener.OnFailureListener(checkAmountResponse.msg);
						} else {// 查询成功，显示余额
							// 刷新余额提示
							resultListener.OnSuccessListener(checkAmountResponse.amount);
						}
					}
				});
		CrashApplication.getInstance().getQueue().add(checkAmountRequest);
	}

	/**
	 * 
	 * @param userId
	 *          AccountManager.Instance(mContext).userId
	 * @param amount
	 *          10
	 * @param year
	 * @param resultListener
	 *          功能：
	 */
	public void payAmount(final Context context, final int userId,
			final String amount, final String month,
			final OnResultListener resultListener) {
		PayRequest payRequest = new PayRequest(userId, amount, month,
				new RequestCallBack() {

					@Override
					public void requestResult(Request result) {
						PayRequest payResponse = (PayRequest) result;
						if (payResponse.isPaySuccess()) {
							pay_amount = Integer.parseInt(amount);
							AccountManager.getInstance().VipRefresh(context, null);
							resultListener.OnSuccessListener(payResponse.amount);
						} else {// 支付失败
							if (Integer.parseInt(payResponse.amount) < Integer
									.parseInt(amount))// 余额不足
							{// 提示用户余额不足，并跳转到充值页面
								resultListener.OnFailureListener(payResponse.amount);
							} else {
								resultListener.OnFailureListener(payResponse.msg);
							}

						}
					}
				});
		CrashApplication.getInstance().getQueue().add(payRequest);
	}
}
