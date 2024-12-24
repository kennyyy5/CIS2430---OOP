package discussionboard;
import java.util.*;
import java.io.*;

class User {
    private String fullName;
    private String username;

    public User(String fullName, String username) {
        this.fullName = fullName;
        if (username.trim().isEmpty()) {
            this.username = fullName.split(" ")[0].toLowerCase();
        } else {
            this.username = username.toLowerCase();
        }
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getUsername() {
        return this.username;
    }
}

abstract class Post {
    protected String title;
    protected User user;
    protected int postId;
    private static int postIdCounter = 0;

    public Post(String title, User user) {
        this.title = title;
        this.user = user;
        this.postId = postIdCounter++;
    }

    public String getTitle() {
        return this.title;
    }

    public User getUser() {
        return this.user;
    }

    public int getPostId() {
        return this.postId;
    }

    public abstract void printPost();
}

class TextPost extends Post {
    private String content;

    public TextPost(String title, User user, String content) {
        super(title, user);
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void printPost() {
		System.out.println("Post #" + postId);
        System.out.println("Created By: " + this.user.getFullName() + " (@" + this.user.getUsername() + ")");
        System.out.println("Title: " + title);
        System.out.println(content + "\n");
    }
}

class PollPost extends Post {
    private Map<String, Integer> options;

    public PollPost(String title, User user, String optionsString) {
        super(title, user);
        this.options = new HashMap<>();
        String[] optionsArray = optionsString.split(";");
        for (String option : optionsArray) {
            String[] parts = option.trim().split(":");
            this.options.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
    }

    public Map<String, Integer> getOptions() {
        return options;
    }

    public void vote(String option) {
        if (options.containsKey(option)) {
            options.put(option, options.get(option) + 1);
        } else {
            options.put(option,  1);
        }
    }

    public void printPost() {
        System.out.println("Post #" + postId);
        System.out.println("Created By: " + user.getFullName() + " (@" + this.user.getUsername() + ")");
        System.out.println("Title: " + title);
        options.forEach((option, count) -> System.out.println(option + ": " + count));
    }
}

public class DiscussionBoard {
    private List<User> users = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();

    public void loadBoard(String filename) {
    try (Scanner scanner = new Scanner(new File(filename))) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";", 4);
            String type = parts[0];
            User user = new User(parts[2], parts[2]);

           

            if (type.equals("TEXT")) {
                posts.add(new TextPost(parts[1], user, parts[3]));
            } else if (type.equals("POLL")) {
                posts.add(new PollPost(parts[1], user, parts[3]));
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + e.getMessage());
    }
}


    public void saveBoard(String filename) {
		 File file = new File(filename);
    // Create the parent directories if they don't exist
    file.getParentFile().mkdirs();
    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
        for (Post post : posts) {
            if (post instanceof TextPost) {
                writer.println("TEXT;" + post.getTitle() + ";" + post.getUser().getUsername() + ";" + ((TextPost) post).getContent());
            } else if (post instanceof PollPost) {
                PollPost pollPost = (PollPost) post;
                StringBuilder optionsBuilder = new StringBuilder();

                // Format each option as "Option:VoteCount" and join with ";"
                for (Map.Entry<String, Integer> entry : pollPost.getOptions().entrySet()) {
                    optionsBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
                }

                // Remove the trailing ";" if options were appended
                if (optionsBuilder.length() > 0) {
                    optionsBuilder.setLength(optionsBuilder.length() - 1);
                }

                writer.println("POLL;" + pollPost.getTitle() + ";" + pollPost.getUser().getUsername() + ";" + optionsBuilder.toString());
            }
        }
    } catch (IOException e) {
        System.out.println("Error saving file: " + e.getMessage());
    }
}


    public static void main(String[] args) {
        DiscussionBoard board = new DiscussionBoard();
        Scanner inputScanner = new Scanner(System.in);
        int choice;

        if (args.length > 0) {
            String filename = "./boards/" + args[0] + ".dboard";
            board.loadBoard(filename);
        } else {
            System.out.println("Filename argument missing.");
            return;
        }

        do {
            System.out.println("\nDiscussion Board Menu:");
            System.out.println("(1) Create new user");
            System.out.println("(2) Create new post");
            System.out.println("(3) View all posts");
            System.out.println("(4) Vote in poll");
            System.out.println("(5) View all posts with a given username");
            System.out.println("(6) View all posts with a given keyword");
            System.out.println("(7) Save discussion board");
            System.out.println("(8) End Program");
            System.out.print("Enter your choice: ");

            choice = inputScanner.nextInt();
            inputScanner.nextLine();

            switch (choice) {
                case 1:
                    board.newUser(inputScanner);
                    break;
                case 2:
                    board.newPost(inputScanner);
                    break;
                case 3:
                    board.viewAllPosts();
                    break;
                case 4:
                    System.out.print("Enter option to vote for: ");
                    String option = inputScanner.nextLine();
                    board.voteInPoll(inputScanner);
                    break;
                case 5:
                    board.viewAllPostsUsername(inputScanner);
                    break;
                case 6:
                    board.viewAllPostsKeyword(inputScanner);
                    break;
                case 7:
                    String filename = "./boards/" + args[0] + ".dboard";
                    board.saveBoard(filename);
                    break;
                case 8:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
    }

    private void newUser(Scanner inputScanner) {
        System.out.print("Enter full name: ");
        String fullName = inputScanner.nextLine();
        System.out.print("Enter username: ");
        String username = inputScanner.nextLine().toLowerCase();
        if (findUser(username) != null) {
            System.out.println("Username already exists.");
        } else {
            User user = new User(fullName, username);
            users.add(user);
            System.out.println("User created successfully.");
        }
    }

private void newPost(Scanner inputScanner) {
    System.out.print("Enter username: ");
    String username = inputScanner.nextLine().toLowerCase();
    User user = findUser(username);

    if (user == null) {
        System.out.println("User not registered.");
        return;
    }

    System.out.print("Text or Poll: ");
    String textOrPoll = inputScanner.nextLine().toLowerCase();
    System.out.print("Enter post title: ");
    String title = inputScanner.nextLine().trim();

    if (textOrPoll.equals("text")) {
        System.out.print("Enter post content: ");
        String content = inputScanner.nextLine().trim();
        Post post = new TextPost(title, user, content);
        posts.add(post);

    } else if (textOrPoll.equals("poll")) {
        System.out.print("Enter poll options (format: Option1:0;Option2:0;...): ");
        String options = inputScanner.nextLine().trim();
        Post post = new PollPost(title, user, options);
        posts.add(post);

    } else {
        System.out.println("Invalid post type. Please enter either 'text' or 'poll'.");
        return;
    }

    System.out.println("Post created.");
}


    private void viewAllPosts() {
        for (Post post : posts) {
            post.printPost();
        }
    }

    private void viewAllPostsUsername(Scanner inputScanner) {
        System.out.print("Enter username: ");
        String username = inputScanner.nextLine().toLowerCase();
        for (Post post : posts) {
            if (post.getUser().getUsername().equals(username)) {
                post.printPost();
            }
        }
    }

    private void viewAllPostsKeyword(Scanner inputScanner) {
        System.out.print("Enter keyword: ");
        String keyword = inputScanner.nextLine().toLowerCase();
        for (Post post : posts) {
            if (post.getTitle().toLowerCase().contains(keyword) || 
               (post instanceof TextPost && ((TextPost) post).getContent().toLowerCase().contains(keyword))) {
                post.printPost();
            }
        }
    }

	private Post findPostById(int postId) {
        for (Post post : posts) {
            if (post.getPostId() == postId) {
                return post;
            }
        }
        return null;
    }

	public void voteInPoll(Scanner inputScanner) {
        System.out.print("Enter Poll Post ID to vote on: ");
        int postId = inputScanner.nextInt();
        inputScanner.nextLine(); // Consume newline

        // Find the PollPost with the given postId
        Post post = findPostById(postId);
        if (post instanceof PollPost) {
            PollPost pollPost = (PollPost) post;
            System.out.print("Enter option to vote for: ");
            String option = inputScanner.nextLine();
            pollPost.vote(option);
            System.out.println("Vote recorded.");
        } else {
            System.out.println("No poll found with that ID.");
        }
    }

    private User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
