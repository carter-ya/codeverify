package com.ifengxue.code.verify.history;

import com.ifengxue.code.verify.Args;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcurrentCodeHistoryProvider implements CodeHistoryProvider {

  private final ConcurrentMap<String, CodeHistoryHolder> cache = new ConcurrentHashMap<>();

  @Override
  public CodeHistory get(String key) {
    Args.isNotNull(key, "key不能为null");
    CodeHistoryHolder holder = cache.get(key);
    if (holder == null || holder.getExpiredAt() < 0) {
      return Optional.ofNullable(holder).map(CodeHistoryHolder::getCodeHistory).orElse(null);
    }
    long now = System.currentTimeMillis();
    if (holder.getExpiredAt() < now) {
      log.debug("delete key. key[{}],expiredAt[{}],now[{}]", key, holder.getExpiredAt(), now);
      delete(key);
      return null;
    }
    if (log.isDebugEnabled()) {
      log.debug("key[{}], pastCodesCount[{}}", key, holder.getCodeHistory().getPastCodesCount());
    }
    return holder.getCodeHistory();
  }

  @Override
  public long getExpireMillis(String key) {
    long expiredAt = Optional.ofNullable(cache.get(key)).map(CodeHistoryHolder::getExpiredAt)
        .orElseThrow(() -> new IllegalStateException("key:" + key + "不存在"));
    if (expiredAt < 0) {
      return expiredAt;
    }
    if (expiredAt < System.currentTimeMillis()) {// key 已过期
      throw new IllegalStateException("key:" + key + "不存在");
    }
    return expiredAt;
  }

  @Override
  public void save(String key, CodeHistory history, long expireMillis) {
    Args.isNotNull(key, "key不能为null");
    Args.isNotNull(history, "history不能为null");
    cache.put(key, new CodeHistoryHolder(history, expireMillis < 0 ? -1 : System.currentTimeMillis() + expireMillis));
  }

  @Override
  public void delete(String key) {
    Args.isNotNull(key, "key不能为null");
    cache.remove(key);
  }

  private static class CodeHistoryHolder {

    @Getter
    private final CodeHistory codeHistory;
    @Getter
    private final long expiredAt;

    CodeHistoryHolder(CodeHistory codeHistory, long expiredAt) {
      this.codeHistory = codeHistory;
      this.expiredAt = expiredAt;
    }
  }
}
