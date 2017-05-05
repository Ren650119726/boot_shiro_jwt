package dmiaes.app.config.redis;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/** 
* @ClassName: RedisUtil
* @Description: rediscache工具类
* @author REEFE
* @date 2017-4-28 下午3:48:19
*  
*/
@SuppressWarnings("unchecked")
@Service
public class RedisUtil {
	
	 @SuppressWarnings("rawtypes")  
	  @Autowired  
	  private RedisTemplate redisTemplate;
	
	  /** 
	   * 批量删除对应的value 
	   *  
	   * @param keys 
	   */  
	  public void remove(final String... keys) {  
	      for (String key : keys) {  
	          remove(key);  
	      }  
	  }  
	
	  /** 
	   * 批量删除key 
	   *  
	   * @param pattern 
	   */  
	  public void removePattern(final String pattern) {  
	      Set<Serializable> keys = redisTemplate.keys(pattern);  
	      if (keys.size() > 0)  
	          redisTemplate.delete(keys);  
	  }  
	
	  /** 
	   * 删除对应的value 
	   *  
	   * @param key 
	   */  
	  public void remove(final String key) {  
	      if (exists(key)) {  
	          redisTemplate.delete(key);  
	      }  
	  }  
	
	  /** 
	   * 判断缓存中是否有对应的value 
	   *  
	   * @param key 
	   * @return 
	   */  
	  public boolean exists(final String key) {  
	      return redisTemplate.hasKey(key);  
	  }  
	
	  /** 
	   * 读取缓存 
	   *  
	   * @param key 
	   * @return 
	   */  
	  public Object get(final String key) {  
	      Object result = null;  
	      ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();  
	      result = operations.get(key);  
	      return result;  
	  }  
	
	  /** 
	   * 写入缓存 
	   *  
	   * @param key 
	   * @param value 
	   * @return 
	   */  
	  public boolean set(final String key, Object value) {  
	      boolean result = false;  
	      try {  
	          ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();  
	          operations.set(key, value);  
	          result = true;  
	      } catch (Exception e) {  
	          e.printStackTrace();  
	      }  
	      return result;  
	  }  
	
	  /** 
	   * 写入缓存 
	   *  
	   * @param key 
	   * @param value 
	   * @return 
	   */  
	  public boolean set(final String key, Object value, Long expireTime) {  
	      boolean result = false;  
	      try {  
	          ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();  
	          operations.set(key, value);  
	          redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);  
	          result = true;  
	      } catch (Exception e) {  
	          e.printStackTrace();  
	      }  
	      return result;  
	  }  
}
