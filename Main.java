//OpenAI (2023) ChatGPT (Aug 3 version) [Social Media Analyzer Tips], accessed 25 Aug 2023. https://chat.openai.com/c/7b8eea74-d579-4313-94d7-f99a52e31bfb
//OpenAI (2023) ChatGPT (Aug 3 version) [CheckEqual Method Implementation], accessed 25 Aug 2023. https://chat.openai.com/c/301be210-650f-4ec3-b687-7f06d245b8ee
//OpenAI (2023) ChatGPT (Aug 3 version) [HashMap Syntax Correction], accessed 25 Aug 2023.https://chat.openai.com/c/c5121bba-925f-4f96-8de2-2b922c8cc512
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	private PostMap post_analyzer = new PostMap();

	public Main() {
		this.post_analyzer = new PostMap();
	}

	public static void main(String[] args) {
		Main mainInstance = new Main();
		mainInstance.run();
	}

	public void run() {
		String file = "posts.csv";
		try (Scanner scanner = new Scanner(System.in)) {

			post_analyzer.loadData(file); // Load data from file

			boolean exitMenu = false;

			while (!exitMenu) {
				int choice = getMenuChoice(scanner); // Get user's menu choice

				if (choice == 1) {
					addAPost_via_menu(scanner); // Call method to add a new post
				} else if (choice == 2) {
					deleteAPost_via_menu(scanner);// Call method to retrieve a post
				} else if (choice == 3) {
					retrieveAPost_via_menu(scanner);// Call method to delete a post
				} else if (choice == 4) {
					retrieveNPostsMostLikes_via_menu(scanner); // Call method to retrieve top N liked posts
				} else if (choice == 5) {
					retrieveNPostsMostShares_via_menu(scanner); // Call method to retrieve top N shared posts
				} else if (choice == 6) {
					exitMenu = true; // Exit the menu loop
				}

			}

			System.out.println("Thanks for using Social Media Analyzer. Goodbye!");
		}
	}


	public int getMenuChoice(Scanner scanner) {
		String menu = "%n" + "Welcome to Social Media Analyzer!%n"
				+ "----------------------------------------------------------------------------%n"
				+ "> Select from main menu%n"
				+ "----------------------------------------------------------------------------%n"
				+ "    1) Add a social media post%n" + "    2) Delete an existing social media post%n"
				+ "    3) Retrieve a social media post%n" + "    4) Retrieve the top N posts with most likes%n"
				+ "    5) Retrieve the top N posts with most shares%n" + "    6) Exit%n";

		System.out.printf(menu);

		while (true) {
			System.out.print("Please select: ");
			
			try {
				int choice = getIntegerInput(scanner);
				if (choice < 1 || choice > 6) {
					throw new IllegalArgumentException("Invalid choice! Please enter a number between 1 and 6.");
					}
				return choice;
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
	}
			
	
	public int getIntegerInput(Scanner scanner) {
		int number = -1;
		try {
			number = Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e){
		}
		return number;
	}

	public void addAPost_via_menu(Scanner scanner) {
		System.out.println("Please provide the post ID, numerics only [e.g: 123]");
		String postID = scanner.nextLine().trim();
		
		System.out.println("Please provide the post content, without any ',' or ';'");
		String content = scanner.nextLine().trim();
		
		System.out.println("Please provide the post author, 6 characters of numerics and letters [e.g: AB1234]");
		String authorID = scanner.nextLine().trim();
		
		System.out.println("Please provide the number of likes of the post");
		int likes = getIntegerInput(scanner);
		
		System.out.println("Please provide the number of shares of the post");
		int shares = getIntegerInput(scanner);

		System.out.println("Please provide the date and time of the post in the format of DD/MM/YYYY HH:MM");
		String datetime = scanner.nextLine().trim();

		try {
			post_analyzer.addAPost(postID, content, authorID, likes, shares, datetime);
			System.out.println("The post has been added to the collection");
		} catch (IllegalArgumentException e) {
			System.out.println("Error occur while adding post. " + e.getMessage());
		}

	}

	public void deleteAPost_via_menu(Scanner scanner) {
		System.out.println("Please provide the post ID");
		String postID = scanner.nextLine().trim();

		PostData postData = post_analyzer.retrieveAPost(postID);

		if (postData != null) {
			post_analyzer.deleteAPost(postID);
			System.out.println("The post has been deleted!");
		} else {
			System.out.println("Sorry the post does not exist in the collection!");
		}
	}

	public void retrieveAPost_via_menu(Scanner scanner) {
		System.out.println("Please provide the post ID");
		String postID = scanner.nextLine().trim();

		PostData postData = post_analyzer.retrieveAPost(postID);

		if (postData != null) {
			System.out.printf("%s | %s | %d likes | %d shares%n", postID, postData.getContent(), postData.getLikes(), postData.getShares());
		} else {
			System.out.println("Sorry the post does not exist in the collection!");
		}

	}

	public void retrieveNPostsMostLikes_via_menu(Scanner scanner) {
		retrieveNPostsMostProperty_via_menu(scanner, "likes");
	}

	public void retrieveNPostsMostShares_via_menu(Scanner scanner) {
		retrieveNPostsMostProperty_via_menu(scanner, "shares");
	}

	private void retrieveNPostsMostProperty_via_menu(Scanner scanner, String property) {

		System.out.println("Please specify the number of posts to retrieve (N):");
		int quantity = getIntegerInput(scanner);

		List<Map.Entry<String, PostData>> posts = null;

		try {
			posts = post_analyzer.retrieveNPostsByProperty(quantity, property);
			if (quantity > posts.size()) {
				System.out.printf("Only %d posts exist in the collection. Showing all of them%n", posts.size());
			}
			printPosts(posts, property);
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	// Method to print posts with a specific property
	private void printPosts(List<Map.Entry<String, PostData>> posts, String property) {
		String propertyFormatted = property.equals("likes") ? "likes" : "shares";

		System.out.printf("%s %d %s %s %s%n", "The top", posts.size(), "posts with the most", propertyFormatted,
				"are:");
		for (int i = 0; i < posts.size(); i++) {
			Map.Entry<String, PostData> entry = posts.get(i);
			String postID = entry.getKey();
			PostData postData = entry.getValue();
			int value = property.equals("likes") ? postData.getLikes() : postData.getShares();
			System.out.printf("%-5s%d) %s | %s | %d %s%n", "", i + 1, postID, postData.getContent(), value,
					propertyFormatted);
		}
	}

}
