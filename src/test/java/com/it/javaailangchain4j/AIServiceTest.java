package com.it.javaailangchain4j;

import com.it.javaailangchain4j.assistant.Assistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIServiceTest {

    @Autowired
    private Assistant assistant;
    @Test
    public  void  testChat(){
        String answer = assistant.chat("我是谁");
        System.out.println(answer);
    }
}
