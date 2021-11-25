package repo;

import fileio.*;
import repo.Actions.ActionExecutor;
import repo.Actions.Command;
import repo.Actions.Query;
import repo.Actions.Recommendation;
import repo.Entities.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class Repository {

    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<String, Actor> actors = new HashMap<>();
    private final HashMap<String, Series> seriesMap = new HashMap<>();
    private final HashMap<String, Movie> moviesMap = new HashMap<>();
    private final HashSet<String> genres = new HashSet<>();
    private final HashMap<String, ActionExecutor> executors = new HashMap<>();
    private int numberOfVideos = 0;

    public Repository(Input input) {
        executors.put("command", new Command(this));
        executors.put("query", new Query(this));
        executors.put("recommendation", new Recommendation(this));
        initializeRepo(input);
    }

    public void initializeRepo(Input input) {
        for (MovieInputData movieInputData : input.getMovies()) {
            Movie movie = new Movie(movieInputData, numberOfVideos++);
            moviesMap.put(movieInputData.getTitle(), movie);
            genres.addAll(movieInputData.getGenres());
        }
        for (SerialInputData serialInputData : input.getSerials()) {
            Series series = new Series(serialInputData, numberOfVideos++);
            seriesMap.put(serialInputData.getTitle(), series);
            genres.addAll(serialInputData.getGenres());
        }

        for (UserInputData userInputData : input.getUsers()) {
            User user = (userInputData.getSubscriptionType().equals("PREMIUM")) ?
                    new User(userInputData) :
                    new PremiumUser(userInputData);

            users.put(userInputData.getUsername(), user);

            for (Map.Entry<String, Integer> entry : userInputData.getHistory().entrySet()) {
                Video video = getVideo(entry.getKey());
                int nr_views = entry.getValue();
                user.getHistory().put(video, nr_views);
                video.View(nr_views);

            }

            for (String videoName : userInputData.getFavoriteMovies()) {
                Video video = getVideo(videoName);
                user.Favourite(video);
                video.Favourite();
            }
        }

        for (ActorInputData actorInputData : input.getActors()) {
            Actor actor = new Actor(actorInputData);
            for (String videoName : actorInputData.getFilmography()) {
                Video video = getVideo(videoName);
                if (video != null) {
                    actor.getFilmography().put(videoName, video);
                    video.getCast().add(actor);
                }
            }
            actors.put(actor.toString(), actor);
        }
    }

    public Video getVideo(String title) {
        Video video = moviesMap.get(title);
        return (video == null) ? seriesMap.get(title) : video;
    }

    public User getUser(String username) {
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

    public void doActions(List<ActionInputData> actions) throws IOException {
        for (ActionInputData action : actions)
            executors.get(action.getActionType()).executeAction(action);

    }

}
