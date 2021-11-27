package repo.Entities;

import actor.ActorsAwards;
import fileio.ActorInputData;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Actor extends Entity {
  private final String name;
  private final String careerDescription;
  private final Set<Video> filmography;
  private final Map<ActorsAwards, Integer> awards;

  public Actor(final ActorInputData actorInputData) {
    this.name = actorInputData.getName();
    this.careerDescription = actorInputData.getCareerDescription();
    this.filmography = new HashSet<>();
    this.awards = actorInputData.getAwards();
  }

  public Set<Video> getFilmography() {
    return filmography;
  }

  /**Computes average rating of rated videos in this actor's filmography*/
  public Double getRating() {
    double sum = 0;
    int cnt = 0;
    for (Video video : filmography) {
      double videoRating = video.getRating();
      if (videoRating > 0) {
        cnt++;
      }
      sum += videoRating;
    }
    return sum / cnt;
  }

  /**Finds how many awards does this actor have*/
  public Integer numberOfAwards() {
    return awards.values().stream().reduce(0, Integer::sum);
  }

  /**Finds how many awards does this actor have*/
  public boolean hasAwards(final List<String> awardList) {
    if (awardList != null) {
      for (String award : awardList) {
        if (!awards.containsKey(ActorsAwards.valueOf(award))) {
          return false;
        }
      }
    }
    return true;
  }

  /**checks if the words are included in Actor's description*/
  public boolean hasWords(final List<String> wordList) {
    String description = careerDescription.toLowerCase();

    if (wordList != null) {
      for (String searchedWord : wordList) {
        String pattern = "\\b" + searchedWord.toLowerCase() + "\\b";
        Matcher m = Pattern.compile(pattern).matcher(description);
        if (!m.find()) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return name;
  }
}
