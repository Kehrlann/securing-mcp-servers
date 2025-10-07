package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;

	private final AppointmentSlotRepository slotRepository;

	public AppointmentService(AppointmentRepository appointmentRepository, AppointmentSlotRepository slotRepository) {
		this.appointmentRepository = appointmentRepository;
		this.slotRepository = slotRepository;
	}

	public void bookAppointment(Integer appointmentId, String userEmail) {
		if (appointmentRepository.findByAppointmentIdAndUserEmail(appointmentId, userEmail).isEmpty()) {
			appointmentRepository.save(new Appointment(appointmentId, userEmail));
		}
	}

	public void unbookAppointment(Integer appointmentId, String userEmail) {
		appointmentRepository.deleteByAppointmentIdAndUserEmail(appointmentId, userEmail);
	}

	public boolean isBooked(Integer appointmentId, String userEmail) {
		return appointmentRepository.findByAppointmentIdAndUserEmail(appointmentId, userEmail).isPresent();
	}

	public List<Appointment> findByUserEmail(String userEmail) {
		return appointmentRepository.findByUserEmail(userEmail);
	}

	public List<Appointment> findByUserEmailAndDateRange(String userEmail, LocalDate startDate, LocalDate endDate) {
		List<Appointment> appointments = appointmentRepository.findByUserEmail(userEmail);

		if (startDate == null && endDate == null) {
			return appointments;
		}

		return appointments.stream().filter(appointment -> {
			var slot = slotRepository.findAll()
				.stream()
				.filter(s -> s.id().equals(appointment.appointmentId()))
				.findFirst();

			if (slot.isEmpty()) {
				return false;
			}

			LocalDate appointmentDate = slot.get().localDateTime().toLocalDate();

			if (startDate != null && appointmentDate.isBefore(startDate)) {
				return false;
			}

			if (endDate != null && appointmentDate.isAfter(endDate)) {
				return false;
			}

			return true;
		}).toList();
	}

	public List<AppointmentSlot> findSlotsByUserEmailAndDateRange(String userEmail, LocalDate startDate,
			LocalDate endDate) {
		List<Appointment> appointments = appointmentRepository.findByUserEmail(userEmail);
		List<AppointmentSlot> allSlots = slotRepository.findAll();

		return appointments.stream()
			.map(appointment -> allSlots.stream()
				.filter(s -> s.id().equals(appointment.appointmentId()))
				.findFirst()
				.orElse(null))
			.filter(slot -> slot != null)
			.filter(slot -> {
				LocalDate appointmentDate = slot.localDateTime().toLocalDate();

				if (startDate != null && appointmentDate.isBefore(startDate)) {
					return false;
				}

				if (endDate != null && appointmentDate.isAfter(endDate)) {
					return false;
				}

				return true;
			})
			.toList();
	}

}
