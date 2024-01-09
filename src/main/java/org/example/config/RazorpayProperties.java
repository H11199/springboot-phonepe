package org.example.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "razorpay")
public record RazorpayProperties(
        String rzp_key_id, String   rzp_key_secret, String rzp_currency, String rzp_company_name
) {
}
