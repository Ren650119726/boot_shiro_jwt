package dmiaes.app.base;

/** 
* @ClassName: RequsetPage
* @Description: 分页参数请求对象
* @author REEFE
* @date 2017-5-4 上午10:45:47
*  
*/
public class RequsetPage {
	
	/**每页多少记录数 */
	private Integer pageSize;
	
	/**页码 */
	private Integer page;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
