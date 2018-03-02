package com.ifengxue.code.verify;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单的验证码属性实现
 */
public class SimpleCodeAttribute implements CodeAttribute {

  private final Map<String, Object> attributeMap = new HashMap<>();

  @Override
  public Object getAttribute(String attributeName) {
    return attributeMap.get(attributeName);
  }

  @Override
  public Object getAttributeOrDefault(String attributeName, Object defaultValue) {
    return attributeMap.getOrDefault(attributeName, defaultValue);
  }

  @Override
  public CodeAttribute addAttribute(String attributeName, Object attributeValue) {
    attributeMap.put(attributeName, attributeValue);
    return this;
  }

  @Override
  public void removeAttributes(String... attributeNames) {
    for (String attributeName : attributeNames) {
      attributeMap.remove(attributeName);
    }
  }

  @Override
  public void clear() {
    attributeMap.clear();
  }
}
