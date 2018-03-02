package com.ifengxue.code.verify.history;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class ConcurrentCodeHistoryProviderTest {

  private final CodeHistoryProvider historyProvider = new ConcurrentCodeHistoryProvider();

  @Test
  public void get() throws InterruptedException {
    CodeHistory history = new CodeHistory();
    long expireMillis = 1000;
    historyProvider.save("key1", history, expireMillis);
    historyProvider.save("key2", history, -1);
    assertNotNull(historyProvider.get("key1"));
    assertNotNull(historyProvider.get("key2"));
    TimeUnit.MILLISECONDS.sleep(expireMillis);
    assertNull(historyProvider.get("key1"));
    assertNotNull(historyProvider.get("key2"));
    historyProvider.delete("key2");
    assertNull(historyProvider.get("key2"));
  }
}