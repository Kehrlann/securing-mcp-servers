package wf.garnier.mcp.security.demo.appointmentmcpserver;

import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;

	public AppointmentService(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
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

}
