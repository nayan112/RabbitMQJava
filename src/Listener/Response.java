package Listener;

import java.time.LocalDateTime;

public class Response {
    protected Request request;
    protected LocalDateTime repliedAt;

    public Response(Request _request, LocalDateTime _repliedAt) {
        request = _request;
        repliedAt = _repliedAt;
    }
    public Response(LocalDateTime _repliedAt) {
        repliedAt = _repliedAt;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public LocalDateTime getRepliedAt() {
        return repliedAt;
    }

    public void setRepliedAt(LocalDateTime repliedAt) {
        this.repliedAt = repliedAt;
    }
}