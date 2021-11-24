package repo;

import actor.ActorsAwards;
import entertainment.Season;
import fileio.ActionInputData;
import fileio.ActorInputData;

import javax.naming.directory.SearchControls;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Actor {
	private final String name;
	private final String careerDescription;
	private final Map<String, Video> filmography;
	private final Map<ActorsAwards, Integer> awards;

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
		double sum = 0;
		int cnt = 0;
		for (Video video : filmography.values()) {
			double video_rating = video.getRating();
			if (video_rating > 0)
				cnt++;
			sum += video_rating;
		}
		return sum/cnt;

	}

	public int numberOfAwards()
	{
		int s = 0;
		for (int x : awards.values())
		{
			s+=x;
		}
		return s;
	}

	public boolean hasAwards(List<String> awardList) {
		if (awardList != null)
			for (String award : awardList)
				if (!awards.containsKey(ActorsAwards.valueOf(award)))
					return false;
				return true;
	}

	public boolean hasWords(List<String> wordList) {
		String description = careerDescription.toLowerCase();

		if(wordList != null)
			for (String searchedWord : wordList) {
				String pattern = "\\b"+searchedWord.toLowerCase()+"\\b";
				Pattern p=Pattern.compile(pattern);
				Matcher m=p.matcher(description);
				if (!m.find())
					return false;
			}
				return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
