package com.jc.idea.config;

import org.apache.commons.lang3.StringUtils;

public class DatabaseBean {

    private String databaseName;
    private String hostname;
    private Integer port;
    private String username;
    private String password;
    private String driver;
    private String schema;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public boolean isInvalid() {
        return StringUtils.isBlank(databaseName)||StringUtils.isBlank(hostname)||StringUtils.isBlank(username)||StringUtils.isBlank(password)||StringUtils.isBlank(schema);
    }

    public String getNullField() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        String delim = "";
        if(StringUtils.isBlank(databaseName)){
            builder.append(delim).append("databaseName");
            delim = ", ";
        }
        if(StringUtils.isBlank(hostname)){
            builder.append(delim).append("Hostname");
            delim = ", ";
        }
        if(StringUtils.isBlank(username)){
            builder.append(delim).append("Username");
            delim = ", ";
        }
        if(StringUtils.isBlank(schema)){
            builder.append(delim).append("schema");
            delim = ", ";
        }
        if(StringUtils.isBlank(password)){
            builder.append(delim).append("Password");
            delim = ", ";
        }
        return builder.append("]").toString();
    }

    public String getUrl() {
        String url = "jdbc:mysql://"+this.getHostname()+":"+this.getPort();
        String schema = this.getSchema();
        if(StringUtils.isNoneBlank(schema)){
            url= url + "/"+schema;
        }
        return url;
    }
}
