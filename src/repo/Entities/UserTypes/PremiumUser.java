package repo.Entities.UserTypes;

import fileio.ActionInputData;
import fileio.UserInputData;
import utils.RecommendStrategy;
import repo.Entities.User;

public final class PremiumUser extends User {
  public PremiumUser(final UserInputData userInputData) {
    super(userInputData);
  }

  @Override
  public String acceptRecommendation(
      final RecommendStrategy recommendStrategy, final ActionInputData action) {
    return recommendStrategy.visit(this, action);
  }
}
