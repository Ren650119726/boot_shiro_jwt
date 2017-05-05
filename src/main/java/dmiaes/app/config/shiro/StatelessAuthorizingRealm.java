package dmiaes.app.config.shiro;
import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import dmiaes.app.system.service.UserService;
import dmiaes.app.util.HmacSHA256Utils;
 
/** 
 *@deprecated 无状态Realm Demo类
* @ClassName: StatelessAuthorizingRealm
* @Description: 自定义Realm
* @author REEFE
* @date 2017-4-25 下午1:43:55
*  
*/
public class StatelessAuthorizingRealm extends AuthorizingRealm{
	
	@Resource
	private UserService userService;
	
    /**
     * 仅支持StatelessToken 类型的Token，
     * 那么如果在StatelessAuthcFilter类中返回的是UsernamePasswordToken，那么将会报如下错误信息：
     * Please ensure that the appropriate Realm implementation is configured correctly or
     * that the realm accepts AuthenticationTokens of this type.StatelessAuthcFilter.isAccessAllowed()
     */
    @Override
    public boolean supports(AuthenticationToken token) {
       return token instanceof StatelessAuthenticationToken;
    }
    
	/**
     * 身份验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    	
       StatelessAuthenticationToken statelessToken = (StatelessAuthenticationToken)authcToken;
       String username = (String)statelessToken.getPrincipal();//不能为null,否则会报错的.
       //TODO  数据库查询   判断是否存在  从redis取缓存，没有则保存到redis 
     
       //在服务器端生成客户端参数消息摘要
       String serverDigest = HmacSHA256Utils.digest(statelessToken.getUserId(), statelessToken.getParams());
       statelessToken.setClientDigest(serverDigest);
       //然后进行客户端消息摘要和服务器端消息摘要的匹配
       SimpleAuthenticationInfo  authenticationInfo = new SimpleAuthenticationInfo(
              username,
              serverDigest,
              getName());
       return authenticationInfo;
    }
   
    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
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