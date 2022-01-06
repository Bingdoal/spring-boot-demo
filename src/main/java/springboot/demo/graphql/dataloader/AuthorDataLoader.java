package springboot.demo.graphql.dataloader;

import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springboot.demo.model.dao.UserDao;
import springboot.demo.model.entity.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class AuthorDataLoader {
    public static final String KEY = "AUTHOR_DATA_LOADER";

    private final Executor executor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Autowired
    private UserDao userDao;

    public DataLoader<Long, User> getLoader() {
        return DataLoaderFactory.newDataLoader((List<Long> userIds) ->
                CompletableFuture.supplyAsync(() -> userDao.findAllById(userIds), executor));
    }
}
