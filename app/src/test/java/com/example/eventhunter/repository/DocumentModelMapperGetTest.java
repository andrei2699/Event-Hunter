package com.example.eventhunter.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class DocumentModelMapperGetTest {

    static class DummyModel {
        private String name;
        private String lastName;
        private int age;
        private float favoriteNumber;

        public String getName() {
            return name;
        }

        public String getLastName() {
            return lastName;
        }

        public int getAge() {
            return age;
        }

        public float getFavoriteNumber() {
            return favoriteNumber;
        }
    }

    private DocumentModelMapper<DummyModel> documentModelMapper;

    @Before
    public void setUp() {
        documentModelMapper = new DocumentModelMapper<>(DummyModel.class);
    }

    @After
    public void tearDown() {
        documentModelMapper = null;
    }

    @Test
    public void test_emptyMap_ValuesShouldNotBeEmpty() throws NoSuchMethodException {
        DummyModel model = documentModelMapper.getModel(new HashMap<>());

        assertNull(model.getName());
        assertNull(model.getLastName());
        assertEquals(0, model.getAge());
        assertEquals(0.0, model.getFavoriteNumber(), 0.1);
    }

    @Test
    public void test_mapWithAllValues_ValuesShouldBePlacedInInstance() throws NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();
        String name = "My Name";
        String lastName = "LastName";
        int age = 30;
        float favouriteNumber = 2.4f;

        map.put("name", name);
        map.put("lastName", lastName);
        map.put("age", age);
        map.put("favoriteNumber", favouriteNumber);
        DummyModel model = documentModelMapper.getModel(map);

        assertEquals(name, model.getName());
        assertEquals(lastName, model.getLastName());
        assertEquals(age, model.getAge());
        assertEquals(favouriteNumber, model.getFavoriteNumber(), 0.1);
    }

    @Test
    public void test_mapWithAllWrongValues_ValuesShouldBeEmpty() throws NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();

        map.put("Name", "MyName");
        map.put("Lasnaame", "Last Name");
        map.put("Varsta", 12);
        map.put("Numar_Favorit", -51.2);
        DummyModel model = documentModelMapper.getModel(map);

        assertNull(model.getName());
        assertNull(model.getLastName());
        assertEquals(0, model.getAge());
        assertEquals(0.0, model.getFavoriteNumber(), 0.1);
    }

    @Test
    public void test_mapWithSomeValuesWrong_CorrectValuesShouldBePlacedInInstance() throws NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();
        String name = "My Name";
        String lastName = "LastName";
        int age = 30;
        float favouriteNumber = 2.4f;

        map.put("name", name);
        map.put("LastName", lastName);
        map.put("age", age);
        map.put("Favorite_Number1", favouriteNumber);
        DummyModel model = documentModelMapper.getModel(map);

        assertEquals(name, model.getName());
        assertNotEquals(lastName, model.getLastName());
        assertEquals(age, model.getAge());
        assertNotEquals(favouriteNumber, model.getFavoriteNumber(), 0.1);
    }

    @Test
    public void test_mapWithSomeValuesWrongAndOfWrongType_CorrectValuesShouldBePlacedInInstance() throws NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();

        String lastName = "LastName";

        map.put("name", 6);
        map.put("lastName", lastName);
        map.put("age", 6.1);
        map.put("favoriteNumber", "27.8");

        DummyModel model = documentModelMapper.getModel(map);

        assertNull(model.getName());
        assertEquals(lastName, model.getLastName());
        assertEquals(0, model.getAge());
        assertEquals(0.0, model.getFavoriteNumber(), 0.1);
    }
}