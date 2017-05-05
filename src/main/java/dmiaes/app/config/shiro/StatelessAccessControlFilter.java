package dmiaes.app.config.shiro;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

import dmiaes.app.base.ResponseBase;
import dmiaes.app.config.redis.RedisUtil;
import dmiaes.app.constants.Constants;
import dmiaes.app.util.AssertUtil;
import dmiaes.app.util.StringUtils;
import dmiaes.app.util.WebUtil;
 
/** 
 * @deprecated
* @ClassName: StatelessAccessControlFilter
* @Description:访问控制过滤器
* @author REEFE
* @date 2017-4-25 上午11:15:02
*  
*/
public class StatelessAccessControlFilter extends AccessControlFilter{
	
	@Resource 
	private RedisUtil redisUtil;
	
    /**
     * 先执行：isAccessAllowed 再执行onAccessDenied
     *
     * isAccessAllowed：表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，
     * 如果允许访问返回true，否则false；
     *
     * 如果返回true的话，就直接返回交给下一个filter进行处理。
     * 如果返回false的话，回往下执行onAccessDenied
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
           throws Exception {
       String token = request.getParameter("app");
       String userId = request.getParameter("userId");
       boolean isAccessAllowed = false;
       if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(userId)  ){
           String cacheToken = (String) redisUtil.get(Constants.CACHE_TOKNID + userId);
           if(StringUtils.isNotBlank(cacheToken) && token.equals(cacheToken)){
               isAccessAllowed = true;
           }
       }
       return isAccessAllowed;
    }
 
    /**
     * onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；
     * 如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     */
	@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
       
       //1、客户端生成的消息摘要
       String clientDigest = request.getParameter("digest");
       String tokenId = request.getParameter("tokenId");
       //2、客户端传入的用户身份
       String username = request.getParameter("username");
       String userId = request.getParameter("userid");
       String requestURL = getPathWithinApplication(request);//获取url
       //3、客户端请求的参数列表
       Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());
       params.remove("digest");//为什么要移除呢？签名或者消息摘要算法的时候不能包含digest.
      
       //4、生成无状态Token
       //用户名，参数，客户端需验证的密码
	   StatelessAuthenticationToken token = new StatelessAuthenticationToken(username,params,clientDigest);
       try {
    	    //如当前URL匹配拦截器名字（URL模式）
           if (requestURL.endsWith("login")) {//如为登陆，就只对密码进行加密
               AssertUtil.notEmpty(username,"用户名不能为空!");
               token.setClientDigest(clientDigest);
               token.setIsLogin(true);
           }else {
               AssertUtil.notEmpty(tokenId,"请先登陆获取授权!");
               AssertUtil.notEmpty(userId,"用户ID不能为空!");
               token.setTokenId(tokenId);
               token.setUserName(username);
               token.setUserId(userId);
               token.setClientDigest(clientDigest);
               token.setIsLogin(false);
           }
           //5、委托给Realm进行登录
           getSubject(request, response).login(token);
       } catch (Exception e) {
           e.printStackTrace();
           //6、登录失败
           failure(response,"用户名或密码错误!",Constants.RESCODE_NOLOGIN);
           return false;//就直接返回给请求者.
       }
       return true;
    }
   
    //登录失败时默认返回 状态码
    private void failure(ServletResponse response, String message, int code){
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ResponseBase<String> responseMessage = new ResponseBase<>();
        responseMessage.setCode(code);//错误码
        responseMessage.setMsg(message);
        WebUtil.responseWrit(httpResponse, responseMessage);
     }

}