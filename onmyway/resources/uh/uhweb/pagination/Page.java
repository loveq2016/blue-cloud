package uh.uhweb.pagination;

import nc.vo.pub.SuperVO;

public class Page implements IPage {
	private static final long serialVersionUID = 1L;
	public static final int DEF_COUNT = 20;

	private int totalCount = 0;
	private int pageSize = 20;
	private int pageNo = 1;
	private Object vos;
	private String[] allpks;

	public Page() {
	}

	public Page(int pageNo, int pageSize, String[] allpks) {
		this.allpks = allpks;
		this.totalCount = allpks.length;
		if (pageSize <= 0) {
			this.pageSize = DEF_COUNT;
		} else {
			this.pageSize = pageSize;
		}
		if (pageNo <= 0) {
			this.pageNo = 1;
		} else {
			this.pageNo = pageNo;
		}
		if ((this.pageNo - 1) * this.pageSize >= totalCount) {
			this.pageNo = totalCount / pageSize;
		}
		adjustPage();
	}

	public String[] getPagePks() {
		if (allpks.length == 0) {
			return new String[] {};
		} else {
			int length = getLastResult() - getFirstResult() + 1;
			if (length > 0) {
				String[] pks = new String[length];
				System.arraycopy(allpks, getFirstResult(), pks, 0, length);
				return pks;
			} else {
				return new String[] {};
			}
		}
	}

	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	public int getLastResult() {
		int lastResult = pageNo * pageSize - 1;
		if (lastResult > totalCount - 1) {
			lastResult = totalCount - 1;
		}
		return lastResult;
	}

	/**
	 * 调整分页参数，使合理化
	 */
	public void adjustPage() {
		if (totalCount <= 0) {
			totalCount = 0;
		}
		if (pageSize <= 0) {
			pageSize = DEF_COUNT;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		}
		if ((pageNo - 1) * pageSize >= totalCount) {
			pageNo = totalCount / pageSize;
		}
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalPage() {
		int totalPage = totalCount / pageSize;
		if (totalCount % pageSize != 0 || totalPage == 0) {
			totalPage++;
		}
		return totalPage;
	}

	public boolean isFirstPage() {
		return pageNo <= 1;
	}

	public boolean isLastPage() {
		return pageNo >= getTotalPage();
	}

	public int getNextPage() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}

	public int getPrePage() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Object getVos() {
		return vos;
	}

	public void setVos(Object vos) {
		this.vos = vos;
	}

}
