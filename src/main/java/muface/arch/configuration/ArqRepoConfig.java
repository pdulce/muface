package muface.arch.configuration;

import muface.arch.repository.ArqMongoAdapterRepository;
import muface.arch.repository.ArqPortRepository;
import muface.arch.repository.ArqRelationalAdapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ArqRepoConfig {
    @Autowired
    private ApplicationContext applicationContext;
    @Bean
    @Profile("mongo")
    public Map<String, ArqPortRepository<?, String>> mongoDiplomaDTORepository(MongoOperations mongoOperations) {
        Map<String, ArqPortRepository<?, String>> repositoryMap = new HashMap<>();
        Map<String, MongoRepository> mongoRepositories = applicationContext.getBeansOfType(MongoRepository.class);

        mongoRepositories.keySet().forEach((repositorykey) -> {
            ArqMongoAdapterRepository<?, String> repository = new ArqMongoAdapterRepository<>();
            MongoRepository<?, String> mongoRepository = mongoRepositories.get(repositorykey);
            repository.setMongoRepository(mongoRepository);
            repository.setMongoOperations(mongoOperations);
            repositoryMap.put(repositorykey, repository);
        });
        return repositoryMap;
    }

    @Bean
    @Profile("jpa")
    public Map<String, ArqPortRepository<?, Long>> jpaCommonRepositories() {
        Map<String, ArqPortRepository<?, Long>> repositoryMap = new HashMap<>();
        Map<String, JpaRepository>  jpaRepositories = applicationContext.getBeansOfType(JpaRepository.class);

        jpaRepositories.keySet().forEach((repositorykey) -> {
            ArqRelationalAdapterRepository<?, Long> repository = new ArqRelationalAdapterRepository();
            JpaRepository<?, Long> jpaRepository = jpaRepositories.get(repositorykey);
            repository.setJpaRepository(jpaRepository);
            repositoryMap.put(repositorykey, repository);
        });
        return repositoryMap;
    }

}
