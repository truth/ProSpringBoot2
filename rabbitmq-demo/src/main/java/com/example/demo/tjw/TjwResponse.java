package com.example.demo.tjw;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
/*
  "code": "0",
  "message": "OK",
  "responseTime": 1625390547991,
  "responseBody": {
 */
@Data
public class TjwResponse {
    private String code;
    private String message;
    private Date responseTime;
    private Object responseBody;
    public TjwResponse(){
        Date date = Calendar.getInstance().getTime();
        this.code = UUID.randomUUID().toString();
        this.responseTime = date;
    }
    public TjwResponse(String message){
        this();
        this.message = message;
    }
}