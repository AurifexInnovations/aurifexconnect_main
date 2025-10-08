package com.erp.Exception.Task;

import lombok.Getter;

@Getter
public class TaskNoFoundException  extends  RuntimeException{

    public  TaskNoFoundException(String message){
        super(message);    }

}
