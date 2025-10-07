package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class AppointmentSlotRepository {

	private final List<AppointmentSlot> slots = new ArrayList<>();

	private int nextId = 1;

	public void save(AppointmentSlot slot) {
		if (slot.id() == null) {
			slots.add(new AppointmentSlot(nextId++, slot.name(), slot.dateTime()));
		}
		else {
			slots.add(slot);
			nextId = Math.max(nextId, slot.id() + 1);
		}
	}

	public List<AppointmentSlot> findAll() {
		return new ArrayList<>(slots);
	}

}
