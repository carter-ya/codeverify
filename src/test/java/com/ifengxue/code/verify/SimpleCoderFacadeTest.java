package com.ifengxue.code.verify;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import com.ifengxue.code.verify.Coder.CodeCheckStatus;
import com.ifengxue.code.verify.PostResult.PostResultStatus;
import com.ifengxue.code.verify.event.LoggerCoderEvent;
import com.ifengxue.code.verify.filter.BanishPostHistoryPostFilter;
import com.ifengxue.code.verify.filter.BanishPostHistoryPostFilter.LastDayPredicate;
import com.ifengxue.code.verify.filter.PostCountPostFilter;
import com.ifengxue.code.verify.filter.PostIntervalPostFilter;
import com.ifengxue.code.verify.history.ConcurrentCodeHistoryProvider;
import com.ifengxue.code.verify.poster.LoggerPoster;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class SimpleCoderFacadeTest {

  private final SimpleCoderFacade coderFacade;
  private final long postIntervalMillis = 200;
  private final long codeExpiredMillis = 300;
  private final int acceptMaxCount = 10;
  private final String smsRecipient = "13000000000";
  private final String emailRecipient = "example@example.com";

  public SimpleCoderFacadeTest() {
    Context context = new SimpleContext();
    context.setCodeGenerator(new SimpleCodeGenerator(6, true, true, true));
    context.setKeyGenerator(new SimpleKeyGenerator("simple_coder_facade:"));
    context.setCodeConfiguration(
        new SimpleCodeConfiguration(postIntervalMillis, codeExpiredMillis, new ConcurrentCodeHistoryProvider(),
            new SimpleCacheExpireCalculator(),
            new HashSet<>(Arrays.asList(new PostCountPostFilter(acceptMaxCount), new PostIntervalPostFilter(),
                new BanishPostHistoryPostFilter(new LastDayPredicate()))),
            new HashSet<>(Collections.singletonList(new LoggerCoderEvent()))));
    coderFacade = new SimpleCoderFacade(context, new SimpleContentGenerator(), new LoggerPoster());
  }

  @Test
  public void post() throws InterruptedException {
    PostResult postResult = coderFacade.post(smsRecipient);

    // 发送成功情况（第一次发送）
    assertNotNull(postResult);
    assertEquals(PostResultStatus.SUCCESS, postResult.getStatus());

    // 频繁发送情况（还在冷却期）
    postResult = coderFacade.post(smsRecipient);
    assertNotNull(postResult);
    assertEquals(PostResultStatus.POST_FREQUENTLY, postResult.getStatus());
    System.out.println(postResult);

    // 发送成功情况（过了发送冷却期）
    TimeUnit.MILLISECONDS.sleep(postIntervalMillis);
    postResult = coderFacade.post(smsRecipient);
    assertNotNull(postResult);
    assertEquals(PostResultStatus.SUCCESS, postResult.getStatus());

    // 跳过剩余的可发送次数
    for (int i = 0; i < acceptMaxCount - 2; i++) {
      TimeUnit.MILLISECONDS.sleep(postIntervalMillis);
      postResult = coderFacade.post(smsRecipient);
      assertNotNull(postResult);
      assertEquals(PostResultStatus.SUCCESS, postResult.getStatus());
    }

    // 发送失败情况（发送数量超出最大限制）
    TimeUnit.MILLISECONDS.sleep(postIntervalMillis);
    postResult = coderFacade.post(smsRecipient);
    assertNotNull(postResult);
    assertEquals(PostResultStatus.POST_OVER_COUNT_LIMIT, postResult.getStatus());
    System.out.println(postResult);
  }

  @Test
  public void check() throws InterruptedException {
    PostResult postResult = coderFacade.post(emailRecipient);
    assertNotNull(postResult);
    assertEquals(PostResultStatus.SUCCESS, postResult.getStatus());

    // 验证码正确的情况，且在有效期
    CodeCheckStatus checkStatus = coderFacade.check(emailRecipient, postResult.getCode());
    assertNotNull(checkStatus);
    assertEquals(CodeCheckStatus.CORRECT, checkStatus);

    checkStatus = coderFacade.check(emailRecipient, postResult.getCode());
    assertEquals(CodeCheckStatus.CORRECT, checkStatus);

    // 验证码错误，且在有效期
    checkStatus = coderFacade.check(emailRecipient, postResult.getCode() + "X");
    assertEquals(CodeCheckStatus.INCORRECT, checkStatus);

    // 验证码正确，过了有效期
    TimeUnit.MILLISECONDS.sleep(codeExpiredMillis);
    checkStatus = coderFacade.check(emailRecipient, postResult.getCode());
    assertEquals(CodeCheckStatus.EXPIRED, checkStatus);

    // 验证码错误，过了有效期
    checkStatus = coderFacade.check(emailRecipient, postResult.getCode() + "X");
    assertEquals(CodeCheckStatus.EXPIRED, checkStatus);

    // 未发送验证码（或缓存已失效）
    checkStatus = coderFacade.check(smsRecipient, postResult.getCode());
    assertEquals(CodeCheckStatus.INCORRECT, checkStatus);
  }

  @Test
  public void checkAndInvalid() throws InterruptedException {
    PostResult postResult = coderFacade.post(emailRecipient);
    assertEquals(PostResultStatus.SUCCESS, postResult.getStatus());

    // 验证码正确的情况，且在有效期
    CodeCheckStatus checkStatus = coderFacade.checkAndInvalid(emailRecipient, postResult.getCode());
    assertEquals(CodeCheckStatus.CORRECT, checkStatus);

    // 验证码正确，但是已被验证
    checkStatus = coderFacade.checkAndInvalid(emailRecipient, postResult.getCode());
    assertEquals(CodeCheckStatus.INVALID, checkStatus);

    TimeUnit.MILLISECONDS.sleep(postIntervalMillis);

    // 验证码错误，且在有效期
    postResult = coderFacade.post(emailRecipient);
    assertEquals(PostResultStatus.SUCCESS, postResult.getStatus());

    checkStatus = coderFacade.checkAndInvalid(emailRecipient, postResult.getCode() + "X");
    assertEquals(CodeCheckStatus.INCORRECT, checkStatus);

    checkStatus = coderFacade.checkAndInvalid(emailRecipient, postResult.getCode());
    assertEquals(CodeCheckStatus.CORRECT, checkStatus);

    checkStatus = coderFacade.checkAndInvalid(emailRecipient, postResult.getCode());
    assertEquals(CodeCheckStatus.INVALID, checkStatus);
  }
}