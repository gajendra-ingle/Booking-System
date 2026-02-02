package com.booking.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.booking.entity.Booking;
import com.booking.entity.Slot;
import com.booking.entity.enums.BookingStatus;
import com.booking.service.BookingService;

@WebMvcTest(AdminBookingController.class)
class AdminBookingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookingService bookingService;

	@Test
	@WithMockUser(roles = "ADMIN")
	void cancelBooking_asAdmin_shouldReturnCancelledBooking() throws Exception {
		// given
		Long bookingId = 1L;

		Slot slot = new Slot();
		slot.setId(10L);

		Booking booking = new Booking();
		booking.setId(bookingId);
		booking.setSlot(slot);
		booking.setUserId("user-123");
		booking.setStatus(BookingStatus.CANCELLED);
		booking.setCreatedAt(LocalDateTime.now());

		when(bookingService.adminCancelBooking(bookingId)).thenReturn(booking);

		// when / then
		mockMvc.perform(post("/admin/bookings/{id}/cancel", bookingId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(bookingId))
				.andExpect(jsonPath("$.status").value("CANCELLED"))
				.andExpect(jsonPath("$.userId").value("user-123"));
	}

	@Test
	@WithMockUser(roles = "USER")
	void cancelBooking_asNonAdmin_shouldReturnForbidden() throws Exception {
		mockMvc.perform(post("/admin/bookings/{id}/cancel", 1L))
		.andExpect(status().isForbidden());
	}
}