package com.dmiaes.test;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dmiaes.app.Application;
import dmiaes.app.model.User;
import dmiaes.app.system.service.UserService;

/**
 * @author zhao
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestUserService {
	
		@Resource
	    private UserService userService;

	    @Test
	    public void testFindById() {
	    	User user = userService.queryByUserId(1L);
	    	Assert.assertEquals("wzadmin", user.getUsername());
	    }

//	    @Test
	    public void testFindByUserName() {
	      
	    }
}
