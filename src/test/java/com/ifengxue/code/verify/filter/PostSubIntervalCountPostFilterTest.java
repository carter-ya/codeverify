package com.ifengxue.code.verify.filter;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.PostResult;
import com.ifengxue.code.verify.PostResult.PostResultStatus;
import com.ifengxue.code.verify.filter.PostFilter.PostChain;
import com.ifengxue.code.verify.history.CodeHistory;
import com.ifengxue.code.verify.history.CodeHistory.PastCode;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class PostSubIntervalCountPostFilterTest {

  @Test
  public void doFilter() throws Exception {
    long intervalMillis = 60 * 60 * 1000;
    int acceptMaxCount = 3;
    PostSubIntervalCountPostFilter filter = new PostSubIntervalCountPostFilter(intervalMillis, acceptMaxCount);
    String recipient = "13000000000";
    CodeHistory history = mock(CodeHistory.class);
    Context context = mock(Context.class);
    PostChain postChain = mock(PostChain.class);
    when(postChain.doFilter(recipient, false, history, context)).thenReturn(null);

    // 无发送记录情况
    when(history.getPastCodes()).thenReturn(Collections.emptyList());
    PostResult postResult = filter.doFilter(recipient, false, history, context, postChain);
    assertNull(postResult);

    // 子周期内发送数量未超过限定值情况
    when(history.getPastCodes())
        .thenReturn(Arrays.asList(
            PastCode.builder().postedAt(System.currentTimeMillis()).build(),
            PastCode.builder().postedAt(System.currentTimeMillis()).build()));
    postResult = filter.doFilter(recipient, false, history, context, postChain);
    assertNull(postResult);

    // 子周期内发送数量超出限定值情况
    when(history.getPastCodes()).thenReturn(Arrays.asList(
        PastCode.builder().postedAt(System.currentTimeMillis()).build(),
        PastCode.builder().postedAt(System.currentTimeMillis()).build(),
        PastCode.builder().postedAt(System.currentTimeMillis()).build()));
    postResult = filter.doFilter(recipient, false, history, context, postChain);
    assertNotNull(postResult);
    assertEquals(PostResultStatus.POST_FREQUENTLY, postResult.getStatus());
    System.out.println(postResult);

  }
}