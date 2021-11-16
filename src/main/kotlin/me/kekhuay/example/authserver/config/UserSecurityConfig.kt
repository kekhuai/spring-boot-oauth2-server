package me.kekhuay.example.authserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource

@Configuration
class UserSecurityConfig(val ds: DataSource) : WebSecurityConfigurerAdapter() {
    @Bean(BeanIds.USER_DETAILS_SERVICE)
    override fun userDetailsServiceBean(): UserDetailsService {
        return super.userDetailsServiceBean()
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean("userPasswordEncoder")
    fun userPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(4)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        val cfg = auth.jdbcAuthentication().passwordEncoder(userPasswordEncoder()).dataSource(ds)
        cfg.userDetailsService.setEnableGroups(true)
        cfg.userDetailsService.setEnableAuthorities(false)
    }
}