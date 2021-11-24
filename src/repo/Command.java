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
		if (action.getType().equals("view")) {
			View(user, video, action);
			return;
		}

		if (user.seenVideo(video)) {
			Output.write(action.getActionId(), "", "error -> " +
					video + " is not seen");
			return;
		}

		if (action.getType().equals("favorite"))
			Favourite(user, video, action);
		if (action.getType().equals("rating"))
			Rate(user, video, action);

	}

	private static void View(User user, Video video, ActionInputData action) throws IOException {
		int views = user.view(video);
		video.View();
		Output.write(action.getActionId(), "", "success -> " +
				video + " was viewed with total views of " + views);
	}

	private static void Favourite(User user, Video video, ActionInputData action) throws IOException {

		if (user.isFavourite(video)) {
			Output.write( action.getActionId(), "", "error -> " +
					video + " is already in favourite list");
			return;
		}

		user.Favourite(video);
		video.Favourite();
		Output.write(action.getActionId(), "","success -> " +
				video + " was added as favourite");
	}

	private static void Rate(User user, Video video, ActionInputData action) throws IOException {
		double grade = action.getGrade();
		int seasonNumber = action.getSeasonNumber();
		boolean error = false;

		if (seasonNumber==0) {
			Movie movie = (Movie) video;
			if (user.rated(movie)) {
				error = true;
			}
			else {
				user.rate(movie, grade);
				movie.Rate(grade);
			}
		}
		else {
			Series series = (Series) video;
			Season season = series.getSeason(seasonNumber);
			if (user.rated(season)) {
				error = true;
			}
			else {
				user.rate(season, grade);
				series.Rate(seasonNumber, grade);
			}
		}


		if (error)
			Output.write(action.getActionId(), "", "error -> " + video + " has been already rated");
		else
			Output.write(action.getActionId(), "", "success -> " +
				video + " was rated with " + grade + " by " + user.getUsername());
	}



}
