package repo.Entities;

import fileio.UserInputData;

import java.util.*;

public class User extends Entity{
	private final String username;

	//TODO Visitor pattern for Recommendations
	private final String subscriptionType;
	private final Set<Video> favoriteMovies;
	private final Map<Video,Integer> views;
	private final Set<Ratable> ratings;
	public User(UserInputData userInputData) {
		this.username = userInputData.getUsername();
		this.subscriptionType = userInputData.getSubscriptionType();
		this.favoriteMovies = new HashSet<>();
		ratings = new HashSet<>();
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
		return ratings.size();
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public boolean rated(Ratable ratable) { return ratings.contains(ratable); }

	public void rate(Ratable ratable) { ratings.add(ratable); }

	public String getUsername() {
		return username;
	}

	public boolean notSeenVideo(Video video) {
		return !views.containsKey(video);
	}

	public boolean isFavourite(Video video) { return favoriteMovies.contains(video); }

	@Override
	public String toString() {
		return username;
	}
}
