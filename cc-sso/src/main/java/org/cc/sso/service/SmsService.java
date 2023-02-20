package org.cc.sso.service;

import java.util.Map;

public interface SmsService {
    Map<String, Object> sendSms(String mobile);
    Map<String, Object> checkSms(String mobile, String smsCode);
}
