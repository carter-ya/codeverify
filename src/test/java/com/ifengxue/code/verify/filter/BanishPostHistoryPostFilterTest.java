package com.ifengxue.code.verify.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.filter.BanishPostHistoryPostFilter.LastDayPredicate;
import com.ifengxue.code.verify.filter.PostFilter.PostChain;
import com.ifengxue.code.verify.history.CodeHistory;
import com.ifengxue.code.verify.history.CodeHistory.PastCode;
import java.util.Arrays;
import java.util.LinkedList;
import org.junit.Test;

public class BanishPostHistoryPostFilterTest {

  @Test
  public void doFilter() throws Exception {
    BanishPostHistoryPostFilter filter = new BanishPostHistoryPostFilter(new LastDayPredicate());
    String recipient = "1300000000";
    CodeHistory history = mock(CodeHistory.class);
    Context context = mock(Context.class);
    PostChain postChain = mock(PostChain.class);
    when(postChain.doFilter(recipient, false, history, context)).thenReturn(null);

    // 测试所有验证码都已经是今日之前发送的验证码，且最后一次验证码依旧有效
    when(history.getPastCodes()).thenReturn(new LinkedList<>(Arrays.asList(
        PastCode.builder().postedAt(1).code("expired").valid(true).build(),
        PastCode.builder().postedAt(1).code("expired2").valid(false).build(),
        PastCode.builder().postedAt(1).code("expired3").valid(true).expiredAt(System.currentTimeMillis()).build()
    )));
    filter.doFilter(recipient, false, history, context, postChain);
    assertEquals(1, history.getPastCodes().size());

    // 测试所有验证码都已经是今日之前发送的验证码，且最后一次验证码已经失效
    when(history.getPastCodes()).thenReturn(new LinkedList<>(Arrays.asList(
        PastCode.builder().postedAt(1).code("expired").valid(true).build(),
        PastCode.builder().postedAt(1).code("expired2").valid(false).build(),
        PastCode.builder().postedAt(1).code("expired3").valid(false).expiredAt(System.currentTimeMillis()).build()
    )));
    filter.doFilter(recipient, false, history, context, postChain);
    assertEquals(0, history.getPastCodes().size());

    // 测试所有验证码都已经是今日之前发送的验证码，且最后一次验证码有效，但是已经过期
    when(history.getPastCodes()).thenReturn(new LinkedList<>(Arrays.asList(
        PastCode.builder().postedAt(1).code("expired").valid(true).build(),
        PastCode.builder().postedAt(1).code("expired2").valid(false).build(),
        PastCode.builder().postedAt(1).code("expired3").valid(true).expiredAt(2).build()
    )));
    filter.doFilter(recipient, false, history, context, postChain);
    assertEquals(0, history.getPastCodes().size());

    // 测试所有验证码都是今日发送的
    when(history.getPastCodes()).thenReturn(new LinkedList<>(Arrays.asList(
        PastCode.builder().postedAt(System.currentTimeMillis()).code("expired").valid(true).build(),
        PastCode.builder().postedAt(System.currentTimeMillis()).code("expired2").valid(false).build(),
        PastCode.builder().postedAt(System.currentTimeMillis()).code("expired3").valid(true).expiredAt(2).build()
    )));
    filter.doFilter(recipient, false, history, context, postChain);
    assertEquals(3, history.getPastCodes().size());
  }
}