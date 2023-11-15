package homework1;

public class TwitterFeed implements Visitor {
    private String tweet;

    public TwitterFeed(String tweet) {
        this.tweet = tweet;
    }

    @Override
    public void visit(User user) {
        user.getNewsFeed().add(tweet);
    }
}
