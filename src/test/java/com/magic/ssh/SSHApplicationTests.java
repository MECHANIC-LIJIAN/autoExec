package com.magic.ssh;

import com.magic.ssh.controller.SSHController;
import com.magic.ssh.util.SqliteUtils;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteConnectionConfig;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SSHApplicationTests {

	@Autowired
	private SSHController ssh;
	@Autowired
	DataSourceProperties dataSourceProperties;

	@Autowired
	protected ApplicationContext applicationContext;

	@Test
	public void query() {
		// 获取配置的数据源
		DataSource dataSource = applicationContext.getBean(DataSource.class);
		// 查看配置数据源信息
		System.out.println(dataSource);
		System.out.println(dataSource.getClass().getName());
		System.out.println(dataSourceProperties.getUrl());
		//执行SQL,输出查到的数据
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<?> resultList = jdbcTemplate.queryForList("select * from user");
		System.out.println(resultList);
	}


	@Test
	public void init() {
		SqliteUtils sqliteUtils = new SqliteUtils(dataSourceProperties);
		 sqliteUtils.sqliteDataSource();


	}

}
