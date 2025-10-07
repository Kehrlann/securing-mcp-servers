package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDate;
import java.util.List;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
class McpAppointmentService {

	private final AppointmentService appointmentService;

	McpAppointmentService(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@McpTool(name = "my-appointments", description = "List my appointments between two dates")
	public List<AppointmentSlot> myAppointments(
			@McpToolParam(description = "the start date, inclusive", required = false) LocalDate startDate,
			@McpToolParam(description = "the end date, inclusive", required = false) LocalDate endDate) {
		Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return appointmentService.findSlotsByUserEmailAndDateRange(jwt.getSubject(), startDate, endDate);
	}

}
