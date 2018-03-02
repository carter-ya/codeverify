package com.ifengxue.code.verify;

import com.ifengxue.code.verify.history.CodeHistory;
import java.time.Duration;
import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单缓存过期时间计算器
 */
@Slf4j
public class SimpleCacheExpireCalculator implements CacheExpireCalculator {

  @Override
  public long expireMillis(String recipient, CodeHistory history, Context context) {
    long expiredMillis = context.getCodeConfiguration().codeExpiredMillis();
    LocalTime max = LocalTime.MAX;
    LocalTime now = LocalTime.now();
    Duration duration = Duration.between(now, max);
    long durationMillis = duration.toMillis();
    long maxExpireMillis = Math.max(durationMillis, expiredMillis);
    log.debug("now[{}],max[{}],expireMillis[{}]", now, max, maxExpireMillis);
    return maxExpireMillis;
  }
}
