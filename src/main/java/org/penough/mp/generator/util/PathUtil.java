package org.penough.mp.generator.util;

import org.penough.mp.generator.config.PeGlobalConfig;

import java.io.File;

public class PathUtil {
    public static final String SRC_MAIN_JAVA = "src" + File.separator + "main" + File.separator + "java";
    /**
     * 获取项目根路径，直到main/java
     */
    public static String getRootBasePath(PeGlobalConfig globalConfig){
        var rootPath = globalConfig.getProjectRootPath();
        if(!rootPath.endsWith(File.separator)) rootPath += File.separator;
        var sb = new StringBuilder(rootPath);
        sb.append(globalConfig.getServicePrefix()
                + globalConfig.getServiceName()
                + File.separator
                + SRC_MAIN_JAVA
                + File.separator);
        return sb.toString();
    }
}
