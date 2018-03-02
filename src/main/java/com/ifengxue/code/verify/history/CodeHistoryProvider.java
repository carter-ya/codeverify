package com.ifengxue.code.verify.history;

public interface CodeHistoryProvider {

  /**
   * 获取历史
   *
   * @param key key
   */
  CodeHistory get(String key);

  /**
   * 获取过期时间
   *
   * @param key key
   * @return 缓存有效期，任何<code><0</code>的数字都认为永久有效
   */
  long getExpireMillis(String key);

  /**
   * 保存历史
   *
   * @param key key
   * @param history 历史
   * @param expireMillis 缓存有效期，任何<code><0</code>的数字都认为永久有效
   */
  void save(String key, CodeHistory history, long expireMillis);

  /**
   * 删除历史
   *
   * @param key key
   */
  void delete(String key);
}
