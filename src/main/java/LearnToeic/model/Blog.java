package LearnToeic.model;
import jakarta.persistence.*;

@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    private int blogId;

    private int userId;

    private String title;
    
    @Lob
    private String content;

    private int upVote;

    public Blog() {
    }

    public Blog(int blogId, int userId, String title, String content, int upVote) {
        this.blogId = blogId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.upVote = upVote;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUpVote() {
        return upVote;
    }

    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }

}
