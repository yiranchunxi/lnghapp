package com.siasun.rtd.lngh.http.bean;

public class MessageEvent {
    private String message;
    private String eventTag;

    public MessageEvent(String eventTag,String message){
        this.eventTag=eventTag;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEventTag(String eventTag) {
        this.eventTag = eventTag;
    }

    public String getEventTag() {
        return eventTag;
    }
}
