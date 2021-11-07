package repo;

import fileio.ActionInputData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Query {
	public static void executeAction(ActionInputData action) throws IOException {
		Repository repository = Repository.getInstance();
		//TODO think about better system
		if (action.getObjectType().equals("actors"))
		{
			if (action.getCriteria().equals("average"))
			{
				List<Actor> actorArrayList =
				repository.getActors().filter(actor -> actor.getRating() > 0).sorted(
						Comparator.comparingDouble(Actor::getRating)).toList();
				outputActorList(action, actorArrayList);

			}

		}
	}

	private static void outputActorList(ActionInputData action, List<Actor> actors) throws IOException {
		Output.write( action.getActionId(), "", "Query result: " + Arrays.toString(actors.toArray()));
	}


}
