package org.ming.oa.util.pager;

public class PageModel {

	/**总记录数 */
	private int recordCount;
	
	/**当前页码 */
	private int pageIndex=1;

	/**每页显示的数量 */
	private int pageSize=4;
	
	/**总页数 */
	private int totalSize;

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getPageIndex() {
		//计算总页码
		int totalPageNum = this.getRecordCount() % this.getPageSize() == 0 ? this.getRecordCount() / this.getPageSize() : (this.getRecordCount() / this.getPageSize()) + 1;
		return pageIndex > totalPageNum ? totalPageNum : pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		
		this.pageIndex=pageIndex;
		
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public Integer getFirstLimitParam() {
		return (this.pageIndex-1)*this.getPageSize();
	}
	
	
	
	
}
