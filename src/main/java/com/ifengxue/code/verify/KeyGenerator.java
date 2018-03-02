package com.ifengxue.code.verify;

/**
 * key生成器
 */
@FunctionalInterface
public interface KeyGenerator {

  /**
   * 生成key
   *
   * @param recipient 验证码接受者
   * @param context 上下文
   */
  String doGenerate(String recipient, Context context);
}
