package com.jc.idea.db;

public class DataSourceException extends  RuntimeException{


    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(Exception e) {
        super(e.getMessage());
        this.setStackTrace(e.getStackTrace());
    }
}
