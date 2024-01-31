package com.magic.ssh;

import com.magic.ssh.controller.HelloController;
import com.magic.ssh.controller.SSHController;
import com.magic.ssh.entity.security.JwtUser;
import com.magic.ssh.util.JwtTokenUtil;
import com.magic.ssh.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SSHApplicationTests {

	@Autowired
	private HelloController hello;

	@Autowired
	private SSHController ssh;

	@Autowired
	private RedisUtil redisUtil;


	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;


	@Test
	void testHello() {
	}

	@Test
	void testToken(){
		JwtUser sysUser=new JwtUser();
		sysUser.setUsername("li");

		String token= JwtTokenUtil.sign(sysUser);
		log.info(token);

		log.info(String.valueOf(JwtTokenUtil.verify(token)));

	}

	@Test
	void testRedis(){

		redisUtil.set("hhhhh","ggggggg");

		redisUtil.del("test");

	}


}
