package dmiaes.app.base;

/** 
* @ClassName: ResponseBase
* @Description: 响应对象
* @author REEFE
* @date 2017-4-26 下午3:01:45
*  
*/
public class ResponseBase<T> {
    private int code;
    private String msg;
    private T data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
  
}
