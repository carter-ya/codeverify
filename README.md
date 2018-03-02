### 实现发送器来真实的发送验证码
需要实现[Poster](src/main/java/com/ifengxue/code/verify/poster/Poster.java)接口来真实的发送验证码，可参考[LoggerPoster](src/main/java/com/ifengxue/code/verify/poster/LoggerPoster.java)实现。
### 如何生成验证码
需要实现[CodeGenerator](src/main/java/com/ifengxue/code/verify/CodeGenerator.java)接口来生成验证码，[SimpleCodeGenerator](src/main/java/com/ifengxue/code/verify/SimpleCodeGenerator.java)已经可以满足绝大多数的应用场景了。
### 如何生成验证码内容
需要实现[ContentGenerator](src/main/java/com/ifengxue/code/verify/ContentGenerator.java)接口来生成验证码内容，
可参考[SimpleContentGenerator](src/main/java/com/ifengxue/code/verify/SimpleContentGenerator.java)实现。
通过访问[Context](src/main/java/com/ifengxue/code/verify/Context.java)可获取到足够多的环境信息，生成足够复杂的验证码。
其中`Context.getCodeAttribute()`是每次发送、验证验证码都需要传入的自定义属性。
### 如何生成缓存验证码发送历史的key
需要实现[KeyGenerator](src/main/java/com/ifengxue/code/verify/KeyGenerator.java)。
通过`KeyGenerator`可以很容易的实现**多类型**，**多通道**的验证码。
```java
KeyGenerator generator = (recipient, context) -> {
  CodeAttribute attribute = context.getCodeAttribute();
  StringBuilder sb = new StringBuilder();
  sb.append(attribute.getCodeType())
    .append("_")
    .append(recipient)
    .append("_")
    .append(attribute.getAttribute("VERIFY_TYPE"));
  Long userId = (Long) attribute.getAttribute("USER_ID");
  if (userId != null) {
    sb.append("_user_id_").append(userId);
  }
  return sb.toString();
};
```
### 如何控制缓存的过期时间
需要实现[CacheExpireCalculator](src/main/java/com/ifengxue/code/verify/CacheExpireCalculator.java)接口来控制。
可参考[SimpleCacheExpireCalculator](src/main/java/com/ifengxue/code/verify/SimpleCacheExpireCalculator.java)实现，该实现会尽量控制发送历史在当日的最后时刻（`23:59:59`）过期，
但如果用户发送的验证码的有效期持续到第二天，那么该验证码发送历史的过期时间则持续到该验证码过期。
因此需要[BanishPostHistoryPostFilter](src/main/java/com/ifengxue/code/verify/filter/BanishPostHistoryPostFilter.java)来驱逐那些前一天发送的验证码。
### 如何控制验证码的发送过程
需要实现[PostFilter](src/main/java/com/ifengxue/code/verify/filter/PostFilter.java)接口.
#### `PostFilter`的一些内置实现
1. [BanishPostHistoryPostFilter](src/main/java/com/ifengxue/code/verify/filter/BanishPostHistoryPostFilter.java)：主要是驱逐那些昨日发送的验证码，以确保后续的`PostFilter`能够正常工作。
2. [PostCountPostFilter](src/main/java/com/ifengxue/code/verify/filter/PostCountPostFilter.java)：主要作用是控制验证码缓存在失效前的总发送数量。
3. [PostIntervalPostFilter](src/main/java/com/ifengxue/code/verify/filter/PostIntervalPostFilter.java)：主要作用是控制每个验证码之间的发送间隔，确保用户无法频繁发送验证码。
4. [PostSubIntervalCountPostFilter](src/main/java/com/ifengxue/code/verify/filter/PostSubIntervalCountPostFilter.java)：主要作用是在`PostCountPostFilter`的基础上，控制一定时间范围内的总验证码发送数量，如每天最多发送`20`条验证码且每小时最多发送`5`条验证码。