package repo;

import repo.Entities.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Helper {
	public static double average (List<Double> list) {
		if (list.size()==0) return 0;
		return list.stream().reduce((double) 0,Double::sum)/list.size();
	}
	public static Stream<? extends Entity> reverse (Stream<? extends Entity> stream)
	{
		List<? extends Entity> list = new ArrayList<>(stream.toList());
		Collections.reverse(list);
		return list.stream();
	}
}
