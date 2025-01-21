package com.cariniana.appalurachallengebooks;

import com.cariniana.appalurachallengebooks.dto.BookResponse;
import com.cariniana.appalurachallengebooks.menu.Menu;
import com.cariniana.appalurachallengebooks.services.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Menu menu = new Menu();

        menu.showMenu();

    }
}
