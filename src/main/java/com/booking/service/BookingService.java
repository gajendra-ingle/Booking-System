package com.booking.service;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.booking.entity.Booking;
import com.booking.entity.Slot;
import com.booking.entity.enums.BookingStatus;
import com.booking.entity.enums.SlotStatus;
import com.booking.exception.BookingNotFoundException;
import com.booking.exception.SlotAlreadyBookedException;
import com.booking.exception.SlotNotFoundException;
import com.booking.exception.UnauthorizedBookingActionException;
import com.booking.repository.BookingRepository;
import com.booking.repository.SlotRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

	private final SlotRepository slotRepository;
	private final BookingRepository bookingRepository;

	@Transactional
	public Booking bookSlot(Long slotId) {
		Slot slot = slotRepository.findByIdForUpdate(slotId)
				.orElseThrow(() -> new SlotNotFoundException("Slot not found"));

		if (slot.getStatus() == SlotStatus.BOOKED) {
			throw new SlotAlreadyBookedException("Slot is already booked");
		}

		String currentUserId = getCurrentUserId();

		Booking booking = new Booking();
		booking.setSlot(slot);
		booking.setUserId(currentUserId);
		booking.setStatus(BookingStatus.ACTIVE);
		booking.setCreatedAt(LocalDateTime.now());

		slot.setStatus(SlotStatus.BOOKED);

		slotRepository.save(slot);
		return bookingRepository.save(booking);
	}

	@Transactional
	public Booking cancelBooking(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));

		String currentUserId = getCurrentUserId();
		if (!booking.getUserId().equals(currentUserId)) {
			throw new UnauthorizedBookingActionException("Cannot cancel other user's booking");
		}

		booking.setStatus(BookingStatus.CANCELLED);
		booking.getSlot().setStatus(SlotStatus.AVAILABLE);

		slotRepository.save(booking.getSlot());
		return bookingRepository.save(booking);
	}

	@Transactional
	public Booking adminCancelBooking(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new BookingNotFoundException("Booking not found"));

		booking.setStatus(BookingStatus.CANCELLED);
		booking.getSlot().setStatus(SlotStatus.AVAILABLE);

		slotRepository.save(booking.getSlot());
		return bookingRepository.save(booking);
	}

	private String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
