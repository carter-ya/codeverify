package com.ifengxue.code.verify;

import org.junit.Test;

public class SimpleCodeGeneratorTest {

  @Test
  public void doGenerate() {
    CodeGenerator generator = new SimpleCodeGenerator(4, true, false, false);
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    generator = new SimpleCodeGenerator(4, true, true, false);
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    generator = new SimpleCodeGenerator(6, true, true, true);
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    generator = new SimpleCodeGenerator(6, false, true, true);
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
    System.out.println(generator.doGenerate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void doGenerate2() {
    new SimpleCodeGenerator(0, true, true, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void doGenerate3() {
    new SimpleCodeGenerator(4, false, false, false);
  }
}