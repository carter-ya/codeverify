package com.ifengxue.code.verify.event;

import com.ifengxue.code.verify.Context;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerCoderEvent implements CoderEvent {

  @Override
  public void onEvent(EventType type, Context context, Object... params) {
    log.info("事件[{}], 参数[{}]", type, Arrays.toString(params));
  }
}
