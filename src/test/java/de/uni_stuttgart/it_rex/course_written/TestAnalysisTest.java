package de.uni_stuttgart.it_rex.course_written;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestAnalysisTest {

  private TestAnalysisTest() {
  }

  @Test
  void add() {
    assertEquals(TestAnalysis.add(5, 5), 10);
    assertNotEquals(TestAnalysis.add(5, 5), -999999999);
  }
}