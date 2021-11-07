package repo;

import entertainment.Season;
import fileio.SerialInputData;

import java.util.ArrayList;

public class Series extends Video {
	private final ArrayList<Season> seasons;

	public Series(SerialInputData movieInputData) {
		super(movieInputData.getTitle(), movieInputData.getYear(), movieInputData.getGenres());
		this.seasons = movieInputData.getSeasons();
	}

	public Season getSeason(int number) {
		return seasons.get(number-1);
	}

	public void Rate(int season_number, double grade)
	{
		getSeason(season_number).getRatings().add(grade);
	}

	public double getRating(){
		double sum = 0;
		int cnt = 0;
		for (Season season : seasons)
		{
			double season_rating = Util.average(season.getRatings());
			if (season_rating > 0)
				cnt++;
			sum += season_rating;
		}
		return sum/cnt;
	}


}
