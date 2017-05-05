package dmiaes.app.config.shiro;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dmiaes.app.base.UserCredentials;
import dmiaes.app.config.jwt.JWTAuthenticationToken;
import dmiaes.app.config.jwt.JwtHelper;
import dmiaes.app.config.jwt.JwtInfo;
import dmiaes.app.config.redis.RedisUtil;
import dmiaes.app.constants.Constants;
import dmiaes.app.model.User;
import dmiaes.app.system.service.UserService;
import dmiaes.app.util.AssertUtil;
import dmiaes.app.util.StringUtils;

/** 
* @ClassName: JWTRealm
* @Description:  自定义Realm
* @author REEFE
* @date 2017-5-4 上午10:44:42
*  
*/
public class JWTRealm extends AuthorizingRealm{
	private final Logger logger = LoggerFactory.getLogger(JWTRealm.class);
	
	@Autowired
	private JwtInfo jwtInfo;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private UserCredentials userCredentials;
	
	@Autowired
	private UserService userService;
    /**
     * 仅支持JWTAuthenticationToken 类型的Token，
     * 那么如果在JWTAuthenticationToken类中返回的是UsernamePasswordToken，那么将会报如下错误信息：
     * Please ensure that the appropriate Realm implementation is configured correctly or
     * that the realm accepts AuthenticationTokens of this type.StatelessAuthcFilter.isAccessAllowed()
     */
    @Override
    public boolean supports(AuthenticationToken token) {
       return token instanceof JWTAuthenticationToken;
    }
    
	/**
     * 身份验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
       logger.info("JWTRealm.doGetAuthenticationInfo()");
       JWTAuthenticationToken token = (JWTAuthenticationToken)authcToken;
       String userName = (String) token.getPrincipal();
       
       User user = userService.queryByUserName(userName);
       
       if(user == null){
    	   throw new UnknownAccountException("帐号不存在！");
       }
       String ip = token.getClientIP();
       Long userId = user.getUserid();
       //在服务器端生成客户端参数消息摘要
       String serverDigest = null;
       if (token.getIsLogin()) {// 登陆
			serverDigest = user.getPassword();
			redisUtil.remove(Constants.CACHE_TOKNID + userId);
			String tokenId =  (String) redisUtil.get(Constants.CACHE_TOKNID + userId);// 从缓存获取token
			if (StringUtils.isNotBlank(tokenId)) {
				redisUtil.set(Constants.CACHE_TOKNID + userId, tokenId,
						Constants.CACHE_TOKENID_TIME);// 设置Token
			} else {
	            //拼装token  
				tokenId = JwtHelper.createJWT(userName, ip,
						jwtInfo.getClientId(), 
						jwtInfo.getName(),
						jwtInfo.getExpiresSecond() * 1000,
						jwtInfo.getBase64Secret());
				
				userCredentials.setToken(tokenId);
				redisUtil.set(Constants.CACHE_TOKNID + userId, tokenId,
						Constants.CACHE_TOKENID_TIME);// 设置Token
			}
		} else {
			String tokenId = (String) redisUtil.get(Constants.CACHE_TOKNID + userId);// 从缓存获取token
			AssertUtil.notEmpty(tokenId, "需重新登陆获取");
		    if(!tokenId.equals(token.getToken())){
              AssertUtil.isFalse(true, "无效的token");
            }
		    //登陆后生成的消息摘要
			serverDigest = userCredentials.getDigest();
		}
		// 然后进行客户端消息摘要和服务器端消息摘要的匹配
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				userName, serverDigest, getName());
		
		return authenticationInfo;
    }	
   
    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
       logger.info("JWTRealm.doGetAuthorizationInfo()");
       //根据用户名查找权限组，请根据需求实现
       String username = (String) principals.getPrimaryPrincipal();
       
       SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
       //这里模拟admin账号才有role的权限.
       if("admin".equals(username)){
           authorizationInfo.addRole("admin");
       }
       return authorizationInfo;
    }
   
}