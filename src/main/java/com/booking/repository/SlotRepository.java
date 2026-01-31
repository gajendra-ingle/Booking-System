package com.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.booking.entity.Slot;

import jakarta.persistence.LockModeType;

public interface SlotRepository extends JpaRepository<Slot, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s FROM Slot s WHERE s.id = :id")
	Optional<Slot> findByIdForUpdate(Long id);
}



