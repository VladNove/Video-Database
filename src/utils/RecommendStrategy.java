package utils;

import fileio.ActionInputData;
import repo.Entities.UserTypes.PremiumUser;
import repo.Entities.UserTypes.StandardUser;
import repo.Entities.User;
import repo.Entities.Video;
import repo.Repository;

import java.util.Comparator;
import java.util.stream.Stream;


/**Strategy and Visitor pattern for recommendation strategies*/
public abstract class RecommendStrategy {

  protected final Repository repository;

  public RecommendStrategy(final Repository repository) {
    this.repository = repository;
  }

  protected final Stream<Video> getUnseenVideos(final User user) {
    return repository
        .getVideos()
        .filter(user::notSeenVideo)
        .sorted(Comparator.comparing(Video::getId));
  }

  /**Applies recommendation for Standard user*/
  public abstract String visit(StandardUser user, ActionInputData action);

  /**Applies recommendation for Premium user*/
  public abstract String visit(PremiumUser user, ActionInputData action);
}
