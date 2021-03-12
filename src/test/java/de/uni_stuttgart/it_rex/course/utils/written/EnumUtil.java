package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.CONTENTREFERENCETYPE;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;

public class EnumUtil {
  /**
   * Generates a random publish state.
   *
   * @return the publish state
   */
  public static PUBLISHSTATE generateRandomPublishState() {
    return PUBLISHSTATE.values()[NumbersUtil.generateRandomInteger(0, PUBLISHSTATE.values().length)];
  }

  /**
   * Generates a random content reference type.
   *
   * @return the content reference type
   */
  public static CONTENTREFERENCETYPE generateRandomContentReferenceType() {
    return CONTENTREFERENCETYPE.values()[NumbersUtil.generateRandomInteger(0, CONTENTREFERENCETYPE.values().length)];
  }
}
