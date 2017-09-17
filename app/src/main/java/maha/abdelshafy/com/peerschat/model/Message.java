package maha.abdelshafy.com.peerschat.model;

/**
 * Created by 007 on 19/08/2017.
 */
public class Message {

    private String message_body;
    private String sender_name;
    private String recipient_name;
    private int mPeerStatus;

    public Message() {
    }

    public Message(String message_body, String sender_name, String recipient_name) {
        this.message_body = message_body;
        this.sender_name = sender_name;
        this.recipient_name = recipient_name;

    }

    public int getPeerStatus() {
        return mPeerStatus;
    }

    public void setRecipientOrSenderStatus(int mPeerStatus) {
        this.mPeerStatus = mPeerStatus;
    }

    public String getMessage_body() {
        return message_body;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getRecipient_name() {
        return recipient_name;
    }
}
