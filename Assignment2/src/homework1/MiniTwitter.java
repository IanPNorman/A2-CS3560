package homework1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import java.awt.*;


// Main class representing the Twitter application
public class MiniTwitter {
	private UserGroup rootGroup;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;

    public MiniTwitter() {
        this.rootGroup = new UserGroup("Root");
        this.rootNode = new DefaultMutableTreeNode(rootGroup);
        this.treeModel = new DefaultTreeModel(rootNode);
    }

    public void addUser(User user, UserGroup group) {
        group.addUser(user);
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user);
        rootNode.add(userNode);
        treeModel.reload();
    }

    public void addGroup(UserGroup group, UserGroup parentGroup) {
        parentGroup.addGroup(group);
        addGroupNode(group, parentGroup, rootNode);
        treeModel.reload();
    }

    public void addUserToGroup(User user, UserGroup group) {
        group.addUser(user);
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user);
        DefaultMutableTreeNode groupNode = findNode(rootNode, group);
        groupNode.add(userNode);
        treeModel.reload();
    }

    private void addGroupNode(UserGroup group, UserGroup parentGroup, DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode parentGroupNode = findNode(parentNode, parentGroup);
        if (parentGroupNode != null) {
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group);
            parentGroupNode.add(groupNode);
        }
    }
    
    
    private DefaultMutableTreeNode findNode(DefaultMutableTreeNode parentNode, UserGroup group) {
        if (parentNode.getUserObject().equals(group)) {
            return parentNode;
        } else {
            int childCount = parentNode.getChildCount();
            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                DefaultMutableTreeNode result = findNode(childNode, group);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
    }

    public void displayAdminControlPanel() {
        JFrame frame = new JFrame("Admin Control Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JButton createUserButton = new JButton("Create User");
        JTextField userIdTextField = new JTextField(15);
        JButton createGroupButton = new JButton("Create Group");
        JTextField groupIdTextField = new JTextField(15);
        JButton showTotalUsersButton = new JButton("Total Users");
        JButton showTotalGroupsButton = new JButton("Total Groups");
        JButton showTotalTweetsButton = new JButton("Total Tweets");
        JButton showPositiveTweetsButton = new JButton("Positive Tweets");
        JButton openUserPanelButton = new JButton("Open User Panel");
        JButton addUserToSelectedGroupButton = new JButton("Add User to Selected Group");
        JButton verifyIDButton = new JButton("Verify IDs");
        JTextField newUserIdTextField = new JTextField(15);


        JTree userTree = new JTree(treeModel);
        userTree.setCellRenderer(new CustomTreeCellRenderer());
        JScrollPane treeScrollPane = new JScrollPane(userTree);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("Create User: "), constraints);
        constraints.gridx++;
        panel.add(userIdTextField, constraints);
        constraints.gridx++;
        panel.add(createUserButton, constraints);
        constraints.gridy++;
        panel.add(openUserPanelButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel("Create Group: "), constraints);
        constraints.gridx++;
        panel.add(groupIdTextField, constraints);
        constraints.gridx++;
        panel.add(createGroupButton, constraints);
        
        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel("Add User to Selected Group: "), constraints);
        constraints.gridx++;
        panel.add(newUserIdTextField, constraints);
        constraints.gridx++;
        panel.add(addUserToSelectedGroupButton, constraints);
        
        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(showTotalUsersButton, constraints);
        constraints.gridx++;
        panel.add(showTotalGroupsButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(showTotalTweetsButton, constraints);
        constraints.gridx++;
        panel.add(showPositiveTweetsButton, constraints);
        
        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(verifyIDButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        panel.add(treeScrollPane, constraints);

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdTextField.getText().trim();
                if (!userId.isEmpty()) {
                    User newUser = new User(userId);
                    addUser(newUser, rootGroup);
                    userIdTextField.setText("");
                }
            }
        });
        
        verifyIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean idsValid = verifyIDs();
                if (idsValid) {
                    JOptionPane.showMessageDialog(frame, "All IDs are valid!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Some invalid IDs. They are printed in the console.");
                }
            }
        });

        createGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupId = groupIdTextField.getText().trim();
                if (!groupId.isEmpty()) {
                    UserGroup newGroup = new UserGroup(groupId);
                    addGroup(newGroup, rootGroup);
                    groupIdTextField.setText("");
                }
            }
        });

        showTotalUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalUsers = countTotalUsers(rootGroup);
                JOptionPane.showMessageDialog(frame, "Total Users: " + totalUsers);
            }
        });

        showTotalGroupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalGroups = countTotalGroups(rootGroup);
                JOptionPane.showMessageDialog(frame, "Total Groups: " + totalGroups);
            }
        });

        showTotalTweetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalTweets = countTotalTweets(rootGroup);
                JOptionPane.showMessageDialog(frame, "Total Tweets: " + totalTweets);
            }
        });
        
        openUserPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath[] selectedPaths = userTree.getSelectionPaths();

                if (selectedPaths != null) {
                    for (TreePath path : selectedPaths) {
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                        Object selectedObject = selectedNode.getUserObject();

                        if (selectedObject instanceof User) {
                            User selectedUser = (User) selectedObject;
                            JFrame userFrame = new JFrame(selectedUser.getId() + " User Panel");
                            userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            displayUserView(selectedUser);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select user(s) first.");
                }
            }
        });

        showPositiveTweetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalTweets = countTotalTweets(rootGroup);
                int positiveTweets = countPositiveTweets(rootGroup);
                double percentage = (double) positiveTweets / totalTweets * 100;
                JOptionPane.showMessageDialog(frame, "Positive Tweets: " + String.format("%.2f", percentage) + "%");
            }
        });
        
        addUserToSelectedGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUserId = newUserIdTextField.getText().trim();
                TreePath selectedPath = userTree.getSelectionPath();

                if (selectedPath != null) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
                    Object selectedObject = selectedNode.getUserObject();

                    if (selectedObject instanceof UserGroup && !newUserId.isEmpty()) {
                        User newUser = new User(newUserId);
                        addUserToGroup(newUser, (UserGroup) selectedObject);
                        newUserIdTextField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a user group and enter a user ID.");
                    }
                }
            }
        });
        
        
        

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private int countTotalUsers(UserGroup group) {
        int count = group.getUsers().size();
        for (UserGroup subGroup : group.getGroups()) {
            count += countTotalUsers(subGroup);
        }
        return count;
    }

    private int countTotalGroups(UserGroup group) {
        int count = group.getGroups().size();
        for (UserGroup subGroup : group.getGroups()) {
            count += countTotalGroups(subGroup);
        }
        return count;
    }

    private int countTotalTweets(UserGroup group) {
        int count = 0;
        for (User user : group.getUsers()) {
            count += user.getNewsFeed().size();
        }
        for (UserGroup subGroup : group.getGroups()) {
            count += countTotalTweets(subGroup);
        }
        return count;
    }

    private int countPositiveTweets(UserGroup group) {
        int count = 0;
        for (User user : group.getUsers()) {
            for (String tweet : user.getNewsFeed()) {
                if (isPositiveTweet(tweet)) {
                    count++;
                }
            }
        }
        for (UserGroup subGroup : group.getGroups()) {
            count += countPositiveTweets(subGroup);
        }
        return count;
    }

    private boolean isPositiveTweet(String tweet) {
        String[] positiveWords = {"cool", "nice", "epic", "awesome", "great", "good",};
        for (String word : positiveWords) {
            if (tweet.toLowerCase().contains(word)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean verifyIDs() {
        Set<String> idSet = new HashSet<>();
        return verifyIDs(rootNode, idSet);
    }

    private boolean verifyIDs(DefaultMutableTreeNode node, Set<String> idSet) {
        if (node.getUserObject() instanceof User) {
            User user = (User) node.getUserObject();
            String userID = user.getId();
            if (userID.contains(" ")) {
                System.out.println("Invalid Spaces in user IDs: " + userID);
                return false;
            } else if (idSet.contains(userID)) {
                System.out.println("Duplicate user IDs: " + userID);
                return false;
            }
            idSet.add(userID);
        } else if (node.getUserObject() instanceof UserGroup) {
            UserGroup group = (UserGroup) node.getUserObject();
            String groupID = group.getId();
            if (groupID.contains(" ")) {
                System.out.println("Invalid Spaces in groupIDs: " + groupID);
                return false;
            } else if (idSet.contains(groupID)) {
                System.out.println("Duplicate group IDs: " + groupID);
                return false;
            }
            idSet.add(groupID);
        }

        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
            if (!verifyIDs(childNode, idSet)) {
                return false;
            }
        }
        return true;
    }

    public void displayUserView(User user) {
        JFrame frame = new JFrame(user.getId() + " User View");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        

        DefaultListModel<String> followingListModel = new DefaultListModel<>();
        JList<String> followingList = new JList<>(followingListModel);
        JScrollPane followingScrollPane = new JScrollPane(followingList);

        DefaultListModel<String> newsFeedListModel = new DefaultListModel<>();
        JList<String> newsFeedList = new JList<>(newsFeedListModel);
        JScrollPane newsFeedScrollPane = new JScrollPane(newsFeedList);

        JTextField followUserIdTextField = new JTextField(15);
        JButton followUserButton = new JButton("Follow");

        JTextField tweetTextField = new JTextField(15);
        JButton postTweetButton = new JButton("Post Tweet");
        
        JLabel creationTimeLabel = new JLabel("Creation Time: " + user.getCreationTime());
        
        JLabel lastUpdateTimeLabel = new JLabel("Last Update Time: " + user.getLastUpdateTime());


        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("Following: "), constraints);
        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        panel.add(followingScrollPane, constraints);

        constraints.gridy++;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        constraints.weighty = 0;
        panel.add(new JLabel("Follow User: "), constraints);
        constraints.gridx++;
        panel.add(followUserIdTextField, constraints);
        constraints.gridx++;
        panel.add(followUserButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        panel.add(newsFeedScrollPane, constraints);

        constraints.gridy++;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        constraints.weighty = 0;
        panel.add(new JLabel("Tweet: "), constraints);
        constraints.gridx++;
        panel.add(tweetTextField, constraints);
        constraints.gridx++;
        panel.add(postTweetButton, constraints);
        constraints.gridy++;
        panel.add(creationTimeLabel, constraints);
        constraints.gridy++;
        panel.add(lastUpdateTimeLabel, constraints);

        for (User following : user.getFollowings()) {
            followingListModel.addElement(following.getId());

            for (String tweet : following.getNewsFeed()) {
                newsFeedListModel.addElement(following.getId() + ": " + tweet);
            }
        }
        
        followUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = followUserIdTextField.getText().trim();
                if (!userId.isEmpty()) {
                    User following = findUser(userId);
                    if (following != null && !user.getFollowings().contains(following)) {
                        user.addFollowing(following);
                        followingListModel.addElement(following.getId());
                    }
                    followUserIdTextField.setText("");
                }
            }
        });

        postTweetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tweet = tweetTextField.getText().trim();
                if (!tweet.isEmpty()) {
                    user.postTweet(tweet);
                    newsFeedListModel.addElement(tweet);
                    tweetTextField.setText("");
                }
            }
        });

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private User findUser(String userId) {
        return findUser(rootGroup, userId);
    }

    private User findUser(UserGroup group, String userId) {
        for (User user : group.getUsers()) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        for (UserGroup subGroup : group.getGroups()) {
            User result = findUser(subGroup, userId);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    
    @SuppressWarnings("serial")
	private static class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            // Set the text based on the toString() method of the user or user group
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userOrGroup = node.getUserObject();
                if (userOrGroup != null) {
                    setText(userOrGroup.toString());
                }
            }

            return this;
        }
    }
}