package discussionboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class User {
	private String fullName;
	private String username;

	public User(String fullName, String username) throws IllegalArgumentException{
		if(fullName.trim().isEmpty() || fullName == null){
			throw new IllegalArgumentException("fullname can't be empty!");
		}else{
		this.fullName = fullName;}
		if(username.isEmpty() || username == null){
			this.username = fullName.split(" ")[0].toLowerCase();
		}else{
			this.username = username.toLowerCase();
		}
	}
		public String getFullName(){
			return this.fullName;
		}
		public String getUsername(){
			return this.username;
		}
	
}

class Post {
	private String content;
	private User user;

	public Post (String content, User user) throws IllegalArgumentException{
		
		if(content.isEmpty() || content == null){
			throw new IllegalArgumentException("content can't be empty!");
		}
		
		this.content = content;
		this.user = user;
	}
	
	public String getContent(){
		return this.content;
	}
	public User getUser() {
		return this.user;
	}
	public void printPost(){
		System.out.println("Created By: "+ this.user.getFullName() + " (@" + this.user.getUsername() + ")");
		System.out.println(content + "\n");
	}
}

public class DiscussionBoard extends JFrame{
    private JTextArea messageArea;
    private JPanel createUserPanel, createPostPanel, searchPanel;
    JTextField fullNameField, usernameField, searchUsernameField, registerUsernameField;
    private JTextArea postContentArea;
    String fullName, username;
    User user;
    Post post;

    private static ArrayList<User> users = new ArrayList<>();
	private static ArrayList<Post> posts = new ArrayList<>();
	private static HashMap<String, ArrayList<Integer>> userPostsMap = new HashMap<>();

    private static User findUser(String username){
			for(int i = 0; i < users.size(); i++){
				if(users.get(i).getUsername().equals(username)){
					return users.get(i);
				}
			}
			return null;
		}

    public DiscussionBoard(){
        super("Discussion Board");
        setSize(500,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1,2));

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        

        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem createUser = new JMenuItem("Create User");
        JMenuItem createPost = new JMenuItem("Create Post");
        JMenuItem searchPosts = new JMenuItem("Search Posts");

        optionsMenu.add(createUser);
        optionsMenu.add(createPost);
        optionsMenu.add(searchPosts);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);

        /** createUserPanel */
        createUserPanel = new JPanel(new GridLayout(6, 1));
        fullNameField = new JTextField();
        registerUsernameField = new JTextField();
        JButton registerButton = new JButton("Register");

       // panel = new JPanel(new GridLayout(6, 1));
        createUserPanel.add(new JLabel("Full Name:"));
        createUserPanel.add(fullNameField);
        createUserPanel.add(new JLabel("Username:"));
        createUserPanel.add(registerUsernameField);
        createUserPanel.add(registerButton);
       // createUserPanel.add(panel);
      // add(new JScrollPane(messageArea));

        registerButton.addActionListener(e -> {
        fullName = fullNameField.getText(); 
        username = usernameField.getText();
        try {
            if (findUser(username) != null) {
                displayMessage("Username already exists.");
            } else {
                user = new User(fullName, username);
                users.add(user);
                displayMessage("User created successfully.");
            }
        } catch (IllegalArgumentException er) {
            displayMessage("Error: " + er.getMessage());
        }
    });

    /** createPostPanel */
    createPostPanel = new JPanel(new GridLayout(6, 1));
    
    usernameField = new JTextField();

    JTextArea postContentArea = new JTextArea(5, 20);
    JButton createPostButton = new JButton("Create Post");

    createPostPanel.add(new JLabel("Username:"));
    createPostPanel.add(usernameField); 
    createPostPanel.add(new JLabel("Content:"));
    createPostPanel.add(new JScrollPane(postContentArea));
    createPostPanel.add(createPostButton);

    createPostButton.addActionListener(e -> {
        username = usernameField.getText();
        user = findUser(username);
        if (user == null) {
            displayMessage("User not registered.");
        } else {
            
            String content = postContentArea.getText();
            try {
                post = new Post(content, user);
                posts.add(post);
                userPostsMap.computeIfAbsent(username, k -> new ArrayList<>()).add(posts.size() - 1);
                displayMessage("Post created successfully.");
            } catch (IllegalArgumentException err) {
                displayMessage("Error: " + err.getMessage());
            }
        }
    });

/** searchPanel */
    searchPanel = new JPanel(new GridLayout(4, 1));
    
    searchUsernameField = new JTextField();
    JButton searchButton = new JButton("Search");

    searchPanel.add(new JLabel("Username:"));
    searchPanel.add(searchUsernameField);
    searchPanel.add(searchButton);

    searchButton.addActionListener(e -> {
        username = searchUsernameField.getText();
        ArrayList<Integer> userPostsIndices = userPostsMap.get(username);

        if (userPostsIndices == null || userPostsIndices.isEmpty()) {
            displayMessage("No posts made by user!");
        } else {
            displayMessage("Posts by " + username + ":");
            for (int index : userPostsIndices) {
                post = posts.get(index);
                displayMessage(post.getContent());
            }
        }
    });

    // Set default panel
        add(createUserPanel);
        add(new JScrollPane(messageArea));
        // Menu actions
        createUser.addActionListener(e -> switchPanel(createUserPanel));
        createPost.addActionListener(e -> switchPanel(createPostPanel));
        searchPosts.addActionListener(e -> switchPanel(searchPanel));

    }

    private void switchPanel(JPanel panel) {
        getContentPane().removeAll();
        add(panel);
        add(new JScrollPane(messageArea));
        revalidate();
        repaint();
    }

    private void displayMessage(String message) {
        messageArea.append(message + "\n");
    }
    

    public static void main(String[] args) {
           DiscussionBoard gui =  new DiscussionBoard();
           gui.setVisible(true);  
    }
}
