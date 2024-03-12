package com.magic.ssh.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.sqlite.JDBC;
import org.sqlite.SQLiteConfig;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SqliteUtils {


    private final DataSourceProperties dataSourceProperties;

    public SqliteUtils(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @PostConstruct
    public void sqliteDataSource() {


        //尝试创建sqlite文件-不存在时创建
//        initSqliteFile(filePath);
        //创建数据源
        DataSource dataSource = build();
         try {
            //尝试初始化数据库-表不存在时创建
            initProDb(dataSource.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //创建sqlite文件
    public static void initSqliteFile(String filePath) {
        File file = new File(filePath);

        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public DataSource build() {
        //url不为空
        if (StringUtils.hasText(dataSourceProperties.getUrl())) {
            return DataSourceBuilder.create().
                    url(dataSourceProperties.getUrl()).driverClassName(JDBC.class.getName()).build();
        }
        //文件路径不为空
//        if (StringUtils.hasText(filePath)) {
//            return DataSourceBuilder.create().
//                    url("jdbc:sqlite:" + filePath).driverClassName(JDBC.class.getName()).build();
//        }
        return DataSourceBuilder.create().build();
    }

    /**
     * 初始化项目db
     *
     * @param connection
     */
    public static void initProDb(Connection connection) {
        //判断数据表是否存在
        boolean hasPro = false;
        try {
            hasPro = true;
            //测试数据表是否存在
            connection.prepareStatement("select * from ssh_host").execute();
        } catch (SQLException e) {
            //不存在
            log.debug("table ssh_host is not exist");
            hasPro = false;
        }
        //不存在时创建db
        if (!hasPro) {
            log.debug(">>>start init ssh db");
            String sql = "";
            ClassPathResource cpr = new ClassPathResource("sql/init.sql");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(cpr.getInputStream()));
                sql = reader.lines().collect(Collectors.joining(" "));
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //分割sql
            String[] sqls = sql.split(";");

            try {
                for (String str : sqls) {
                    System.out.println(str);
                    //开始初始化数据库
                    connection.setAutoCommit(false);
                    connection.prepareStatement(str).execute();
                }
                //提交sql
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            log.debug("finish init pro db>>>");
        } else {
            log.debug("pro db is exist");
        }
    }

}
