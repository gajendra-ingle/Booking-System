package com.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SlotRequest {

	@NotNull
	private LocalDateTime startTime;

	@NotNull
	private LocalDateTime endTime;

}