package com.ifengxue.code.verify.filter;

import com.ifengxue.code.verify.Args;
import com.ifengxue.code.verify.Context;
import com.ifengxue.code.verify.Ordered;
import com.ifengxue.code.verify.PostResult;
import com.ifengxue.code.verify.history.CodeHistory;
import java.util.ListIterator;

/**
 * 发送过滤器
 */
public interface PostFilter extends Ordered {

  /**
   * 执行过滤
   *
   * @param recipient 接收者
   * @param isFirstPost 是否是首次发送.true:是;false:不是
   * @param history 发送历史
   * @param context 上下文
   * @param postChain 发送过滤器执行链
   * @return null:可以发送验证码;非null:不可发送验证码
   */
  PostResult doFilter(String recipient, boolean isFirstPost, CodeHistory history, Context context, PostChain postChain)
      throws Exception;

  /**
   * 过滤器执行链
   */
  class PostChain {

    private final ListIterator<PostFilter> itr;

    public PostChain(ListIterator<PostFilter> itr) {
      Args.isNotNull(itr);
      this.itr = itr;
    }

    public PostResult doFilter(String recipient, boolean isFirstPost, CodeHistory history, Context context)
        throws Exception {
      return itr.hasNext() ? itr.next().doFilter(recipient, isFirstPost, history, context, this) : null;
    }
  }
}
