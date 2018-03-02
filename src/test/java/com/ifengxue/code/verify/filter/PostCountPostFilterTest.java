package com.ifengxue.code.verify.filter;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ifengxue.code.verify.CodeConfiguration;
import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.PostResult;
import com.ifengxue.code.verify.PostResult.PostResultStatus;
import com.ifengxue.code.verify.SimpleCacheExpireCalculator;
import com.ifengxue.code.verify.filter.PostFilter.PostChain;
import com.ifengxue.code.verify.history.CodeHistory;
import org.junit.Test;

public class PostCountPostFilterTest {

  @Test
  public void doFilter() throws Exception {
    int acceptMaxCount = 5;
    PostCountPostFilter filter = new PostCountPostFilter(acceptMaxCount);
    String recipient = "13400000000";
    CodeHistory history = mock(CodeHistory.class);
    Context context = mock(Context.class);
    CodeConfiguration codeConfiguration = mock(CodeConfiguration.class);
    when(context.getCodeConfiguration()).thenReturn(codeConfiguration);
    when(codeConfiguration.getCacheExpireCalculator()).thenReturn(new SimpleCacheExpireCalculator());
    PostChain postChain = mock(PostChain.class);
    when(postChain.doFilter(recipient, true, history, context)).thenReturn(null);
    PostResult postResult = filter.doFilter(recipient, true, history, context, postChain);
    // 首次发送
    assertNull(postResult);

    // 发送数量在限制内
    when(history.getPastCodesCount()).thenReturn(acceptMaxCount - 1);
    postResult = filter.doFilter(recipient, false, history, context, postChain);
    assertNull(postResult);

    // 发送数量超出限制
    when(history.getPastCodesCount()).thenReturn(acceptMaxCount);
    postResult = filter.doFilter(recipient, false, history, context, postChain);
    assertNotNull(postResult);
    assertEquals(PostResultStatus.POST_OVER_COUNT_LIMIT, postResult.getStatus());
    System.out.println(postResult);
  }
}