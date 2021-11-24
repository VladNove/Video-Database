package repo;

import java.util.ArrayList;
import java.util.List;

public abstract class Video {
    private final String title;

    private final int id;

    private final int year;

    private final ArrayList<Actor> cast;

    private final ArrayList<String> genres;
    public double rating;
    private int views;
    private int favourites;

    public Video(String title, int year, ArrayList<String> genres, int id) {
        this.title = title;
        this.year = year;
        this.cast = new ArrayList<>();
        this.genres = genres;
        this.views = 0;
        this.favourites = 0;
        this.id = id;
    }

    @Override
    public String toString() {
        return title;
    }

    public ArrayList<Actor> getCast() {
        return cast;
    }

    public void Favourite() {
        favourites++;
    }

    public void View() {
        views++;
    }

    public void View(int n) {
        views += n;
    }

    public int getViews() {
        return views;
    }

    abstract public double getRating();

    public int getFavourites() {
        return favourites;
    }

    public abstract int getDuration();


    public abstract boolean isMovie();

    public abstract boolean isSeries();

    public boolean isFromYear(String year) {
        if (year == null) return true;
        return this.year == Integer.parseInt(year);
    }

    public boolean hasGenre(String searchedGenre) {
        return genres.contains(searchedGenre);
    }

    public boolean hasGenres(List<String> searchedGenres) {
        if (searchedGenres != null)
            for (String word : searchedGenres)
                if (!genres.contains(word) && word != null)
                    return false;
        return true;
    }

    public int getId() {
        return id;
    }
}
