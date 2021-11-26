package repo.Entities;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

public abstract class Video extends Entity {
  private final String title;

  private final int id;

  private final int year;

  private final HashSet<String> genres;
  private int views;
  private int favourites;

  public Video(final String title, final int year, final ArrayList<String> genres, final int id) {
    this.title = title;
    this.year = year;
    this.genres = new HashSet<>(genres);
    this.views = 0;
    this.favourites = 0;
    this.id = id;
  }

  @Override
  public final String toString() {
    return title;
  }

  /** increments this video's favourites counter */
  public final void favourite() {
    favourites++;
  }

  /** adds 1 to this video's total views */
  public final void view() {
    views++;
  }

  /** adds n views to this video's total views */
  public final void view(final int n) {
    views += n;
  }

  /** gets total number of views */
  public final int getViews() {
    return views;
  }

  /** computes average rating of video */
  public abstract double getRating();

  /** returns favourites counter */
  public final int getFavourites() {
    return favourites;
  }

  /** returns duration of video */
  public abstract int getDuration();

  /** checks if video is from specified year */
  public final boolean isFromYear(final String searchedYear) {
    if (searchedYear == null) {
      return true;
    }
    return year == Integer.parseInt(searchedYear);
  }

  /** checks if video is from specified year */
  public final boolean hasGenre(final String searchedGenre) {
    return genres.contains(searchedGenre);
  }

  /** checks if video contains all searched genres */
  public final boolean hasGenres(final List<String> searchedGenres) {
    if (searchedGenres != null) {
      for (final String word : searchedGenres) {
        if (!genres.contains(word) && word != null) {
          return false;
        }
      }
    }
    return true;
  }

  public final int getId() {
    return id;
  }
}
