package repo;

import fileio.ActionInputData;

import java.io.IOException;
import java.util.*;

public class Recommendation {
    public static void executeAction(ActionInputData action) throws IOException {
        Repository repository = Repository.getInstance();
        User user = repository.getUser(action.getUsername());
        Optional<Video> recommendedVideo;


        if(action.getType().equals("standard"))
        {
            recommendedVideo = repository.getVideos().
                    filter(user::seenVideo).
                    min(Comparator.comparing(Video::getId));

            if (recommendedVideo.isPresent())
                Output.write(action.getActionId(), "", "StandardRecommendation result: " + recommendedVideo.get());
            else
                Output.write(action.getActionId(), "", "StandardRecommendation cannot be applied!");
        }

        if(action.getType().equals("search"))
        {
            Object[] result = repository.getVideos().
                    filter(user::seenVideo).
                    filter(video -> video.hasGenre(action.getGenre())).
                    sorted((o1, o2) -> {
                        int rating = Double.compare(o1.getRating(), o2.getRating());
                        if (rating != 0)
                            return rating;
                        else
                            return o1.toString().compareTo(o2.toString());
                    }).toArray();
            if (result.length == 0)
                Output.write(action.getActionId(), "", "SearchRecommendation cannot be applied!");
            else
                Output.write(action.getActionId(), "", "SearchRecommendation result: " + Arrays.toString(result));
        }

        if(action.getType().equals("best_unseen"))
        {
            recommendedVideo = repository.getVideos().
                    filter(video -> user.seenVideo(video)).
                    max((o1, o2) -> {
                        int rating = Double.compare(o1.getRating(), o2.getRating());
                        if (rating != 0)
                            return rating;
                        else
                            return -Integer.compare(o1.getId(), o2.getId());
                    });

            if (recommendedVideo.isPresent())
                Output.write(action.getActionId(), "", "BestRatedUnseenRecommendation result: " + recommendedVideo.get());
            else
                Output.write(action.getActionId(), "", "BestRatedUnseenRecommendation cannot be applied!");
        }

        if(action.getType().equals("favorite"))
        {
            recommendedVideo = repository.getVideos().
                    filter(video -> user.seenVideo(video)).
                    max((o1, o2) -> {
                        int rating = Double.compare(o1.getFavourites(), o2.getFavourites());
                        if (rating != 0)
                            return rating;
                        else
                            return -Integer.compare(o1.getId(), o2.getId());
                    });

            if (recommendedVideo.isPresent())
                Output.write(action.getActionId(), "", "FavoriteRecommendation result: " + recommendedVideo.get());
            else
                Output.write(action.getActionId(), "", "FavoriteRecommendation cannot be applied!");
        }

        if(action.getType().equals("popular"))
        {
            if (!user.getSubscriptionType().equals("PREMIUM")) {
                Output.write(action.getActionId(), "", "PopularRecommendation cannot be applied!");
                return;
            }

            List<String> popularGenres = new ArrayList<>(
                    repository.getGenres().sorted(Comparator.comparing(Recommendation::getGenreViews).reversed()).toList()
            );

            for(String genre : popularGenres)
            {
                recommendedVideo = repository.getVideos().
                        filter(video -> user.seenVideo(video)).
                        filter(video -> video.hasGenre(genre)).
                        min(Comparator.comparing(Video::getId));
                if (recommendedVideo.isPresent()) {
                    Output.write(action.getActionId(), "", "PopularRecommendation result: " + recommendedVideo.get());
                    return;
                }
            }

            Output.write(action.getActionId(), "", "PopularRecommendation cannot be applied!");
        }
    }

    public static int getGenreViews(String genre) {
        int views = 0;
        List<Video> videos = new ArrayList<>(Repository.getInstance().getVideos().filter(video -> video.hasGenre(genre)).toList());

        for (Video video : videos) {
            views += video.getViews();
        }
        return views;
    }
}
