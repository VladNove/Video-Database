package repo.Entities.VideoTypes;

import fileio.MovieInputData;
import repo.Entities.Video;
import utils.Helper;

import java.util.ArrayList;
import java.util.List;
import utils.Ratable;

public final class Movie extends Video implements Ratable {
  private final int duration;
  private final List<Double> ratings;

  public Movie(final MovieInputData movieInputData, final int id) {
    super(movieInputData.getTitle(), movieInputData.getYear(), movieInputData.getGenres(), id);
    this.duration = movieInputData.getDuration();
    this.ratings = new ArrayList<>();
  }

  @Override
  public void rate(final double grade) {
    ratings.add(grade);
  }

  @Override
  public double getRating() {
    return Helper.average(ratings);
  }

  @Override
  public int getDuration() {
    return duration;
  }
}
