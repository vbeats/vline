package com.codestepfish.vline.core.redis;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
public class RedisBaseConfig {
    private int idleConnectionTimeout = 10000;
    private int connectTimeout = 10000;
    private int timeout = 3000;
    private int retryAttempts = 3;
    private int retryInterval = 1500;
    private String password;
    private String username;
    private int subscriptionsPerConnection = 5;
    private String clientName;
    private boolean sslEnableEndpointIdentification = true;
    private SslProvider sslProvider = SslProvider.JDK;

    private URL sslTruststore;

    private String sslTruststorePassword;

    private URL sslKeystore;

    private String sslKeystorePassword;

    private String[] sslProtocols;

    private int pingConnectionInterval = 30000;

    private boolean keepAlive;

    private boolean tcpNoDelay = true;

    public enum SslProvider {
        JDK,
        OPENSSL
    }
}
