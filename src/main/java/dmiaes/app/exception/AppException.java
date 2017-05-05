package dmiaes.app.exception;

/** 
* @ClassName: REEFEException
* @Description: 自定义异常类
* @author REEFE
* @date 2017-4-28 下午3:01:11
*  
*/
public class AppException extends RuntimeException{

	/** */
	private static final long serialVersionUID = 1L;

	public AppException(String message) {
		super(message);
	} 
}	
