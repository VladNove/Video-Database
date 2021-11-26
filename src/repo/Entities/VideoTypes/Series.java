package repo.Entities.VideoTypes;

import entertainment.Season;
import fileio.SerialInputData;
import repo.Entities.Video;
import utils.Helper;

import java.util.ArrayList;

public final class Series extends Video {
  private final ArrayList<Season> seasons;

  public Series(final SerialInputData movieInputData, final int id) {
    super(movieInputData.getTitle(), movieInputData.getYear(), movieInputData.getGenres(), id);
    this.seasons = movieInputData.getSeasons();
  }

  /** returns specified Season */
  public Season getSeason(final int number) {
    return seasons.get(number - 1);
  }

  @Override
  public double getRating() {
    if (seasons.size() == 0) {
      return 0;
    }

    double sum = 0;
    for (Season season : seasons) {
      sum += Helper.average(season.getRatings());
    }
    return sum / seasons.size();
  }

  @Override
  public int getDuration() {
    int sum = 0;
    for (Season season : seasons) {
      sum += season.getDuration();
    }
    return sum;
  }
}
