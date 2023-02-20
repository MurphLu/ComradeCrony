package org.cc.sso;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SSOApplicationTest {
    @Autowired
    RocketMQTemplate template;

    @Test
    public void testRocketMq() {
        Map<String,String> obj = new HashMap<>();
        obj.put("aa", "bb");
        template.convertAndSend("broker_comrade_crony", obj);
    }
}