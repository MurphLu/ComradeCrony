package org.cc.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.cc.server.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cc.sso.url}")
    private String url;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public User getUserByToken(String token) {
        User user = null;
        try {
            String userStr = this.restTemplate.getForObject(url + "/user/" + token, String.class);
            if(userStr == null) {
                return null;
            }
            user = MAPPER.readValue(userStr, User.class);
        } catch (JsonProcessingException e) {
            log.info("get login user error: {}", e.toString());
        }
        return user;
    }
}
