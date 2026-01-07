package com.leomarx.whereareyou.chat;


import com.google.firebase.database.IgnoreExtraProperties;
import com.leomarx.whereareyou.utils.Date;

@IgnoreExtraProperties
public class Message {

	public String id;
	public String from;
	public String to;
	public String body;
	public String created;

	public Message() {
		// TODO - implement Message.Message
	}

	public Message(String id, String from, String to , String body){
        this.id = id;
        this.from = from;
        this.to = to;
        this.body = body;
        this.created = Date.now();
	}

}