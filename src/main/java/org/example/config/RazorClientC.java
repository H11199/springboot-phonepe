package org.example.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorClientC {

    @Bean
    RazorpayClient RazorClientC(final RazorpayProperties properties) throws RazorpayException {
        return new RazorpayClient(properties.rzp_key_id(), properties.rzp_key_secret());
    }
}
