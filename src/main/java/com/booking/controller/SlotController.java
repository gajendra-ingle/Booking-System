package com.booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.dto.SlotRequest;
import com.booking.entity.Slot;
import com.booking.service.SlotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/slots")
@RequiredArgsConstructor
public class SlotController {

	private final SlotService slotService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Slot> create(@RequestBody SlotRequest request) {
		Slot slot = slotService.createSlot(request.getStartTime(), request.getEndTime());
		return ResponseEntity.status(HttpStatus.CREATED).body(slot);
	}

	@GetMapping
	public ResponseEntity<List<Slot>> list() {
		List<Slot> slots = slotService.getAllSlots();
		return ResponseEntity.ok(slots);
	}

}
