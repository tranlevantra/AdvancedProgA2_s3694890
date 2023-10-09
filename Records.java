import java.util.*;

import java.io.*;

public class Records {

    private HashMap<Integer, Post> postMap = new HashMap<>();
    private HashMap<String, User> userMap = new HashMap<>();

    
    public int getPostMapSize() {
    	return this.postMap.size();
    }

    private void checkExistingPost(int postID) throws IllegalArgumentException {
        Post existingPost = retrieveAPost(postID);
        if (existingPost != null) {
            throw new IllegalArgumentException("Post with ID " + postID + " already exists");
        }
    }
 

    // Method to add a new post to the collection
    public void addAPost(int postID, String content, String authorID, int likes, int shares, String formattedDatetime) throws CreatingPostException {

        checkExistingPost(postID);

        Post post = new Post(postID, content, authorID, likes, shares, formattedDatetime);
        this.postMap.put(postID, post);
    }

    // Method to delete a post from the collection
    public void deleteAPost(Integer postID) {
        this.postMap.remove(postID);
    }

    // Method to retrieve a post based on its ID
    public Post retrieveAPost(int postID) {
        return this.postMap.get(postID);
    }
    

    public List<Map.Entry<Integer, Post>> arrangePostsByLikes() {
        Comparator<Map.Entry<Integer, Post>> propertyComparator = Comparator.comparingInt((Map.Entry<Integer, Post> entry) -> entry.getValue().getLikes()).reversed();;
        List<Map.Entry<Integer, Post>> sortedPosts = new ArrayList<>(this.postMap.entrySet());
          
        sortedPosts.sort(propertyComparator);

        return sortedPosts;
    }




    // Method to load data from a file and populate the collection
    public void loadData(String filename) throws CreatingPostException {
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split("[;,]");

                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }
                if (fields.length != 6) {
                    System.out.println("Skipping line: " + line + " - Expected 6 elements, found " + fields.length);
                    continue;
                }
                int postID = Integer.parseInt(fields[0]);
                String content = fields[1];
                String authorID = fields[2];
                int likes = Integer.parseInt(fields[3]); 
                int shares = Integer.parseInt(fields[4]); 
                String formattedDatetime = fields[5];

                try {// Create and add a new post to the collection
                    addAPost(postID, content, authorID, likes, shares, formattedDatetime);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error adding post on line: " + line);
                    System.out.println("Error message: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

	public void setUserMap(HashMap<String, User> userMap) {
		this.userMap = userMap;
	}


}


