package com.ifengxue.code.verify.filter;

import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.PostResult;
import com.ifengxue.code.verify.PostResult.PostResultStatus;
import com.ifengxue.code.verify.history.CodeHistory;
import lombok.extern.slf4j.Slf4j;

/**
 * 发送数量过滤器
 */
@Slf4j
public class PostCountPostFilter implements PostFilter {

  private final int acceptMaxCount;

  /**
   * @param acceptMaxCount 允许的最大发送数量
   */
  public PostCountPostFilter(int acceptMaxCount) {
    this.acceptMaxCount = acceptMaxCount;
  }

  @Override
  public PostResult doFilter(String recipient, boolean isFirstPost, CodeHistory history, Context context,
      PostChain postChain) throws Exception {
    if (isFirstPost) {
      return postChain.doFilter(recipient, true, history, context);
    }
    log.debug("pastCodesCount[{}],acceptMaxCount[{}]", history.getPastCodesCount(), acceptMaxCount);
    if (history.getPastCodesCount() >= acceptMaxCount) {
      long nextPostableAt =
          context.getCodeConfiguration().getCacheExpireCalculator().expireMillis(recipient, history, context)
              + System.currentTimeMillis() + 1;
      return PostResult.builder()
          .status(PostResultStatus.POST_OVER_COUNT_LIMIT)
          .codeConfiguration(context.getCodeConfiguration())
          .build()
          .addError(PostResult.NEXT_POSTABLE_AT, nextPostableAt);
    }
    return postChain.doFilter(recipient, false, history, context);
  }

  @Override
  public int getOrder() {
    return HIGHEST_ORDER - 1000;
  }
}
