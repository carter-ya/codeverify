package com.ifengxue.code.verify.event;

import com.ifengxue.code.verify.Context;

/**
 * 验证码事件
 */
@FunctionalInterface
public interface CoderEvent {

  /**
   * 发布事件
   *
   * @param type 事件类型
   * @param context 上下文
   * @param params 附加参数
   */
  void onEvent(EventType type, Context context, Object... params);
}
