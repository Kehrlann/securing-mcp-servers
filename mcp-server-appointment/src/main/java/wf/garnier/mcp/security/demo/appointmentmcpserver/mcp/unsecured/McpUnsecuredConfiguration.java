package wf.garnier.mcp.security.demo.appointmentmcpserver.mcp.unsecured;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.WebMvcStatelessServerTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.springaicommunity.mcp.provider.tool.SyncStatelessMcpToolProvider;
import wf.garnier.mcp.security.demo.appointmentmcpserver.AllowAllCorsConfigurationSource;
import wf.garnier.mcp.security.demo.appointmentmcpserver.AppointmentService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.function.RouterFunction;

@Configuration
class McpUnsecuredConfiguration {

	@Bean
	@Order(1)
	SecurityFilterChain unsecuredMcpServerFilterChain(HttpSecurity http) throws Exception {
		return http.securityMatcher("/unsecured/*")
			.authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.cors(cors -> cors.configurationSource(new AllowAllCorsConfigurationSource()))
			.csrf(CsrfConfigurer::disable)
			.build();
	}

	@Bean
	RouterFunction<?> unsecuredRouterFunction(ObjectMapper objectMapper, AppointmentService appointmentService) {
		var service = new McpUnsecuredAppointmentService(appointmentService);
		var toolsSpecs = new SyncStatelessMcpToolProvider(List.of(service)).getToolSpecifications();

		var transport = WebMvcStatelessServerTransport.builder()
			.jsonMapper(new JacksonMcpJsonMapper(objectMapper))
			.messageEndpoint("/unsecured/mcp")
			.build();
		McpSchema.Implementation serverInfo = new McpSchema.Implementation("Unsecured MCP server", "1.0.0");
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
