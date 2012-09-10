package com.yonyouhealth.uaph.framework.org.bizsys.web.security.logon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.rbac.function.IFuncPower;
import nc.itf.uap.sf.ISMVerifyService;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.RefPubUtil;
import nc.ui.sm.funcreg.FunRegisterConst;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;
import nc.vo.sm.config.ConfigParameter;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.login.IControlConstant;
import nc.vo.sm.login.LoginFailureInfo;
import nc.vo.sm.login.LoginSessBean;

import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.controller.BaseDomainCtrl;
import com.yonyouhealth.uaph.framework.web.controller.annotations.CTRL;
import com.yonyouhealth.uaph.framework.web.event.IReqData;
import com.yonyouhealth.uaph.framework.web.event.IResData;
import com.yonyouhealth.uaph.framework.web.event.UhwebRes;

/**
 * 框架控制类
 * 
 * @author 刘敏
 * 
 */
@CTRL("LogonCTRL")
public class LogonCTRL extends BaseDomainCtrl {
	private static final String LOGIN_STATE = "state";

	public LogonCTRL() {
	}

	/**
	 * 用户登录
	 * 
	 * @param ireqdata
	 * @return
	 * @throws BusinessException
	 */
	public IResData logon(IReqData ireqdata) throws BusinessException {
		LogonUser logonUser = (LogonUser) ireqdata.getForm("form", LogonUser.class);

		// 取第一个帐套
		ConfigParameter configPara = SFServiceFacility.getConfigService().getAccountConfigPara();
		Account[] accounts = configPara.getAryAccounts();
		String accountCode = null;
		String dsName = null;
		for (Account acc : accounts) {
			if (acc.getDataSourceName().equalsIgnoreCase(IControlConstant.SYS_ADM_DATASOURCE) || acc.getAccountCode().equalsIgnoreCase("design"))
				continue;
			accountCode = acc.getAccountCode();
			dsName = acc.getDataSourceName();
			break;
		}

		if (accountCode == null)
			throw new RuntimeException("没有找到帐套！");
		// String pk_corp = logonUser.getPk_corp();
		// String corpcode = logonUser.getCorpCode();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String workDate = sdf.format(new Date());
		String language = "simpchn";
		String userCode = logonUser.getUserCode();
		String pwd = logonUser.getPassword();

		IUserManageQuery userManage = NCLocator.getInstance().lookup(IUserManageQuery.class);
		UserVO userVo = null;
		try {
			userVo = userManage.findUserByCode(userCode, dsName);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}

		IResData res = new UhwebRes();

		if (userVo == null) {
			res.addAttr(LOGIN_STATE, false);
			res.addAttr("msg", "用户不存在！");
			return res;
		}

		String pk_corp = userVo.getCorpId();

		ISMVerifyService loginService = NCLocator.getInstance().lookup(ISMVerifyService.class);

		LoginSessBean lsb = new LoginSessBean();
		lsb.setAccountId(accountCode);
		lsb.setDataSourceName(dsName);
		lsb.setPk_corp(pk_corp);
		// lsb.setCorpCode(corpcode);
		lsb.setWorkDate(workDate);
		lsb.setLanguage(language);
		lsb.setUserCode(userCode);
		lsb.setPassword(pwd);

		lsb.setForcedLogin(true);
		try {
			Object[] loginResult = loginService.login(lsb);
			int resultCode = ((Integer) loginResult[0]).intValue();

			if (resultCode == LoginFailureInfo.LOGIN_SUCCESS) {
				// 登录成功
				res.addAttr(LOGIN_STATE, true);
				// 注册登录信息
				lsb = (LoginSessBean) loginResult[1];
				ContextAPI.registerLoginInfo(lsb);
			} else {
				// 登录失败
				res.addAttr(LOGIN_STATE, false);
				res.addAttr("msg", "登录失败！");
			}

		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		return res;
	}

	/**
	 * 主页面
	 * 
	 * @param ireqdata
	 * @return
	 */
	public IResData main(IReqData ireqdata) {
		UhwebRes res = new UhwebRes();
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		if (lsb != null) {
			res.addAttr("yhmc", lsb.getUserName());
			res.addAttr("jgmc", lsb.getCorpName());
			res.addAttr("yhid", lsb.getUserCode());
			res.addAttr("jgid", lsb.getPk_crop());
		}
		String s = (String) ireqdata.getAttr("mainPage");
		if (s != null && !"".equals(s))
			res.addPage(s);
		else
			res.addPage("orgpage/orgframe.html");

		return res;
	}

	/**
	 * 获取用户功能权限
	 * 
	 * @param ireqdata
	 * @return
	 */
	public IResData getYhFuncRight(IReqData ireqdata) {
		IResData res = new UhwebRes();
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		String workDate = lsb.getWorkDate();
		res.addTree("tree", queryLoginFunc(workDate.substring(0, 4), workDate.substring(5, 7), lsb.getUserId(), lsb.getPk_crop()));
		return res;
	}

	/**
	 * 退出登录
	 * 
	 * @param ireqdata
	 * @return
	 */
	public IResData logout(IReqData ireqdata) {
		IResData res = getRes();
		ContextAPI.removeLoginInfo();

		return res;
	}

	/**
	 * 查询登录权限
	 * 
	 */
	private List queryLoginFunc(String loginYear, String loginMonth, String userid, String corp) {
		List res = new ArrayList();
		IFuncPower funcPower = NCLocator.getInstance().lookup(IFuncPower.class);
		try {
			FuncRegisterVO[] funcVos = funcPower.queryCorpLoginFunc(loginYear, loginMonth, userid, corp, null, FunRegisterConst.LFW_FUNC_NODE);
			if (funcVos == null) {
				return res;
			}
			for (FuncRegisterVO vo : funcVos) {
				Map func = new HashMap();
				func.put("code", vo.getDispCode());
				if (vo.getDispCode().length() > 2)
					func.put("pcode", vo.getDispCode().substring(0, vo.getDispCode().length() - 2));
				func.put("caption", vo.getFunName());
				func.put("path", vo.getClassName());
				res.add(func);
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		return res;
	}

	/**
	 * 登录主页面
	 * 
	 * @param req
	 * @return
	 */
	public IResData init(IReqData req) {
		IResData res = getRes();

		try {
			ConfigParameter configPara = SFServiceFacility.getConfigService().getAccountConfigPara();
			Account[] accounts = configPara.getAryAccounts();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (Account acc : accounts) {
				if (acc.getDataSourceName().equalsIgnoreCase(IControlConstant.SYS_ADM_DATASOURCE) || acc.getAccountCode().equalsIgnoreCase("design"))
					continue;
				Map map = new HashMap<String, Object>();
				map.put("code", acc.getAccountCode());
				map.put("caption", acc.getAccountName());
				map.put("ds", acc.getDataSourceName());
				list.add(map);
			}
			res.addSelectWithWidgetName("account", list, new String[] { "asdf,sd", "fsd,f" });
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}

		// res.addTree("corp", getCorpInfo());

		res.addPage("syslogon.html");

		return res;
	}
	/*
	 * private List<Map<String, Object>> getCorpInfo() { List<Map<String,
	 * Object>> res = new ArrayList<Map<String,Object>>(); AbstractRefModel
	 * corpModel = RefPubUtil.getRefModel("公司目录"); Vector corpData =
	 * corpModel.getData(); for (int i = 0; i < corpData.size(); i++) { Vector
	 * corp = (Vector) corpData.get(i); Map<String, Object> mapCorp = new
	 * HashMap<String, Object>(); mapCorp.put("code", corp.get(2));
	 * mapCorp.put("pcode", corp.get(3)); mapCorp.put("caption", corp.get(1));
	 * mapCorp.put("unitcode", corp.get(0)); res.add(mapCorp); } return res; }
	 */
}
