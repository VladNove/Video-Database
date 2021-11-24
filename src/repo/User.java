package repo;

import entertainment.Season;
import fileio.UserInputData;

import java.util.*;

public class User {
	private final String username;

	//TODO Visitor pattern for Recommendations
	private final String subscriptionType;
	private final Set<Video> favoriteMovies;
	private final Map<Video,Integer> views;
	private final Map<Movie, Double> movieRatings;
	private final Map<Season, Double> seasonRatings;

	public User(UserInputData userInputData) {
		this.username = userInputData.getUsername();
		this.subscriptionType = userInputData.getSubscriptionType();
		this.favoriteMovies = new HashSet<>();
		this.movieRatings = new HashMap<>();
		this.seasonRatings =  new HashMap<>();
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

	public int ratings() {
		return movieRatings.size() + seasonRatings.size();
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public boolean rated(Movie movie) { return movieRatings.containsKey(movie); }

	public boolean rated(Season season) { return seasonRatings.containsKey(season); }

	public void rate(Movie movie, double rating) {
		movieRatings.put(movie, rating);
	}

	public void rate(Season season, double rating) {
		seasonRatings.put(season, rating);
	}

	public String getUsername() {
		return username;
	}

	public boolean seenVideo(Video video) {
		return !views.containsKey(video);
	}

	public boolean isFavourite(Video video) {
		return favoriteMovies.contains(video);
	}

	@Override
	public String toString() {
		return username;
	}
}
