package dmiaes.app.util;

import java.util.List;
import java.util.Map;

/**
 * @description 分页工具
 * 
 * @author Jxl
 * @date 2014年9月12日
 * @time 下午2:41:05
 *
 */
public class Pagination implements java.io.Serializable {
	private static final long serialVersionUID = 5514179505070466128L;
	private int nowPage = 1;// 当前页
	private int startPage;// 起始页
	private int endPage;// 结束页
	private int totalRecord;// 总记录数
	private int totalPage;// 总页数
	private List<?> objects;// 根据条件查出的对象集合
	private Map<String, String> conditions;// 查询条件
	private int pagesize = 20;// 每页显示的条数
	private int showpages = 10;// 每页显示的页数

	public Pagination() {
	}

	/**
	 * @description 构造函数创建
	 * 
	 * @param nowPage 当前页
	 * @param totalRecord 总记录数
	 * @param pagesize 每页显示的条数
	 * @param showpages 每页显示的页数
	 * @return
	 */
	public Pagination(int nowPage, int totalRecord, int pagesize, int showpages) {
		this.nowPage = nowPage;
		this.totalRecord = totalRecord;
		this.pagesize = pagesize;
		this.showpages = showpages;
		totalPage = (int) (totalRecord + pagesize - 1) / pagesize;
		paginationTool(nowPage, totalPage);
	}

	/**
	 * @description 方法创建
	 * 
	 * @param nowPage 当前页
	 * @param totalRecord 总记录数
	 * @param pagesize 每页显示的条数
	 * @param showpages 每页显示的页数
	 * @return
	 */
	public void paginationInit(int nowPage, int totalRecord, int pagesize, int showpages) {
		this.nowPage = nowPage;
		this.totalRecord = totalRecord;
		this.pagesize = pagesize;
		this.showpages = showpages;
		totalPage = (int) (totalRecord + pagesize - 1) / pagesize;
		paginationTool(nowPage, totalPage);
	}

	/**
	 * @description 设置每页显示的条数和每页显示的页数,默认pagesize=10，showpages=10
	 * 
	 * @param pagesize 每页显示的条数
	 * @param showpages 每页显示的页数
	 * @return
	 */
	public void paginationPs(int pagesize, int showpages) {
		this.pagesize = pagesize;
		this.showpages = showpages;
	}

	/**
	 * @description 分页方法 通过这个方法,得到两个数据——startPage和endPage 页面上的页码就是根据这两个数据处理后显示
	 * 
	 * @param nowPage当前页
	 * @param totalPage总页数
	 */
	public void paginationTool(int nowPage, int totalPage) {
		this.nowPage = nowPage;
		this.totalPage = totalPage;
		/** 计算startPage与endPage的值 **/
		if (this.totalPage < showpages) {
			/** if中是总页数小于SHOWPAGES的情况 */
			this.startPage = 1;
			this.endPage = totalPage;
		} else {
			/** else中是总页数大于SHOWPAGES的情况 */
			if (this.nowPage <= showpages / 2 + 1) {
				this.startPage = 1;
				this.endPage = showpages;
			} else {
				this.startPage = this.nowPage - (showpages / 2);
				if (showpages % 2 == 0)
					this.endPage = this.nowPage + (showpages / 2 - 1);//showpages为偶数
				else
					this.endPage = this.nowPage + (showpages / 2);//showpages为奇数
				if (this.endPage >= this.totalPage) {
					this.endPage = this.totalPage;
					this.startPage = this.totalPage - showpages + 1;
				}
			}
		}
	}

	/**
	 * @return the nowPage
	 */
	public int getNowPage() {
		return nowPage;
	}

	/**
	 * @param nowPage the nowPage to set
	 */
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	/**
	 * @return the startPage
	 */
	public int getStartPage() {
		return startPage;
	}

	/**
	 * @param startPage the startPage to set
	 */
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	/**
	 * @return the endPage
	 */
	public int getEndPage() {
		return endPage;
	}

	/**
	 * @param endPage the endPage to set
	 */
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	/**
	 * @return the totalRecord
	 */
	public int getTotalRecord() {
		return totalRecord;
	}

	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	/**
	 * @return the totalPage
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @param totalPage the totalPage to set
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * @return the pagesize
	 */
	public int getPagesize() {
		return pagesize;
	}

	/**
	 * @param pagesize the pagesize to set
	 */
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	/**
	 * @return the showpages
	 */
	public int getShowpages() {
		return showpages;
	}

	/**
	 * @param showpages the showpages to set
	 */
	public void setShowpages(int showpages) {
		this.showpages = showpages;
	}

	public List<?> getObjects() {
		return objects;
	}

	public void setObjects(List<?> objects) {
		this.objects = objects;
	}

	public Map<String, String> getConditions() {
		return conditions;
	}

	public void setConditions(Map<String, String> conditions) {
		this.conditions = conditions;
	}

}
