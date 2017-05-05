package dmiaes.app.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 
* @ClassName: RedisCfg
* @Description: redis信息类
* @author REEFE
* @date 2017-4-25 下午1:42:14
*  
*/
@Component("redisCfg")
@ConfigurationProperties(locations = "classpath:redis.properties",prefix="redis")
public class RedisCfg {
	private String host;
	private String password;
	private int database;
	private int port;
	private int timeout;
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getDatabase() {
		return database;
	}
	public void setDatabase(int database) {
		this.database = database;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
