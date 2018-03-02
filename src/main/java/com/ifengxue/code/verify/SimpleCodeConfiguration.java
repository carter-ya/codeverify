package com.ifengxue.code.verify;

import com.ifengxue.code.verify.event.CoderEvent;
import com.ifengxue.code.verify.filter.PostFilter;
import com.ifengxue.code.verify.history.CodeHistoryProvider;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 简单验证码配置
 */
public class SimpleCodeConfiguration implements CodeConfiguration {

  private final long postIntervalMillis;
  private final long codeExpiredMillis;
  private final CodeHistoryProvider historyProvider;
  private final CacheExpireCalculator cacheExpireCalculator;
  private final CopyOnWriteArrayList<PostFilter> postFilters;
  private final CopyOnWriteArrayList<CoderEvent> coderEvents;

  public SimpleCodeConfiguration(long postIntervalMillis, long codeExpiredMillis,
      CodeHistoryProvider historyProvider,
      CacheExpireCalculator cacheExpireCalculator,
      Set<PostFilter> postFilters,
      Set<CoderEvent> coderEvents) {
    Args.isNotNull(postFilters);
    Args.isNotNull(coderEvents);
    Args.isNotNull(historyProvider);
    Args.isNotNull(cacheExpireCalculator);

    this.postIntervalMillis = postIntervalMillis;
    this.codeExpiredMillis = codeExpiredMillis;
    this.postFilters = new CopyOnWriteArrayList<>(postFilters);
    this.postFilters.sort((o1, o2) -> o2.getOrder() - o1.getOrder());
    this.coderEvents = new CopyOnWriteArrayList<>(coderEvents);
    this.historyProvider = historyProvider;
    this.cacheExpireCalculator = cacheExpireCalculator;
  }

  @Override
  public long postIntervalMillis() {
    return postIntervalMillis;
  }

  @Override
  public long codeExpiredMillis() {
    return codeExpiredMillis;
  }

  @Override
  public List<PostFilter> getPostFilters() {
    return Collections.unmodifiableList(postFilters);
  }

  @Override
  public List<CoderEvent> getCoderEvents() {
    return Collections.unmodifiableList(coderEvents);
  }

  @Override
  public CodeHistoryProvider getHistoryProvider() {
    return historyProvider;
  }

  @Override
  public CacheExpireCalculator getCacheExpireCalculator() {
    return cacheExpireCalculator;
  }
}
