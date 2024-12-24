package discussionboard;
import java.util.Scanner;

public class DiscussionBoard {
	private static final int MAX_POSTS = 10;
	private static String[] posts = new String[MAX_POSTS];
	private static int postsCount = 0;
	public static void main(String[] args) {
		Scanner inputScanner = new Scanner(System.in);
		int choice;
		do {
            System.out.println("\nDiscussion Board Menu:");
            System.out.println("(1) Post new message");
            System.out.println("(2) Print all posts");
            System.out.println("(3) Print all posts in reverse order");
            System.out.println("(4) Print number of posts entered so far");
            System.out.println("(5) Print all posts from a user");
            System.out.println("(6) Print the number of vowels across all posts");
            System.out.println("(7) Perform a search of posts containing a given word (case sensitive)");
            System.out.println("(8) Perform a search of posts containing a given word (case insensitive)");
            System.out.println("(9) End Program");
            System.out.print("Enter your choice: ");

            choice = inputScanner.nextInt();
            inputScanner.nextLine(); // get rid of the newline character

            switch (choice) {
                case 1:
                    newMessage(inputScanner);
                    break;
                case 2:
                    printAllPosts();
                    break;
                case 3:
                    printAllPostsInReverse();
                    break;
                case 4:
                    postsCount();
                    break;
                case 5:
                    postsFromUser(inputScanner);
                    break;
                case 6:
                    allPostsVowelCount();
                    break;
                case 7:
                    searchPosts(inputScanner, true);
                    break;
                case 8:
                    searchPosts(inputScanner, false);
                    break;
                case 9:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);
		}

		private static void newMessage(Scanner inputScanner){
		  if(postsCount ==MAX_POSTS){System.out.println("Discussion board is full, sorry!");}
		  else{
		     System.out.print("Enter your name: ");
		     String name = inputScanner.nextLine();

		     System.out.print("Enter your message: ");
                     String message = inputScanner.nextLine();

		     posts[postsCount++]=name + " says: " + message;
		     System.out.println("Message posted successfully.");

		  }
		}

		private static void printAllPosts(){
              for (int i = 0; i < postsCount; i++) {
            		System.out.println(posts[i]);
        	  }
        }

		private static void printAllPostsInReverse(){
                  for (int i = postsCount-1; i >=0; i--) {
                        System.out.println(posts[i]);
                  }
                }

		 private static void postsCount(){
                  
                     System.out.println("Number of posts: " + postsCount);
                  
                }

		 private static void postsFromUser(Scanner inputScanner) {
		        System.out.print("Enter the user's name: ");
        		String name = inputScanner.nextLine();
			String nameToLowerCase = name.toLowerCase();
        		for (int i = 0; i < postsCount; i++) {
				String postsToLowerCase = posts[i].toLowerCase();
            			if (postsToLowerCase.startsWith(nameToLowerCase)) {
                			System.out.println(posts[i]);
            			}
        		}
    			}

		  private static void allPostsVowelCount() {
     		   	int vowelCount = 0;
        		for (int i = 0; i < postsCount; i++) {
            			String postToLowerCase = posts[i].toLowerCase();
            			for (int j = 0; j < postToLowerCase.length(); j++) {
                			char c = postToLowerCase.charAt(j);
                			if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                    			vowelCount++;
                			}
            			}
        		}
        		System.out.println("Total number of vowels across all posts: " + vowelCount);
    	          }

    		

    		private static void searchPosts(Scanner inputScanner, boolean caseSensitive) {
        		System.out.print("Enter the word to search for: ");
        		String word = inputScanner.nextLine();
        		if (!caseSensitive) {
        		    word = word.toLowerCase();
        		}
        		for (int i = 0; i < postsCount; i++) {
            		String post = caseSensitive ? posts[i] : posts[i].toLowerCase();
            		if (post.contains(word)) {
                		System.out.println(posts[i]);
            		}
        		}
    		}
}
