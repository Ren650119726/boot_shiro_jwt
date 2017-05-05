package dmiaes.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dmiaes.app.base.RequestBase;
import dmiaes.app.base.ResponseBase;
import dmiaes.app.base.UserCredentials;
import dmiaes.app.config.jwt.JwtInfo;
import dmiaes.app.config.redis.RedisUtil;
import dmiaes.app.constants.Constants;
import dmiaes.app.system.service.UserService;

@RestController
@RequestMapping
public class LoginController {
	
	@Autowired
	private JwtInfo jwtInfo;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UserCredentials userCredentials;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseBase<?> login(@RequestBody RequestBase<?> params,HttpServletRequest request){
		ResponseBase<UserCredentials> responseMessage = new ResponseBase<>();
		Subject currenUser = SecurityUtils.getSubject();
		//验证成功
		if(currenUser.isAuthenticated()){
			responseMessage.setCode(Constants.RESCODE_SUCCESS);
			responseMessage.setMsg("登陆成功!");
	        responseMessage.setData(userCredentials);
		}
		return responseMessage;
	}
	/**
     * 退出登录
     */
    @RequestMapping(value="/logout")
    public ResponseBase<?>  logout() {
        ResponseBase<String> responseMessage = new ResponseBase<>();
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        redisUtil.remove(Constants.CACHE_TOKNID + userCredentials.getUserId());
        responseMessage.setMsg("退出成功!");
        return responseMessage;
    }
}
