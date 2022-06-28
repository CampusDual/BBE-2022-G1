package com.campusdual.fisionnucelar.gestionHoteles.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.campusdual.fisionnucelar.gestionHoteles.api.core.service.IHotelService;
import com.campusdual.fisionnucelar.gestionHoteles.model.core.dao.HotelDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {

 @Autowired
 private HotelDao hotelDao;
 @Autowired
 private DefaultOntimizeDaoHelper daoHelper;

 @Override
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		EntityResult searchResult = this.daoHelper.query(this.hotelDao, keyMap, attrList);
		if (searchResult!=null && searchResult.getCode()==EntityResult.OPERATION_WRONG) {
			searchResult.setMessage("ERROR_WHILE_SEARCHING");
		}
		
		System.out.println(keyMap);
		return searchResult;
	}
	
 
	@Override
	public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult insertResult = this.daoHelper.insert(this.hotelDao, attrMap);
		if (insertResult!=null && insertResult.getCode()==EntityResult.OPERATION_WRONG) {
			insertResult.setMessage("ERROR_WHILE_INSERTING");
		}else {
			insertResult.setMessage("SUCCESSFUL_INSERTION");
			
		}
		return insertResult;
	}

	@Override
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult updateResult = this.daoHelper.update(this.hotelDao, attrMap, keyMap);
		if (updateResult!=null && updateResult.getCode()==EntityResult.OPERATION_WRONG) {
			updateResult.setMessage("ERROR_WHILE_UPDATING");
		}else {
			updateResult.setMessage("SUCCESSFUL_UPDATE");
		}
		return updateResult;
	}

	@Override
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		EntityResult deleteResult = this.daoHelper.delete(this.hotelDao, keyMap);
		if (deleteResult!=null && deleteResult.getCode()==EntityResult.OPERATION_WRONG) {
			deleteResult.setMessage("ERROR_WHILE_DELETING");
		}else {
			deleteResult.setMessage("SUCCESSFUL_DELETE");
		}
		return deleteResult;
	}}