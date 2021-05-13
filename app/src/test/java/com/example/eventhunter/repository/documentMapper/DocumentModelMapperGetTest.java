package com.example.eventhunter.repository.documentMapper;

import com.example.eventhunter.repository.DocumentModelMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class DocumentModelMapperGetTest {

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


    static class SuperClassModel {
        private String superPrivate;
        protected String superProtected;
        public String superPublic;

        public SuperClassModel() {
        }

        public String getSuperPrivate() {
            return superPrivate;
        }

        public String getSuperProtected() {
            return superProtected;
        }

        public String getSuperPublic() {
            return superPublic;
        }
    }

    static class SubClassModel extends SuperClassModel {
        private String subPrivate;
        protected String subProtected;
        public String subPublic;

        public SubClassModel() {
        }

        public String getSubPrivate() {
            return subPrivate;
        }

        public String getSubProtected() {
            return subProtected;
        }

        public String getSubPublic() {
            return subPublic;
        }
    }


    @Test
    public void test_classAndSuperClassModel_ShouldBeCreatedFromMap() throws NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();

        String subPrivate = "SUBPRIVATE";
        String subProtected = "SUBPROTECTED";
        String subPublic = "SUBPUBLIC";

        String superPrivate = "SUPERPRIVATE";
        String superProtected = "SUPERPROTECTED";
        String superPublic = "SUPERPUBLIC";

        map.put("subPrivate", subPrivate);
        map.put("subProtected", subProtected);
        map.put("subPublic", subPublic);

        map.put("superPrivate", superPrivate);
        map.put("superProtected", superProtected);
        map.put("superPublic", superPublic);

        SubClassModel model = new DocumentModelMapper<>(SubClassModel.class).getModel(map);

        assertEquals(subPrivate, model.getSubPrivate());
        assertEquals(subProtected, model.getSubProtected());
        assertEquals(subPublic, model.getSubPublic());

        assertEquals(superPrivate, model.getSuperPrivate());
        assertEquals(superProtected, model.getSuperProtected());
        assertEquals(superPublic, model.getSuperPublic());
    }


    static class Top {
        private String top;

        public String getTop() {
            return top;
        }
    }

    static class Middle extends Top {
        private String middle;

        public String getMiddle() {
            return middle;
        }
    }

    static class Bottom extends Middle {
        private String bottom;

        public String getBottom() {
            return bottom;
        }
    }


    @Test
    public void test_ClassHierarchyModel_ShouldBeCreatedFromMap() throws NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();

        String top = "TOP";
        String middle = "MIDDLE";
        String bottom = "BOTTOM";

        map.put("top", top);
        map.put("middle", middle);
        map.put("bottom", bottom);

        Bottom bottomModel = new DocumentModelMapper<>(Bottom.class).getModel(map);
        assertEquals(top, bottomModel.getTop());
        assertEquals(middle, bottomModel.getMiddle());
        assertEquals(bottom, bottomModel.getBottom());

        Middle middleModel = new DocumentModelMapper<>(Middle.class).getModel(map);
        assertEquals(top, middleModel.getTop());
        assertEquals(middle, middleModel.getMiddle());


        Top topModel = new DocumentModelMapper<>(Top.class).getModel(map);
        assertEquals(top, topModel.getTop());
    }
}