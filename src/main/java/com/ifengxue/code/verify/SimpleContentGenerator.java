package com.ifengxue.code.verify;

import java.util.concurrent.TimeUnit;

/**
 * 简单内容生成器
 */
public class SimpleContentGenerator implements ContentGenerator {

  @Override
  public Content doGenerate(String code, Context context) {
    long codeExpiredMillis = context.getCodeConfiguration().codeExpiredMillis();
    String content;
    if (codeExpiredMillis > 0) {
      content = String.format("您的验证码是%s，该验证码即将在%s分钟后失效。", code,
          TimeUnit.MILLISECONDS.toMinutes(context.getCodeConfiguration().codeExpiredMillis()));
    } else {
      content = String.format("您的验证码是%s。", code);
    }
    if (context.getCodeAttribute().isEmailType()) {
      return new EmailContent("邮箱验证", content);
    }
    return new Content(content);
  }
}
