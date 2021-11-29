package repo;

import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import repo.Actions.ActionExecutor;
import repo.Actions.Command;
import repo.Actions.Query;
import repo.Actions.Recommend;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import repo.Entities.Actor;
import repo.Entities.User;
import repo.Entities.UserTypes.PremiumUser;
import repo.Entities.UserTypes.StandardUser;
import repo.Entities.Video;
import repo.Entities.VideoTypes.Movie;
import repo.Entities.VideoTypes.Series;

public final class Repository {

  private final HashMap<String, User> users = new HashMap<>();
  private final HashMap<String, Actor> actors = new HashMap<>();
  private final HashMap<String, Series> seriesMap = new HashMap<>();
  private final HashMap<String, Movie> moviesMap = new HashMap<>();
  private final HashSet<String> genres = new HashSet<>();
  private final HashMap<String, ActionExecutor> executors = new HashMap<>();
  private int numberOfVideos = 0;

  public Repository(final Input input) {
    loadInput(input);
    executors.put("command", new Command(this));
    executors.put("query", new Query(this));
    executors.put("recommendation", new Recommend(this));
  }

  /** executes list of actions */
  public void doActions(final List<ActionInputData> actions) throws IOException {
    for (ActionInputData action : actions) {
      executors.get(action.getActionType()).executeAction(action);
    }
  }

  private void loadInput(final Input input) {
    for (MovieInputData movieInputData : input.getMovies()) {
      loadMovie(movieInputData);
    }
    for (SerialInputData serialInputData : input.getSerials()) {
      loadSerial(serialInputData);
    }
    for (UserInputData userInputData : input.getUsers()) {
      loadUser(userInputData);
    }
    for (ActorInputData actorInputData : input.getActors()) {
      loadActor(actorInputData);
    }
  }

  private void loadSerial(final SerialInputData serialInputData) {
    Series series = new Series(serialInputData, numberOfVideos++);
    seriesMap.put(serialInputData.getTitle(), series);
    genres.addAll(serialInputData.getGenres());
  }

  private void loadMovie(final MovieInputData movieInputData) {
    Movie movie = new Movie(movieInputData, numberOfVideos++);
    moviesMap.put(movieInputData.getTitle(), movie);
    genres.addAll(movieInputData.getGenres());
  }

  private void loadActor(final ActorInputData actorInputData) {
    Actor actor = new Actor(actorInputData);
    for (String videoName : actorInputData.getFilmography()) {
      Video video = getVideo(videoName);
      if (video != null) {
        actor.getFilmography().add(video);
      }
    }
    actors.put(actor.toString(), actor);
  }

  private void loadUser(final UserInputData userInputData) {
    User user = (userInputData.getSubscriptionType().equals("PREMIUM"))
            ? new PremiumUser(userInputData)
            : new StandardUser(userInputData);

    //loads user view history
    for (Map.Entry<String, Integer> entry : userInputData.getHistory().entrySet()) {
      Video video = getVideo(entry.getKey());
      int nrViews = entry.getValue();
      user.getHistory().put(video, nrViews);
      video.view(nrViews);
    }

    for (String videoName : userInputData.getFavoriteMovies()) {
      Video video = getVideo(videoName);
      user.favourite(video);
      video.favourite();
    }

    users.put(userInputData.getUsername(), user);
  }

  /** returns Video with specified title from database */
  public Video getVideo(final String title) {
    Video video = moviesMap.get(title);
    return (video == null) ? seriesMap.get(title) : video;
  }

  /** returns User with specified username from database */
  public User getUser(final String username) {
    return users.get(username);
  }

  public Stream<User> getUsers() {
    return users.values().stream().sorted();
  }

  public Stream<Actor> getActors() {
    return actors.values().stream().sorted();
  }

  public Stream<Movie> getMovies() {
    return moviesMap.values().stream().sorted();
  }

  public Stream<Series> getSeries() {
    return seriesMap.values().stream().sorted();
  }

  public Stream<Video> getVideos() {
    return Stream.concat(moviesMap.values().stream(), seriesMap.values().stream()).sorted();
  }

  public Stream<String> getGenres() {
    return genres.stream();
  }
}
