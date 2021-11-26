package repo.Actions;

import fileio.ActionInputData;

import repo.Entities.User;
import repo.Entities.Video;
import utils.Ratable;
import utils.SeasonRating;
import repo.Entities.VideoTypes.Movie;
import repo.Entities.VideoTypes.Series;
import repo.Output;
import repo.Repository;

import java.io.IOException;

public final class Command extends ActionExecutor {

    public Command(final Repository repository) {
        super(repository);
    }

    private static void view(final User user, final Video video, final ActionInputData action)
            throws IOException {
        int views = user.view(video);
        video.view();
        Output.write(action.getActionId(), "success -> "
                + video + " was viewed with total views of " + views);
    }

    private static void favourite(final User user, final Video video, final ActionInputData action)
            throws IOException {

        if (user.isFavourite(video)) {
            Output.write(action.getActionId(), "error -> "
                   + video + " is already in favourite list");
            return;
        }

        user.favourite(video);
        video.favourite();
        Output.write(action.getActionId(), "success -> "
                + video + " was added as favourite");
    }

    private static void rate(final User user, final Video video, final ActionInputData action)
            throws IOException {
        double grade = action.getGrade();
        int seasonNumber = action.getSeasonNumber();
        Ratable target;
        if (seasonNumber == 0) {
            target = (Movie) video;
        } else {
            target = new SeasonRating(((Series) video).getSeason(seasonNumber));
        }

        if (user.rated(target)) {
            Output.write(action.getActionId(), "error -> " + video + " has been already rated");
        } else {
            user.rate(target);
            target.rate(grade);
            Output.write(action.getActionId(), "success -> "
                   + video + " was rated with " + grade + " by " + user);
        }

    }

    /** executes an action of the Command type */
    public void executeAction(final ActionInputData action) throws IOException {
        User user = repository.getUser(action.getUsername());
        Video video = repository.getVideo(action.getTitle());
        String actionType = action.getType();

        if (!actionType.equals("view") && user.notSeenVideo(video)) {
            Output.write(action.getActionId(), "error -> " + video + " is not seen");
            return;
        }

        CommandFunction cmd = switch (actionType) {
            case "view" -> Command::view;
            case "rating" -> Command::rate;
            default -> Command::favourite;
        };

        cmd.apply(user, video, action);
    }

    @FunctionalInterface
    interface CommandFunction {
        void apply(User user, Video video, ActionInputData action) throws IOException;
    }


}
