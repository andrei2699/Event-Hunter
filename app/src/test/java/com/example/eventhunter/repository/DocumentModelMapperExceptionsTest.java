package com.example.eventhunter.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class DocumentModelMapperExceptionsTest {

    static class NoNoArgModel {
        private final String value;

        public NoNoArgModel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private DocumentModelMapper<NoNoArgModel> documentModelMapper;
    private Map<String, Object> expectedMap;

    public DocumentModelMapperExceptionsTest(Map<String, Object> expectedMap) {
        this.expectedMap = expectedMap;
    }

    @Before
    public void setUp() {
        documentModelMapper = new DocumentModelMapper<>(NoNoArgModel.class);
    }

    @After
    public void tearDown() {
        documentModelMapper = null;
    }

    @Parameters
    public static Collection<Map<String, Object>> mapData() {
        Map<String, Object> oneValueMap = new HashMap<>();
        oneValueMap.put("test", "TEST");

        Map<String, Object> moreValuesMap = new HashMap<>();
        oneValueMap.put("test1", "TEST");
        oneValueMap.put("test2", 5);
        oneValueMap.put("test3", -61.2);

        Map<String, Object> valueMap = new HashMap<>();
        oneValueMap.put("value", "Value");

        return Arrays.asList(new HashMap<>(), oneValueMap, moreValuesMap, valueMap);
    }

    @Test(expected = NoSuchMethodException.class)
    public void test_noNoArgConstructor_ShouldThrowNoSuchMethodException() throws NoSuchMethodException {
        documentModelMapper.getModel(expectedMap);
    }
}