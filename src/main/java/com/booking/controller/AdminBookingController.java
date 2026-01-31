package com.booking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.entity.Booking;
import com.booking.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

	private final BookingService bookingService;

	@PostMapping("/{id}/cancel")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Booking> cancel(@PathVariable Long id) {
		Booking booking = bookingService.adminCancelBooking(id);
		return ResponseEntity.ok(booking);
	}

}
