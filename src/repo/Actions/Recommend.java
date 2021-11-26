package repo.Actions;

import fileio.ActionInputData;
import utils.RecommendStrategy;
import repo.Actions.Recommendations.BestRecommend;
import repo.Actions.Recommendations.FavouriteRecommend;
import repo.Actions.Recommendations.SearchRecommend;
import repo.Actions.Recommendations.PopularRecommend;
import repo.Actions.Recommendations.StandardRecommend;
import repo.Entities.User;
import repo.Output;
import repo.Repository;

import java.io.IOException;
import java.util.HashMap;

public final class Recommend extends ActionExecutor {

  private final HashMap<String, RecommendStrategy> recommendStrategies;

  public Recommend(final Repository repository) {
    super(repository);
    recommendStrategies = new HashMap<>();
    recommendStrategies.put("best_unseen", new BestRecommend(repository));
    recommendStrategies.put("search", new SearchRecommend(repository));
    recommendStrategies.put("favorite", new FavouriteRecommend(repository));
    recommendStrategies.put("popular", new PopularRecommend(repository));
    recommendStrategies.put("standard", new StandardRecommend(repository));
  }

  /** executes a recommendation action */
  public void executeAction(final ActionInputData action) throws IOException {
    User user = repository.getUser(action.getUsername());
    RecommendStrategy rs = recommendStrategies.get(action.getType());

    String result = user.acceptRecommendation(rs, action);
    Output.write(action.getActionId(), result);
  }
}
