package com.imaginovate.csvapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.imaginovate.csvapplication.constants.Constants;
import com.imaginovate.csvapplication.model.Response;
/**
 * 
 * @author sampath
 * */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	/**
	 * 
	 * This method hadles internal server error wherever it is occuring in the spring boot project and 
	 * return the default response as shown below
	 * 
	 * @return Response - response object with status, code , message and data
	 * 
	 * 
	 * */
	@ExceptionHandler(InternalServerError.class)
	public ResponseEntity<Response> handleISE() {
		Response response = new Response(Constants.ERROR, Constants.HTTP_400, "Internal Server Error");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
