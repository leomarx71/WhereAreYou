package com.leomarx.whereareyou.notification;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by RicardoSantos on 06/12/16.
 */

@IgnoreExtraProperties
public class Notification {

    public String title;
    public String body;
    public String origin;
    public String target;

    public Notification(){
        this.title="title Example";
        this.body="body Example";
        this.origin="requester Example";
        this.target="target Example";
    }

    public Notification(String title, String body, String origin, String target){
        this.title=title;
        this.body=body;
        this.origin=origin;
        this.target=target;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getOrigin() {
        return origin;
    }

    public String getTarget() {
        return target;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
