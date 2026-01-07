package com.leomarx.whereareyou.request;

import com.google.firebase.database.IgnoreExtraProperties;
import com.leomarx.whereareyou.account.AccountManager;

import java.util.Date;

/**
 * Created by RicardoSantos on 01/01/17.
 */

@IgnoreExtraProperties
public class Request {

    public String id;
    public String from;
    public String to;
    public Date created;
    private RequestState state;

    public Request() {
        // TODO - implement Request.Request
        //throw new UnsupportedOperationException();
    }

    public Request(String to) {
        this.from = new AccountManager().getUserId();
        this.to = to;
        this.to = to;
        this.state = RequestState.CREATED;
        this.created = new Date();
    }

}