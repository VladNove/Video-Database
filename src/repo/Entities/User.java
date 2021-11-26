package repo.Entities;

import fileio.ActionInputData;
import fileio.UserInputData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import utils.RecommendStrategy;

import java.io.IOException;


import utils.Ratable;

public abstract class User extends Entity {
  private final String username;

  private final Set<Video> favoriteMovies;
  private final Map<Video, Integer> views;
  private final Set<Ratable> ratings;

  public User(final UserInputData userInputData) {
    this.username = userInputData.getUsername();
    this.favoriteMovies = new HashSet<>();
    ratings = new HashSet<>();
    this.views = new HashMap<>();
  }

  public final Map<Video, Integer> getHistory() {
    return views;
  }

  /** Adds video to favourite list*/
  public final void favourite(final Video video) {
    favoriteMovies.add(video);
  }

  /** Adds video to history and returns number of views */
  public final int view(final Video video) {
    if (!views.containsKey(video)) {
      views.put(video, 1);
    } else {
      views.put(video, views.get(video) + 1);
    }
    return views.get(video);
  }

  /** returns number of ratings given */
  public final int ratings() {
    return ratings.size();
  }

  /** checks if user already rated the Ratable object */
  public final boolean rated(final Ratable ratable) {
    return ratings.contains(ratable);
  }

  /** adds ratable to list of rated objects */
  public final void rate(final Ratable ratable) {
    ratings.add(ratable);
  }

  /** checks if video is not in this user's history */
  public final boolean notSeenVideo(final Video video) {
    return !views.containsKey(video);
  }

  /** checks if video is in this user's favourites */
  public final boolean isFavourite(final Video video) {
    return favoriteMovies.contains(video);
  }

  @Override
  public final String toString() {
    return username;
  }

  /** for Visitor pattern of recommendations */
  public abstract String acceptRecommendation(
      RecommendStrategy recommendStrategy, ActionInputData action) throws IOException;
}
