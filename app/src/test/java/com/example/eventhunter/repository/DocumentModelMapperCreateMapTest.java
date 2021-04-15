package com.example.eventhunter.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DocumentModelMapperCreateMapTest {

    static class TestModel {
        private Double height;
        private String nickName;
        private boolean isValidBoolean;
        private long time;

        public TestModel() {
        }

        public TestModel(Double height, String nickName, boolean isValidBoolean, long time) {
            this.height = height;
            this.nickName = nickName;
            this.isValidBoolean = isValidBoolean;
            this.time = time;
        }

        public Double getHeight() {
            return height;
        }

        public String getNickName() {
            return nickName;
        }

        public boolean isValidBoolean() {
            return isValidBoolean;
        }

        public long getTime() {
            return time;
        }
    }

    static class TransientTestModel {
        private String stringValue;
        private short shortValue;
        private transient String transientValue;
        private transient int transientIntValue;

        public TransientTestModel() {
        }

        public TransientTestModel(String stringValue, short shortValue, String transientValue, int transientIntValue) {
            this.stringValue = stringValue;
            this.shortValue = shortValue;
            this.transientValue = transientValue;
            this.transientIntValue = transientIntValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public short getShortValue() {
            return shortValue;
        }

        public String getTransientValue() {
            return transientValue;
        }

        public int getTransientIntValue() {
            return transientIntValue;
        }
    }

    private DocumentModelMapper<TestModel> documentModelMapper;

    @Before
    public void setUp() {
        documentModelMapper = new DocumentModelMapper<>(TestModel.class);
    }

    @After
    public void tearDown() {
        documentModelMapper = null;
    }


    @Test
    public void test_mapWithAllCorrectValues_CorrectValuesShouldBeInInstance() throws NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();

        double height = 180.2;
        long time = 61561325;
        String nickname = "My Awesome Name";

        map.put("height", height);
        map.put("nickName", nickname);
        map.put("isValidBoolean", true);
        map.put("time", time);
        TestModel model = documentModelMapper.getModel(map);

        assertEquals(height, model.getHeight(), 0.1);
        assertEquals(nickname, model.getNickName());
        assertTrue(model.isValidBoolean());
        assertEquals(time, model.getTime());
    }

    @Test
    public void test_createdMapShouldHaveAllFieldsPresent() {

        double height = 176.2;
        String nickname = "Name";
        long time = 10005;

        Map<String, Object> map = documentModelMapper.createMap(new TestModel(height, nickname, true, time));

        assertEquals(4, map.size());

        Boolean isValid = (Boolean) map.get("isValidBoolean");
        Double heightActual = (Double) map.get("height");

        long timeActual = (long) map.get("time");

        if (isValid == null || heightActual == null) {
            fail();
        }


        assertEquals(nickname, map.get("nickName"));
        assertEquals(height, heightActual, 0.1);
        assertTrue(isValid);
        assertEquals(time, timeActual);
    }

    @Test
    public void test_createdMapShouldHaveAllNonTransientFieldsPresent() {

        DocumentModelMapper<TransientTestModel> transientTestModelDocumentModelMapper = new DocumentModelMapper<>(TransientTestModel.class);

        String stringValue = "Abracadabra";
        short shortValue = 15;

        Map<String, Object> map = transientTestModelDocumentModelMapper.createMap(new TransientTestModel(stringValue, shortValue, "Transient", -66));

        assertEquals(2, map.size());

        assertFalse(map.containsKey("transientValue"));
        assertFalse(map.containsKey("transientIntValue"));

        short actualShortValue = (short) map.get("shortValue");

        assertEquals(stringValue, map.get("stringValue"));
        assertEquals(shortValue, actualShortValue);
    }
}