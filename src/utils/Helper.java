package utils;

import repo.Entities.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public final class Helper {
  private Helper() { }

  /** Computes average of double list, returns 0 if list is empty */
  public static double average(final List<Double> list) {
    if (list.size() == 0) {
      return 0;
    }
    return list.stream().reduce((double) 0, Double::sum) / list.size();
  }

  /** returns reversed stream */
  public static Stream<? extends Entity> reverse(final Stream<? extends Entity> stream) {
    List<? extends Entity> list = new ArrayList<>(stream.toList());
    Collections.reverse(list);
    return list.stream();
  }
}
