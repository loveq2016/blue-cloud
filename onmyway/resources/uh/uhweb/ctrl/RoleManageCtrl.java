package uh.uhweb.ctrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IRoleManage;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.sm.login.LoginSessBean;
import nc.vo.uap.rbac.RoleVO;
import nc.vo.uap.rbac.role.RoleUpdateVO;
import com.yonyouhealth.uaph.framework.comm.exception.CSSBaseCheckedException;
import com.yonyouhealth.uaph.framework.web.context.ContextAPI;
import com.yonyouhealth.uaph.framework.web.controller.BaseDomainCtrl;
import com.yonyouhealth.uaph.framework.web.controller.annotations.CTRL;
import com.yonyouhealth.uaph.framework.web.event.IReqData;
import com.yonyouhealth.uaph.framework.web.event.IResData;
import com.yonyouhealth.uaph.framework.web.event.UhwebRes;

/**
 * <b>角色管理控制类 </b>
 * 
 * <p>
 * 角色管理控制类，包括:角色列表查看、角色条件查找、角色新增、角色修改、角色删除、角色查看、按角色查看授权用户功能
 * </p>
 * 
 * Creation Time: 2012-8-14 上午08:45:30
 * 
 * @author 陈振江，李运通
 * @version RHIP V1.0
 * 
 */
@CTRL("RoleManageCtrl")
public class RoleManageCtrl extends BaseDomainCtrl {
	/**
	 * 角色管理主页面
	 * 
	 * @param req
	 * @return
	 */
	public IResData main(IReqData req) throws Exception {
		IResData res = getRes();
		IRoleManageQuery rm = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		try {
			RoleVO[] rvArray = rm.getCorpRoles(lsb.getPk_crop());
			res.addTable("roleGrid", Arrays.asList(rvArray));
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		res.addPage("pages/uhweb/RoleManage/RoleManageMain.jsp");
		return res;
	}

	/**
	 * 跳转到角色管理新增页面
	 * 
	 * @param req
	 * @return
	 */
	public IResData toAddRole(IReqData req) throws Exception {
		IResData res = getRes();
		res.addPage("pages/uhweb/RoleManage/RoleManageAdd.jsp");
		return res;
	}

	/**
	 * 角色管理新增修改功能
	 * 
	 * @param req
	 * @return
	 */
	public IResData saveOrUpdateRole(IReqData req) throws Exception {
		IResData res = getRes();
		IRoleManage roleManage = NCLocator.getInstance().lookup(
				IRoleManage.class);
		IRoleManageQuery roleManageQuery = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);
		RoleVO oldRoleVO = null;
		RoleVO roleVO = (RoleVO) req.getForm("roleform");
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		try {
			if (null != roleVO.getPk_role()) {
				oldRoleVO = roleManageQuery.getRoleInfo(roleVO.getPk_role(),
						lsb.getPk_crop()).getRoleVO();
			}
			if (null != oldRoleVO) {
				roleVoToVo(oldRoleVO, roleVO);
				RoleUpdateVO roleUpdateVO = new RoleUpdateVO();
				roleUpdateVO.setRoleVO(oldRoleVO);
				roleManage.updateRole(roleUpdateVO);
			} else {
				roleVO.setPk_corp(lsb.getPk_crop());
				roleVO.setResource_type(RoleVO.COMPANY_TYPE);
				roleVO.setPk_role(roleManage.newRole(roleVO, null, null, null));
				res.addForm("form", roleVO);
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return res;
	}

	/**
	 * 更新角色vo
	 * 
	 * @param oldVo
	 * @param newVo
	 * @return
	 */
	private RoleVO roleVoToVo(RoleVO oldVo, RoleVO newVo) {
		oldVo.setRole_code(newVo.getRole_code());
		oldVo.setRole_memo(newVo.getRole_memo());
		oldVo.setRole_name(newVo.getRole_name());
		return oldVo;
	}

	/**
	 * 跳转到角色修改页面
	 * 
	 * @param req
	 * @return
	 */
	public IResData toUpdateRole(IReqData req) throws Exception {
		IResData res = getRes();
		IRoleManageQuery roleManageQuery = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		try {
			String id = (String) req.getAttr("pk");
			RoleVO roleVO = roleManageQuery.getRoleInfo(id, lsb.getPk_crop())
					.getRoleVO();
			res.addForm("roleform", roleVO);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		res.addPage("pages/uhweb/RoleManage/RoleManageChange.jsp");
		return res;
	}

	/**
	 * 跳转到角色详细信息页面
	 * 
	 * @param req
	 * @return
	 */
	public IResData showRoleInfo(IReqData req) throws Exception {
		IResData res = getRes();
		IRoleManageQuery roleManageQuery = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		try {
			String id = (String) req.getAttr("pk");
			RoleVO roleVO = roleManageQuery.getRoleInfo(id, lsb.getPk_crop())
					.getRoleVO();
			res.addForm("dclickroleform", roleVO);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		res.addPage("pages/uhweb/RoleManage/RoleManageInfo.jsp");
		return res;
	}

	/**
	 * 按条件查询角色
	 * 
	 * @param req
	 * @return IResData
	 * @throws CSSBaseCheckedException
	 * @throws DAOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IResData queryRole(IReqData req) throws Exception {
		IResData res = new UhwebRes();
		RoleVO roleVO = (RoleVO) req.getForm("roleform");
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		roleVO.setPk_corp(lsb.getPk_crop());
		IUAPQueryBS uqb = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = generatorSql(roleVO);
		try {
			List<RoleVO> list = (List<RoleVO>) uqb.retrieveByClause(
					RoleVO.class, sql);
			res.addTable("roleGrid", list);
//			res.addTable("userGrid", new ArrayList());
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return res;
	}

	/**
	 * 根据rolevo获取查询条件字符串
	 * 
	 * @param roleVO
	 * @return
	 */
	private String generatorSql(RoleVO roleVO) {
		StringBuffer sql = new StringBuffer("(PK_CORP = '"
				+ roleVO.getPk_corp() + "')");
		if (null != roleVO.getRole_code()
				&& roleVO.getRole_code().trim().length() > 0) {
			sql.append(" AND (ROLE_CODE like '%" + roleVO.getRole_code()
					+ "%')");
		}
		if (null != roleVO.getRole_name()
				&& roleVO.getRole_name().trim().length() > 0) {
			sql.append(" AND (ROLE_NAME like '%" + roleVO.getRole_name()
					+ "%')");
		}
		return sql.toString();
	}

	/**
	 * 删除选中角色记录
	 * 
	 * @param req
	 * @return IResData
	 * @throws CSSBaseCheckedException
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public IResData deleteRole(IReqData req) throws Exception {
		IResData res = new UhwebRes();
		// 取页面选中的被删除记录列表
		List<RoleVO> deleteList = (List<RoleVO>) req.getDeleteTable("roleGrid");
		IRoleManage roleManage = NCLocator.getInstance().lookup(
				IRoleManage.class);
		if (null != deleteList && deleteList.size() > 0) {
			for (RoleVO rv : deleteList) {
				if (null != rv) {
					try {
						roleManage.delRole(rv.getPk_role());
					} catch (BusinessException e) {
						Logger.error(e.getMessage(), e);
						throw new BusinessException(e.getMessage());
					}
				}
			}
		}
		return res;
	}

	/**
	 * 跳转到根据角色查找用户界面
	 * 
	 * @param req
	 * @return
	 */
	public IResData showRoleUsers(IReqData req) throws Exception {
		IResData res = getRes();
		IRoleManageQuery roleManageQuery = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		try {
			String id = (String) req.getAttr("pk");
			UserVO[] uvArray = roleManageQuery.getUsers(id, lsb.getPk_crop());
			res.addTable("userGrid", Arrays.asList(uvArray));
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return res;
	}

	/**
	 * 刷新显示所有角色记录
	 * 
	 * @param
	 * @return
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public IResData refreshRole(IReqData req) throws Exception {
		IResData res = getRes();
		IRoleManageQuery rm = NCLocator.getInstance().lookup(
				IRoleManageQuery.class);
		LoginSessBean lsb = ContextAPI.getLoginInfo();
		try {
			RoleVO[] rvArray = rm.getCorpRoles(lsb.getPk_crop());
			res.addTable("roleGrid", Arrays.asList(rvArray));
			res.addTable("userGrid", new ArrayList());
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return res;
	}

	/**
	 * 验证角色编码是否存在
	 * 
	 * @param
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public IResData validateRoleCode(IReqData req) throws Exception {
		IResData res = getRes();
		// 校验
		String pk_role = (String) req.getFormData("roleform").get("pk_role");
		String role_code = (String) req.getFormData("roleform")
				.get("role_code");
		String sql = generatorValidateSql(pk_role, role_code, null);
		IUAPQueryBS uqb = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<RoleVO> list = null;
		try {
			list = (List<RoleVO>) uqb.retrieveByClause(RoleVO.class, sql);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		if (null != list && list.size() > 0)
			res.addValidator(false, "角色编码已存在，请检查");
		else
			res.addValidator(true);
		return res;

	}

	/**
	 * 验证角色名称是否存在
	 * 
	 * @param
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public IResData validateRoleName(IReqData req) throws Exception {
		IResData res = getRes();
		// 校验
		String pk_role = (String) req.getFormData("roleform").get("pk_role");
		String role_name = (String) req.getFormData("roleform")
				.get("role_name");
		String sql = generatorValidateSql(pk_role, null, role_name);
		IUAPQueryBS uqb = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		List<RoleVO> list = null;
		try {
			list = (List<RoleVO>) uqb.retrieveByClause(RoleVO.class, sql);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		if (null != list && list.size() > 0)
			res.addValidator(false, "角色名称已存在，请检查");
		else
			res.addValidator(true);
		return res;

	}

	/**
	 * 根据rolevo获取查询条件字符串
	 * 
	 * @param roleVO
	 * @return
	 */
	private String generatorValidateSql(String pk_role, String role_code,
			String role_name) {
		StringBuffer sql = new StringBuffer("");
		if (null != role_code && role_code.trim().length() > 0) {
			sql.append(" (ROLE_CODE = '" + role_code.trim() + "')");
			if (null != pk_role && pk_role.trim().length() > 0) {
				sql.append(" AND (PK_ROLE <> '" + pk_role.trim() + "')");
			}
		} else if (null != role_name && role_name.trim().length() > 0) {
			sql.append(" (ROLE_NAME = '" + role_name.trim() + "')");
			if (null != pk_role && pk_role.trim().length() > 0) {
				sql.append(" AND (PK_ROLE <> '" + pk_role.trim() + "')");
			}
		}
		return sql.toString();
	}

}
