package com.ifengxue.code.verify.filter;

import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.PostResult;
import com.ifengxue.code.verify.PostResult.PostResultStatus;
import com.ifengxue.code.verify.history.CodeHistory;
import lombok.extern.slf4j.Slf4j;

/**
 * 发送周期频率过滤器
 *
 * @see PostSubIntervalCountPostFilter
 */
@Slf4j
public class PostIntervalPostFilter implements PostFilter {

  @Override
  public PostResult doFilter(String recipient, boolean isFirstPost, CodeHistory history, Context context,
      PostChain postChain) throws Exception {
    if (isFirstPost) {
      return postChain.doFilter(recipient, true, history, context);
    }
    long now = System.currentTimeMillis();
    log.debug("nextCodeAt[{}],now[{}]", history.getNextCodeAt(), now);
    if (history.getNextCodeAt() >= 0 && history.getNextCodeAt() > now) {
      return PostResult.builder()
          .status(PostResultStatus.POST_FREQUENTLY)
          .codeConfiguration(context.getCodeConfiguration())
          .build()
          .addError(PostResult.NEXT_POSTABLE_AT, history.getNextCodeAt());// 发送频率过于频繁
    }
    return postChain.doFilter(recipient, false, history, context);
  }

  @Override
  public int getOrder() {
    return HIGHEST_ORDER - 2000;
  }
}
