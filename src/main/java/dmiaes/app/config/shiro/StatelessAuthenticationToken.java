package dmiaes.app.config.shiro;
 
import java.util.Map;

import org.apache.shiro.authc.AuthenticationToken;
 
/** 
 *@deprecated 无状态Token类
* @ClassName: StatelessAuthenticationToken
* @Description: 用于授权的Token对象
				<br>用户身份即用户名；
* 				<br> 凭证即客户端传入的消息摘要。
* @author REEFE
* @date 2017-4-25 上午11:11:14
*  
*/
public class StatelessAuthenticationToken implements AuthenticationToken{
    private static final long serialVersionUID = 1L;
    private String userName;//用户身份即用户名；
    private String userId; //用户Id；
    private Map<String,?> params;//参数.
    private String clientDigest;//凭证即客户端传入的消息摘要。
    private String tokenId;//token
    private Boolean isLogin;  //是否登陆
    public StatelessAuthenticationToken() {
    }
    public StatelessAuthenticationToken(String username, Map<String, ?> params, String clientDigest) {
       super();
       this.userName = username;
       this.params = params;
       this.clientDigest = clientDigest;
    }
   
    public StatelessAuthenticationToken(String username, String clientDigest) {
       super();
       this.userName = username;
       this.clientDigest = clientDigest;
    }
 
    @Override
    public Object getPrincipal() {
       return userName;
    }
    
    @Override
    public Object getCredentials() {
       return clientDigest;
    }
    
    public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
       return userName;
    }
 
    public void setUserName(String username) {
       this.userName = username;
    }
 
    public Map<String, ?> getParams() {
       return params;
    }
 
    public void setParams(Map<String, ?> params) {
       this.params = params;
    }
 
    public String getClientDigest() {
       return clientDigest;
    }
 
    public void setClientDigest(String clientDigest) {
       this.clientDigest = clientDigest;
    }
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public Boolean getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}
    
}