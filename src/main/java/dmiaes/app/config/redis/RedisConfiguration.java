package dmiaes.app.config.redis;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
* @ClassName: RedisConfiguration
* @Description:  缓存配置
* @author REEFE
* @date 2017-4-25 下午1:42:34
*  
*/
@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport{
	// 日志对象
	private static Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);
	@Resource
	private RedisCfg cfg;
	
	@Bean
	@Override
	public CacheManager cacheManager() {
	    RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
	    cacheManager.setDefaultExpiration(60 * 30); // 30min
	    return cacheManager;
	}

	 @Bean
	public RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(connectionFactory());
		
		redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		
		return redisTemplate;
	}
	 @Bean
	public JedisConnectionFactory connectionFactory(){
		JedisConnectionFactory conn = new JedisConnectionFactory();
		
		conn.setDatabase(cfg.getDatabase());
		conn.setHostName(cfg.getHost());
		conn.setPassword(cfg.getPassword());
		conn.setPort(cfg.getPort());
		conn.setTimeout(cfg.getTimeout());
		return conn;
	}
	
	
	
	 /**
	     * 自定义key.
	     * 此方法将会根据完全限定类名+方法名+所有参数的值生成唯一的一个key,即使@Cacheable中的value属性一样，key也会不一样。
	     */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
	    return new KeyGenerator() {
	        @Override
	        public Object generate(Object o, Method method, Object... objects) {
	            StringBuilder sb = new StringBuilder();
	            sb.append(o.getClass().getName());
	            sb.append(method.getName());
	            for (Object obj : objects) {
	                sb.append(obj.toString());
	            }
	            logger.info("RedisCacheConfig : keyGenerator <== " + sb.toString());
	            return sb.toString();
	        }
	    };
	}

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        // 用于捕获从Cache中进行CRUD时的异常的回调处理器。
        return new SimpleCacheErrorHandler();
    }
    
    /** 
     * 设置序列化方法 
     */  
    @SuppressWarnings("unused")
	private void setMySerializer(RedisTemplate<Object, Object> template) {  
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);  
        ObjectMapper om = new ObjectMapper();  
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);  
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);  
        jackson2JsonRedisSerializer.setObjectMapper(om);  
        template.setKeySerializer(template.getStringSerializer());  
        template.setValueSerializer(jackson2JsonRedisSerializer);  
    }
	
}
