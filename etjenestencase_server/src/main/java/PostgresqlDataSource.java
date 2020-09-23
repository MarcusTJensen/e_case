import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PostgresqlDataSource {
    public DataSource getDataSource(){
        PGSimpleDataSource ds = new PGSimpleDataSource();
        Properties props = new Properties();
        String fName = "postgresql.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fName)){
            props.load(inputStream);
            ds.setUrl(props.getProperty("url"));
            ds.setUser(props.getProperty("user"));
            ds.setPassword(props.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
