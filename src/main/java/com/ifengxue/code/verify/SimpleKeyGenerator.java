package com.ifengxue.code.verify;

/**
 * 简单key生成器
 */
public class SimpleKeyGenerator implements KeyGenerator {

  private final String prefix;

  /**
   * @param prefix key前缀
   */
  public SimpleKeyGenerator(String prefix) {
    Args.isNotNull(prefix);
    this.prefix = prefix;
  }

  @Override
  public String doGenerate(String recipient, Context context) {
    return prefix + recipient;
  }
}
