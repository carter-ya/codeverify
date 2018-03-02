package com.ifengxue.code.verify.filter;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.PostResult;
import com.ifengxue.code.verify.PostResult.PostResultStatus;
import com.ifengxue.code.verify.filter.PostFilter.PostChain;
import com.ifengxue.code.verify.history.CodeHistory;
import org.junit.Test;

public class PostIntervalPostFilterTest {

  @Test
  public void doFilter() throws Exception {
    CodeHistory history = mock(CodeHistory.class);
    Context context = mock(Context.class);
    PostChain postChain = mock(PostChain.class);

    String recipient = "13000000000";
    PostIntervalPostFilter filter = new PostIntervalPostFilter();

    // 无法送间隔
    when(history.getNextCodeAt()).thenReturn(-1L);
    PostResult postResult = filter.doFilter(recipient, false, history, context, postChain);
    assertNull(postResult);

    // 没过发送间隔，不允许发送
    when(history.getNextCodeAt()).thenReturn(1L);
    postResult = filter.doFilter(recipient, false, history, context, postChain);
    assertNull(postResult);

    // 过了发送间隔，可以发送
    when(history.getNextCodeAt()).thenReturn(System.currentTimeMillis() + 100_000L);
    postResult = filter.doFilter(recipient, false, history, context, postChain);
    assertEquals(PostResultStatus.POST_FREQUENTLY, postResult.getStatus());
    System.out.println(postResult);
  }
}