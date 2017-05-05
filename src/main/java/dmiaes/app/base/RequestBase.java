package dmiaes.app.base;

/** 
* @ClassName: RequestBase
* @Description: 请求对象
* @author REEFE
* @date 2017-5-4 上午10:45:37
*  
*/
public class RequestBase<T> {
	
	/**登录时定义返回的token */
	private String digest;
	
	/**请求分页参数 */
	private RequsetPage reqpage;
	
	/**请求业务参数 */
	private T reqparam;

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}
	
	public RequsetPage getReqpage() {
		return reqpage;
	}

	public void setReqpage(RequsetPage reqpage) {
		this.reqpage = reqpage;
	}

	public T getReqparam() {
		return reqparam;
	}

	public void setReqparam(T reqparam) {
		this.reqparam = reqparam;
	}
	
}
