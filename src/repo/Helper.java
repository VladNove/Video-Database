package repo;

import java.util.List;

public class Helper {
	public static double average (List<Double> list) {
		if (list.size()==0) return 0;
		return list.stream().reduce((double) 0,Double::sum)/list.size();
	}
}
