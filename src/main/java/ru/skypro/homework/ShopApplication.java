package ru.skypro.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения магазина, использующий Spring Boot.
 */
@SpringBootApplication
public class ShopApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
}
