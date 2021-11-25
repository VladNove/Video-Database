package repo.Entities;

import entertainment.Season;

import java.util.Objects;

public class SeasonRating implements Ratable {
    private final Season season;

    public SeasonRating(Season season) {
        this.season = season;
    }

    public void Rate(double grade)
    {
        season.getRatings().add(grade);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeasonRating that)) return false;
        return season.equals(that.season);
    }

    @Override
    public int hashCode() {
        return Objects.hash(season);
    }
}
