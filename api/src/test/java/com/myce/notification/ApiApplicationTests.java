package com.myce.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
class ApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
