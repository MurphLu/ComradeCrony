package org.cc.dubbo.provider.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.cc.dubbo.api.RecommendUserApi;
import org.cc.dubbo.pojo.RecommendUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RecommendUserApiTest {

    @DubboReference
    private RecommendUserApi api;

    @Test
    public void testFindOne() {
        RecommendUser recommendUser = api.queryWithMaxScore(1L);
        log.info("get recommendUser: {}", recommendUser);
    }
}
