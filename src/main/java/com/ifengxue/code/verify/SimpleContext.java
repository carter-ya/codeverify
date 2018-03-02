package com.ifengxue.code.verify;

import static java.util.Collections.emptySet;

import com.ifengxue.code.verify.history.ConcurrentCodeHistoryProvider;
import lombok.Data;

/**
 * 简单上下文
 */
@Data
public class SimpleContext implements Context {

  private CodeGenerator codeGenerator = new SimpleCodeGenerator(6, true, false, false);
  private CodeConfiguration codeConfiguration = new SimpleCodeConfiguration(2 * 60 * 1_000, 15 * 60 * 1_000,
      new ConcurrentCodeHistoryProvider(), new SimpleCacheExpireCalculator(), emptySet(), emptySet());
  private CodeAttribute codeAttribute = new SimpleCodeAttribute();
  private KeyGenerator keyGenerator = new SimpleKeyGenerator("simple_cache_key:");

  @Override
  public void setCodeGenerator(CodeGenerator generator) {
    this.codeGenerator = generator;
  }

  @Override
  public CodeGenerator getCodeGenerator() {
    return codeGenerator;
  }

  @Override
  public CodeConfiguration getCodeConfiguration() {
    return codeConfiguration;
  }

  @Override
  public CodeAttribute getCodeAttribute() {
    return codeAttribute;
  }

  @Override
  public void setKeyGenerator(KeyGenerator keyGenerator) {
    this.keyGenerator = keyGenerator;
  }

  @Override
  public KeyGenerator getKeyGenerator() {
    return keyGenerator;
  }

  @Override
  public Context clone(CodeAttribute codeAttribute) {
    SimpleContext context = new SimpleContext();
    context.setCodeGenerator(codeGenerator);
    context.setCodeConfiguration(codeConfiguration);
    context.setCodeAttribute(codeAttribute);
    context.setKeyGenerator(keyGenerator);
    return context;
  }
}
