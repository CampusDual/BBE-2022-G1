package com.campusdual.fisionnucelar.gestionHoteles.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IHotelService {

 // HOTEL
 public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
 public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
 public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
 public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}