package com.ifengxue.code.verify;

import static com.ifengxue.code.verify.Coder.CodeCheckStatus.CORRECT;
import static com.ifengxue.code.verify.Coder.CodeCheckStatus.EXPIRED;
import static com.ifengxue.code.verify.Coder.CodeCheckStatus.INCORRECT;
import static com.ifengxue.code.verify.Coder.CodeCheckStatus.INVALID;
import static com.ifengxue.code.verify.PostResult.PostResultStatus.INTERNAL_ERROR;
import static com.ifengxue.code.verify.PostResult.PostResultStatus.SUCCESS;
import static com.ifengxue.code.verify.event.EventType.POST_SUCCESS;

import com.ifengxue.code.verify.filter.PostFilter;
import com.ifengxue.code.verify.filter.PostFilter.PostChain;
import com.ifengxue.code.verify.history.CodeHistory;
import com.ifengxue.code.verify.history.CodeHistoryProvider;
import com.ifengxue.code.verify.poster.Poster;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单的验证码实现
 */
@Slf4j
public class SimpleCoder implements Coder {

  private final Poster poster;

  public SimpleCoder(Poster poster) {
    Args.isNotNull(poster, "poster不能为null");
    this.poster = poster;
  }

  @Override
  public PostResult post(String recipient, Context context, ContentGenerator contentGenerator) {
    Args.isNotNull(recipient, context, contentGenerator);

    String key = Optional.ofNullable(context.getKeyGenerator().doGenerate(recipient, context))
        .orElseThrow(() -> new IllegalStateException("key不能为null"));
    CodeHistory history = context.getCodeConfiguration().getHistoryProvider().get(key);
    boolean isFirstPost = false;
    if (history == null) {
      isFirstPost = true;
      history = new CodeHistory();
    }
    // 执行过滤器链
    List<PostFilter> filters = context.getCodeConfiguration().getPostFilters();
    PostChain postChain = new PostChain(filters.listIterator());
    PostResult postResult = null;
    boolean isFilterChainError = false;
    String code = context.getCodeGenerator().doGenerate(); // 生成默认验证码
    try {
      postResult = postChain.doFilter(recipient, isFirstPost, history, context);
    } catch (Exception e) {
      log.error("执行过滤器链时发生异常", e);
      isFilterChainError = true;
    } finally {
      code = Optional.ofNullable(context.getCodeAttribute().getCode()).orElse(code);// 更新验证码为过滤器链重新生成的验证码
      if (isFilterChainError) {
        postResult = PostResult.builder().status(INTERNAL_ERROR).codeConfiguration(context.getCodeConfiguration())
            .build();
      }
    }
    if (postResult != null) {
      return postResult;
    }
    // 执行发送验证码
    Content content = contentGenerator.doGenerate(code, context);
    try {
      poster.post(recipient, content, context);
    } catch (Exception e) {
      log.error("发送验证码时发生异常", e);
      return PostResult.builder().status(INTERNAL_ERROR).code(code).content(content)
          .codeConfiguration(context.getCodeConfiguration()).build();// 发送失败
    }
    // 保存验证码发送历史
    CodeConfiguration codeConfiguration = context.getCodeConfiguration();
    long now = System.currentTimeMillis();
    long expiredAt = codeConfiguration.codeExpiredMillis();
    expiredAt = expiredAt < 0 ? expiredAt : now + expiredAt;// 验证码过期时间
    long nextCodeAt = codeConfiguration.postIntervalMillis();
    nextCodeAt = nextCodeAt < 0 ? nextCodeAt : now + nextCodeAt + 1;// 下一次可发送验证码时间
    history.newPostCode(code, expiredAt, nextCodeAt);
    try {
      CodeHistoryProvider historyProvider = context.getCodeConfiguration().getHistoryProvider();
      CacheExpireCalculator cacheExpireCalculator = context.getCodeConfiguration().getCacheExpireCalculator();
      historyProvider.save(key, history, cacheExpireCalculator.expireMillis(recipient, history, context));
    } catch (Exception e) {
      log.error("保存验证码发送历史失败", e);
      return PostResult.builder().status(INTERNAL_ERROR).code(code).content(content)
          .codeConfiguration(codeConfiguration).build();
    }

    // 发布事件
    String unmodifiableCode = code;
    try {
      context.getCodeConfiguration().getCoderEvents()
          .forEach(coderEvent -> coderEvent.onEvent(POST_SUCCESS, context, recipient, unmodifiableCode, content));
    } catch (Exception e) {
      log.warn("发布事件失败", e);
    }
    return PostResult.builder().status(SUCCESS).code(code).content(content)
        .codeConfiguration(codeConfiguration).build();// 发送成功
  }

  @Override
  public CodeCheckStatus check(String recipient, String code, Context context) {
    Args.isNotNull(recipient, code, context);

    String key = context.getKeyGenerator().doGenerate(recipient, context);
    CodeHistoryProvider historyProvider = context.getCodeConfiguration().getHistoryProvider();
    CodeHistory history = historyProvider.get(key);
    CodeCheckStatus status = check(code, history);
    if (status == INCORRECT && history != null) {
      history.setIncorrectCheckTimes(history.getIncorrectCheckTimes() + 1);// 错误次数自增
      historyProvider.save(key, history, historyProvider.getExpireMillis(key));
    }
    return status;
  }

  @Override
  public CodeCheckStatus checkAndInvalid(String recipient, String code, Context context) {
    Args.isNotNull(recipient, code, context);

    String key = context.getKeyGenerator().doGenerate(recipient, context);
    CodeHistoryProvider historyProvider = context.getCodeConfiguration().getHistoryProvider();
    CodeHistory history = historyProvider.get(key);
    CodeCheckStatus status = check(code, history);

    if (status == CORRECT) {
      history.setValid(false);// 使验证码失效
      historyProvider.save(key, history, historyProvider.getExpireMillis(key));
    } else if (status == INCORRECT && history != null) {
      history.setIncorrectCheckTimes(history.getIncorrectCheckTimes() + 1);
      historyProvider.save(key, history, historyProvider.getExpireMillis(key));// 验证错误次数自增
    }
    return status;
  }

  private CodeCheckStatus check(String code, CodeHistory history) {
    if (history == null) {
      return INCORRECT;// 验证码不存在
    }
    if (!history.isValid()) {
      return INVALID;// 验证码已失效
    }
    if (history.getExpiredAt() >= 0 && System.currentTimeMillis() > history.getExpiredAt()) {
      return EXPIRED;// 验证码已过期
    }
    if (Objects.equals(code, history.getCode())) {
      return CORRECT;
    } else {
      return INCORRECT;
    }
  }
}
