package com.ifengxue.code.verify;

import com.ifengxue.code.verify.event.CoderEvent;
import com.ifengxue.code.verify.filter.PostFilter;
import com.ifengxue.code.verify.history.CodeHistoryProvider;
import java.util.List;

/**
 * 验证码配置信息
 */
public interface CodeConfiguration {

  /**
   * 每次验证码之间的发送间隔，任何<code><0</code>的数字都认为无间隔
   */
  long postIntervalMillis();

  /**
   * 验证码过期市场，任何<code><=0</code>的数字都认为永久有效
   */
  long codeExpiredMillis();

  /**
   * 发送过滤器列表
   */
  List<PostFilter> getPostFilters();

  /**
   * 验证码事件列表
   */
  List<CoderEvent> getCoderEvents();

  /**
   * 验证码历史provider
   */
  CodeHistoryProvider getHistoryProvider();

  /**
   * 缓存时间计算器
   */
  CacheExpireCalculator getCacheExpireCalculator();
}
