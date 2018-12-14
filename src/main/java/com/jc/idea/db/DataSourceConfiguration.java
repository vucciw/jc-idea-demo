package com.jc.idea.db;

public class DataSourceConfiguration {

    private static final String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";

    private String url;
    private String username;
    private String password;
    private String driver = DEFAULT_DRIVER;


    public DataSourceConfiguration(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public DataSourceConfiguration(String url, String username, String password, String driver) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
