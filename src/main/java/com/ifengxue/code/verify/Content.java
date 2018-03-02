package com.ifengxue.code.verify;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Content {

  /**
   * 内容
   */
  @Getter
  private final String content;

  public Content(String content) {
    Args.isNotNull(content);
    this.content = content;
  }
}
