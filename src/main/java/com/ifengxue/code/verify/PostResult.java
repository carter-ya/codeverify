package com.ifengxue.code.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * 验证码发送结果
 */
@Data
@Builder
public class PostResult {

  /**
   * 下一次可发送时间
   */
  public static final String NEXT_POSTABLE_AT = "next_postable_at";
  /**
   * 发送结果
   */
  private PostResultStatus status;
  /**
   * 验证码
   */
  private String code;
  /**
   * 发送内容
   */
  private Content content;
  /**
   * 发送配置信息
   */
  private CodeConfiguration codeConfiguration;
  /**
   * 验证码发送失败绑定的错误属性
   */
  private Map<String, Object> bindErrorMap;

  @Getter
  public enum PostResultStatus {
    /**
     * 发送成功
     */
    SUCCESS,
    /**
     * 发送过于频繁
     */
    POST_FREQUENTLY,
    /**
     * 发送超出数量限制
     */
    POST_OVER_COUNT_LIMIT,
    /**
     * 发送时发生错误
     */
    INTERNAL_ERROR
  }

  /**
   * 绑定错误信息
   */
  public PostResult addError(String key, Object value) {
    if (bindErrorMap == null) {
      bindErrorMap = new HashMap<>();
    }
    bindErrorMap.put(key, value);
    return this;
  }

  /**
   * 获取错误信息
   */
  public Object getError(String key) {
    return Optional.ofNullable(bindErrorMap).map(m -> m.get(key)).orElse(null);
  }
}
