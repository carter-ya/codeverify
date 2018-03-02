package com.ifengxue.code.verify.filter;

import com.ifengxue.code.verify.Args;
import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.PostResult;
import com.ifengxue.code.verify.history.CodeHistory;
import com.ifengxue.code.verify.history.CodeHistory.PastCode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

/**
 * 驱逐发送历史过滤器
 * <br>通常该过滤器的用法是驱逐昨日发送的验证码历史
 */
@Slf4j
public class BanishPostHistoryPostFilter implements PostFilter {

  private final MultiPredicate<Boolean, PastCode, Context> predicate;

  /**
   * @param predicate {@link LastDayPredicate}
   */
  public BanishPostHistoryPostFilter(MultiPredicate<Boolean, PastCode, Context> predicate) {
    Args.isNotNull(predicate);
    this.predicate = predicate;
  }

  @Override
  public PostResult doFilter(String recipient, boolean isFirstPost, CodeHistory history, Context context,
      PostChain postChain) throws Exception {
    if (isFirstPost) {
      return postChain.doFilter(recipient, true, history, context);
    }
    Iterator<PastCode> itr = history.getPastCodes().iterator();
    while (itr.hasNext()) {
      PastCode pc = itr.next();
      if (predicate.test(!itr.hasNext(), pc, context)) {
        log.debug("驱逐发送记录. 验证码[{}],发送时间[{}],是否有效[{}],过期时间[{}]",
            pc.getCode(), pc.getPostedAt(), pc.isValid(), pc.getExpiredAt());
        itr.remove();
        history.setPastCodesCount(history.getPastCodesCount() - 1);
      }
    }
    return postChain.doFilter(recipient, false, history, context);
  }

  @Override
  public int getOrder() {
    return HIGHEST_ORDER - 500;
  }


  @FunctionalInterface
  public interface MultiPredicate<B, P, C> {

    boolean test(B b, P p, C c);
  }

  /**
   * 是否是前一日发送的验证码验证器
   */
  public static class LastDayPredicate implements MultiPredicate<Boolean, PastCode, Context> {

    @Override
    public boolean test(Boolean isLastCode, PastCode pastCode, Context context) {
      LocalDateTime dayOfStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
      long dayOfStartMillis = Timestamp.valueOf(dayOfStart).getTime();
      if (!isLastCode) {
        return dayOfStartMillis > pastCode.getPostedAt();
      }
      return dayOfStartMillis > pastCode.getPostedAt() &&
          (!pastCode.isValid() || dayOfStartMillis > pastCode.getExpiredAt());
    }
  }
}
