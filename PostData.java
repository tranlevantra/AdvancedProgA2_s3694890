import java.time.LocalDateTime;
import java.time.format.*;

public class PostData {

    private String content;
    private String authorID;
    private int likes;
    private int shares;
    private LocalDateTime datetime;

    public static final DateTimeFormatter postDatetimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final String authorIDFormatter = "[A-Z0-9]{6}";

    
    public PostData(String content, String authorID, int likes, int shares, String formattedDatetime) {
        setContent(content);
        setAuthorID(authorID);
        setLikes(likes);
        setShares(shares);
        setDatetime(formattedDatetime);
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        if (!content.contains(",") && !content.contains(";")) {
            this.content = content;
        } else {
            throw new IllegalArgumentException("Invalid input! Content should not contain ',' or ';'");
        }
    }

    public String getAuthorID() {
        return this.authorID;
    }

    public void setAuthorID(String authorID) {
        if (authorID.toUpperCase().matches(authorIDFormatter)) {
            this.authorID = authorID.toUpperCase();
        } else {
            throw new IllegalArgumentException("Invalid Author ID format. Use 6 uppercase letters or digits");
        }
    }

    public int getLikes() {
        return this.likes;
    }

    public void setLikes(int likes) {
        checkPositiveInt(likes, "Likes must be a positive integer");  // Change the variable name
        this.likes = likes;
    }

    public int getShares() {
        return this.shares;
    }

    public void setShares(int shares) {
        checkPositiveInt(shares, "Shares must be a positive integer");
        this.shares = shares;
    }

    private void checkPositiveInt(int value, String errorMessage) throws IllegalArgumentException {
        if (value < 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }


    public LocalDateTime getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetimeInput) {
        try {
            LocalDateTime parsedDatetime = LocalDateTime.parse(datetimeInput, postDatetimeFormatter);
            this.datetime = parsedDatetime;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid datetime format. Use 'dd/MM/yyyy HH:mm'");
        }
    }

}
