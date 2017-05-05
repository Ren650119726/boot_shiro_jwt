package dmiaes.app.config.jwt;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTAuthenticationToken implements AuthenticationToken {
    /** */
	private static final long serialVersionUID = 1L;
	
	private String token; //jwt token
	private String userName;//用户身份即用户名；
    private String auth;//凭证
    private Boolean isLogin;  //是否是登陆登陆请求
    private String clientIP; //客户端IP
    
	@Override
	public Object getPrincipal() {
		return userName;
	}
	//认证信息(没登录就是密码，登陆了就是token)
	@Override
	public Object getCredentials() {
		return auth;
	}
    
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public Boolean getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
}