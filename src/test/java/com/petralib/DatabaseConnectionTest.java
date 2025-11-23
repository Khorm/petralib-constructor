package com.petralib;

import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoTest
class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() {
        assertNotNull(dataSource, "DataSource should not be null");
        
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
            
            // Проверяем, что можем выполнить простой запрос
            boolean isValid = connection.isValid(5);
            assertTrue(isValid, "Connection should be valid");
            
            System.out.println("✅ Подключение к базе данных успешно!");
            System.out.println("   Database: " + connection.getCatalog());
            System.out.println("   URL: " + connection.getMetaData().getURL());
            
        } catch (SQLException e) {
            fail("Failed to connect to database: " + e.getMessage());
        }
    }
}

