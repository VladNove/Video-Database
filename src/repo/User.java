package repo;

import entertainment.Season;
import fileio.UserInputData;

import java.util.*;

public class User {
	private final String username;
	private final String subscriptionType;
	private final Set<Video> favoriteMovies;
	private final Map<Video,Integer> views;
	private final Map<Movie, Double> movieRatings;
	private final Map<Season, Double> seriesRatings;

	public User(UserInputData userInputData) {
		this.username = userInputData.getUsername();
		this.subscriptionType = userInputData.getSubscriptionType();
		this.favoriteMovies = new HashSet<>();
		this.movieRatings = new HashMap<>();
		this.seriesRatings =  new HashMap<>();
		this.views = new HashMap<>();
	}

	public Map<Video,Integer> getHistory() {
		return views;
	}

	public void Favourite(Video video)
	{
		favoriteMovies.add(video);
	}

	public int view(Video video){
		if (!views.containsKey(video))
			views.put(video,1);
		else
			views.put(video,views.get(video)+1);
		return  views.get(video);
	}

	public void Rate(Movie movie, double rating) {
		movieRatings.put(movie, rating);
	}

	public void Rate(Season season, double rating) {
		seriesRatings.put(season, rating);
	}

	public String getUsername() {
		return username;
	}

	public boolean seenVideo(Video video) {
		return views.containsKey(video);
	}

	public boolean isFavourite(Video video) {
		return favoriteMovies.contains(video);
	}
}
