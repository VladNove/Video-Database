package repo.Actions;

import fileio.ActionInputData;
import repo.Entities.*;
import repo.Entities.Movie;
import repo.Output;
import repo.Repository;

import java.io.IOException;

public class Command extends ActionExecutor {

    public Command(Repository repository) {
        super(repository);
    }

    public void executeAction(ActionInputData action) throws IOException {
        User user = repository.getUser(action.getUsername());
        Video video = repository.getVideo(action.getTitle());
        String actionType = action.getType();

        if (!actionType.equals("view") && user.notSeenVideo(video)) {
            Output.write(action.getActionId(), "error -> " +
                    video + " is not seen");
            return;
        }

        CommandFunction cmd = switch (actionType) {
            case "view" -> Command::View;
            case "rating" -> Command::Rate;
            default -> Command::Favourite;
        };

        cmd.apply(user, video, action);
    }

    @FunctionalInterface
    interface CommandFunction {
        void apply(User user, Video video, ActionInputData action) throws IOException;
    }

    private static void View(User user, Video video, ActionInputData action) throws IOException {
        int views = user.view(video);
        video.View();
        Output.write(action.getActionId(), "success -> " +
                video + " was viewed with total views of " + views);
    }

    private static void Favourite(User user, Video video, ActionInputData action) throws IOException {

        if (user.isFavourite(video)) {
            Output.write(action.getActionId(), "error -> " +
                    video + " is already in favourite list");
            return;
        }

        user.Favourite(video);
        video.Favourite();
        Output.write(action.getActionId(), "success -> " +
                video + " was added as favourite");
    }

    private static void Rate(User user, Video video, ActionInputData action) throws IOException {
        double grade = action.getGrade();
        int seasonNumber = action.getSeasonNumber();
        Ratable target;
        if (seasonNumber == 0)
            target = (Movie) video;
        else
            target = new SeasonRating(((Series) video).getSeason(seasonNumber));


        if (user.rated(target)) {
            Output.write(action.getActionId(), "error -> " + video + " has been already rated");
        } else {
            user.rate(target);
            target.Rate(grade);
            Output.write(action.getActionId(), "success -> " +
                    video + " was rated with " + grade + " by " + user.getUsername());
        }

    }


}
