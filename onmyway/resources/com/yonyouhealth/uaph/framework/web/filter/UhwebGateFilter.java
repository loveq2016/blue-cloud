package com.yonyouhealth.uaph.framework.web.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.mvc.UhwebDataSet;

public class UhwebGateFilter implements Filter {

	private static final String LOGIN_URL = "/uhweb?ctrl=LogonCTRL_init";

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String currentUrl = req.getRequestURI();
		HttpSession session = req.getSession();
		if (canFilter(req, res)) {
			if (session.getAttribute(ContextAPI.LOGIN_INFO) == null) {
				req.getRequestDispatcher(getErrorpagePath()).forward(request, response);
				return;
			}
		};
		filterChain.doFilter(request, response);
	}

	private String getErrorpagePath() {
		return LOGIN_URL;
	}

	private boolean canFilter(HttpServletRequest req, HttpServletResponse res) {
		Map localMap = req.getParameterMap();
		String currentUrl = req.getRequestURI();
		if (currentUrl.endsWith("/ajax.uhweb")) {
			return canFilter(new UhwebDataSet(req));
		} else if (currentUrl.endsWith("/uhweb")) {
			if (localMap != null && localMap.get("ctrl") != null) {
				UhwebDataSet dataset = new UhwebDataSet(buildJson(localMap));
				return canFilter(dataset);
			}
		}
		return currentUrl.indexOf("/uhweb?ctrl=LogonCTRL_logout") == -1 && currentUrl.indexOf("/uhweb?ctrl=LogonCTRL_logon") == -1 
			&& currentUrl.indexOf("/uhweb?ctrl=LogonCTRL_init") == -1;
	}

	private boolean canFilter(UhwebDataSet dataset) {
		String[] arrLogin = new String[] {"LogonCTRL_logon", "LogonCTRL_logout", "LogonCTRL_init"};
		//by liumin, 2012.9.6 健康卡单点登录需求  在url参数里增加&logon=cs 用于避开登陆权限
		Object logon = dataset.getReqDataObject().getViewData().get("logon");
		if (logon != null && logon.equals("cs")) return false;
		
		Object ctrl = dataset.getReqDataObject().getViewData().get("ctrl");
		if (ctrl != null) {
			for (String strCtrl: arrLogin) {
				if (strCtrl.equals(ctrl)) 
					return false;
			}
		}
		return true;
	}

	private String buildJson(Map<String, String[]> paramMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		Iterator iter = paramMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			sb.append("\"");
			sb.append((String) entry.getKey()).append("\"");
			sb.append(":").append("\"").append(((String[]) entry.getValue())[0]).append("\"");
			if (iter.hasNext())
				sb.append(",");
		}
		sb.append("}");
		return sb.toString();
	}
	
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
