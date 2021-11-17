package repo;

import fileio.ActionInputData;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class Repository {

	private static Repository instance;
	private Repository(){}

	private final HashMap<String, User> users = new HashMap<>();
	private final HashMap<String, Actor> actors = new HashMap<>();
	private final HashMap<String, Video> videos = new HashMap<>();

//TODO remove singleton
	public static Repository getInstance()
	{
		return instance;
	}

	public static Repository createInstance()
	{
		instance = new Repository();
		return instance;
	}

	public void initializeRepo(Input input)
	{
		for (MovieInputData movieInputData: input.getMovies()) {
			Movie movie = new Movie(movieInputData);
			videos.put(movieInputData.getTitle(),movie);
		}
		for (SerialInputData serialInputData: input.getSerials()) {
			Series series = new Series(serialInputData);
			videos.put(serialInputData.getTitle(),series);
		}

		for (UserInputData userInputData: input.getUsers()) {
			User user = new User(userInputData);
			if (userInputData.getSubscriptionType().equals("PREMIUM"))
				user = new PremiumUser(userInputData);

			users.put(userInputData.getUsername(),user);

			for (Map.Entry<String,Integer> entry: userInputData.getHistory().entrySet()) {
				Video video = videos.get(entry.getKey());
				int nr_views = entry.getValue();
				user.getHistory().put(video, nr_views);
				video.View(nr_views);

			}

			for (String videoName: userInputData.getFavoriteMovies()) {
				user.Favourite(videos.get(videoName));
				videos.get(videoName).Favourite();
			}
		}

		for (ActorInputData actorInputData: input.getActors()) {
			Actor actor = new Actor(actorInputData);
			for (String videoName: actorInputData.getFilmography()) {
				Video video = videos.get(videoName);
				if (video != null) {
					actor.getFilmography().put(videoName, video);
					video.getCast().add(actor);
				}
			}
			actors.put(actor.toString(), actor);
		}
	}

	public Video getVideo(String title) {
		return videos.get(title);
	}
	public User getUser(String username) {
		return users.get(username);
	}

	public Stream<User> getUsers() { return users.values().stream(); }

	public Stream<Actor> getActors() {
		return actors.values().stream();
	}

	public Stream<Video> getVideos() { return videos.values().stream(); }

	public void doActions(List<ActionInputData> actions) throws IOException {
		for (ActionInputData action : actions)
		{
			if (action.getActionType().equals("command"))
				Command.executeAction(action);
			if (action.getActionType().equals("query"))
				Query.executeAction(action);
		}

	}

}
