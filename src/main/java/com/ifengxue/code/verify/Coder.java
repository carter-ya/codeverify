package com.ifengxue.code.verify;

import lombok.Getter;

/**
 * 验证码
 */
public interface Coder {

  /**
   * 发送验证码
   *
   * @param recipient 接收者
   * @param context 上下文
   * @param contentGenerator 验证码内容生成器
   * @return 发送结果
   */
  PostResult post(String recipient, Context context, ContentGenerator contentGenerator);

  /**
   * 检查验证码
   *
   * @param recipient 接收者
   * @param code 验证码
   * @param context 上下文
   * @return 检查结果
   */
  CodeCheckStatus check(String recipient, String code, Context context);

  /**
   * 检查验证码，当验证码正确时，则失效验证码
   *
   * @param recipient 接收者
   * @param code 验证码
   * @param context 上下文
   * @return 检查结果
   */
  CodeCheckStatus checkAndInvalid(String recipient, String code, Context context);

  /**
   * 验证码检查状态
   */
  @Getter
  enum CodeCheckStatus {
    /**
     * 验证码已过期
     */
    EXPIRED,
    /**
     * 验证码已失效（验证码正确，且在有效期内，只是验证次数超过允许的最大验证次数）
     */
    INVALID,
    /**
     * 验证码正确
     */
    CORRECT,
    /**
     * 验证码不正确
     */
    INCORRECT
  }
}
