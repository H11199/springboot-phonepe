package org.example.config;

import com.phonepe.sdk.pg.Env;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type App properties.
 */
@ConfigurationProperties(prefix = "spring.payments.phonepe")
public record PhonePeProperties (String merchantId,
                             String saltKey,
                             Integer saltIndex,
                                 Env env, boolean shouldPublishEvents,
                             String callbackUrl) {
}
