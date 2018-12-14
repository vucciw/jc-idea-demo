package com.jc.idea.config;

public class TableBean {


    /**
     * 表名
     */
    private String tableName;

    /**
     * 表描述
     */
    private String description;

    /**
     * 表的主键名
     */
    private String primaryKey;


    /**
     * 类的ArtifactId (模块名)
     */
    private String artifactId;


    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
