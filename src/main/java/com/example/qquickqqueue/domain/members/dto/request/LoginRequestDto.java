package com.example.qquickqqueue.domain.members.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginRequestDto {
	private String email;
	private String password;
}
