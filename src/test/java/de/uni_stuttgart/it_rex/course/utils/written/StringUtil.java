package de.uni_stuttgart.it_rex.course.utils.written;

import java.nio.charset.Charset;
import java.util.Random;

import static de.uni_stuttgart.it_rex.course.utils.written.NumbersUtil.generateRandomInteger;

public final class StringUtil {
  private static final long seed = 7777777777L;
  private static final Random RANDOM = new Random(seed);

  /**
   * Generates a random String with a length between two numbers.
   *
   * @param lowerBound the upper bound
   * @param upperBound the lower bound
   * @return the String
   */
  public static String generateRandomString(final int lowerBound, final int upperBound) {
    byte[] array = new byte[generateRandomInteger(lowerBound, upperBound)];
    RANDOM.nextBytes(array);
    return new String(array, Charset.forName("UTF-8"));
  }
}
