package uh.uhweb.ctrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.rbac.IUserManage;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.processor.BeanMappingListProcessor;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.rbac.RoleVO;
import nc.vo.uap.rbac.UserVoMappingMeta;
import nc.vo.uap.rbac.role.RoleAndAllocCorpsVO;

import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.controller.BaseDomainCtrl;
import com.yonyouhealth.uaph.framework.web.controller.annotations.CTRL;
import com.yonyouhealth.uaph.framework.web.event.IReqData;
import com.yonyouhealth.uaph.framework.web.event.IResData;
import com.yonyouhealth.uaph.framework.web.event.UhwebRes;

/**
 * 用户管理控制类
 * 
 * @author 刘敏
 * 
 * 
 * 
 */
@CTRL("UserManageCtrl")
public class UserManageCtrl extends BaseDomainCtrl {
	// private String corpPK = ContextAPI.getLoginInfo().getPk_crop();
	private String corpPK = null;

	public String getCorpPK() {
		return corpPK;
	}

	public void setCorpPK() {
		this.corpPK = ContextAPI.getLoginInfo().getPk_crop();
	}

	/**
	 * @author
	 * @param req
	 * @return 某个公司的所有客户以及角色
	 * 
	 */
	public IResData main(IReqData req) {
		IResData res = new UhwebRes();
		UserVO[] alluser = null;
		alluser = this.getAlluser();	
		res.addTable("users", Arrays.asList(alluser));
		res.addPage("pages/uhweb/UserManage/UserManageMain.html");
		return res;
	}

	/**
	 * 
	 * @author
	 * @return 某个公司的所有客户
	 * 
	 */
	public UserVO[] getAlluser() {
		UserVO[] alluser = null;
		IUserManageQuery userquery = (IUserManageQuery) SFServiceFacility
				.getUserQueryService();
		this.setCorpPK();
		try {
			alluser = userquery.getUserList(corpPK);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return alluser;
	}

	/**
	 * 
	 * @author
	 * @return 用户角色
	 * 
	 */
	public RoleVO[] queryRoles(String userPK) {
		RoleVO[] roles = null;
		IUserManageQuery userquery = (IUserManageQuery) SFServiceFacility
				.getUserQueryService();
		this.setCorpPK();
		try {
			roles = userquery.getUserRole(userPK, corpPK);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return roles;

	}

	/**
	 * 
	 * @author
	 * @return 异步显示角色
	 * 
	 */
	public IResData showRoles(IReqData req) {
		IResData res = new UhwebRes();
		if (req.getAttr("cuserid") == null) {
			res.addMessage("用户关键信息未取到");
			return res;
		}
		RoleVO[] roles = null;
		String userPK = null;
		userPK = (String) req.getAttr("cuserid");
		roles = this.queryRoles(userPK);
		res.addTable("role", Arrays.asList(roles));
		return res;
	}

	/**
	 * 获取Userid
	 * 
	 * @return userid
	 * 
	 */
	public String getUserId(String dsName, String userCode) {
		String userid = null;
		IUserManageQuery userquery = (IUserManageQuery) SFServiceFacility
				.getUserQueryService();
		try {
			userid = ((UserVO) userquery.findUserByCode(userCode, dsName))
					.getPrimaryKey();
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return userid;
	}

	/**
	 * 
	 * @return 修改结果
	 */
	public IResData editUser(IReqData req) {
		IResData res = new UhwebRes();
		if (req.getAttr("userCode") == null) {
			res.addMessage("用户的编码未取到");
			return res;
		}
		if (req.getAttr("username") == null) {
			res.addMessage("用户名称未取到");
			return res;
		}
		String dsName = null;
		String userCode = null;
		UserVO user = null;
		Encode encoder = new Encode();
		IUserManageQuery userquery = (IUserManageQuery) SFServiceFacility
				.getUserQueryService();
		userCode = (String) req.getAttr("userCode");
		dsName = (String) req.getAttr("username");
		try {
			user = (UserVO) userquery.findUserByCode(userCode, dsName);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setUserPassword(encoder.decode(user.getUserPassword()));
		res.addForm("UserEditForm", user);
		res.addPage("pages/uhweb/UserManage/UserManageUserEdit.jsp?");// 跳转到新增页面
		return res;
	}

	/**
	 * 
	 * @return 删除结果
	 */
	@SuppressWarnings("unchecked")
	public IResData deleteUser(IReqData req) {
		IResData res = new UhwebRes();
		IUserManage usermanage = SFServiceFacility.getIUserManage();
		// 取页面选中的被删除记录列表
		if(req.getDeleteTable("users")==null){
			res.addMessage("删除数据获取失败！");
			return res;
		}
		List<UserVO> delete = (List<UserVO>)req.getDeleteTable("users");
		if (null != delete && delete.size() > 0) {
			for (UserVO user : delete) {
				if (null != user) {
					try {
						usermanage.delUser(user.getPrimaryKey());
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						Logger.error(e.getMessage(), e);
						throw new RuntimeException(e);
					}
				}
			}
		}
		res.addMessage("删除成功！");
		return res;

	}

	/**
	 * 新增用户的页面跳转
	 * 
	 * @author
	 * 
	 */
	public IResData insert(IReqData req) {
		IResData res = new UhwebRes();
		res.addPage("pages/uhweb/UserManage/UserManageAdd.jsp"); // 跳转到新增页面
		return res;
	}

	/**
	 * 更新信息
	 * 
	 * */
	public IResData updateUser(IReqData req) {
		IResData res = new UhwebRes();
		Encode encoder = new Encode();
		UserVO user = (UserVO) req.getForm("UserEditForm");
		user.setUserPassword(encoder.encode(user.getUserPassword()));
		IUserManage userManage = SFServiceFacility.getIUserManage();
		if(user.getDisableDate().before(user.getAbleDate())){
			res.addMessage("您所选择的失效时间有误，请重新选择！");
			return res;
		}
		try {
			
			userManage.updateUser(user);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			
			res.addMessage("更新失败！");
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} // 进行更新操作
		res.addForm("UserAddForm", user);
		res.addMessage("更新成功！");
		return res;
	}

	/**
	 * 用戶添加
	 * 
	 * 
	 */
	public IResData saveUser(IReqData req) {
		IResData res = new UhwebRes();
		UserVO user = (UserVO) req.getForm("UserAddForm");
		this.setCorpPK();
		user.setCorpId(corpPK);
		Encode encoder = new Encode();
		UserVO oldUserVO = null;
		IUserManage userManage = SFServiceFacility.getIUserManage();
		IUserManageQuery userManageQuery = SFServiceFacility
				.getIUserManageQuery();
		try {
			oldUserVO = userManageQuery.findUserByCode(user.getUserCode(),
					user.getUserName());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		user.setUserPassword(encoder.encode(user.getUserPassword()));
		if (oldUserVO != null) {
			res.addMessage("您所添加的用户已经存在");
			return res;
		}
		if(user.getDisableDate().before(user.getAbleDate())){
			
			res.addMessage("您所选择的失效时间有误，请重新选择！");
			return res;
		}
		String id;
		try {
			id = userManage.addUser(user);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}// 进行添加操作
		user.setPrimaryKey(id);
		res.addMessage("新建成功！");
		res.addForm("UserAddForm", user);
		return res;
	}

	/**
	 * 委派角色时的跳转页面
	 * 
	 * @author
	 * 
	 */
	public IResData beforeRoleAdd(IReqData req) {
		IResData res = new UhwebRes();
		if (req.getAttr("userCode") == null) {
			res.addMessage("用户的编码未取到");
			return res;
		}
		if (req.getAttr("username") == null) {
			res.addMessage("用户名称未取到");
			return res;
		}
		IRoleManageQuery rolequery = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);
		String code = null;
		String userpk = null;
		RoleVO[] roles = null;
		RoleAndAllocCorpsVO[] corproles = null;
		RoleVO[] corprole = null;
		code = (String) req.getAttr("userCode");
		userpk = this.getUserId(ContextAPI.getLoginInfo().getDataSourceName(),
				code);
		// 某个客户的角色
		roles = this.queryRoles(userpk);
		try {
			// 获得某个公司的所有角色
			this.setCorpPK();
			corproles = rolequery.getRoles(corpPK, true).getOwnRoles();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		corprole = new RoleVO[corproles.length];
		for (int i = 0; i < corproles.length; i++) {
			corprole[i] = corproles[i].getRoleVO();
		}
		// 构建树
		List<Map<String, Object>> list = getRoleTree(roles, corprole, userpk);
		res.addTree("roleTree", list);
		res.addJSTL("userpk", userpk);
		res.addPage("pages/uhweb/UserManage/UserManageRoleAdd.jsp"); // 跳转到委派角色页面
		return res;
	}

	/**
	 * 判断是否是客户是否也有这个角色。
	 * 
	 * @param userrole
	 * @param corprole
	 * @return
	 */
	private String getChecked(RoleVO[] userrole, RoleVO corprole) {
		// TODO Auto-generated method stub
		String flag = "false";
		for (int i = 0; i < userrole.length; i++) {
			if (userrole[i].getRole_name().equals(corprole.getRole_name())) {
				flag = "true";
				break;
			}
		}
		return flag;
	}

	/**
	 * 获得角色树
	 * 
	 * @param roles
	 * @param corprole
	 * @return
	 */
	private List<Map<String, Object>> getRoleTree(RoleVO[] roles,
			RoleVO[] corprole, String userpk) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pcode", null);
		map.put("code", "0");
		map.put("caption", "角色");
		map.put("userpk", userpk);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("pcode", "0");
		map.put("code", "10");
		map.put("caption", "本公司角色");
		map.put("userpk", userpk);
		list.add(map);

		for (int i = 0; i < corprole.length; i++) {
			map = new HashMap<String, Object>();
			map.put("pcode", "10");
			map.put("code", corprole[i].getRole_code());
			map.put("caption", corprole[i].getRole_name());
			map.put("isChecked", getChecked(roles, corprole[i]));
			map.put("noteid", corprole[i].getRole_code());
			list.add(map);
		}
		return list;
	}

	/**
	 * updateUserRoles(String userPK, RoleVO[] rolesAdd, RoleVO[] rolesDel)
	 * 委派角色的处理
	 * 
	 * @param req
	 * @return
	 */
	public IResData dealRole(IReqData req) {
		IResData res = new UhwebRes();
		String noteid = null;// 获得前台rolecode拼接的字符串
		RoleVO[] newroles = null;
		RoleVO[] oldroles = null;
		RoleVO[] deleteroles = null;
		String userpk = null;// 用户主键
		IUserManage manage = SFServiceFacility.getIUserManage();
		IUserManageQuery query = (IUserManageQuery) SFServiceFacility
				.getUserQueryService();
		@SuppressWarnings("unused")
		IRoleManageQuery rolequery = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);
		this.setCorpPK();
		if (req.getAttr("pk") == null) {
			res.addMessage("未取到用戶主鍵");
			return res;
		}
		// 获取旧角色
		userpk = (String) req.getAttr("pk");
		try {
			oldroles = query.getUserRole(userpk, corpPK);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//
		if (req.getAttr("checked") == null) {
			res.addMessage("未取到選中的項");
			return res;
		}
		noteid = (String) req.getAttr("checked");
		noteid = noteid + ",";
		// 添加新角色
		newroles = getRoles(oldroles, noteid);
		// 获得要删除的roles
		deleteroles = getDeleteRole(noteid, oldroles);
		// 进行更新角色
		try {
			manage.updateUserRoles(userpk, newroles, deleteroles);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		//
		try {
			RoleVO[] showroles = query.getUserRole(userpk, corpPK);
			res.addTable("role", Arrays.asList(showroles));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		res.addMessage("角色委派成功");
		return res;
	}


	/**
	 * 获得要删除的角色。
	 * 
	 * 
	 * 
	 */
	private RoleVO[] getDeleteRole(String noteid, RoleVO[] oldrole) {
		RoleVO[] deleterole = null;
		String oldstr = "";
		String[] rolecode = null;
		int t = oldrole.length;
		for (int i = 0; i < t; i++) {
			if (noteid.indexOf(oldrole[i].getRole_code()) > -1) {
				continue;
			}
			if (i != t - 1) {
				oldstr = oldstr + oldrole[i].getRole_code() + ",";
			} else {
				oldstr = oldstr + oldrole[i].getRole_code();
			}
		}

		if (!("".equals(oldstr))) {
			rolecode = oldstr.split(",");
			deleterole = new RoleVO[rolecode.length];
			for (int i = 0; i < deleterole.length; i++) {
				deleterole[i] = getRole(rolecode[i]);
			}
		}
		return deleterole;

	}

	/**
	 * 获得新增的用户角色
	 * 
	 * 
	 */
	private RoleVO[] getRoles(RoleVO[] old, String noteid) {
		String note = noteid;
		String[] rolecode = null;
		RoleVO[] newroles = null;
		// 获得新增的用户角色的字符串拼接
		for (RoleVO role : old) {
			String sub = role.getRole_code() + ",";
			note = note.replaceAll(sub, "");
		}
		if (!("".equals(note))) {
			note = note.substring(0, note.length() - 1);
		}

		if (!("".equals(note))) {
			rolecode = note.split(",");
			newroles = new RoleVO[rolecode.length];
			for (int i = 0; i < newroles.length; i++) {
				newroles[i] = getRole(rolecode[i]);
			}
		}

		return newroles;
	}

	/**
	 * 获得角色的id
	 * 
	 * 
	 * 
	 */
	private RoleVO getRole(String rolecode) {
		RoleVO role = null;
		Map<String, RoleVO> map = new HashMap<String, RoleVO>();
		map = getRoleCodeMaping();
		role = map.get(rolecode);
		return role;
	}

	/**
	 * 获得rolecode和roleid的对应值
	 * 
	 * 
	 */
	private Map<String, RoleVO> getRoleCodeMaping() {
		RoleAndAllocCorpsVO[] corproles = null;
		IRoleManageQuery rolequery = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);
		Map<String, RoleVO> map = new HashMap<String, RoleVO>();
		this.setCorpPK();
		try {
			corproles = rolequery.getRoles(corpPK, true).getOwnRoles();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		for (int i = 0; i < corproles.length; i++) {
			map.put(corproles[i].getRoleVO().getRole_code(), corproles[i].getRoleVO());
		}
		return map;
	}

	/**
	 * 根据UserVO获取查询条件字符串
	 * 
	 * @param
	 * @return
	 */
	private String generatorSql(String userName, String userCode, String corpID) {
		StringBuffer sql = new StringBuffer(
				"select * from sm_user where (PK_CORP = '" + corpID.trim()
						+ "')");
		if (0 != userCode.length() && userCode.trim().length() > 0) {

			sql.append(" AND (USER_CODE like '%" + userCode.trim() + "%')");
		}
		if (0 != userName.length() && userName.trim().length() > 0) {

			sql.append(" AND (USER_NAME like '%" + userName.trim() + "%')");
		}
		return sql.toString();
	}

	/**
	 * 条件查询
	 * 
	 * @param req
	 * @return IResData
	 */
	public IResData query(IReqData req) {
		IResData res = new UhwebRes();
		String userCode = null;
		String userName = null;
		String corpID = null;
		userCode = (String) req.getFormData("search_user").get("code");
		userName = (String) req.getFormData("search_user").get("name");
		corpID = (String) ContextAPI.getLoginInfo().getPk_crop();
		IUAPQueryBS uqb = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = generatorSql(userName, userCode, corpID);
		try {
			@SuppressWarnings("unchecked")
			List<UserVO> list = (List<UserVO>) uqb.executeQuery(sql,
					new BeanMappingListProcessor(UserVO.class,
							new UserVoMappingMeta()));
			res.addTable("users", list);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return res;
	}

	/**
	 * 跳转到用户详细信息页面
	 * 
	 * @param req
	 * @return
	 */
	public IResData showInfo(IReqData req) {
		IResData res = getRes();
		IUserManageQuery userManageQuery = SFServiceFacility
				.getIUserManageQuery();

		String id = (String) req.getAttr("pk");
		UserVO userVO;
		try {
			userVO = userManageQuery.getUser(id);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		res.addForm("dclickuserform", userVO);
		res.addPage("pages/uhweb/UserManage/UserManageInfo.jsp");
		return res;
	}
}
