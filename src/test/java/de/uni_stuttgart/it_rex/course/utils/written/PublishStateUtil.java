package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;

public class PublishStateUtil {
  /**
   * Generates a random publish state.
   *
   * @return the publish state
   */
  public static PUBLISHSTATE generateRandomPublishState() {
    return PUBLISHSTATE.values()[NumbersUtil.generateRandomInteger(0, PUBLISHSTATE.values().length)];
  }
}
