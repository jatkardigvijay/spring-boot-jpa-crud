package com.jbd.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

	@JsonInclude(Include.NON_NULL)
	private String message;
	@JsonInclude(Include.NON_NULL)
	private Object data;
	@JsonInclude(Include.NON_NULL)
	private String errorMessage;
}