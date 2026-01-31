package com.booking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.entity.Slot;
import com.booking.entity.SlotStatus;
import com.booking.repository.SlotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlotService {

	private final SlotRepository slotRepository;

	public Slot createSlot(LocalDateTime start, LocalDateTime end) {
		Slot slot = new Slot();
		slot.setStartTime(start);
		slot.setEndTime(end);
		slot.setStatus(SlotStatus.AVAILABLE);
		return slotRepository.save(slot);
	}

	public List<Slot> getAllSlots() {
		return slotRepository.findAll();
	}
}
