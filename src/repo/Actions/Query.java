package repo.Actions;

import fileio.ActionInputData;
import repo.Entities.Actor;
import repo.Entities.Entity;
import repo.Entities.User;
import repo.Entities.Video;
import repo.Helper;
import repo.Output;
import repo.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Query extends ActionExecutor {


    public Query(Repository repository) {
        super(repository);
    }

    private static void outputQuery(ActionInputData action, Stream<?> result) throws IOException {
        Output.write(action.getActionId(), "Query result: " + Arrays.toString(result.toArray()));
    }

    private Stream<Actor> QueryActors(ActionInputData action) {
        Predicate<Actor> filter = actor -> switch (action.getCriteria()) {
            case "average" -> actor.getRating() > 0;
            case "awards" -> actor.hasAwards(action.getFilters().get(3));
            default -> actor.hasWords(action.getFilters().get(2));
        };
        Comparator<Actor> comparator = switch (action.getCriteria()) {
            case "average" -> Comparator.comparing(Actor::getRating);
            case "awards" -> Comparator.comparing(Actor::numberOfAwards);
            default -> (o1, o2) -> 0;
        };

        return repository.getActors().filter(filter).sorted(comparator);
    }

    private Stream<? extends Video> QueryVideos(ActionInputData action) {
        String year = action.getFilters().get(0).get(0);
        List<String> genres = action.getFilters().get(1);
        Stream<? extends Video> videoStream = (action.getObjectType().equals("movies")) ?
                repository.getMovies() :
                repository.getSeries();

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

    public void executeAction(ActionInputData action) throws IOException {
        Stream<? extends Entity> result = Stream.empty();
        String object = action.getObjectType();

        if (object.equals("actors"))
            result = QueryActors(action);

        if (object.equals("users"))
            result = repository.getUsers()
                    .filter(user -> user.ratings() > 0)
                    .sorted(Comparator.comparing(User::ratings));

        if (object.equals("movies") || object.equals("shows"))
            result = QueryVideos(action);

        if (action.getSortType().equals("desc"))
            result = Helper.reverse(result);

        outputQuery(action, result.limit(action.getNumber()));
    }

}
