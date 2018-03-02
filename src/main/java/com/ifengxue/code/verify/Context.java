package com.ifengxue.code.verify;

/**
 * 上下文
 */
public interface Context {

  /**
   * 设置验证码生成器
   */
  void setCodeGenerator(CodeGenerator generator);

  /**
   * 验证码生成器
   */
  CodeGenerator getCodeGenerator();

  /**
   * 设置验证码配置
   */
  void setCodeConfiguration(CodeConfiguration codeConfiguration);

  /**
   * 验证码配置
   */
  CodeConfiguration getCodeConfiguration();

  /**
   * 验证码属性
   */
  CodeAttribute getCodeAttribute();

  /**
   * 设置key生成器
   */
  void setKeyGenerator(KeyGenerator keyGenerator);

  /**
   * key生成器
   */
  KeyGenerator getKeyGenerator();

  /**
   * 克隆context
   */
  Context clone(CodeAttribute codeAttribute);
}
