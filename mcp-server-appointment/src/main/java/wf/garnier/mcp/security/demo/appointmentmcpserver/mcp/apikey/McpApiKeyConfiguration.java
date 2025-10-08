package wf.garnier.mcp.security.demo.appointmentmcpserver.mcp.apikey;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.WebMvcStatelessServerTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.springaicommunity.mcp.provider.tool.SyncStatelessMcpToolProvider;
import org.springaicommunity.mcp.security.server.apikey.ApiKeyEntity;
import org.springaicommunity.mcp.security.server.apikey.ApiKeyEntityRepository;
import org.springaicommunity.mcp.security.server.apikey.memory.ApiKeyEntityImpl;
import org.springaicommunity.mcp.security.server.apikey.memory.InMemoryApiKeyEntityRepository;
import org.springaicommunity.mcp.security.server.config.McpApiKeyConfigurer;
import wf.garnier.mcp.security.demo.appointmentmcpserver.AllowAllCorsConfigurationSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.function.RouterFunction;

@Configuration
class McpApiKeyConfiguration {

	@Bean
	@Order(1)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.securityMatcher("/apikey/**")
			.authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
			.with(McpApiKeyConfigurer.mcpServerApiKey(), apiKey -> {
				apiKey.apiKeyRepository(apiKeyRepo());
			})
			.csrf(csrf -> csrf.ignoringRequestMatchers("/apikey/**"))
			.cors(cors -> cors.configurationSource(new AllowAllCorsConfigurationSource()))
			.build();
	}

	private static ApiKeyEntityRepository<ApiKeyEntity> apiKeyRepo() {
		return new InMemoryApiKeyEntityRepository<>(List.of(ApiKeyEntityImpl.builder()
			.id("api01")
			.secret("some-super-secret")
			.name("Devoxx demo api key")
			.build()));
	}

	/**
	 * An MCP server for weather info.
	 * <p>
	 * It is available at {@code /apikey/mcp}.
	 * <p>
	 * Note that it is configured manually in this bean, and not through Spring Boot
	 * auto-configuration, so we can launch it independently from the main, OAuth2-secured
	 * MCP server.
	 */
	@Bean
	RouterFunction<?> apiKeyRouterFunction(ObjectMapper objectMapper, RestClient.Builder restClientBuilder) {
		var service = new McpWeatherService(restClientBuilder);
		var toolsSpecs = new SyncStatelessMcpToolProvider(List.of(service)).getToolSpecifications();

		var transport = WebMvcStatelessServerTransport.builder()
			.jsonMapper(new JacksonMcpJsonMapper(objectMapper))
			.messageEndpoint("/apikey/mcp")
			.build();
		McpSchema.Implementation serverInfo = new McpSchema.Implementation("API Key MCP server", "1.0.0");
		// Claude code calls prompts and resources, event if you don't advertise the
		// capabilities...
		var serverCapabilities = McpSchema.ServerCapabilities.builder()
			.tools(false)
			.prompts(false)
			.resources(false, false)
			.build();
		McpServer.sync(transport)
			.serverInfo(serverInfo)
			.immediateExecution(true)
			.capabilities(serverCapabilities)
			.requestTimeout(Duration.ofSeconds(10))
			.tools(toolsSpecs)
			.build();

		return transport.getRouterFunction();
	}

}
