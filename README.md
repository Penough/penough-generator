# penough-generator

用于和penough-boot配套的生成器
本项目基于zuihou-generator项目进行改造和删减(算不算受启发&#x1F923;)
处于个人项目不断迭代中

该项目基于mybatis-plus + FreeMarker进行开发
可以通过如下方式进行代码生成

```
public class Test {
    private static String DATABASE = "jdbc:mysql://***.***.***.***:3306/pblog?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true";
    private static String USER_NAME = "root";
    private static String DB_PWD = "*****";
    private static String SERVICE_NAME = "user";
    private static String AUTHOR = "penough";
    private static String TABLE_REPFIX = "pblog";
    private static String PARENT_PKG = "org.penough.pblog";
    private static String MODULE_NAME = "user";
    public static void main(String[] args) {
        CommonConfig config = new CommonConfig();
        config.getGlobalConfig()
                .setDatabaseUrl(DATABASE)
                .setPwd(DB_PWD)
                .setServiceName(SERVICE_NAME)
                .setAuthor(AUTHOR);
        config.getPackageConfig()
                .setParentPackage(PARENT_PKG)
                .setModule(MODULE_NAME);
        config.getStratrgyConfig().setTablePrefix(TABLE_REPFIX);
        CommonBuilder.buildFiles(config);
    }
}
```

本项目旨在通过简单易懂的配置，即可生成满足基本CRUD的MVC操作类
生成后的文件树结构如下

```
├─penough-user
│  └─src
│      └─main
│          └─java
│              └─org
│                  └─penough
│                      └─pblog
│                          └─user
│                              ├─controller
│                              ├─mapper
│                              ├─model
│                              │  ├─dto
│                              │  │  └─user
│                              │  └─enumeration
│                              └─service
│                                  └─impl
```

penough-user: penough-默认前缀，user-ServiceName  
org.penough.pblog: 指定的项目父目录  
contorller：提供生成指定服务的类  
mapper：提供生成dao层mapper，并生成mapper.xml  
model：包含数据DTO模型、实体模型、枚举类模型  
service：服务接口类  
impl：接口实现类

本项目使用mp-generator：3.5.0版本，关于InjectionConfig有做相关的适配，对基本设置采用builder和链式set方式进行设置；同时填充了大量默认设置，使用户只需进行少量且易读的配置即可完成相关类的生成。  
项目对枚举类型进行了读取处理，通过读取数据库注释中的*#{[,];[,]}*形式的注解来动态生成枚举类，同时对枚举类中的code(description)，name(code,description,...)进行了处理。  
项目保留了hibernate优秀的校验注解，并根据数据库字段类型/长度/是否为空等属性进行动态添加。保留了easy-poi注解，以帮助完成导入导出等工作。  
同时会继续在penough-boot脚手架的基础上持续进行额外的特性添加。

>对框架的使用，功能的新增改造，本身是一个优秀的学习过程。日积跬步