package com.ifengxue.code.verify.poster;

import com.ifengxue.code.verify.CodeAttribute;
import com.ifengxue.code.verify.Content;
import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.EmailContent;
import com.ifengxue.code.verify.SimpleContext;
import org.junit.Test;

public class LoggerPosterTest {

  @Test
  public void postSms() {
    Content content = new Content("验证码是1234，即将在30分钟后过期。");
    Context context = new SimpleContext();
    String recipient = "1340000000";
    LoggerPoster poster = new LoggerPoster();
    poster.post(recipient, content, context);
  }

  @Test
  public void postEmail() {
    Content content = new EmailContent("邮箱验证", "验证码是1234，即将在30分钟后过期。");
    Context context = new SimpleContext();
    context.getCodeAttribute().setCodeType(CodeAttribute.CODE_EMAIL_TYPE);
    String recipient = "example@example.com";
    LoggerPoster poster = new LoggerPoster();
    poster.post(recipient, content, context);
  }
}