package com.ifengxue.code.verify.history;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class CodeHistory implements Serializable {

  private static final long serialVersionUID = -3719175336552040856L;
  /**
   * 最后一次发送的验证码
   */
  private String code;
  /**
   * 是否有效
   */
  private boolean valid;
  /**
   * 过期时间，任何<code><0</code>的数字代表永不过期
   */
  private long expiredAt;
  /**
   * 发送下一次验证码的时间，任何<code><0</code>的数字代表永不过期
   */
  private long nextCodeAt;
  /**
   * 验证码被检查出错误的次数
   */
  private int incorrectCheckTimes;
  /**
   * 第一次发送时间
   */
  private long firstPostedAt;
  /**
   * 最后一次发送时间
   */
  private long lastPostedAt;
  /**
   * 过去发送的验证码数量
   */
  private int pastCodesCount;
  /**
   * 过去发送的验证码
   */
  private List<PastCode> pastCodes;

  /**
   * 添加最新发送的验证码
   *
   * @param code 验证码
   * @param expiredAt 过期时间，任何<code><0</code>的数字代表永不过期
   * @param nextCodeAt 发送下一次验证码的时间，任何<code><0</code>的数字代表永不过期
   */
  public void newPostCode(String code, long expiredAt, long nextCodeAt) {
    PastCode pastCode = PastCode.builder().code(code).postedAt(System.currentTimeMillis()).expiredAt(expiredAt)
        .valid(true).build();
    if (pastCodes == null || pastCodes.isEmpty()) {
      pastCodes = pastCodes == null ? new LinkedList<>() : pastCodes;
      firstPostedAt = pastCode.getPostedAt();
    }
    pastCodes.add(pastCode);
    pastCodesCount++;
    this.code = code;
    this.valid = true;
    this.expiredAt = expiredAt;
    this.nextCodeAt = nextCodeAt;
    this.incorrectCheckTimes = 0;
    this.lastPostedAt = pastCode.getPostedAt();
  }

  @Data
  @Builder
  public static class PastCode implements Serializable {

    private static final long serialVersionUID = 6364354276587783590L;
    private String code;
    /**
     * 发送时间
     */
    private long postedAt;
    /**
     * 过期时间，任何<code><0</code>的数字代表永不过期
     */
    private long expiredAt;
    /**
     * 是否有效
     */
    private boolean valid;
    /**
     * 验证码被检查出错误的次数
     */
    private int incorrectCheckTimes;
  }
}
