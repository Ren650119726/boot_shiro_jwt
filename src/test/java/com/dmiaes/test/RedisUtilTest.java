package com.dmiaes.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import dmiaes.app.Application;
import dmiaes.app.config.redis.RedisUtil;
@SpringBootTest(classes=Application.class)
@RunWith(SpringRunner.class)
public class RedisUtilTest {
	@Autowired
	private RedisUtil redisUtil;
	
	@Test
	public void testSet(){
		redisUtil.set("test", "test11111111");
		Assert.assertEquals("test11111111",redisUtil.get("test").toString());
	}
}
