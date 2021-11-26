package repo.Actions.Recommendations;

import fileio.ActionInputData;
import repo.Entities.UserTypes.PremiumUser;
import repo.Entities.UserTypes.StandardUser;
import repo.Entities.User;
import repo.Entities.Video;
import repo.Repository;

import java.util.Optional;
import utils.RecommendStrategy;

public final class StandardRecommend extends RecommendStrategy {
  public StandardRecommend(final Repository repository) {
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
    Optional<Video> recommendedVideo;
    recommendedVideo = getUnseenVideos(user).findFirst();

    return recommendedVideo
        .map(video -> "StandardRecommendation result: " + video)
        .orElse("StandardRecommendation cannot be applied!");
  }
}
