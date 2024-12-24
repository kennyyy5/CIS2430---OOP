package discussionboard;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.StringTokenizer;

class User {
	private String fullName;
	private String username;

	public User (String fullName, String username){
		this.fullName = fullName;
		if(username.trim().isEmpty()){
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

	public Post (String title, String content, User user){
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

            choice = inputScanner.nextInt();
            inputScanner.nextLine(); // get rid of the newline character

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

		private static void newUser(Scanner inputScanner){
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

		private static void newPost(Scanner inputScanner){
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
		  System.out.println("Post created.");
		}

		private static void viewAllPosts(){
		  for (int i = 0; i < posts.size(); i++) {
            posts.get(i).printPost();
          }
		}

		private static void viewAllPostsUsername(Scanner inputScanner){
		  System.out.print("Enter username: ");
		  String username = inputScanner.nextLine().toLowerCase();
		  for (int i = 0; i < posts.size(); i++) {
			if(posts.get(i).getUser().getUsername().equals(username)){
            	posts.get(i).printPost(); }
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
