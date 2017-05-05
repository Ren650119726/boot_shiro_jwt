package dmiaes.app.system.service;

import dmiaes.app.model.User;

/** 
* @ClassName: UserService
* @Description:用户信息
* @author REEFE
* @date 2017-4-26 下午4:04:21
*  
*/
public interface UserService {
	/**
	 * 查询用户
	 * @param br
	 */
	User queryByUserId(Long id);

	User queryByUserName(String userName);

	User queryByUserNameAndPassWord(String userName, String passWord);

}
