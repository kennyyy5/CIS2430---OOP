package discussionboard;
import java.util.*;

class User {
	private String fullName;
	private String username;

	public User (String fullName, String username) throws IllegalArgumentException{
		if(fullName.trim().isEmpty() || fullName == null){
			throw new IllegalArgumentException("fullname can't be empty!");
		}else{
		this.fullName = fullName;}
		if(username.trim().isEmpty() || username == null){
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
	private String title;
	private String content;
	private User user;

	public Post (String title, String content, User user) throws IllegalArgumentException{
		if(title.trim().isEmpty() || title == null){
			throw new IllegalArgumentException("title can't be empty!");
		}
		if(content.trim().isEmpty() || content == null){
			throw new IllegalArgumentException("content can't be empty!");
		}
		
		this.title = title;
		this.content = content;
		this.user = user;
	}
	public String getTitle(){
		return this.title;
	}
	public String getContent(){
		return this.content;
	}
	public User getUser() {
		return this.user;
	}
	public void printPost(){
		System.out.println("Created By: "+ this.user.getFullName() + " (@" + this.user.getUsername() + ")");
		System.out.println("Title: "+ title);
		System.out.println(content + "\n");
	}
}

public class DiscussionBoard {
	private static ArrayList<User> users = new ArrayList<>();
	private static ArrayList<Post> posts = new ArrayList<>();
	private static HashMap<String, ArrayList<Integer>> userPostsMap = new HashMap<>();

	public static void main(String[] args) {
		Scanner inputScanner = new Scanner(System.in);
		int choice;
		do {
            System.out.println("\nDiscussion Board Menu:");
            System.out.println("(1) Create new user");
            System.out.println("(2) Create new post");
            System.out.println("(3) View all posts");
            System.out.println("(4) View all posts with a given username");
            System.out.println("(5) View all posts with a given keyword");
            System.out.println("(6) End Program");
            System.out.print("Enter your choice: ");

            choice = getIntInput(inputScanner);

            switch (choice) {
                case 1:
                    newUser(inputScanner);
                    break;
                case 2:
					newPost(inputScanner);
                    
                    break;
                case 3:
                    viewAllPosts();
                    break;
                case 4:
                    viewAllPostsUsername(inputScanner);
                    break;
                case 5:
                    viewAllPostsKeyword(inputScanner);
                    break;
                case 6:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
		}

	private static int getIntInput(Scanner inputScanner) {
		int choice;
		while (true) {
			try {
				if (!inputScanner.hasNextInt()) {
					System.out.print("Invalid input. Please enter an integer: ");
					inputScanner.next(); // Clear invalid input
				} else {
					choice = inputScanner.nextInt();
					inputScanner.nextLine(); // Consume newline after integer input
					break;
				}
			} catch (NoSuchElementException | IllegalStateException e) {
				System.out.print("Error reading input. Please enter a valid integer: ");
				inputScanner.next(); // Clear invalid input
			}
		}
		return choice;
    }

		private static void newUser(Scanner inputScanner){
		 try{
			System.out.println("Enter full name: ");
			String fullName = inputScanner.nextLine();
			System.out.println("Enter username: ");
			String username = inputScanner.nextLine().toLowerCase();
			if(findUser(username) != null){
				System.out.println("Username already exists.");
			} else{
				User user = new User(fullName, username);
				users.add(user);
				System.out.println("User created successfully.");
			}
		 }
		 catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
         }
		}

		private static void newPost(Scanner inputScanner){
			try{
		  System.out.println("Enter username: ");
		  String username = inputScanner.nextLine().toLowerCase();
		  User user = findUser(username);
		  if(user == null){
			System.out.println("User not registered.");
			return;
		  } 
		  System.out.println("Enter post title: ");
		  String title = inputScanner.nextLine().trim();
		  System.out.println("Enter post content: ");
		  String content = inputScanner.nextLine().trim();

		  Post post  = new Post(title, content, user);
		  posts.add(post);
		  userPostsMap.computeIfAbsent(username, k -> new ArrayList<>()).add(posts.size() - 1);
		  System.out.println("Post created.");}
		  catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
         }
		}

		private static void viewAllPosts(){
		  for (int i = 0; i < posts.size(); i++) {
            posts.get(i).printPost();
          }
		}

		private static void viewAllPostsUsername(Scanner inputScanner){
		  System.out.print("Enter username: ");
		  String username = inputScanner.nextLine().toLowerCase();
		  ArrayList<Integer> userPostsIndices = userPostsMap.get(username);

		  if(userPostsIndices.isEmpty() || userPostsIndices == null){
			System.out.println("No posts made by user!");
		  }else{
		    for (int i = 0; i < userPostsIndices.size(); i++) {
            	posts.get(i).printPost(); 
            }
		  }
		}

		private static void viewAllPostsKeyword(Scanner inputScanner){
		  System.out.print("Enter keyword: ");
		  String keyword = inputScanner.nextLine().toLowerCase();
		  String delimiters = " "; //blank space
		  
		  for (int i = 0; i < posts.size(); i++) {
			Post currentPost = posts.get(i);
			String searchContent = currentPost.getTitle() + " " + currentPost.getContent();
			StringTokenizer stringTokeniser = new StringTokenizer(searchContent, delimiters);
			while(stringTokeniser.hasMoreTokens()){
				if(stringTokeniser.nextToken().equals(keyword)){
					posts.get(i).printPost() ;
					break;
				}
			}
          }

		}

		private static User findUser(String username){
			for(int i = 0; i < users.size(); i++){
				if(users.get(i).getUsername().equals(username)){
					return users.get(i);
				}
			}
			return null;
		}

		
}
