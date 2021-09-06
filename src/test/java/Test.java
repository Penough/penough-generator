import org.penough.mp.generator.builder.CommonBuilder;
import org.penough.mp.generator.config.CommonConfig;

public class Test {
    private static String DATABASE = "jdbc:mysql://192.168.249.131:3306/pblog?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true";
//    private static String DATABASE = "jdbc:mysql://127.0.0.1:3308/pblog?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true";
    private static String USER_NAME = "root";
    private static String DB_PWD = "My.Gatsby.32";
//    private static String DB_PWD = "root";
    private static String SERVICE_NAME = "user";
    private static String AUTHOR = "penough";
    private static String TABLE_REPFIX = "Pblog";
    public static void main(String[] args) {
        CommonConfig config = new CommonConfig();
        config.getGlobalConfig()
                .setDatabaseUrl(DATABASE)
                .setPwd(DB_PWD)
                .setServiceName(SERVICE_NAME)
                .setAuthor(AUTHOR)
                .setTablePrefix(TABLE_REPFIX);
        CommonBuilder.buildFiles(config);
    }
}
