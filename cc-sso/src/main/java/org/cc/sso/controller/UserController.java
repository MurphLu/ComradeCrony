package org.cc.sso.controller;

import org.cc.sso.pojo.User;
import org.cc.sso.service.UserService;
import org.cc.sso.vo.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping("loginVerification")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> param) {
        try {
            String mobile = param.get("phone");
            String code = param.get("smsCode");
            String tokenResp = this.userService.login(mobile, code);
            if(StringUtils.isNotEmpty(tokenResp)) {
                String[] split = StringUtils.split(tokenResp, "|");
                boolean isNew = Boolean.parseBoolean(split[0]);
                String token = split[1];
                Map<String, Object> result = new HashMap<>();
                result.put("isNew", isNew);
                result.put("token", token);
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            log.error("登录失败：", e);
        }
        ErrorResult.ErrorResultBuilder builder = ErrorResult.builder().errCode("-1").errMsg("登录失败");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(builder.build());
    }

    @GetMapping("{token}")
    public User queryUserByToken(@PathVariable("token") String token) {
        return this.userService.queryUserByToken(token);
    }
}
