package org.example.authorize.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Caching Configuration.
 */
@Configuration
@EnableCaching
public class CachingConfiguration {

//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("addresses");
//    }
}
