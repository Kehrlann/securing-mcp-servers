package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.modelcontextprotocol.spec.McpSchema;
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

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~
	//
	//
	// PRE-IMPLEMENTED STUFF BELOW
	//
	//
	// I'm not lazy. I' well prepared.
	//
	//
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~


//	@McpTool(name = "all-appointment-slots",
//			description = "List all available appointment slots between two datetimes, with a status indicating whether it's booked")
//	public List<AppointmentWithBooking> allAppointmentSlots(
//			@McpToolParam(description = "the start date and time, inclusive", required = false) LocalDate startDate,
//			@McpToolParam(description = "the end date and time, inclusive", required = false) LocalDate endDate) {
//
//		return appointmentService.findSlotsByDateRange(startDate, endDate)
//			.stream()
//			.map(slot -> new AppointmentWithBooking(slot.id(), slot.name(), slot.dateTime(), isBooked(slot)))
//			.toList();
//	}
//
//	private BookingStatus isBooked(AppointmentSlot slot) {
//		if (SecurityContextHolder.getContext().getAuthentication() == null) {
//			return BookingStatus.UNKNOWN;
//		}
//		if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt jwt)) {
//			return BookingStatus.UNKNOWN;
//		}
//
//		return appointmentService.isBooked(slot.id(), jwt.getSubject()) ? BookingStatus.BOOKED
//				: BookingStatus.NOT_BOOKED;
//	}
//
//	@McpTool(name = "book-appointment", description = "Book an appointment by name and datetime")
//	public McpSchema.CallToolResult bookAppointment(@McpToolParam(description = "the name of the slot") String name,
//			@McpToolParam(description = "the datetime of the appointment slot") LocalDateTime date) {
//		Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		var email = jwt.getSubject();
//
//		var slotOpt = appointmentService.findSlotByNameAndDateTime(name, date);
//
//		if (slotOpt.isEmpty()) {
//			return McpSchema.CallToolResult.builder().isError(true).addTextContent("slot does not exist").build();
//		}
//
//		var slot = slotOpt.get();
//
//		if (appointmentService.isBooked(slot.id(), email)) {
//			return McpSchema.CallToolResult.builder()
//				.isError(true)
//				.addTextContent("appointment is already booked")
//				.build();
//		}
//
//		appointmentService.bookAppointment(slot.id(), email);
//
//		var booking = new AppointmentWithBooking(slot.id(), slot.name(), slot.dateTime(), BookingStatus.BOOKED);
//		return McpSchema.CallToolResult.builder().structuredContent(booking).build();
//	}
//
//	record AppointmentWithBooking(Integer id, String name, LocalDateTime localDateTime, BookingStatus status) {
//	}
//
//	enum BookingStatus {
//
//		BOOKED, NOT_BOOKED, UNKNOWN
//
//	}

}
