package repo;

import fileio.ActionInputData;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Query {
	public static void executeAction(ActionInputData action) throws IOException {
		Repository repository = Repository.getInstance();
		//TODO think about better system

		Object[] result = new Object[0];
		if (action.getObjectType().equals("actors")) {

			List <Actor> actorList = new ArrayList<>();
			if (action.getCriteria().equals("average")) {
				actorList = getActorsWithRating(repository);
				sortActorsByAverage(actorList);
			}
			if (action.getCriteria().equals("awards")) {
				actorList = getActorsWithAwards(repository,action.getFilters().get(3));
				actorList.sort(Comparator.comparing(Actor::numberOfAwards));

			}
			if (action.getCriteria().equals("filter_description")) {
				actorList = getActorsWithWords(repository,action.getFilters().get(2));
				actorList.sort(Comparator.comparing(Actor::toString));
			}

			if (action.getSortType().equals("desc"))
				Collections.reverse(actorList);

			result = actorList.subList(0, Math.min(actorList.size(), action.getNumber())).toArray();
		}

		if (action.getObjectType().equals("users")) {

			List <User> userList = new ArrayList<>(repository.getUsers().filter(user -> user.ratings() > 0).toList());
			userList.sort(Comparator.comparing(User::ratings));


			if (action.getSortType().equals("desc"))
				Collections.reverse(userList);

			result = userList.subList(0, Math.min(userList.size(), action.getNumber())).toArray();
		}

		if (action.getObjectType().equals("movies") || action.getObjectType().equals("shows")) {
			String year = action.getFilters().get(0).get(0);
			List<String> genres = action.getFilters().get(1);
			List<Video> videoList = getFilteredVideos(repository, year, genres);


			if (action.getCriteria().equals("ratings")) {
				videoList = new ArrayList<>(videoList.stream().filter(video -> video.getRating() > 0).toList());
				videoList.sort(Comparator.comparing(Video::getRating));
			}

			if (action.getCriteria().equals("most_viewed")) {
				videoList = new ArrayList<>(videoList.stream().filter(video -> video.getViews() > 0).toList());
				videoList.sort(Comparator.comparing(Video::getViews));
			}

			if (action.getCriteria().equals("favorite")) {
				videoList = new ArrayList<>(videoList.stream().filter(video -> video.getFavourites() > 0).toList());
				videoList.sort(Comparator.comparing(Video::getFavourites));
			}
			if (action.getCriteria().equals("longest"))
				videoList.sort(Comparator.comparing(Video::getDuration));


			if (action.getSortType().equals("desc"))
				Collections.reverse(videoList);


			result = videoList.subList(0, Math.min(videoList.size(), action.getNumber())).toArray();
		}

		outputQuery(action, result);
	}

	private static ArrayList<Actor> getActorsWithRating(Repository repository)
	{
		return new ArrayList<>(repository.getActors().filter(actor -> actor.getRating() > 0).toList());
	}

	private static ArrayList<Actor> getActorsWithAwards(Repository repository, List<String> awards)
	{
		return new ArrayList<>(repository.getActors().filter(actor -> actor.hasAwards(awards)).toList());
	}

	private static ArrayList<Actor> getActorsWithWords(Repository repository, List<String> words)
	{
		return new ArrayList<>(repository.getActors().filter(actor -> actor.hasWords(words)).toList());
	}

	private static ArrayList<Video> getFilteredVideos(Repository repository, String year ,List<String> genres)
	{
		return new ArrayList<>(repository.getVideos().filter(video -> video.isFromYear(year) && video.hasGenres(genres)).toList());
	}

	private static void sortActorsByAverage(List<Actor> actors)
	{
		actors.sort((o1, o2) -> {
			int rating = Double.compare(o1.getRating(), o2.getRating());
			if (rating != 0)
				return rating;
			else
				return o1.toString().compareTo(o2.toString());
		});
	}


	private static void outputQuery(ActionInputData action, Object[] result ) throws IOException {
		Output.write(action.getActionId(), "", "Query result: " + Arrays.toString(result));
	}


}
