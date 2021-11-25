package repo.Entities;

import fileio.MovieInputData;
import repo.Entities.Ratable;
import repo.Entities.Video;
import repo.Helper;

import java.util.ArrayList;
import java.util.List;

public class Movie extends Video implements Ratable {
	private final int duration;
	private final List<Double> ratings;
	public Movie(MovieInputData movieInputData, int id) {
		super(movieInputData.getTitle(), movieInputData.getYear(), movieInputData.getGenres(),id);
		this.duration = movieInputData.getDuration();
		this.ratings = new ArrayList<>();
	}
	public void Rate(double grade)
	{
		ratings.add(grade);
	}

	public double getRating(){
		rating = Helper.average(ratings);
		return rating;
	}

	@Override
	public int getDuration() {
		return duration;
	}
}
