package cn.com.connext.msf.data.mongo;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryComposition;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ConnextMongoRepositoryBuilder {

    private final static ConnextMongoOperationManager mongoOperationManager = ConnextMongoOperationManager.create();

    public static <T> T build(Class<T> repositoryInterface) {
        return build("mongodb://saas:123456@192.168.0.10:27017/test", repositoryInterface);
    }

    public static <T> T build(String uri, Class<T> repositoryInterface) {
        MongoOperations mongoOperations = mongoOperationManager.getMongoOperations(uri);
        MongoRepositoryFactory mongoRepositoryFactory = new MongoRepositoryFactory(mongoOperations);

        Class<?> entityClass = getEntityClass(repositoryInterface);
        RepositoryComposition.RepositoryFragments repositoryFragments =
                RepositoryComposition.RepositoryFragments.just(
                        new ConnextMongoRepositoryImpl<>(
                                mongoRepositoryFactory.getEntityInformation(entityClass),
                                mongoOperations
                        )
                );

        return mongoRepositoryFactory.getRepository(repositoryInterface, repositoryFragments);
    }

    private static Class<?> getEntityClass(Class<?> repositoryInterface) {
        Type[] types = repositoryInterface.getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type[] actualTypes = parameterizedType.getActualTypeArguments();
        return (Class) actualTypes[0];
    }
}
