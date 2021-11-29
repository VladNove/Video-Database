package repo.Actions.Recommendations;

import fileio.ActionInputData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import repo.Entities.UserTypes.PremiumUser;
import repo.Entities.UserTypes.StandardUser;
import repo.Entities.Video;
import repo.Repository;
import utils.RecommendStrategy;


public final class PopularRecommend extends RecommendStrategy {
  private final String errorMessage = "PopularRecommendation cannot be applied!";

  public PopularRecommend(final Repository repository) {
    super(repository);
  }

  @Override
  public String visit(final StandardUser user, final ActionInputData action) {
    return errorMessage;
  }

  @Override
  public String visit(final PremiumUser user, final ActionInputData action) {
    List<String> popularGenres =
        new ArrayList<>(
            repository
                .getGenres()
                .sorted(Comparator.comparingInt(this::getGenreViews).reversed())
                .toList());

    for (String genre : popularGenres) {
      Optional<Video> recommendedVideo =
          getUnseenVideos(user)
              .filter(video -> video.hasGenre(genre))
              .findFirst();
      if (recommendedVideo.isPresent()) {
        return "PopularRecommendation result: " + recommendedVideo.get();
      }
    }
    return errorMessage;
  }

  private int getGenreViews(final String genre) {
    return repository
        .getVideos()
        .filter(video -> video.hasGenre(genre))
        .mapToInt(Video::getViews)
        .sum();
  }
}
