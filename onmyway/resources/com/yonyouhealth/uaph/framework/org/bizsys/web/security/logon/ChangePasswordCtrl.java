package com.yonyouhealth.uaph.framework.org.bizsys.web.security.logon;

import nc.bs.framework.common.NCLocator;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.sm.login.LoginSessBean;

import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.controller.BaseDomainCtrl;
import com.yonyouhealth.uaph.framework.web.controller.annotations.CTRL;
import com.yonyouhealth.uaph.framework.web.event.IReqData;
import com.yonyouhealth.uaph.framework.web.event.IResData;

/**
 * 修改密码控制类
 * 
 * @author 刘敏
 *
 */
@CTRL("ChangePasswordCtrl")
public class ChangePasswordCtrl extends BaseDomainCtrl {
	/**
	 * 修改密码主页
	 * 
	 * @param req
	 * @return
	 */
	public IResData main(IReqData req) {
		IResData res = getRes();
		
		res.addPage("orgpage/changePassword.html");
		return res;
	}

	/**
	 * 修改密码
	 * 
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	public IResData save(IReqData req) throws Exception {
		IResData res = getRes();
		
		String oldMm = (String) req.getAttr("oldMm");
		String newMm = (String) req.getAttr("newMm");
		
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		if (!oldMm.equals(lsb.getPassword())) {
			res.addMessage("原始密码输入错误!");
			res.addAttr("state", "false");
		} else {
			changePasswd(lsb, newMm);
			res.addAttr("state", "true");
		}
		
		return res;
	}

	private void changePasswd(LoginSessBean lsb, String newMm) throws Exception {
		IUserManageQuery query = SFServiceFacility.getIUserManageQuery();
		try {
			UserVO userVo = query.findUserByCode(lsb.getUserCode(), lsb.getDataSourceName());
			Encode encode = new Encode();
			userVo.setUserPassword(encode.encode(newMm));
			SFServiceFacility.getIUserManage().updateUser(userVo);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
}
