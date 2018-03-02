package com.ifengxue.code.verify.poster;

import com.ifengxue.code.verify.Content;
import com.ifengxue.code.verify.Context;

/**
 * 发送器
 */
public interface Poster {

  /**
   * 发送验证码
   *
   * @param recipient 接收者
   * @param content 验证码内容
   * @param context 上下文
   * @throws Exception 任何发送失败引发的异常
   */
  void post(String recipient, Content content, Context context) throws Exception;
}
