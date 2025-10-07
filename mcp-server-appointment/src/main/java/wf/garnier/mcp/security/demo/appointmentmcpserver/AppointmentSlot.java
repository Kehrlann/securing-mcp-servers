package wf.garnier.mcp.security.demo.appointmentmcpserver;

import java.time.LocalDateTime;

record AppointmentSlot(Integer id, String name, LocalDateTime localDateTime) {

}
