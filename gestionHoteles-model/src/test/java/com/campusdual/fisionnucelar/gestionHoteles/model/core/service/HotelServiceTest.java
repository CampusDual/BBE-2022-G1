package com.campusdual.fisionnucelar.gestionHoteles.model.core.service;
import static com.campusdual.fisionnucelar.gestionHoteles.model.core.service.HotelTestData.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.campusdual.fisionnucelar.gestionHoteles.model.core.dao.HotelDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @Mock
    DefaultOntimizeDaoHelper daoHelper;

    @InjectMocks
    HotelService hotelService;
    
    HotelDao hotelDao;
    
    @BeforeEach
    void setUp() {
        this.hotelService = new HotelService();
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Test for Hotel queries")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class HotelQuery {

        @Test
        @DisplayName("Obtain all data from Hotels table")
        void when_queryOnlyWithAllColumns_return_allHotelData() {
            doReturn(getAllHotelData()).when(daoHelper).query(any(), anyMap(), anyList());
            EntityResult entityResult = hotelService.hotelQuery(new HashMap<>(), new ArrayList<>());
            assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
            assertEquals(3, entityResult.calculateRecordNumber());
            verify(daoHelper).query(any(), anyMap(), anyList());
        }

        @Test
        @DisplayName("Obtain all data columns from Hotels table when ID is -> 2")
        void when_queryAllColumns_return_specificData() {
            HashMap<String, Object> keyMap = new HashMap<>() {{
                put("ID_HOTEL", 2);
            }};
            List<String> attrList = Arrays.asList("ID_HOTEL", "HTL_NAME");
            doReturn(getSpecificHotelData(keyMap, attrList)).when(daoHelper).query(any(), anyMap(), anyList());
            EntityResult entityResult = hotelService.hotelQuery(new HashMap<>(), new ArrayList<>());
            assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
            assertEquals(1, entityResult.calculateRecordNumber());
            assertEquals(2, entityResult.getRecordValues(0).get(HotelDao.ATTR_ID));
            verify(daoHelper).query(any(), anyMap(), anyList());
        }

        @Test
        @DisplayName("Obtain all data columns from Hotels table when ID not exist")
        void when_queryAllColumnsNotExisting_return_empty() {
            HashMap<String, Object> keyMap = new HashMap<>() {{
                put("ID_HOTEL", 5);
            }};
            List<String> attrList = Arrays.asList("ID_HOTEL", "HTL_NAME", "HTL_PHONE","HTL_ADDRESS", "HTL_EMAIL");
            when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelData(keyMap, attrList));
            EntityResult entityResult = hotelService.hotelQuery(new HashMap<>(), new ArrayList<>());
            assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
            assertEquals(0, entityResult.calculateRecordNumber());
            verify(daoHelper).query(any(), anyMap(), anyList());
        }

        @ParameterizedTest(name = "Obtain data with ID -> {0}")
        @MethodSource("randomIDGenerator")
        @DisplayName("Obtain all data columns from HOTELS table when ID is random")
        void when_queryAllColumnsWithRandomValue_return_specificData(int random) {
            HashMap<String, Object> keyMap = new HashMap<>() {{
                put("ID_HOTEL", random);
            }};
            List<String> attrList = Arrays.asList("ID_HOTEL", "HTL_NAME");
            when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(getSpecificHotelData(keyMap, attrList));
            EntityResult entityResult = hotelService.hotelQuery(new HashMap<>(), new ArrayList<>());
            assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
            assertEquals(1, entityResult.calculateRecordNumber());
            assertEquals(random, entityResult.getRecordValues(0).get(HotelDao.ATTR_ID));
            verify(daoHelper).query(any(), anyMap(), anyList());
        }


        List<Integer> randomIDGenerator() {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                list.add(ThreadLocalRandom.current().nextInt(0, 3));
            }
            return list;
        }
    }
    
    @Nested
    @DisplayName("Test for Hotel inserts")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class HotelInsert {
        @Test
        @DisplayName("Insert a hotel successfully")
        void hotel_insert_success() {
        	EntityResult er = new EntityResultMapImpl(Arrays.asList("ID_HOTEL"));
            er.addRecord(new HashMap<String, Object>() {{put("ID_HOTEL", 2);}});
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            HashMap<String, Object> keyMap = new HashMap<>();
            keyMap.put("ID_HOTEL", 2);
            when(daoHelper.insert(any(), anyMap())).thenReturn(er);
            EntityResult entityResult = hotelService.hotelInsert(new HashMap<>());
            assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
            int recordIndex = entityResult.getRecordIndex(keyMap);
            assertEquals(2, entityResult.getRecordValues(recordIndex).get("ID_HOTEL"));
            verify(daoHelper).insert(any(), anyMap());
            
            }
       
        @Test
        @DisplayName("Fail trying to insert duplicated email")
        void hotel_insert_duplicated_mail() {
        	when(daoHelper.insert(any(), anyMap())).thenThrow(DuplicateKeyException.class);
        	EntityResult entityResult = hotelService.hotelInsert(new HashMap<>());
        	assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
        	assertEquals("HOTEL_NAME_OR_EMAIL_ALREADY_EXISTS", entityResult.getMessage());
        	verify(daoHelper).insert(any(), anyMap());
        }
        @Test
        @DisplayName("Fail trying to insert without hotel name or email fields")
        void hotel_insert_without_mail_or_hotel_name() {
        	when(daoHelper.insert(any(), anyMap())).thenThrow(DataIntegrityViolationException.class);
        	EntityResult entityResult = hotelService.hotelInsert(new HashMap<>());
        	assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
        	assertEquals("HOTEL_NAME_AND_EMAIL_REQUIRED", entityResult.getMessage());
        	verify(daoHelper).insert(any(), anyMap());
        }
    }
    
    @Nested
    @DisplayName("Test for Hotel updates")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class HotelUpdate {
        @Test
        @DisplayName("hotel update succesful")
        void hotel_update_success() {
        	EntityResult er = new EntityResultMapImpl();
        	er.setCode(EntityResult.OPERATION_SUCCESSFUL);
        	EntityResult queryResult = new EntityResultMapImpl(Arrays.asList("ID_HOTEL","HTL_NAME"));
        	when(daoHelper.update(any(), anyMap(),anyMap())).thenReturn(er);
        	when(daoHelper.query(any(), anyMap(),anyList())).thenReturn(queryResult);
        	EntityResult entityResult = hotelService.hotelUpdate(new HashMap<>(),new HashMap<>());
        	assertEquals(EntityResult.OPERATION_SUCCESSFUL, entityResult.getCode());
        	assertEquals("SUCESSFUL_UPDATE", entityResult.getMessage());
        	verify(daoHelper).update(any(), anyMap(),anyMap());
        	verify(daoHelper).query(any(), anyMap(),anyList());
        }
        
        @Test
        @DisplayName("Fail trying to update an hotel with an existing name or email")
        void hotel_fail_update_with_duplicated_hotel_name_or_email() {
        	EntityResult er = new EntityResultMapImpl();
        	er.setCode(EntityResult.OPERATION_WRONG);
        	er.setMessage("HOTEL_NAME_OR_EMAIL_ALREADY_EXISTS");
        	EntityResult queryResult = new EntityResultMapImpl(Arrays.asList("ID_HOTEL","HTL_NAME"));
            er.addRecord(new HashMap<String, Object>() {{put("ID_HOTEL", 2);put("HTL_NAME","FN Vigo");}});
            er.setCode(EntityResult.OPERATION_SUCCESSFUL);
            when(daoHelper.query(any(), anyMap(),anyList())).thenReturn(queryResult);
        	when(daoHelper.update(any(), anyMap(),anyMap())).thenThrow(DuplicateKeyException.class);
        	EntityResult entityResult = hotelService.hotelUpdate(new HashMap<>(),new HashMap<>());
        	assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
        	assertEquals("HOTEL_NAME_OR_EMAIL_ALREADY_EXISTS", entityResult.getMessage());
        	verify(daoHelper).update(any(), anyMap(),anyMap());
        	verify(daoHelper).query(any(), anyMap(),anyList());
        }
        @Test
        @DisplayName("Fail trying to update an hotel that doesn´t exists")
        void update_hotel_doesnt_exists() {
        	EntityResult er = new EntityResultMapImpl();
        	er.setCode(EntityResult.OPERATION_WRONG);
        	er.setMessage("HOTEL_DOESN'T_EXISTS");
        	EntityResult queryResult = new EntityResultMapImpl();
        	when(daoHelper.query(any(), anyMap(),anyList())).thenReturn(queryResult);
        	EntityResult entityResult = hotelService.hotelUpdate(new HashMap<>(),new HashMap<>());
        	assertEquals(EntityResult.OPERATION_WRONG, entityResult.getCode());
        	assertEquals("HOTEL_DOESN'T_EXISTS", entityResult.getMessage());
        	verify(daoHelper).query(any(), anyMap(),anyList());
        }
    }
}