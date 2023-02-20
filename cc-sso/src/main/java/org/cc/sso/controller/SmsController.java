package org.cc.sso.controller;

import org.cc.sso.service.SmsService;
import org.cc.sso.vo.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequestMapping("sms")
@RestController
public class SmsController {
    @Autowired
    SmsService smsService;

    @PostMapping("send")
    public ResponseEntity<Object> sendSmsCode(@RequestBody Map<String, String> param) {
        String phoneNo = param.get("phone");
        Map<String, Object> sentCode = this.smsService.sendSms(phoneNo);
        ErrorResult.ErrorResultBuilder errorResultBuilder = ErrorResult.builder().errCode("-1").errMsg("短信验证码发送失败");
        int code = (Integer) sentCode.get("code");
        if(code == 0) {
            log.info("短信发送成功");
            return ResponseEntity.ok(null);
        } else if(code == 1) {
            log.info("验证码还未失效");
            errorResultBuilder.errCode("-2");
            errorResultBuilder.errMsg((String) sentCode.get("msg"));
        }
        ErrorResult errorResult = ErrorResult.builder().errMsg("短信验证码发送失败").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResultBuilder.build());
    }

    /**
     *
     */
    @PostMapping("verify")
    public ResponseEntity<Object> smsCodeVerify(@RequestBody Map<String, String> param) {
        Map<String, Object> checkSms = smsService.checkSms(param.get("phone"), param.get("smsCode"));
        ErrorResult.ErrorResultBuilder errorResultBuilder = ErrorResult.builder().errCode("-1").errMsg("短信验证码验证失败");
        int code = (Integer) checkSms.get("code");
        if(code == 0) {
            log.info("验证码验证成功");
            return ResponseEntity.ok(null);
        } else if(code == 1) {
            log.info("验证码验证失败");
            errorResultBuilder.errCode("-2");
            errorResultBuilder.errMsg((String) checkSms.get("msg"));
        }
        ErrorResult errorResult = ErrorResult.builder().errMsg("短信验证码发送失败").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResultBuilder.build());
    }
}
