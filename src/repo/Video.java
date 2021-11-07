package repo;

import java.util.ArrayList;

public abstract class Video {
	private final String title;

	private final int year;

	private final ArrayList<Actor> cast;

	private final ArrayList<String> genres;

	private int views;

	public Video(String title, int year, ArrayList<String> genres) {
		this.title = title;
		this.year = year;
		this.cast = new ArrayList<>();
		this.genres = genres;
		this.views = 0;
	}

	public String getTitle() {
		return title;
	}

	public ArrayList<Actor> getCast() {
		return cast;
	}

	public void View() {
		views++;
	}

	public void View(int n) {
		views+=n;
	}

	public int getViews() {
		return views;
	}

	abstract public double getRating();


}
