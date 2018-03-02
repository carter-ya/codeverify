package com.ifengxue.code.verify;

import com.ifengxue.code.verify.Coder.CodeCheckStatus;

/**
 * 验证码门面
 */
public interface CoderFacade {

  /**
   * 发送验证码
   *
   * @param recipient 接收者
   * @return 发送结果
   */
  PostResult post(String recipient);

  /**
   * 发送验证码
   *
   * @param recipient 接收者
   * @param attribute 验证码属性
   * @return 发送结果
   */
  PostResult post(String recipient, CodeAttribute attribute);

  /**
   * 检查验证码
   *
   * @param recipient 接收者
   * @param code 验证码
   * @return 检查结果
   */
  CodeCheckStatus check(String recipient, String code);

  /**
   * 检查验证码
   *
   * @param recipient 接收者
   * @param code 验证码
   * @param codeAttribute 验证码属性
   * @return 检查结果
   */
  CodeCheckStatus check(String recipient, String code, CodeAttribute codeAttribute);

  /**
   * 检查验证码，当验证码正确时，则失效验证码
   *
   * @param recipient 接收者
   * @param code 验证码
   * @return 检查结果
   */
  CodeCheckStatus checkAndInvalid(String recipient, String code);

  /**
   * 检查验证码，当验证码正确时，则失效验证码
   *
   * @param recipient 接收者
   * @param code 验证码
   * @param codeAttribute 验证码属性
   * @return 检查结果
   */
  CodeCheckStatus checkAndInvalid(String recipient, String code, CodeAttribute codeAttribute);
}
