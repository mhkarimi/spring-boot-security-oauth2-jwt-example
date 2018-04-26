package com.devglan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    static final String CLIEN_ID = "devglan-client";
    static final String CLIENT_SECRET = "devglan-secret";
    static final String GRANT_TYPE_PASSWORD = "password";
    static final String AUTHORIZATION_CODE = "authorization_code";
    static final String REFRESH_TOKEN = "refresh_token";
    static final String IMPLICIT = "implicit";
    static final String SCOPE_READ = "read";
    static final String SCOPE_WRITE = "write";
    static final String TRUST = "trust";
    static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1 * 60 * 60;
    static final int FREFRESH_TOKEN_VALIDITY_SECONDS = 6 * 60 * 60;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    OauthSettings oauthSettings;


    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(oauthSettings.getTokenSigningKey());
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(jedisConnectionFactory());
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

        configurer
                //.withClientDetails(clientDetailsService)
                .inMemory()
                .withClient(oauthSettings.getClientId())
                .secret(oauthSettings.getClientSecret())
                .authorizedGrantTypes(oauthSettings.getGrantTypePassword(), oauthSettings.getAuthorizationCode(), oauthSettings.getRefreshToken(), oauthSettings.getImplicit())
                .scopes(oauthSettings.getScopeRead(), oauthSettings.getScopeWrite(), oauthSettings.getTrust())
                .accessTokenValiditySeconds(oauthSettings.getTokenExpirationTime()).
                refreshTokenValiditySeconds(oauthSettings.getRefreshTokenExpTime());

        //this.clientDetailsService = configurer.inMemory().withClient(CLIEN_ID)

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(accessTokenConverter())
                .tokenGranter(myResourceOwnerPasswordTokenGranter())
                .authenticationManager(authenticationManager);
              //  .accessTokenConverter(accessTokenConverter());

    }


    @Bean
    public ClientDetailsService clientDetailsService() {
        return new ClientDetailsService() {
            @Override
            public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                BaseClientDetails details = new BaseClientDetails();
                details.setClientId(oauthSettings.getClientId());
                details.setClientSecret(oauthSettings.getClientSecret());
                details.setAuthorizedGrantTypes(Arrays.asList(oauthSettings.getGrantTypePassword(), oauthSettings.getAuthorizationCode(), oauthSettings.getRefreshToken(), oauthSettings.getImplicit()));
                details.setScope(Arrays.asList(oauthSettings.getScopeRead(), oauthSettings.getScopeWrite(), oauthSettings.getTrust()));
                details.setAccessTokenValiditySeconds(oauthSettings.getTokenExpirationTime());
                details.setRefreshTokenValiditySeconds(oauthSettings.getRefreshTokenExpTime());
                details.setResourceIds(Arrays.asList("resource_id"));
                Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                details.setAuthorities(authorities);
                return details;
            }
        };
    }  //*

    @Bean
    @Primary
    AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setClientDetailsService(clientDetailsService());
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        return defaultTokenServices;
    }

    @Bean
    OAuth2RequestFactory oAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetailsService());
    }


    @Bean
    MyResourceOwnerPasswordTokenGranter myResourceOwnerPasswordTokenGranter() {
        return new MyResourceOwnerPasswordTokenGranter(authenticationManager, authorizationServerTokenServices(), clientDetailsService(), oAuth2RequestFactory());
    }

}