package utils;

import entertainment.Season;

import java.util.Objects;

/**Ratable wrapper for season class*/
public final class SeasonRating implements Ratable {
    private final Season season;

    public SeasonRating(final Season season) {
        this.season = season;
    }

    @Override
    public void rate(final double grade) {
        season.getRatings().add(grade);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeasonRating that)) {
            return false;
        }
        return season.equals(that.season);
    }

    @Override
    public int hashCode() {
        return Objects.hash(season);
    }
}
