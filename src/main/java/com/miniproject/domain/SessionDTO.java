package com.miniproject.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class SessionDTO {
	
	@NonNull
	private String userId;
	
	private Timestamp sessionLimit;
	private String sessionKey;
}
