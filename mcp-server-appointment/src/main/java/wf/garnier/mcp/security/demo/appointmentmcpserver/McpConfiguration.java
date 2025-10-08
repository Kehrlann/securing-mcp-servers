package wf.garnier.mcp.security.demo.appointmentmcpserver;

import org.springaicommunity.mcp.security.server.config.McpServerOAuth2Configurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * See {@link SecurityConfiguration}.
 */
@Configuration
class McpConfiguration {

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUrl;

	@Bean
	@Order(0)
	SecurityFilterChain mcpFilterChain(HttpSecurity http) throws Exception {
		return http.securityMatcher("/mcp", "/.well-known/**")
			.authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
			.with(McpServerOAuth2Configurer.mcpServerOAuth2(), config -> {
				config.authorizationServer(issuerUrl);
			})
			.cors(cors -> cors.configurationSource(new AllowAllCorsConfigurationSource()))
			.csrf(CsrfConfigurer::disable)
			.build();
	}

}
