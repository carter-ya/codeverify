package com.ifengxue.code.verify;

/**
 * 验证码生成器
 */
public interface CodeGenerator {

  /**
   * 生成验证码
   */
  String doGenerate();

  /**
   * 固定验证码的验证码生成器
   */
  class FixedCodeGenerator implements CodeGenerator {

    private final String code;

    public FixedCodeGenerator(String code) {
      Args.isNotNull(code);
      this.code = code;
    }

    @Override
    public String doGenerate() {
      return code;
    }
  }
}
