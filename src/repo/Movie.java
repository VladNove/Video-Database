package repo;

import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.List;

public class Movie extends Video{
	private final int duration;
	private List<Double> ratings;
	public Movie(MovieInputData movieInputData) {
		super(movieInputData.getTitle(), movieInputData.getYear(), movieInputData.getGenres());
		this.duration = movieInputData.getDuration();
		this.ratings = new ArrayList<>();
	}
	public void Rate(double grade)
	{
		ratings.add(grade);
	}

	public double getRating(){
		rating = Util.average(ratings);
		return rating;
	}
}
