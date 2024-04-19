package com.moggi.quizmini;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author weiqirui
 * @Date 2024/4/19
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DbTest {

    @Test
    public void initial() {
        this.addDbFile();
        this.addTable();
    }

    @Test
    public void addDbFile() {
        Connection conn = null;
        try {
            // 连接SQLite的JDBC
            Class.forName("org.sqlite.JDBC");

            // 建立一个数据库名quizmini.db的连接，如果不存在就在当前目录下创建之
            conn = DriverManager.getConnection("jdbc:sqlite:quizmini.db");

            conn.close(); // 结束数据库的连接

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void addTable() {
        Connection conn = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:quizmini.db");
            // 创建Statement对象
            statement = conn.createStatement();

            // 创建表
            statement.executeUpdate(
                    "CREATE TABLE folder" +
                            "(" +
                            "    fo_pkid     INT PRIMARY KEY NOT NULL," +
                            "    fo_name     TEXT            NOT NULL," +
                            "    create_time TEXT            NOT NULL," +
                            "    modify_time TEXT            NOT NULL" +
                            ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE \"card\"" +
                            "(" +
                            "    ca_pkid            INT           not null" +
                            "        primary key," +
                            "    fo_pkid            INT           not null," +
                            "    grammatical_person TEXT," +
                            "    verb               TEXT," +
                            "    conjugation        TEXT          not null," +
                            "    eg_sentence        TEXT," +
                            "    review_time        TEXT," +
                            "    if_done            INT default 0 not null," +
                            "    create_time        TEXT          not null," +
                            "    modify_time        TEXT          not null," +
                            "    hit_times          INT default 0," +
                            "    last_review_time   TEXT" +
                            ")"
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // 关闭Statement和Connection
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
