package com.ifengxue.code.verify;

import lombok.Getter;
import lombok.ToString;

@ToString
public class EmailContent extends Content {

  /**
   * 邮件主题
   */
  @Getter
  private final String subject;

  public EmailContent(String subject, String content) {
    super(content);
    Args.isNotNull(subject);
    this.subject = subject;
  }
}
