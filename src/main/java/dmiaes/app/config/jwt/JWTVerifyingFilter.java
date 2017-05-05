package dmiaes.app.config.jwt;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dmiaes.app.base.ResponseBase;
import dmiaes.app.base.UserCredentials;
import dmiaes.app.config.shiro.JWTRealm;
import dmiaes.app.constants.Constants;
import dmiaes.app.exception.AppException;
import dmiaes.app.util.AssertUtil;
import dmiaes.app.util.NetworkUtil;
import dmiaes.app.util.WebUtil;
 
/** 
 *@deprecated 废弃 更改成HttpServletRequestReplacedFilter
* @ClassName: JWTVerifyingFilter
* @Description: JWT验证过滤器
* @author REEFE
* @date 2017-4-28 上午10:24:34
*  
*/

public class JWTVerifyingFilter extends AccessControlFilter {
	private final Logger logger = LoggerFactory.getLogger(JWTRealm.class);
	@Autowired
	private JwtInfo jwtInfo;
	@Autowired
	private UserCredentials userCredentials;
	private String token;
	private ServletRequest requestWrapper;
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    	logger.info("JWTVerifyingFilter.isAccessAllowed()");
    	boolean accessAllowed = false;
     /*   String requestURL = getPathWithinApplication(request);//获取url
        if(requestURL.endsWith("favicon.ico")){
            return true;
        }
    	
	    HttpServletRequest httpRequest = (HttpServletRequest)request;  
        String auth = httpRequest.getHeader("Authorization");
        token = auth;
		if ((auth != null) && (auth.length() > 7)) {
			String headStr = auth.substring(0, 6).toLowerCase();
			if (headStr.compareTo("bearer") == 0) {
				auth = auth.substring(7, auth.length());
				if (JwtHelper.parseJWT(auth, jwtInfo.getBase64Secret()) != null) {
					accessAllowed = true;
				}
			}
		}*/
        return accessAllowed;
    }
 
	@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    	 logger.info("JWTVerifyingFilter.onAccessDenied()");
    	 boolean jwt = false;
    	 String requestURL = getPathWithinApplication(request);//获取url
         if(requestURL.endsWith("favicon.ico") || requestURL.endsWith("login")){
        	 jwt = true;
         }
 	     HttpServletRequest httpRequest = (HttpServletRequest)request;  
         String auth = httpRequest.getHeader("Authorization");
        
 		if ((auth != null) && (auth.length() > 7)) {
 			String headStr = auth.substring(0, 6).toLowerCase();
 			if (headStr.compareTo("bearer") == 0) {
 				auth = auth.substring(7, auth.length());
 				token = auth;
 				if (JwtHelper.parseJWT(auth, jwtInfo.getBase64Secret()) != null) {
 					jwt = true;
 				}
 			}
 		}
 		boolean dig = false;
    	 //1、客户端生成的消息摘要
	    String digest = request.getParameter("digest");
		// 2、客户端传入的用户身份 不用每次请求都带上用户信息
		String userName = request.getParameter("userName") != null ? request.getParameter("userName") : userCredentials.getUserName();
		String passWord = request.getParameter("passWord");
		String ip = NetworkUtil.getIpAddress(httpRequest);
		//4、生成无状态Token
        //用户名，参数，客户端需验证的密码
        try {
			JWTAuthenticationToken jwtToken = new JWTAuthenticationToken();
			// 4、如当前URL匹配拦截器名字（URL模式）
			if (requestURL.endsWith("login")) {
				AssertUtil.notEmpty(userName, "用户名不能为空!");
				AssertUtil.notEmpty(passWord, "密码不能为空!");
				jwtToken.setAuth(passWord);
				jwtToken.setIsLogin(true);
			} else {
				//页面传入jwt的token
				jwtToken.setToken(token);
				jwtToken.setAuth(digest);
				jwtToken.setIsLogin(false);
			}
			jwtToken.setUserName(userName);
			jwtToken.setClientIP(ip);
			// 5、委托给Realm进行登录
			Subject subject = getSubject(requestWrapper, response);
			subject.login(jwtToken);
			dig = true;
        }catch (AppException ex){
        	// 6、登录失败
        	// 登录失败时默认返回1003 状态码
           failure(response, ex.getMessage(),Constants.RESCODE_NOLOGIN);
		} catch (IncorrectCredentialsException e) {
			failure(response, "用户名密码不正确！", Constants.RESCODE_NOLOGIN);
		} catch (Exception e) {
			e.printStackTrace();
			dig = false;
		}
        return jwt && dig;
    }
    
    private void failure(ServletResponse response, String message, int code){
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ResponseBase<String> responseMessage = new ResponseBase<>();
        responseMessage.setCode(code);//错误码
        responseMessage.setMsg(message);
        WebUtil.responseWrit(httpResponse, responseMessage);
    }
}