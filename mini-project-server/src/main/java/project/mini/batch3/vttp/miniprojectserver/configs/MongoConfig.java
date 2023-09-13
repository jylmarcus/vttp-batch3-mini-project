package project.mini.batch3.vttp.miniprojectserver.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages="project.mini.batch3.vttp.miniprojectserver.repositories")
public class MongoConfig extends AbstractMongoClientConfiguration{

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    protected String getDatabaseName(){
        return "miniprojDB";
    }

    @Override
    public MongoClient mongoClient() {
        final ConnectionString connString = new ConnectionString(connectionString);
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connString).build();
        return MongoClients.create(mongoClientSettings);
    }
    
}
