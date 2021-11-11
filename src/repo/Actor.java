package repo;

import actor.ActorsAwards;
import entertainment.Season;
import fileio.ActorInputData;

import java.util.HashMap;
import java.util.Map;

public class Actor {
	private final String name;
	private final String careerDescription;
	private final Map<String, Video> filmography;
	private final Map<ActorsAwards, Integer> awards;
	private double rating;
	public Actor(ActorInputData actorInputData) {
		this.name = actorInputData.getName();
		this.careerDescription = actorInputData.getCareerDescription();
		this.filmography = new HashMap<>();
		this.awards = actorInputData.getAwards();
	}

	public Map<String, Video> getFilmography() {
		return filmography;
	}

	public double getRating() {
		//TODO stream this
		double sum = 0;
		int cnt = 0;
		for (Video video : filmography.values())
		{
			double video_rating = video.getRating();
			if (video_rating > 0)
				cnt++;
			sum += video_rating;
		}
		if (cnt == 0)
			rating = 0;
		else
			rating = sum/cnt;
		return sum/cnt;
	}

	@Override
	public String toString() {
		return name;
	}
}
