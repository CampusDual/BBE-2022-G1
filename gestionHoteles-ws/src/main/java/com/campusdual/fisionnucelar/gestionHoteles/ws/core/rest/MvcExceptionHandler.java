package com.campusdual.fisionnucelar.gestionHoteles.ws.core.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.campusdual.fisionnucelar.gestionHoteles.model.core.exception.RecordNotFoundException;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

@ControllerAdvice
public class MvcExceptionHandler {
	
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<EntityResult> bookingIdNotFound(RecordNotFoundException e){
		EntityResult errorResult = new EntityResultMapImpl();
		errorResult.setMessage(e.getMessage());
		return new ResponseEntity<EntityResult>(errorResult,HttpStatus.NOT_FOUND);
	}

}