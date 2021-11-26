package repo.Actions.Recommendations;

import fileio.ActionInputData;
import repo.Entities.UserTypes.PremiumUser;
import repo.Entities.UserTypes.StandardUser;
import repo.Entities.User;
import repo.Entities.Video;
import repo.Repository;

import java.util.Comparator;
import java.util.Optional;
import utils.RecommendStrategy;

public final class BestRecommend extends RecommendStrategy {
  public BestRecommend(final Repository repository) {
    super(repository);
  }

  @Override
  public String visit(final StandardUser user, final ActionInputData action) {
    return visit(user);
  }

  @Override
  public String visit(final PremiumUser user, final ActionInputData action) {
    return visit(user);
  }

  private String visit(final User user) {
    Optional<Video> recommendedVideo =
        getUnseenVideos(user).max(Comparator.comparing(Video::getRating));

    return recommendedVideo
        .map(video -> "BestRatedUnseenRecommendation result: " + video)
        .orElse("BestRatedUnseenRecommendation cannot be applied!");
  }
}
