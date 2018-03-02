package com.ifengxue.code.verify;

import com.ifengxue.code.verify.Coder.CodeCheckStatus;
import com.ifengxue.code.verify.poster.Poster;
import lombok.Getter;
import lombok.Setter;

/**
 * 简单验证码门面
 */
public class SimpleCoderFacade implements CoderFacade {

  private final Context contextHolder;
  @Getter
  private final ContentGenerator contentGenerator;
  @Getter
  private final Poster poster;
  @Setter
  @Getter
  private Coder coder;

  /**
   * @param context 上下文
   * @param contentGenerator 验证码内容生成器
   * @param poster 验证码发送器
   */
  public SimpleCoderFacade(Context context, ContentGenerator contentGenerator, Poster poster) {
    Args.isNotNull(context, contentGenerator, poster);

    this.contextHolder = context;
    this.contentGenerator = contentGenerator;
    this.poster = poster;
    this.coder = new SimpleCoder(poster);
  }

  @Override
  public PostResult post(String recipient) {
    return post(recipient, new SimpleCodeAttribute());
  }

  @Override
  public PostResult post(String recipient, CodeAttribute attribute) {
    return coder.post(recipient, contextHolder.clone(attribute), contentGenerator);
  }

  @Override
  public CodeCheckStatus check(String recipient, String code) {
    return check(recipient, code, new SimpleCodeAttribute());
  }

  @Override
  public CodeCheckStatus check(String recipient, String code, CodeAttribute codeAttribute) {
    return coder.check(recipient, code, contextHolder.clone(codeAttribute));
  }

  @Override
  public CodeCheckStatus checkAndInvalid(String recipient, String code) {
    return checkAndInvalid(recipient, code, new SimpleCodeAttribute());
  }

  @Override
  public CodeCheckStatus checkAndInvalid(String recipient, String code, CodeAttribute codeAttribute) {
    return coder.checkAndInvalid(recipient, code, contextHolder.clone(codeAttribute));
  }

  public Context getContext() {
    return contextHolder;
  }
}
