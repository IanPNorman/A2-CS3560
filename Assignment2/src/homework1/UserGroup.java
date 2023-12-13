package homework1;

import java.util.ArrayList;
import java.util.List;


class UserGroup {
    private String id;
    private List<User> users;
    private List<UserGroup> groups;
    private long creationTime;
    

    public UserGroup(String id) {
        this.id = id;
        this.users = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.creationTime = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<UserGroup> getGroups() {
        return groups;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addGroup(UserGroup group) {
        groups.add(group);
    }

    public void addToGroup(UserGroup groupToAdd) {
        groupToAdd.addGroup(this);
    }
    
    @Override
    public String toString() {
        return getId();
    }
    
    public long getCreationTime() {
        return creationTime;
    }
}
