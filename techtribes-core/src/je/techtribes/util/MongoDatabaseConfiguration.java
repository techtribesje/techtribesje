package je.techtribes.util;

import com.mongodb.Mongo;

public class MongoDatabaseConfiguration {

    private Mongo mongo;
    private String database;

    public MongoDatabaseConfiguration(Mongo mongo, String database) {
        this.mongo = mongo;
        this.database = database;
    }

    public Mongo getMongo() {
        return mongo;
    }

    public String getDatabase() {
        return database;
    }

}
