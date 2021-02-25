package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;

public class PublishStateUtil {
  public static PUBLISHSTATE randomPublishState() {
    return PUBLISHSTATE.values()[NumbersUtil.generateRandomInteger(0, PUBLISHSTATE.values().length)];
  }
}
