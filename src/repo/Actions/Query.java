package repo.Actions;

import fileio.ActionInputData;
import repo.Entities.Actor;
import repo.Entities.Entity;
import repo.Entities.User;
import repo.Entities.Video;
import utils.Helper;
import repo.Output;
import repo.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Query extends ActionExecutor {


    public Query(final Repository repository) {
        super(repository);
    }

    private static void outputQuery(final ActionInputData action, final Stream<?> result)
            throws IOException {
        Output.write(action.getActionId(), "Query result: " + Arrays.toString(result.toArray()));
    }

    private Stream<Actor> queryActors(final ActionInputData action) {
        Predicate<Actor> filter = actor -> switch (action.getCriteria()) {
            case "average" -> actor.getRating() > 0;
            case "awards" -> actor.hasAwards(action.getFilters().get(2 + 1));
            default -> actor.hasWords(action.getFilters().get(2));
        };
        Comparator<Actor> comparator = switch (action.getCriteria()) {
            case "average" -> Comparator.comparing(Actor::getRating);
            case "awards" -> Comparator.comparing(Actor::numberOfAwards);
            default -> (o1, o2) -> 0;
        };

        return repository.getActors().filter(filter).sorted(comparator);
    }

    private Stream<? extends Video> queryVideos(final ActionInputData action) {
        String year = action.getFilters().get(0).get(0);
        List<String> genres = action.getFilters().get(1);
        Stream<? extends Video> videoStream = (action.getObjectType().equals("movies"))
                ? repository.getMovies()
                : repository.getSeries();

        Predicate<Video> filter = video -> switch (action.getCriteria()) {
            case "ratings" -> video.getRating() > 0;
            case "most_viewed" -> video.getViews() > 0;
            case "favorite" -> video.getFavourites() > 0;
            default -> true;
        };
        Comparator<Video> comparator = switch (action.getCriteria()) {
            case "ratings" -> Comparator.comparing(Video::getRating);
            case "most_viewed" -> Comparator.comparing(Video::getViews);
            case "favorite" -> Comparator.comparing(Video::getFavourites);
            default -> Comparator.comparing(Video::getDuration);
        };

        return videoStream
                .filter(video -> video.isFromYear(year) && video.hasGenres(genres))
                .filter(filter).sorted(comparator);
    }


    /** executes an action of the Query type */
    public void executeAction(final ActionInputData action) throws IOException {
        Stream<? extends Entity> result = switch (action.getObjectType()) {
            case "actors" -> queryActors(action);
            case "movies", "shows" -> queryVideos(action);
            default -> repository.getUsers()
                    .filter(user -> user.ratings() > 0)
                    .sorted(Comparator.comparing(User::ratings));
        };

        if (action.getSortType().equals("desc")) {
            result = Helper.reverse(result);
        }

        outputQuery(action, result.limit(action.getNumber()));
    }

}
