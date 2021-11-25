package repo.Actions;

import fileio.ActionInputData;
import repo.Entities.User;
import repo.Entities.Video;
import repo.Output;
import repo.Repository;

import java.io.IOException;
import java.util.*;

public class Recommendation extends ActionExecutor {
    public Recommendation(Repository repository) {
        super(repository);
    }

    public void executeAction(ActionInputData action) throws IOException {
        User user = repository.getUser(action.getUsername());
        Optional<Video> recommendedVideo;


        if(action.getType().equals("standard"))
        {
            recommendedVideo = repository.getVideos().
                    filter(user::notSeenVideo).
                    min(Comparator.comparing(Video::getId));

            if (recommendedVideo.isPresent())
                Output.write(action.getActionId(), "StandardRecommendation result: " + recommendedVideo.get());
            else
                Output.write(action.getActionId(), "StandardRecommendation cannot be applied!");
        }

        if(action.getType().equals("search"))
        {
            Object[] result = repository.getVideos().
                    filter(user::notSeenVideo).
                    filter(video -> video.hasGenre(action.getGenre())).
                    sorted(Comparator.comparing(Video::getRating)).
                    toArray();
            if (result.length == 0)
                Output.write(action.getActionId(), "SearchRecommendation cannot be applied!");
            else
                Output.write(action.getActionId(), "SearchRecommendation result: " + Arrays.toString(result));
        }

        if(action.getType().equals("best_unseen"))
        {
            recommendedVideo = repository.getVideos().
                    filter(user::notSeenVideo).
                    sorted(Comparator.comparing(Video::getId)).
                    max(Comparator.comparing(Video::getRating));

            if (recommendedVideo.isPresent())
                Output.write(action.getActionId(), "BestRatedUnseenRecommendation result: " + recommendedVideo.get());
            else
                Output.write(action.getActionId(), "BestRatedUnseenRecommendation cannot be applied!");
        }

        if(action.getType().equals("favorite"))
        {
            recommendedVideo = repository.getVideos().
                    filter(user::notSeenVideo).
                    sorted(Comparator.comparing(Video::getId)).
                    max(Comparator.comparing(Video::getFavourites));

            if (recommendedVideo.isPresent())
                Output.write(action.getActionId(), "FavoriteRecommendation result: " + recommendedVideo.get());
            else
                Output.write(action.getActionId(), "FavoriteRecommendation cannot be applied!");
        }

        if(action.getType().equals("popular"))
        {
            if (!user.getSubscriptionType().equals("PREMIUM")) {
                Output.write(action.getActionId(), "PopularRecommendation cannot be applied!");
                return;
            }

            List<String> popularGenres = new ArrayList<>(
                    repository.getGenres().
                            sorted(Comparator.comparingInt(this::getGenreViews).reversed()).
                            toList()
            );

            for(String genre : popularGenres)
            {
                recommendedVideo = repository.getVideos().
                        filter(user::notSeenVideo).
                        filter(video -> video.hasGenre(genre)).
                        min(Comparator.comparing(Video::getId));
                if (recommendedVideo.isPresent()) {
                    Output.write(action.getActionId(), "PopularRecommendation result: " + recommendedVideo.get());
                    return;
                }
            }

            Output.write(action.getActionId(), "PopularRecommendation cannot be applied!");
        }
    }

    public int getGenreViews(String genre) {
        int views = 0;
        List<Video> videos = new ArrayList<>(repository.getVideos().filter(video -> video.hasGenre(genre)).toList());

        for (Video video : videos) {
            views += video.getViews();
        }
        return views;
    }
}
