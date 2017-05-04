package com.example;

/**
 * 全局共用只读变量
 */

public class Global {
    /**
     * tab标题
     */
    public static String[] Titles = new String[]{"首页", "关系链", "树洞"};
    /**
     * 发布环境用(手机热点)
     */
    // 命名空间       
    public static final String Host = "http://192.168.43.182:8080";
    // servlet的URL  
    public static final String VisionInfo = "http://192.168.43.182:8080/api/VisionInfo";
    /**
     * 发布环境用(局域网（无线）)
     */
    public static final String W_Host = "http://192.168.191.1:8080";
    public static final String W_VisionInfo = "http://192.168.191.1:8080/api/VisionInfo";
    /**
     * 测试环境用(家里局域网)
     */
    // 命名空间       
    public static final String _Host = "http://192.168.0.102:8080";
    // servlet的URL  
    public static final String _VisionInfo = "http://192.168.0.102:8080/api/VisionInfo";
}
