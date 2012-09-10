package com.yonyouhealth.uaph.framework.web.context;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nc.vo.sm.login.LoginSessBean;

import com.yonyouhealth.uaph.framework.comm.pool.ThreadLocalManager;
import com.yonyouhealth.uaph.framework.web.mvc.UhwebDataSet;

public class ContextAPI {
	public static final String LOGIN_INFO = "login_info";

	public static HttpServletRequest getReq() {
		return (HttpServletRequest) ThreadLocalManager.get("httpReq");
	}

	public static HttpServletResponse getRes() {
		return (HttpServletResponse) ThreadLocalManager.get("httpRes");
	}

	public static ServletConfig getServletConfig() {
		return (ServletConfig) ThreadLocalManager.get("servletConfig");
	}

	public static UhwebDataSet getReqDataSet() {
		return (UhwebDataSet) ThreadLocalManager.get("reqDataSet");
	}

	public static void goProcess() throws Exception {
		// todo zzj：
//		 ViewDataHelper.dealViewData(getReqDataSet());
	}

	public static UhwebDataSet getResDataSet() {
		UhwebDataSet localUhwebDataSet = (UhwebDataSet) ThreadLocalManager.get("resDataSet");
		if (localUhwebDataSet == null) {
			localUhwebDataSet = new UhwebDataSet();
			ThreadLocalManager.add("resDataSet", localUhwebDataSet);
		}
		return localUhwebDataSet;
	}
	
	/**
	 * 获取登录信息
	 * 
	 * @return
	 */
	public static LoginSessBean getLoginInfo() {
		return (LoginSessBean) getReq().getSession().getAttribute(LOGIN_INFO);
	}
	
	/**
	 * 在Session中注册登录信息
	 * 
	 * @param lsb
	 */
	public static void registerLoginInfo(LoginSessBean lsb) {
    	HttpServletRequest req = getReq();
    	
    	if (req == null) return;

    	HttpSession session = req.getSession();
    	if (session == null) return;
    	session.setAttribute(LOGIN_INFO, lsb);
	}
	
    /**
     * 移除登录信息,使session失效
     * 
     */
    public static void removeLoginInfo() {
    	HttpServletRequest req = getReq();
    	if (req == null) return;
    	HttpSession session = req.getSession();
    	if (session == null) return;
    	session.invalidate();
    }
	
}