package com.booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.entity.Booking;
import com.booking.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Booking> book(@RequestParam Long slotId) {
		Booking booking = bookingService.bookSlot(slotId);

		return ResponseEntity.status(HttpStatus.CREATED).body(booking);
	}

	@PostMapping("/{id}/cancel")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Booking> cancel(@PathVariable Long id) {
		Booking booking = bookingService.cancelBooking(id);
		return ResponseEntity.ok(booking);
	}

	@PostMapping("/admin/{id}/cancel")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Booking> adminCancel(@PathVariable Long id) {
		Booking booking = bookingService.adminCancelBooking(id);
		return ResponseEntity.ok(booking);
	}

}
