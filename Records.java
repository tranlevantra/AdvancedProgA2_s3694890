import java.util.*;

import java.io.*;

public class Records {

    private HashMap<String, Post> postMap = new HashMap<>();
    private HashMap<String, User> userMap = new HashMap<>();

    
    public int getPostMapSize() {
    	return this.postMap.size();
    }

    private void checkExistingPost(String postID) throws IllegalArgumentException {
        Post existingPost = retrieveAPost(postID);
        if (existingPost != null) {
            throw new IllegalArgumentException("Post with ID " + postID + " already exists");
        }
    }

    public void validatePostID(String postID) throws IllegalArgumentException {
        if (!postID.matches(postIDFormatter)) {
            throw new IllegalArgumentException("Invalid Post ID format. Numerical input only");
        }
    }
    
    

    // Method to add a new post to the collection
    public void addAPost(String postID, String content, String authorID, int likes, int shares, String formattedDatetime) {

        validatePostID(postID);
        checkExistingPost(postID);

        Post post = new Post(postID, content, authorID, likes, shares, formattedDatetime);
        this.postMap.put(postID, post);
    }

    // Method to delete a post from the collection
    public void deleteAPost(String postID) {
        this.postMap.remove(postID);
    }

    // Method to retrieve a post based on its ID
    public Post retrieveAPost(String postID) {
        return this.postMap.get(postID);
    }
    

    public List<Map.Entry<String, Post>> arrangePostsByLikes() {
        Comparator<Map.Entry<String, Post>> propertyComparator = Comparator.comparingInt((Map.Entry<String, Post> entry) -> entry.getValue().getLikes()).reversed();;
        List<Map.Entry<String, Post>> sortedPosts = new ArrayList<>(this.postMap.entrySet());
          
        sortedPosts.sort(propertyComparator);

        return sortedPosts;
    }

    // Method to retrieve the top N posts with a specific property
    public List<Map.Entry<String, Post>> retrieveNPostsByProperty(int quantity, String property) {
        // Arrange posts by the specified property
        List<Map.Entry<String, Post>> sortedPostsByProperty = arrangePostsByProperty(property);

        // Validate input quantity
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid input! Quantity must be greater than 0!");
        }

        // Calculate actual quantity to avoid exceeding available posts
        int actualQuantity = Math.min(quantity, sortedPostsByProperty.size());

        // Return the sublist of top N posts
        return sortedPostsByProperty.subList(0, actualQuantity);
    }




    // Method to load data from a file and populate the collection
    public void loadData(String filename) {
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
                String postID = fields[0];
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


}


