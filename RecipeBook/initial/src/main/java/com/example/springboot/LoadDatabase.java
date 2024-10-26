package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//this is a Database Seed that allow dynamic addition of categories in the future
//insert the predefined categories on application startup
@Configuration
public class LoadDatabase {

    @Autowired
    private CategoryRepository categoryRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.save(new Category("BREAKFAST"));
                categoryRepository.save(new Category("LUNCH"));
                categoryRepository.save(new Category("DINNER"));
                categoryRepository.save(new Category("VEGAN"));
                categoryRepository.save(new Category("DAIRY"));
                categoryRepository.save(new Category("PROTEIN"));
                categoryRepository.save(new Category("SPICY"));
            }
        };
    }
}
