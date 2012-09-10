package uh.uhweb.pagination;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.gen.generator.nc.NCLocalHomeImplGenerator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.mapping.IMappingMeta;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.sqlutil.IInSqlBatchCallBack;
import nc.vo.trade.sqlutil.InSqlBatchCaller;

/**
 * 分页查询工具类
 * 
 * @author shangjun
 *
 */
public class PageQueryDAO {
	
	private static final Map<Class<? extends SuperVO>,SuperVO> class_VOMap=new ConcurrentHashMap<Class<? extends SuperVO>,SuperVO>();
	
	private Class<?> clazz;
	private IMappingMeta meta;
	private String pkFieldName;
	private String tableName;
	private BaseDAO dao;
	


	public PageQueryDAO(Class<? extends SuperVO> clazz) {
		super();
		this.clazz = clazz;
		SuperVO obj = getInstance(clazz);
		pkFieldName=obj.getPKFieldName();
		tableName=obj.getTableName();
	}

	public PageQueryDAO(Class<?> clazz, IMappingMeta meta) {
		super();
		this.clazz = clazz;
		this.meta = meta;
		pkFieldName=meta.getPrimaryKey();
		tableName=meta.getTableName();
	}
	
	public PageQueryDAO(String pkFieldName, String tableNames) {
		this.pkFieldName = pkFieldName;
		this.tableName = tableNames;
	}


	/**
	 * 查询分页数据
	 * @param condition 查询条件
	 * @param orderby 排序条件
	 * @param pageSize 每页大小
	 * @param pageNo 页码，查询第几页的数据
	 * @return
	 * @throws BusinessException
	 */
	public IPage qryPageData(String condition, String orderby, int pageSize,int pageNo) throws BusinessException {
		String[] allpks = qryPKs(condition, orderby);
//		if (allpks.length == 0) {
//			return null;
//		}
		Page page = new Page(pageNo, pageSize, allpks);
		String[] pagePks = page.getPagePks();
		if (pagePks == null) {
			return null;
		}
		SuperVO[] vos = qryVOByPKs(pagePks, orderby);
		page.setVos(vos);
		return page;
	}
	
	public IPage qryPageData(String selectedFields, String condition, String orderby, int pageSize, int pageNo, ResultSetProcessor processor) throws BusinessException {
		String[] allpks = qryPKs(condition, orderby);
//		if (allpks.length == 0) {
//			return null;
//		}
		Page page = new Page(pageNo, pageSize, allpks);
		String[] pagePks = page.getPagePks();
		if (pagePks == null) {
			return null;
		}
		Object vos = qryVOByPKs(pagePks, selectedFields, this.tableName, condition, orderby, processor);
		page.setVos(vos);
		
		return page;
	}
	
	private Object qryVOByPKs(String[] pagePks, final String selectedFields, final String tableNames, final String condition, final String orderby, final ResultSetProcessor processor) throws BusinessException {
		final List<Object> voList=new ArrayList<Object>();
		InSqlBatchCaller caller=new InSqlBatchCaller(pagePks);
		try {
			caller.execute(new IInSqlBatchCallBack() {
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public Object doWithInSql(String inSql) throws BusinessException,
						SQLException {
					String con = null;
					if (condition == null || condition.trim().equalsIgnoreCase("")) 
						con = pkFieldName + " in " + inSql;
					else
						con = condition + " and " + pkFieldName + " in " + inSql;
					String sql = null;
					if (orderby == null || orderby.trim().equalsIgnoreCase("")) 
						sql = String.format("select %s from %s where %s", selectedFields, tableNames, con);
					else
						sql = String.format("select %s from %s where %s order by %s", selectedFields, tableNames, con, orderby);
					Object result = getBaseDAO().executeQuery(sql , processor);
					voList.add(result);
					return null;
				}
			});
		}  catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		if (voList.size() > 0)
			return voList.get(0);
		else 
			return null;
	}

	public SuperVO[] qryVOByPKs(String[] pagePks,final String orderby) throws BusinessException {
		final List<SuperVO> voList=new ArrayList<SuperVO>();
		InSqlBatchCaller caller=new InSqlBatchCaller(pagePks);
		try {
			caller.execute(new IInSqlBatchCallBack() {
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public Object doWithInSql(String inSql) throws BusinessException,
						SQLException {
					String condition = pkFieldName + " in " + inSql;
					Collection result = null;
					if(meta==null){
						result=getBaseDAO().retrieveByClause(clazz, condition, orderby);
					}else{
						condition=isNotNull(orderby)?condition+" order by "+orderby:condition;
						result=getBaseDAO().retrieveByClause(clazz, meta, condition);
					}
					if(result.size()>0){
						voList.addAll(result);
					}
					return null;
				}
			});
		}  catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		if (voList.size() > 0)
			return voList.toArray((SuperVO[])Array.newInstance(clazz, voList.size()));
		else 
			return (SuperVO[]) Array.newInstance(clazz, 0);
	}
	
	@SuppressWarnings("unchecked")
	public String[] qryPKs(String condition,String orderby)throws BusinessException{
		String sql="select "+pkFieldName+" from "+tableName;
		if(isNotNull(condition)){
			sql+=" where "+condition;
		}
		if(isNotNull(orderby)){
			sql+=" order by "+orderby;
		}
		List<String> pkList=(List<String>) getBaseDAO().executeQuery(sql, new ColumnListProcessor());
		return pkList.toArray(new String[pkList.size()]);
	}


	private boolean isNotNull(String str) {
		return str!=null&&str.trim().length()>0;
	}


	private BaseDAO getBaseDAO() {
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;
	}

	@SuppressWarnings("unchecked")
	private static <T extends SuperVO> T getInstance(Class<T> clazz) {
		if(class_VOMap.containsKey(clazz)){
			return (T) class_VOMap.get(clazz);
		}else{
			synchronized(PageQueryDAO.class){
				if(!class_VOMap.containsKey(clazz)){
					try {
						SuperVO obj=(SuperVO) clazz.newInstance();
						class_VOMap.put(clazz, obj);
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					} 
				}
			}
			return (T) class_VOMap.get(clazz);
		}
	}
	

}