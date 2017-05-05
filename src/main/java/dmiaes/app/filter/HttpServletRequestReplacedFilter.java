package dmiaes.app.filter;
import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import dmiaes.app.base.RequestBase;
import dmiaes.app.base.ResponseBase;
import dmiaes.app.base.UserCredentials;
import dmiaes.app.config.jwt.JWTAuthenticationToken;
import dmiaes.app.config.jwt.JwtHelper;
import dmiaes.app.config.jwt.JwtInfo;
import dmiaes.app.constants.Constants;
import dmiaes.app.exception.AppException;
import dmiaes.app.model.User;
import dmiaes.app.system.service.UserService;
import dmiaes.app.util.AssertUtil;
import dmiaes.app.util.JsonUtils;
import dmiaes.app.util.MD5;
import dmiaes.app.util.NetworkUtil;
import dmiaes.app.util.WebUtil;
public class HttpServletRequestReplacedFilter implements Filter {
	@Autowired
	private JwtInfo jwtInfo;
	
	@Autowired
	private UserCredentials userCredentials;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void destroy() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException, UnauthorizedException {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String requestURL = httpRequest.getRequestURI();  
			boolean jwt = false;
			boolean dig = false;
			if(requestURL.endsWith("favicon.ico") || requestURL.endsWith("login")){
	        	 jwt = true;
			}
			String token = null;
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
     		String submitMehtod = httpRequest.getMethod();
     		
            // 防止流读取一次后就没有了, 所以需要将流继续写出去
            ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpRequest);
            String body = null;
            if (submitMehtod.equals("GET")) {
            	 String queryString = httpRequest.getQueryString();
            	 if(queryString != null){
            		 body =  new String(queryString.getBytes("iso-8859-1"),"utf-8").replaceAll("%22", "\"");
            	 }
            } else {
            	body = HttpHelper.getBodyString(requestWrapper);
            }
            try {
            	AssertUtil.notEmpty(body,"请先登陆获取授权!");
            	RequestBase<Map<String, Object>> parse = JsonUtils.parse(body, RequestBase.class);
            	String digest = parse.getDigest();
	        	
	    		Map<String, Object> reqparam = parse.getReqparam();
	    		String userName = reqparam.get("userName") != null? reqparam.get("userName").toString():userCredentials.getUserName();
	    		String passWord = reqparam.get("passWord")!=null?reqparam.get("passWord").toString():"";
	    		String ip = NetworkUtil.getIpAddress(httpRequest);
	    		JWTAuthenticationToken jwtToken = new JWTAuthenticationToken();
    			// 如当前URL匹配拦截器名字（URL模式）
    			if (requestURL.endsWith("login")) {
    				AssertUtil.notEmpty(userName, "用户名不能为空!");
    				AssertUtil.notEmpty(passWord, "密码不能为空!");
    				jwtToken.setAuth(passWord);
    				jwtToken.setIsLogin(true);
    				String authInfo= MD5.encrypt(userName+ip+System.currentTimeMillis());
        			userCredentials.setDigest(authInfo);
        			
        			//验证账号密码，因为捕获不了doGetAuthenticationInfo里的异常，所以提到这里验证方便返回异常信息到前端
        			User user = userService.queryByUserNameAndPassWord(userName, passWord);
        			AssertUtil.notNull(user, "用户名密码不正确!");
        			userCredentials.setUserId(user.getUserid());
        			userCredentials.setHospitalId(user.getHospitalid());
        			userCredentials.setUserName(userName);
        			userCredentials.setTokenType(Constants.JWT_TYPE);
        			
    			} else {
    				AssertUtil.notEmpty(digest,"请先登陆获取授权!");
    				//页面传入jwt的token
    				jwtToken.setToken(token);
    				jwtToken.setAuth(digest);
    				jwtToken.setIsLogin(false);
    			}
    			jwtToken.setUserName(userName);
    			jwtToken.setClientIP(ip);
    			// 委托给Realm进行登录
    			Subject subject = SecurityUtils.getSubject();
    			subject.login(jwtToken);
    			dig = true;
			}catch (AppException e){
	        	// 登录失败
	        	// 登录失败时默认返回1003 状态码
	           failure(response, e.getMessage(),Constants.RESCODE_NOLOGIN);
			} catch (Exception e) {
				e.printStackTrace();
			}
            if(jwt && dig){
            	 chain.doFilter(requestWrapper, response);
            }
	} 

	private void failure(ServletResponse response, String message, int code) {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		ResponseBase<String> responseMessage = new ResponseBase<>();
		responseMessage.setCode(code);
		responseMessage.setMsg(message);
		WebUtil.responseWrit(httpResponse, responseMessage);
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
}