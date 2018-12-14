package com.jc.idea.config;

public class ProjectBean {

    /**
     * 类的注释里的作者
     */
    private String author;


    /**
     * 类的注释里的日期
     */
    private String date;


    /**
     * 类的groupId
     */
    private String groupId;



    /**
     * 文件根路径(绝对路径)
     */
    private String projectDir;

    /**
     * 文件名前缀（为了兼容老项目，新生成的类名（文件名）可能需要不同的前缀）
     */
    private String fileNamePrefix;

}
