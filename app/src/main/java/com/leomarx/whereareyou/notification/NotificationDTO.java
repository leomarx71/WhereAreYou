package com.leomarx.whereareyou.notification;

/**
 * Created by RicardoSantos on 06/12/16.
 */

public class NotificationDTO {

    private String title;
    private String body;
    private String imageURL;

    public NotificationDTO(){

    }

    public NotificationDTO(String title, String body, String imageURL){
        this.title=title;
        this.body=body;
        this.imageURL=imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
