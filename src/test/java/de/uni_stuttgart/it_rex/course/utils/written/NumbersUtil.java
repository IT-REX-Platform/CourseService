package de.uni_stuttgart.it_rex.course.utils.written;

import java.util.Random;

public final  class NumbersUtil {
  private static final long seed = 42694201337L;
  private static  final Random RANDOM = new Random(seed);

  public static int generateRandomInteger(final int lowerBound, final int upperBound) {
    return RANDOM.nextInt(upperBound - lowerBound) + lowerBound;
  }
}
