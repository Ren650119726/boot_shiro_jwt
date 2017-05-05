package dmiaes.app.system.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import dmiaes.app.dao.UserRepository;
import dmiaes.app.model.User;
import dmiaes.app.system.service.UserService;
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Resource
    private UserRepository userRepository;
	
	@Override
	public User queryByUserId(Long id) {
	
		return userRepository.queryByUserid(id);
	}

	@Override
	public User queryByUserName(String userName) {
		return userRepository.queryByUsername(userName);
	}

	@Override
	public User queryByUserNameAndPassWord(String userName, String passWord) {
		
		return userRepository.queryByUsernameAndPassword(userName, passWord);
	}
}
