package repo;

import entertainment.Season;
import fileio.ActionInputData;

import java.io.IOException;

public class Command {

	//TODO maybe change pattern
	public static void executeAction(ActionInputData action) throws IOException {
		Repository repository = Repository.getInstance();
		User user = repository.getUser(action.getUsername());
		if (user == null) return;
		Video video = repository.getVideo(action.getTitle());
		if (video == null) return;
		if (action.getType().equals("view"))
			View(user, video, action);
		if (action.getType().equals("favorite"))
			Favourite(user, video, action);
		if (action.getType().equals("rating"))
			Rate(user, video, action);

	}

	private static void View(User user, Video video, ActionInputData action) throws IOException {
		int views = user.view(video);
		video.View();
		Output.write(action.getActionId(), "", "success -> " +
				video.getTitle() + " was viewed with total views of " + views);
	}

	private static void Favourite(User user, Video video, ActionInputData action) throws IOException {

		if (!user.seenVideo(video)) {
			Output.write(action.getActionId(), "", "error -> " +
					video.getTitle() + " is not seen");
			return;
		}

		if (user.isFavourite(video)) {
			Output.write( action.getActionId(), "", "error -> " +
					video.getTitle() + " is already in favourite list");
			return;
		}

		user.Favourite(video);
		Output.write(action.getActionId(), "","success -> " +
				video.getTitle() + " was added as favourite");
	}

	private static void Rate(User user, Video video, ActionInputData action) throws IOException {
		double grade = action.getGrade();
		int seasonNumber = action.getSeasonNumber();
		if (seasonNumber==0) {
			Movie movie = (Movie) video;
			user.Rate(movie, grade);
			movie.Rate(grade);
		}
		else {
			Series series = (Series) video;
			Season season = series.getSeason(seasonNumber);
			user.Rate(season, grade);
			series.Rate(seasonNumber, grade);
		}

		Output.write(action.getActionId(), "", "success -> " +
				video.getTitle() + " was rated with " + grade + " by " + user.getUsername());
	}



}
