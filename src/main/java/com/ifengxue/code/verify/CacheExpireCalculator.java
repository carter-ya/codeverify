package com.ifengxue.code.verify;

import com.ifengxue.code.verify.history.CodeHistory;

/**
 * 缓存过期时间计算器
 */
@FunctionalInterface
public interface CacheExpireCalculator {

  /**
   * 计算过期时间
   *
   * @param recipient 接收者
   * @param history 发送历史
   * @param context 上下文
   * @return <code><0</code>永不过期;<code>>0</code>过期时间
   */
  long expireMillis(String recipient, CodeHistory history, Context context);
}
