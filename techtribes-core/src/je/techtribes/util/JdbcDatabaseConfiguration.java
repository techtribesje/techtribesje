package je.techtribes.util;

import javax.sql.DataSource;

public class JdbcDatabaseConfiguration {

    private DataSource dataSource;

    public JdbcDatabaseConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
