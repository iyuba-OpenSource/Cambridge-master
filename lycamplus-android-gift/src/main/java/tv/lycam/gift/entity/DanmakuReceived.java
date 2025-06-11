package tv.lycam.gift.entity;

/**
 * Created by su on 16/5/27.
 */
public class DanmakuReceived {
    private String avatarUrl;
    private String username;
    private String content;

    public DanmakuReceived(String avatarUrl, String username, String content) {
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.content = content;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }
}
