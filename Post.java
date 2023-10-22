import java.time.LocalDateTime;
import java.time.format.*;

public class Post {
	
	private int postID;
    private String content;
    private String author;
    private int likes;
    private int shares;
    private LocalDateTime datetime;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    
    public Post(int postID, String content, String username, int likes, int shares, String formattedDatetime) throws CreatingPostException {
        setPostID(postID);
    	setContent(content);
        setAuthor(username);
        setLikes(likes);
        setShares(shares);
        setDatetime(formattedDatetime);
    }
    
	public Integer getPostID() {
		return this.postID;
	}

    public void setPostID(int postID){
        this.postID = postID;
    }
      

    public String getContent() {
        return this.content;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String username) {
        this.author = username;
 
    }

    public Integer getLikes() {
        return this.likes;
    }


    public Integer getShares() {
        return this.shares;
    }

    public String getDatetime() {
        return datetime.format(formatter);
    }


    
    public void setContent(String content) throws CreatingPostException {
        if (!content.contains(",") && !content.contains(";")) {
            this.content = content;
        } else {
            throw new CreatingPostException("Invalid input! Content should not contain ',' or ';'");
        }
    }

    public void setLikes(int likes) throws CreatingPostException {
        checkPositiveInt(likes, "Likes must be a positive integer");
        this.likes = likes;
    }

    public void setShares(int shares) throws CreatingPostException {
        checkPositiveInt(shares, "Shares must be a positive integer");
        this.shares = shares;
    }
    
    public void setDatetime(String datetimeInput) throws CreatingPostException{
        try {
            LocalDateTime parsedDatetime = LocalDateTime.parse(datetimeInput, formatter);
            this.datetime = parsedDatetime;
        } catch (DateTimeParseException e) {
        	throw new CreatingPostException("Invalid datetime format");
        }
    }
    
    private void checkPositiveInt(int value, String errorMessage) throws CreatingPostException {
        if (value < 0) {
            throw new CreatingPostException(errorMessage);
        }
    }


}

