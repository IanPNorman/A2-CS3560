package homework1;

import java.util.ArrayList;
import java.util.List;

// User Class 
public class User{
    private String id;
    private long creationTime;
    private List<User> followers;
    private List<User> followings;
    private List<String> newsFeed;
    private List<Observer> observers;
    private long lastUpdateTime;

    public User(String id) {
        this.id = id;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.newsFeed = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.creationTime = System.currentTimeMillis();
        this.lastUpdateTime = 0;
    }

    public String getId() {
        return id;
    }
    
    public long getCreationTime() {
        return creationTime;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowings() {
        return followings;
    }

    public List<String> getNewsFeed() {
        return newsFeed;
    }

    public void addFollower(User follower) {
        followers.add(follower);
    }

    public void addFollowing(User following) {
        followings.add(following);
    }

    
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers(String tweet) {
        for (Observer observer : observers) {
            observer.update(this, tweet);
        }
    }
    
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void postTweet(String tweet) {
        newsFeed.add(tweet);

        TwitterFeed visitor = new TwitterFeed(tweet);
        for (User follower : followers) {
            follower.accept(visitor);
        }
        lastUpdateTime = System.currentTimeMillis();
        notifyObservers(tweet);
    }
    
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

   
    @Override
    public String toString() {
        return getId();
    }
}
