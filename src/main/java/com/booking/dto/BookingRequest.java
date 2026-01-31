package com.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {

	@NotNull
	private Long slotId;

	@NotNull
	private Long userId;

}
