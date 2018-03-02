package com.ifengxue.code.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;

/**
 * 简单的验证码生成器
 */
@Getter
@ToString
public class SimpleCodeGenerator implements CodeGenerator {

  private final int length;
  private final boolean enableNumber;
  private final boolean enableLowerAlphabet;
  private final boolean enableUpperAlphabet;
  private final char[] randomPool;

  public SimpleCodeGenerator(int length, boolean enableNumber, boolean enableLowerAlphabet,
      boolean enableUpperAlphabet) {
    Args.isTrue(length > 0, "验证码长度不能<=0");
    Args.isTrue(enableNumber || enableLowerAlphabet || enableUpperAlphabet, "验证码不能既不包含数字也不包含字母");
    this.length = length;
    this.enableNumber = enableNumber;
    this.enableLowerAlphabet = enableLowerAlphabet;
    this.enableUpperAlphabet = enableUpperAlphabet;

    // init randomPool;
    Set<Character> characters = new HashSet<>();
    if (enableNumber) {
      characters.addAll(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    }
    if (enableLowerAlphabet) {
      characters.addAll(Arrays
          .asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
              'x', 'y', 'z'));
    }
    if (enableUpperAlphabet) {
      characters.addAll(Arrays
          .asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
              'X', 'Y', 'Z'));
    }
    randomPool = new char[characters.size()];
    int index = 0;
    for (Character character : characters) {
      randomPool[index++] = character;
    }
  }

  @Override
  public String doGenerate() {
    Random rand = new Random();
    char[] randChs = new char[length];
    for (int i = 0; i < length; i++) {
      randChs[i] = randomPool[rand.nextInt(randomPool.length)];
    }
    return new String(randChs);
  }
}
