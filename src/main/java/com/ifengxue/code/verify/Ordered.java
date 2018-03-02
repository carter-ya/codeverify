package com.ifengxue.code.verify;

/**
 * 可排序的
 */
public interface Ordered {

  /**
   * 最低优先级执行顺序
   */
  int LOWEST_ORDER = Integer.MIN_VALUE;
  /**
   * 默认优先级执行顺序
   */
  int NORMAL_ORDER = 0;
  /**
   * 最高优先级执行顺序
   */
  int HIGHEST_ORDER = Integer.MAX_VALUE;

  /**
   * 顺序
   */
  default int getOrder() {
    return NORMAL_ORDER;
  }
}
