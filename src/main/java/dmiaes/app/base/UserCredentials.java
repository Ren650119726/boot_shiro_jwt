package dmiaes.app.base;

import java.io.Serializable;

import org.springframework.stereotype.Component;

/** 
* @ClassName: UserCredentials
* @Description: 存放用户及凭证信息，交付给Spring管理默认单例模式，系统全局使用
* @author REEFE
* @date 2017-4-28 上午9:49:54
*  
*/
@Component
public class UserCredentials implements Serializable {
    private static final long serialVersionUID = 1L;
	private Long userId;
	private String userName;
	private String hospitalId;
	private String token;
	private String tokenType;
	private String digest;
  
    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
        return token;
    }
	
    public void setToken(String token) {
        this.token = token;
    }

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	
}
