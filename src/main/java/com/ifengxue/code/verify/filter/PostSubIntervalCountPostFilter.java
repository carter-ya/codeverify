package com.ifengxue.code.verify.filter;

import static java.util.stream.Collectors.toList;

import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.PostResult;
import com.ifengxue.code.verify.PostResult.PostResultStatus;
import com.ifengxue.code.verify.history.CodeHistory;
import com.ifengxue.code.verify.history.CodeHistory.PastCode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 发送子周期频率控制
 *
 * @see PostIntervalPostFilter
 */
@Slf4j
public class PostSubIntervalCountPostFilter implements PostFilter {

  private final long intervalMillis;
  private final int acceptMaxCount;

  /**
   * 距当前时间<code>intervalMillis</code>内所允许的<code>acceptMaxCount</code>最大验证码发送数量
   */
  public PostSubIntervalCountPostFilter(long intervalMillis, int acceptMaxCount) {
    this.intervalMillis = intervalMillis;
    this.acceptMaxCount = acceptMaxCount;
  }

  @Override
  public PostResult doFilter(String recipient, boolean isFirstPost, CodeHistory history, Context context,
      PostChain postChain) throws Exception {
    if (isFirstPost) {
      return postChain.doFilter(recipient, true, history, context);
    }
    // 子周期区间
    long right = System.currentTimeMillis();
    long left = right - intervalMillis;
    List<PastCode> rangeList = history.getPastCodes().stream()
        .filter(pastCode -> pastCode.getPostedAt() >= left && pastCode.getPostedAt() <= right).collect(toList());
    int count = rangeList.size();
    log.debug("left[{}],right[{}],count/acceptMaxCount[{}/{}]", left, right, count, acceptMaxCount);
    if (count >= acceptMaxCount) {
      return PostResult.builder()
          .status(PostResultStatus.POST_FREQUENTLY)
          .codeConfiguration(context.getCodeConfiguration())
          .build()
          .addError(PostResult.NEXT_POSTABLE_AT, rangeList.get(1).getPostedAt() + intervalMillis);
    }
    return postChain.doFilter(recipient, false, history, context);
  }

  @Override
  public int getOrder() {
    return NORMAL_ORDER;
  }
}
