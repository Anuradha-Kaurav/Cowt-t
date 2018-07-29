package com.quote.cowtit.POJO;

import java.io.Serializable;

/**
 * Created by Android on 5/10/2018.
 */

public class MessagePOJO implements Serializable {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String status;
    String message;
    String userId;

}
