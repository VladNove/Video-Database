package repo.Entities;

import entertainment.Season;
import fileio.SerialInputData;
import repo.Helper;

import java.util.ArrayList;

public class Series extends Video {
	private final ArrayList<Season> seasons;

	public Series(SerialInputData movieInputData, int id) {
		super(movieInputData.getTitle(), movieInputData.getYear(), movieInputData.getGenres(), id);
		this.seasons = movieInputData.getSeasons();
	}

	public Season getSeason(int number) {
		return seasons.get(number-1);
	}

	public double getRating(){
		if (seasons.size() == 0) return 0;

		double sum = 0;
		for (Season season: seasons)
			sum += Helper.average(season.getRatings());
		rating = sum / seasons.size();
		return sum / seasons.size();
	}

	@Override
	public int getDuration() {
		int sum = 0;
		for (Season season: seasons)
			sum += season.getDuration();
		return sum;
	}
}
