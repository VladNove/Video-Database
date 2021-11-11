package repo;

import fileio.ActionInputData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Query {
	public static void executeAction(ActionInputData action) throws IOException {
		Repository repository = Repository.getInstance();
		//TODO think about better system
		if (action.getObjectType().equals("actors")) {
			if (action.getCriteria().equals("average")) {
				List<Actor> ratedActors = new ArrayList<>(repository.getActors().filter(actor -> actor.getRating() > 0).toList());

				ratedActors.sort((o1, o2) -> {
					int rating = Double.compare(o1.getRating(), o2.getRating());
					if (rating != 0)
						return rating;
					else
						return o1.toString().compareTo(o2.toString());
				});
				outputActorList(action, ratedActors.subList(0, Math.min(ratedActors.size(), action.getNumber())));

			}

		}
	}

	private static void outputActorList(ActionInputData action, List<Actor> actors) throws IOException {
		Output.write(action.getActionId(), "", "Query result: " + Arrays.toString(actors.toArray()));
	}


}
