package com.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(String... args) throws Exception {
        // 延迟执行，确保表已经创建
        Thread.sleep(2000);
        initializeDataFromSql();
    }
    
    private void initializeDataFromSql() {
        try {
            ClassPathResource resource = new ClassPathResource("init-data.sql");
            byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String sql = new String(bdata, StandardCharsets.UTF_8);
            
            // 分割SQL语句并执行
            String[] statements = sql.split(";");
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty() && !statement.startsWith("--")) {
                    jdbcTemplate.execute(statement);
                }
            }
        } catch (Exception e) {
            System.err.println("初始化数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 重置数据到初始状态
     */
    public void resetToInitialState() {
        try {
            // 清空现有数据
            jdbcTemplate.execute("DELETE FROM user_coupon");
            jdbcTemplate.execute("DELETE FROM `order`");
            jdbcTemplate.execute("DELETE FROM captcha");
            jdbcTemplate.execute("DELETE FROM coupon");
            jdbcTemplate.execute("DELETE FROM course");
            jdbcTemplate.execute("DELETE FROM user");
            
            // 重新初始化数据
            initializeDataFromSql();
        } catch (Exception e) {
            System.err.println("重置数据失败: " + e.getMessage());
            throw new RuntimeException("重置数据失败", e);
        }
    }
}