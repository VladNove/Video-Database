package repo.Actions.Recommendations;

import fileio.ActionInputData;
import repo.Entities.UserTypes.PremiumUser;
import repo.Entities.UserTypes.StandardUser;
import repo.Entities.Video;
import repo.Repository;
import java.util.Arrays;
import java.util.Comparator;
import utils.RecommendStrategy;

public final class SearchRecommend extends RecommendStrategy {
  private final String errorMessage = "SearchRecommendation cannot be applied!";

  public SearchRecommend(final Repository repository) {
    super(repository);
  }

  @Override
  public String visit(final StandardUser user, final ActionInputData action) {
    return errorMessage;
  }

  @Override
  public String visit(final PremiumUser user, final ActionInputData action) {
    Object[] result = getUnseenVideos(user)
            .filter(video -> video.hasGenre(action.getGenre()))
            .sorted(Comparator.comparing(Video::toString))
            .sorted(Comparator.comparing(Video::getRating))
            .toArray();
    if (result.length == 0) {
      return errorMessage;
    } else {
      return "SearchRecommendation result: " + Arrays.toString(result);
    }
  }
}
