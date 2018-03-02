package com.ifengxue.code.verify.history;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * spring data redis cache
 */
public class SpringDataRedisCodeHistoryProvider implements CodeHistoryProvider {

  private final RedisTemplate<String, CodeHistory> redisTemplate;

  public SpringDataRedisCodeHistoryProvider(RedisTemplate<String, CodeHistory> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public CodeHistory get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public long getExpireMillis(String key) {
    return redisTemplate.getExpire(key);
  }

  @Override
  public void save(String key, CodeHistory history, long expireMillis) {
    if (expireMillis > 0) {
      redisTemplate.opsForValue().set(key, history, expireMillis, TimeUnit.MILLISECONDS);
    } else {
      redisTemplate.opsForValue().set(key, history);
    }
  }

  @Override
  public void delete(String key) {
    redisTemplate.delete(key);
  }
}
