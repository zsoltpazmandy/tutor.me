package zsoltpazmandy.tutorme;


/**
 *
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * Chat message POJO which gets registered in Firebase as soon as the user presses the send message
 * button in the Chat activity.
 */
class ChatMessage {

    private String sender;
    private String recipient;
    private String timeStamp;
    private String message;

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
