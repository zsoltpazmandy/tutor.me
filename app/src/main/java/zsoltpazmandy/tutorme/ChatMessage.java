package zsoltpazmandy.tutorme;

/**
 * Created by zsolt on 28/07/16.
 */
public class ChatMessage {

    String sender;
    String recipient;
    String timeStamp;
    String message;

    public ChatMessage(){

    }

    public ChatMessage(String sender, String recipient, String timeStamp, String message){
        this.sender = sender;
        this.recipient = recipient;
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
