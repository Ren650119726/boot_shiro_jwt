package dmiaes.app.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dmiaes.app.config.redis.RedisCfg;
import dmiaes.app.filter.HttpServletRequestReplacedFilter;

/** 
* @ClassName: ShiroConfiguration
* @Description: shiro配置
* @author REEFE
* @date 2017-4-25 下午1:43:39
*  
*/
@Configuration
public class ShiroConfiguration {
	@Autowired
	private RedisCfg cfg;
	/**
	 * Shiro 入口
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
        Filter Chain定义说明
       1、一个URL可以配置多个Filter，使用逗号分隔
       2、当设置多个过滤器时，全部验证通过，才视为通过
       3、部分过滤器可指定参数，如perms，roles
       
     * @param securityManager  安全管理器
	 * @return ShiroFilterFactoryBean
     */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
		//设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		
		//
		shiroFilterFactoryBean.getFilters().put("statelessAuthc",
				authcFilter());
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
	
		filterChainDefinitionMap.put("/**", "statelessAuthc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}
	
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setSubjectFactory(subjectFactory());
	    /*
         * 禁用使用Sessions 作为存储策略的实现，但它没有完全地禁用Sessions
         * 所以需要配合context.setSessionCreationEnabled(false);
         */
        ((DefaultSessionStorageEvaluator)((DefaultSubjectDAO)securityManager.getSubjectDAO()).getSessionStorageEvaluator()).setSessionStorageEnabled(false);
       
		// 设置realm.
		securityManager.setRealm(realm());
		// 注入session管理器
		securityManager.setSessionManager(sessionManager());
		// 注入Redis缓存
		securityManager.setCacheManager(cacheManager());
		return securityManager;
	}
	
    /**
     * subject工厂管理器.
     * @return
     */
    @Bean
    public DefaultWebSubjectFactory subjectFactory(){
       StatelessDefaultSubjectFactory subjectFactory = new StatelessDefaultSubjectFactory();
       return subjectFactory;
    }
    
    /**
     * 限制同一账号登录同时登录人数控制
     * @return
     */
    public KickoutSessionControlFilter kickoutSessionControlFilter(){
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
        // 也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickoutSessionControlFilter.setCacheManager(cacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(1);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl("/kickout");
        return kickoutSessionControlFilter;
    }

    
    /**
     * session管理器：
     * sessionManager通过sessionValidationSchedulerEnabled禁用掉会话调度器，
     * 因为我们禁用掉了会话，所以没必要再定期过期会话了。
     * @return sessionManager
     */
    @Bean
    public DefaultSessionManager sessionManager(){
       DefaultSessionManager sessionManager = new DefaultSessionManager();
       sessionManager.setSessionValidationSchedulerEnabled(false);
       return sessionManager;
    }
    
    /**
     * 自定义realm.
     * @return
     */
  /*  @Bean
    public StatelessAuthorizingRealm statelessRealm(){
       StatelessAuthorizingRealm realm = new StatelessAuthorizingRealm();
       return realm;
    }*/
    
    @Bean
    public JWTRealm realm(){
    	JWTRealm realm = new JWTRealm();
    	return realm;
    }
    /**
     * 访问控制器.
     * @return
     */
  /*  @Bean
    public StatelessAccessControlFilter statelessAuthcFilter(){
       StatelessAccessControlFilter statelessAuthcFilter = new StatelessAccessControlFilter();
       return statelessAuthcFilter;
    }*/
  /*  @Bean
    public JWTVerifyingFilter authcFilter(){
    	JWTVerifyingFilter filter = new JWTVerifyingFilter();
        return filter;
    }*/
    
    @Bean
    public HttpServletRequestReplacedFilter authcFilter(){
    	HttpServletRequestReplacedFilter filter = new HttpServletRequestReplacedFilter();
        return filter;
    }
    
    /**
     *  Add.5.1
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
       AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
       authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
       return authorizationAttributeSourceAdvisor;
    }
   
    /**
     * Add.5.2
     *   自动代理所有的advisor:
     *   由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
       DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
       daap.setProxyTargetClass(true);
       return daap;
   }
    
	
    /**
	 * cacheManager 缓存 redis实现
	 * 使用的是shiro-redis开源插件
	 * @return
	 */
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		return redisCacheManager;
	}
	
	/**
	 * 配置shiro redisManager
	 * 使用的是shiro-redis开源插件
	 * @return
	 */
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(cfg.getHost());
		redisManager.setPassword(cfg.getPassword());
		redisManager.setPort(cfg.getPort());
		redisManager.setExpire(1800);// 配置缓存过期时间
		redisManager.setTimeout(cfg.getTimeout());
		return redisManager;
	}
    
}
