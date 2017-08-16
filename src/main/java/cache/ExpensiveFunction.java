package cache;

import java.math.BigInteger;

class ExpensiveFunction implements Computable<String, BigInteger> {
  public BigInteger compute(String arg) {
    return new BigInteger(arg);
  }
}
