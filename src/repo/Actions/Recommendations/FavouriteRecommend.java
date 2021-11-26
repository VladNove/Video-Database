package repo.Actions.Recommendations;

import fileio.ActionInputData;
import repo.Entities.UserTypes.PremiumUser;
import repo.Entities.UserTypes.StandardUser;
import repo.Entities.Video;
import repo.Repository;

import java.util.Comparator;
import java.util.Optional;
import utils.RecommendStrategy;

public final class FavouriteRecommend extends RecommendStrategy {
  private final String errorMessage = "FavoriteRecommendation cannot be applied!";

  public FavouriteRecommend(final Repository repository) {
    super(repository);
  }

  @Override
  public String visit(final StandardUser user, final ActionInputData action) {
    return errorMessage;
  }

  @Override
  public String visit(final PremiumUser user, final ActionInputData action) {
    Optional<Video> recommendedVideo =
        getUnseenVideos(user)
            .sorted(Comparator.comparing(Video::getId))
            .max(Comparator.comparing(Video::getFavourites));

    return recommendedVideo
        .map(video -> "FavoriteRecommendation result: " + video)
        .orElse(errorMessage);
  }
}
