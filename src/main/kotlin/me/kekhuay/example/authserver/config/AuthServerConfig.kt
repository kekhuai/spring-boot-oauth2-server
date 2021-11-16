package me.kekhuay.example.authserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import javax.sql.DataSource

@Configuration
class AuthServerConfig(val ds: DataSource, val authMgr: AuthenticationManager, val userSvc: UserDetailsService) :
    AuthorizationServerConfigurerAdapter() {
    @Bean
    fun tokenStore(): TokenStore {
        return JdbcTokenStore(ds)
    }

    @Bean("clientPasswordEncoder")
    fun clientPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(4)
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.checkTokenAccess("permitAll")
        security.passwordEncoder(clientPasswordEncoder())
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.jdbc(ds)
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.tokenStore(tokenStore())
        endpoints.authenticationManager(authMgr)
        endpoints.userDetailsService(userSvc)
    }
}
