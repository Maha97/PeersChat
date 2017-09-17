package maha.abdelshafy.com.peerschat.model;

import com.google.firebase.database.Exclude;

/**
 * Created by 007 on 19/08/2017.
 */
public class User {
    private int id;
    private String name;
    private String email;
    private String connection;
    private int avatar_id;
    private long createdAt;
    private String mRecipientId;

    public User() {
    }


    public User(String name, String email, String connection, int avatarId, long createdAt) {
        this.name = name;
        this.email = email;
        this.connection = connection;
        this.avatar_id = avatarId;
        this.createdAt = createdAt;
    }


    public String createUniqueChatRef(long createdAtCurrentUser, String currentUserEmail) {
        String uniqueChatRef = "";
        if (createdAtCurrentUser > getCreatedAt()) {
            uniqueChatRef = cleanEmailAddress(currentUserEmail) + "-" + cleanEmailAddress(getEmail());
        } else {

            uniqueChatRef = cleanEmailAddress(getEmail()) + "-" + cleanEmailAddress(currentUserEmail);
        }
        return uniqueChatRef;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    private String cleanEmailAddress(String email) {
        //replace dot with comma since firebase does not allow dot
        return email.replace(".", "-");
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getConnection() {
        return connection;
    }

    public int getAvatar_id() {
        return avatar_id;
    }

    public int getId() {
        return id;
    }

    @Exclude
    public String getRecipientId() {
        return mRecipientId;
    }

    public void setRecipientId(String recipientId) {
        this.mRecipientId = recipientId;
    }
}


