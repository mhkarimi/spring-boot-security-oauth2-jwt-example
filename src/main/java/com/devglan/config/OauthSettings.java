package com.devglan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "phonePay.security.oauth")

public class OauthSettings {
    private Integer tokenExpirationTime;
    private Integer refreshTokenExpTime;
    private String tokenSigningKey;
    private String clientId;
    private String clientSecret;
    private String grantTypePassword;
    private String authorizationCode;
    private String refreshToken;
    private String implicit;
    private String scopeRead;
    private String scopeWrite;
    private String trust;


    public Integer getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(Integer tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public Integer getRefreshTokenExpTime() {
        return refreshTokenExpTime;
    }

    public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantTypePassword() {
        return grantTypePassword;
    }

    public void setGrantTypePassword(String grantTypePassword) {
        this.grantTypePassword = grantTypePassword;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getImplicit() {
        return implicit;
    }

    public void setImplicit(String implicit) {
        this.implicit = implicit;
    }

    public String getScopeRead() {
        return scopeRead;
    }

    public void setScopeRead(String scopeRead) {
        this.scopeRead = scopeRead;
    }

    public String getScopeWrite() {
        return scopeWrite;
    }

    public void setScopeWrite(String scopeWrite) {
        this.scopeWrite = scopeWrite;
    }

    public String getTrust() {
        return trust;
    }

    public void setTrust(String trust) {
        this.trust = trust;
    }
}
