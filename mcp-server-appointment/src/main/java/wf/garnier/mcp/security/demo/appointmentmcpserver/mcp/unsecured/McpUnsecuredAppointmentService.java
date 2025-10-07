package wf.garnier.mcp.security.demo.appointmentmcpserver.mcp.unsecured;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.modelcontextprotocol.spec.McpSchema;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import wf.garnier.mcp.security.demo.appointmentmcpserver.AppointmentService;
import wf.garnier.mcp.security.demo.appointmentmcpserver.AppointmentSlot;

class McpUnsecuredAppointmentService {

	private final AppointmentService appointmentService;

	McpUnsecuredAppointmentService(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@McpTool(name = "my-appointments-unsecured", description = "List my appointments between to datetimes")
	public List<AppointmentSlot> myAppointments(@McpToolParam(description = "the email of the user") String userEmail,
			@McpToolParam(description = "the start date and time, inclusive", required = false) LocalDate startDate,
			@McpToolParam(description = "the end date and time, inclusive", required = false) LocalDate endDate) {
		return appointmentService.findSlotsByUserEmailAndDateRange(userEmail, startDate, endDate);
	}

	@McpTool(name = "all-appointment-slots",
			description = "List all available appointment slots between to datetimes, with a status indicating whether it's booked")
	public List<AppointmentWithBooking> allAppointmentSlots(
			@McpToolParam(description = "the email of the user", required = false) String userEmail,
			@McpToolParam(description = "the start date and time, inclusive", required = false) LocalDate startDate,
			@McpToolParam(description = "the end date and time, inclusive", required = false) LocalDate endDate) {
		return appointmentService.findSlotsByDateRange(startDate, endDate).stream().map(slot -> {
			var status = BookingStatus.UNKNOWN;
			if (userEmail != null) {
				status = appointmentService.isBooked(slot.id(), userEmail) ? BookingStatus.BOOKED
						: BookingStatus.NOT_BOOKED;
			}
			return new AppointmentWithBooking(slot.id(), slot.name(), slot.dateTime(), status);
		}).toList();
	}

	@McpTool(name = "book-appointment", description = "Book an appointment by name and datetime")
	public McpSchema.CallToolResult bookAppointment(
			@McpToolParam(description = "the email of the user") String userEmail,
			@McpToolParam(description = "the name of the slot") String name,
			@McpToolParam(description = "the datetime of the appointment slot") LocalDateTime date) {
		var slotOpt = appointmentService.findSlotByNameAndDateTime(name, date);

		if (slotOpt.isEmpty()) {
			return McpSchema.CallToolResult.builder().isError(true).addTextContent("slot does not exist").build();
		}

		var slot = slotOpt.get();

		if (appointmentService.isBooked(slot.id(), userEmail)) {
			return McpSchema.CallToolResult.builder()
				.isError(true)
				.addTextContent("appointment is already booked")
				.build();
		}

		appointmentService.bookAppointment(slot.id(), userEmail);

		var booking = new AppointmentWithBooking(slot.id(), slot.name(), slot.dateTime(), BookingStatus.BOOKED);
		return McpSchema.CallToolResult.builder().structuredContent(booking).build();
	}

	record AppointmentWithBooking(Integer id, String name, LocalDateTime dateTime, BookingStatus status) {
	}

	enum BookingStatus {

		BOOKED, NOT_BOOKED, UNKNOWN

	}

}
