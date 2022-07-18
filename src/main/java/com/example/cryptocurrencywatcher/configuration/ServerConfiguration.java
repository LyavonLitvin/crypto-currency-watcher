package com.example.cryptocurrencywatcher.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;

@Component
public class ServerConfiguration {

    @Bean
    public SSLContext sslContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null,null,null);
            SSLContext.setDefault(sslContext);
            return sslContext;
        } catch (Exception exception) {
            return null;
        }
    }
}
