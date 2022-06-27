package com.campusdual.fisionnucelar.gestionHoteles.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IRoomTypeService {

	 // ROOMTYPE
	 public EntityResult roomtypeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult roomtypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult roomtypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult roomtypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

	}
