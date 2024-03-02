package org.ng.workflow.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse<T> {

	private Integer code;

	private T data;
	
	private String message;


	public ApplicationResponse(Integer code, T data) {
		this.code = code;
		this.data = data;
	}

	public ApplicationResponse(Integer code) {
		super();
		this.code = code;
	}

}
