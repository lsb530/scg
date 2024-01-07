package boki.tistory.springcloudgatewayjava.config;


import boki.tistory.springcloudgatewayjava.filter.ApiKeyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.ZonedDateTime;

@Configuration
public class GatewayConfig {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public GatewayConfig(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public RouteLocator userRouteLocator(RouteLocatorBuilder builder) {
        ApiKeyFilter apiKeyFilter = new ApiKeyFilter(redisTemplate);
        return builder.routes()
                // path: /users
                .route(
                        "user_route",
                        r -> r.path("/users")
                                .filters(
                                        f -> f.filter(apiKeyFilter)
                                                .rewritePath("/users", "/rest/v1/api/users")
                                )
                                .uri("http://localhost:3000"))
//
//                // path: /patients/{id}
//                .route(
//                        "patient_id_route",
//                        r -> r.path("/patients/**")
//                                .filters(f -> f.rewritePath("/patients/(?<id>.*)", "/rest/v1/api/patients/${id}")
//                                        .addRequestHeader("apikey", apiKey))
//                                .uri(baseURL))

                .route(
                        "device_route",
                        r -> r.path("/devices")
                                .filters(
                                        f -> f.filter(apiKeyFilter)
                                                .rewritePath("/devices", "/rest/v1/api/devices")
                                )
                                .uri("http://localhost:8000"))
                .build();
    }
}