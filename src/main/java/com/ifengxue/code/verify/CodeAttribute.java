package com.ifengxue.code.verify;

import java.util.Optional;

/**
 * 验证码属性
 */
public interface CodeAttribute {

  String _CODE = "_CODE";
  String _CODE_TYPE = "_CODE_TYPE";
  String CODE_SMS_TYPE = "_SMS";
  String CODE_EMAIL_TYPE = "_EMAIL";

  /**
   * 获取验证码，如果验证码不为null，则该验证码则为实际发送的验证码
   */
  default String getCode() {
    return Optional.ofNullable(getAttribute(_CODE)).map(Object::toString).orElse(null);
  }

  /**
   * 设置验证码
   */
  default void setCode(String code) {
    Args.isNotNull(code, "code不能为null");
    addAttribute(_CODE, code);
  }

  /**
   * 设置验证码类型
   *
   * @param codeType 验证码类型
   */
  default void setCodeType(String codeType) {
    Args.isNotNull(codeType, "codeType不能为null");
    addAttribute(_CODE_TYPE, codeType);
  }

  /**
   * 获取验证码类型
   */
  default String getCodeType() {
    return Optional.ofNullable(getAttribute(_CODE_TYPE)).map(Object::toString).orElse(null);
  }

  /**
   * 是否是短信类型验证码
   */
  default boolean isSmsType() {
    return CODE_SMS_TYPE.equals(getAttribute(_CODE_TYPE));
  }

  /**
   * 是否是邮箱类型验证码
   */
  default boolean isEmailType() {
    return CODE_EMAIL_TYPE.equals(getAttribute(_CODE_TYPE));
  }

  /**
   * 获取属性
   *
   * @param attributeName 属性名
   * @return 属性值
   */
  Object getAttribute(String attributeName);

  /**
   * 获取属性，如果属性不存在则取默认值
   *
   * @param attributeName 属性名
   * @param defaultValue 默认属性值
   * @return 属性值
   */
  default Object getAttributeOrDefault(String attributeName, Object defaultValue) {
    return Optional.ofNullable(getAttribute(attributeName)).orElse(defaultValue);
  }

  /**
   * 添加属性
   *
   * @param attributeName 属性名称
   * @param attributeValue 属性值
   */
  CodeAttribute addAttribute(String attributeName, Object attributeValue);

  /**
   * 移除属性
   *
   * @param attributeName 属性名称
   */
  default void removeAttribute(String attributeName) {
    removeAttributes(attributeName);
  }

  /**
   * 批量移除属性
   *
   * @param attributeNames 属性名列表
   */
  void removeAttributes(String... attributeNames);

  /**
   * 移除所有的属性
   */
  void clear();
}
