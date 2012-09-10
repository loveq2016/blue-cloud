/**
 * 用友医疗提供的系统中包含的任何文本、图片、图形、音频和/或视频资料均受版权、 
 * 商标和/或其它财产所有权法律的保护，未经相关权利人同意，上述资料均不得在任
 * 何地方直接或间接发布、播放、出于播放或发布目的而改写或再发行，或者被用于
 * 其他任何商业目的。所有这些资料或资料的任何部分仅可作为私人和非商业用途而
 * 保存在某台计算 机内。用友医疗不就由上述资料产生或在传送或递交全部或部分上
 * 述资料过程中产生的延误、不准确、错误和遗漏或从中产生或由此产生的任何损害
 * 赔偿，以任何形式，向用户或任何第三方负责。<p>
 * 
 * 用友医疗为提供服务而使用的任何软件（包括但不限于软件中所含的任何文字、照
 * 片、动画、 录像、录音、音乐、图象和附加程序、随附的帮助材料）的一切权利均
 * 属于该软件的著作权人，未经该软件的著作权人许可，用户不得对该软件进行反向
 * 工程、反向编译或反汇编。<p>
 * 
 * Copyright (C) 2012 用友医疗卫生信息系统有限公司，版权所有，翻版必究。
 */

package uh.uhweb.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.rbac.IPowerManage;
import nc.itf.uap.rbac.IPowerManageQuery;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.sf.IFuncRegisterQueryService;
import nc.vo.pub.BusinessException;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.login.LoginSessBean;
import nc.vo.uap.rbac.IOrgType;
import nc.vo.uap.rbac.PowerResourceVO;
import nc.vo.uap.rbac.PowerVO;
import nc.vo.uap.rbac.RoleVO;
import nc.vo.uap.rbac.power.RolePowerChangeVO;
import nc.vo.uap.rbac.power.RolePowerQueryVO;

import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.controller.BaseDomainCtrl;
import com.yonyouhealth.uaph.framework.web.controller.annotations.CTRL;
import com.yonyouhealth.uaph.framework.web.event.IReqData;
import com.yonyouhealth.uaph.framework.web.event.IResData;

/**
 * 权限分配控制类
 * 
 * @author 刘敏
 *
 */
@CTRL("PowerManageCtrl")
public class PowerManageCtrl extends BaseDomainCtrl {
	
	/**
	 * 默认加载页面，显示公司的所有角色以及功能节点
	 * 
	 * @param req
	 * @return
	 */
	public IResData main(IReqData req) {
		IResData res = getRes();
		IRoleManageQuery rolequery = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);

		try {
			/* 获取某公司的所有角色*/
			RoleVO[] rv = rolequery.getCorpRoles(getCorpPk());
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map;
			for (int i = 0; i < rv.length; i++) {
				map = new HashMap<String, Object>();
				map.put("pcode", 0);
				map.put("code", rv[i].getPk_role());
				map.put("caption", rv[i].getRole_name());
				map.put("pk_role", rv[i].getPk_role());
				list.add(map);
			}
			res.addTree("roleGrid", list);

			// 获取公司的所有的功能权限节点,并以树形结构显示
			res.addTree("treefuncregist", getTreeData(null));
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		res.addPage("pages/uhweb/PowerManage/powerManage.jsp");
		return res;
	}
	
	/**
	 * 根据所选的角色，获取其默认已有的功能权限
	 * @param req
	 * @return
	 */
	public IResData showRolePower(IReqData req) 
	{
		IResData res = getRes();
		String corppk=getCorpPk();
		/*获取角色对应的功能权限，并在树上显示*/
		if (req.getAttr("pk") != null) 
		{
			String rolepk=(String)req.getAttr("pk") ;
			PowerVO []pv=getRolePower(rolepk,corppk);	
			String pvId=null;
			if(pv!=null)
			{
				int i;
				pvId=(pv[0].getResource_data_id()+",");
				for(i=1;i<pv.length-1;i++)
					pvId+=pv[i].getResource_data_id()+",";
				pvId+=pv[i].getResource_data_id();
			}
			else
			{
				pvId="0"; //为空时，以"0"表示
			}
			res.addAttr("pv", pvId);
			
		}
		return res;
	}
	
	/**
	 * 保存修改的权限数据
	 * 
	 * @param req
	 * @return
	 */
	public IResData savePowerRoleData(IReqData req) {
		
		PowerVO[] newpv = null;
		IResData res = getRes();
		//ResourceContext rcontext = new ResourceContext();
		/*判断是否选中角色*/
		if (req.getAttr("pk") == null) return res;
		else{
			String str = (String) req.getAttr("checkedNodes");
			String pk_role = (String) req.getAttr("pk");
			String corppk=getCorpPk();
			String[] strArray = null;
			if(str.length()!=0)
			{
				strArray = str.split(",");
				List<PowerVO> newPowerVOList = new ArrayList<PowerVO>();
				for (int i = 0; i < strArray.length; i++) {
					PowerVO pv = new PowerVO();
					pv.setResource_data_id(strArray[i]);
					pv.setPk_corp(corppk);
					pv.setPk_org(corppk);
					pv.setPk_role(pk_role);
					pv.setResource_id(1);
					newPowerVOList.add(pv);
			    }
				newpv = newPowerVOList.toArray(new PowerVO[] {});
			}
			/*与原有的权限比较，是否发生改变，若未改变，则提示并返回*/
			PowerVO []pv=getRolePower(pk_role,corppk);
			if(pv==null && newpv==null) 
			{
				res.addMessage("权限没有发生改变");
				return res;
			}
			else if (pv!=null&& newpv!=null)
			{
			if (newpv.length == pv.length) {
				if (getCheckedPower(newpv, pv).equals("true")) {
					res.addMessage("权限没有发生改变");
					return res;
				}
			}
			}
			/*保存新分配的权限*/
			RoleVO rvo = new RoleVO();
			rvo.setPk_corp(corppk);
			rvo.setPk_role(pk_role);
			
			PowerResourceVO prvo = new PowerResourceVO();
			prvo.setResource_id(1);


//			PowerResourceVO prvo = PowerResourceBufferClass.getInstance()
//					.getVoByResourceid(1);

		//	rcontext.setPk_corp(corppk);
		//	rcontext.setRole(rvo);
		//	rcontext.setResource(prvo);
		//	rcontext.setRoleIdentity(1);
			
			//构造changevo
			RolePowerChangeVO changeVo=new RolePowerChangeVO();
			changeVo.setCommonPower(false);
			changeVo.setResouceId(prvo.getResource_id().intValue());
			changeVo.setRolePK(rvo.getPk_role());
			changeVo.setCorpPK(corppk);
			changeVo.setOrgTypeCode(IOrgType.COMPANY_TYPE);
			changeVo.setOrgPK(corppk);
			
			HashSet originalset = new HashSet();
			if (pv != null) {
				for (int i = 0; i < pv.length; i++) {
					originalset.add(pv[i].getResource_data_id());
				}
			}
			ArrayList saveList = new ArrayList();
			ArrayList deleteList = new ArrayList();
			if (newpv != null && newpv.length > 0) {
				for (int i = 0; i < newpv.length; i++) {
					if (!originalset.contains(newpv[i].getResource_data_id())) {
						PowerVO vo = new PowerVO();
						vo.setResource_data_id(newpv[i].getResource_data_id());
						saveList.add(vo);
					} else
						originalset.remove(newpv[i].getResource_data_id());
				}

			}
			changeVo.setAddPower((PowerVO[]) saveList.toArray(new PowerVO[0]));
			for (Iterator iter = originalset.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
				PowerVO vo = new PowerVO();
				vo.setResource_data_id(element);
				deleteList.add(vo);
			}
			changeVo.setDelPower((PowerVO[]) deleteList.toArray(new PowerVO[0]));
			
			try {
			//	PowerDataServiceFactory.getDefaultPowerDataService(rcontext)
			 //			.saveRolePower(pv, newpv, rcontext);
				((IPowerManage) NCLocator.getInstance().lookup(IPowerManage.class
						.getName())).saveRolePower(new RolePowerChangeVO[] {changeVo});
				res.addMessage("保存成功");
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
		return res;
	}
	
	/**
	 * 分配时，若未选中角色，则弹出提示信息
	 * 
	 * @param req
	 * @return
	 */
	public IResData showFenPeiMessage(IReqData req)
	{
		IResData res=getRes();
		res.addMessage("请选择角色");
		return res;
	}
	
	/**
	 * 根据角色获取PowerVO
	 * 
	 * @param rolepk
	 * @param corppk
	 * @return
	 */
	PowerVO [] getRolePower(String rolepk,String corppk) {
		PowerVO []pv=null;
		try {
				RolePowerQueryVO[] rpv = new RolePowerQueryVO[1];
				rpv[0] = new RolePowerQueryVO();
				rpv[0].setRolePK(rolepk);
				rpv[0].setCorpPK(corppk);
				rpv[0].setOrgPK(corppk);
				rpv[0].setResouceId(1);
				IPowerManageQuery powerquery = NCLocator.getInstance().lookup(
						IPowerManageQuery.class);

				pv = powerquery.getRolePower(rpv);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return pv;
	}
	
	/**
	 * 根据登录信息，获取公司主键
	 * 
	 *  @return
	 */
	String getCorpPk()
	{
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		String corppk = lsb.getPk_crop();
		return corppk;
	}
	
	/**
	 * 获取公司的所有的功能权限节点,并以树形结构显示
	 * 
	 * @param pv
	 * @return
	 */
	private List<Map<String, Object>> getTreeData(PowerVO[] pv)
	{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map ;
		IFuncRegisterQueryService funcqueryser=NCLocator.getInstance().lookup(IFuncRegisterQueryService.class);
	    try {
	    	//根据公司PK获取FuncRegisterVO
			FuncRegisterVO[] frv= funcqueryser.queryCompanyFuncNodeByOrgType(getCorpPk(), "1");
			//将已有的PowerVO数组放入HashMap中
			Map<String, String> mapFrv = new HashMap<String, String>(frv.length);
			if(pv!=null)
			{
				for(PowerVO pvVo: pv) {
					mapFrv.put(pvVo.getResource_data_id(), null);
				}
			}
		   if(frv!=null)
		   {
			for(int i=0;i<frv.length;i++)	
			{
			map = new HashMap<String, Object>();	
			if (frv[i].getFunCode().length() > 2) 
				map.put("pcode", frv[i].getFunCode().substring(0, frv[i].getFunCode().length() - 2));
			map.put("code", frv[i].getFunCode());
			map.put("caption", frv[i].getFunName());
			//判断是否拥有该功能权限
	    	map.put("isChecked", mapFrv.containsKey(frv[i].getFunID()));	//"true", "false"
	    	map.put("funid", frv[i].getFunID());
			list.add(map);
			}
		   }
	} catch (BusinessException e) {
		Logger.error(e.getMessage(), e);
		throw new RuntimeException(e);
	}
		return list;
	}
	
	/**
	 * 返回树的getCheckedPower方法的"true""false",已确定权限是否发生改变
	 * 
	 * @param newpv
	 * @param pv
	 * @return
	 */
	String getCheckedPower(PowerVO []newpv,PowerVO [] pv)
	{
		String checked = "true";
		Map<String, String> mapNewpv = new HashMap<String, String>(newpv.length);
		if (newpv != null) {
			for (PowerVO pvVo : newpv) {
				mapNewpv.put(pvVo.getResource_data_id(), null);
			}
		}
		for (int i = 0; i < pv.length; i++) {
			if (!mapNewpv.containsKey(pv[i].getResource_data_id())) {
				checked = "false";
			}
		}
		return checked;
	}
	
}


