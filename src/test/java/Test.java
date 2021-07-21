import org.penough.mp.generator.builder.CommonBuilder;
import org.penough.mp.generator.config.CommonConfig;

public class Test {
    private static String DATABASE = "jdbc:mysql://192.168.10.204:3306/aftersalesservice?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true";
    private static String USER_NAME = "root";
    private static String DB_PWD = "Jmgo.123";
    public static void main(String[] args) {
        CommonConfig config = new CommonConfig();
        config.getGlobalConfig()
                .setDatabaseUrl(DATABASE)
                .setPwd(DB_PWD);
        CommonBuilder.buildComponentInExistingSource(config);
    }
}
