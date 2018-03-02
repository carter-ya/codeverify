package com.ifengxue.code.verify;

/**
 * 验证码内容生成器
 */
@FunctionalInterface
public interface ContentGenerator {

  /**
   * 生成验证码内容
   *
   * @param code 验证码
   * @param context 上下文
   */
  Content doGenerate(String code, Context context);
}