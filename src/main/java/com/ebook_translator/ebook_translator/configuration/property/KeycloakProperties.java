package com.ebook_translator.ebook_translator.configuration.property;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakProperties {

    private String clientId;
    private String clientSecret;
    private String realm;
    private String serverUrl;

}
