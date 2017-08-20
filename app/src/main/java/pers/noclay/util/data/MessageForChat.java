package pers.noclay.util.data;

/**
 * Created by 82661 on 2016/9/24.
 */
public class MessageForChat {
    private boolean isAuthor = false;
    private String content;

    public MessageForChat(boolean isAuthor, String content) {
        this.isAuthor = isAuthor;
        this.content = content;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
