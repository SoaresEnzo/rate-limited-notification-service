package dev.soaresenzo.modak.notificationService;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

public class RedisCleanUpExtension implements BeforeEachCallback{
    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        final var templates = SpringExtension.getApplicationContext(extensionContext).getBeansOfType(RedisTemplate.class)
                .values();
        cleanUp(templates);
    }

    private void cleanUp(Collection<RedisTemplate> templates) {
        templates.forEach(template -> {
            template.execute((RedisCallback<String>) connection -> {
                connection.flushDb();
                return "OK";
            });
        });
    }
}
