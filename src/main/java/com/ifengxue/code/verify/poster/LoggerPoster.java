package com.ifengxue.code.verify.poster;

import com.ifengxue.code.verify.Content;
import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.EmailContent;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过记录日志发送验证码
 */
@Slf4j
public class LoggerPoster implements Poster {

  @Override
  public void post(String recipient, Content content, Context context) {
    if (content instanceof EmailContent) {
      log.info("验证码接收者[{}]，验证码主题[{}]，验证码内容[{}]", recipient, ((EmailContent) content).getSubject(),
          content.getContent());
    } else {
      log.info("验证码接收者[{}]，验证码内容[{}]", recipient, content.getContent());
    }
  }
}
