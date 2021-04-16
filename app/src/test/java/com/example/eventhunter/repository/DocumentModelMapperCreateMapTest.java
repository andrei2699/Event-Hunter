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


    static class SuperClassModel {
        private String superPrivate;
        protected String superProtected;
        public String superPublic;

        public SuperClassModel() {
        }

        public SuperClassModel(String superPrivate, String superProtected, String superPublic) {
            this.superPrivate = superPrivate;
            this.superProtected = superProtected;
            this.superPublic = superPublic;
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
        private int subPrivate;
        protected long subProtected;
        public boolean subPublic;

        public SubClassModel() {
        }

        public SubClassModel(int subPrivate, long subProtected, boolean subPublic, String superPrivate, String superProtected, String superPublic) {
            super(superPrivate, superProtected, superPublic);
            this.subPrivate = subPrivate;
            this.subProtected = subProtected;
            this.subPublic = subPublic;
        }

        public int getSubPrivate() {
            return subPrivate;
        }

        public long getSubProtected() {
            return subProtected;
        }

        public boolean isSubPublic() {
            return subPublic;
        }
    }


    @Test
    public void test_ClassAndSuperClassModel_ShouldCreateMap() {
        DocumentModelMapper<SubClassModel> subClassModelDocumentModelMapper = new DocumentModelMapper<>(SubClassModel.class);

        int subPrivate = -1002;
        long subProtected = 6155161;
        boolean subPublic = false;

        String superPrivate = "SUPERPRIVATE";
        String superProtected = "SUPERPROTECTED";
        String superPublic = "SUPERPUBLIC";

        Map<String, Object> map = subClassModelDocumentModelMapper.createMap(new SubClassModel(subPrivate, subProtected, subPublic, superPrivate, superProtected, superPublic));

        assertEquals(6, map.size());

        int actualSubPrivate = (int) map.get("subPrivate");
        long actualSubProtected = (long) map.get("subProtected");
        boolean actualSubPublic = (boolean) map.get("subPublic");

        assertEquals(subPrivate, actualSubPrivate);
        assertEquals(subProtected, actualSubProtected);
        assertEquals(subPublic, actualSubPublic);

        assertEquals(superPrivate, map.get("superPrivate"));
        assertEquals(superProtected, map.get("superProtected"));
        assertEquals(superPublic, map.get("superPublic"));
    }


    static class Top {
        private String top;

        public Top() {
        }

        public Top(String top) {
            this.top = top;
        }

        public String getTop() {
            return top;
        }
    }

    static class Middle extends Top {
        private boolean middle;

        public Middle() {
        }

        public Middle(boolean middle, String top) {
            super(top);
            this.middle = middle;
        }

        public boolean isMiddle() {
            return middle;
        }
    }

    static class Bottom extends Middle {
        private int bottom;

        public Bottom() {
        }

        public Bottom(int bottom, boolean middle, String top) {
            super(middle, top);
            this.bottom = bottom;
        }

        public int getBottom() {
            return bottom;
        }
    }


    @Test
    public void test_classHierarchyModelShouldCreateCorrectMap() {

        String top = "TOP";
        int bottom = 5151;

        Map<String, Object> map = new DocumentModelMapper<>(Bottom.class).createMap(new Bottom(bottom, true, top));

        assertEquals(3, map.size());

        int actualBottom = (int) map.get("bottom");
        boolean actualMiddle = (boolean) map.get("middle");

        assertEquals(bottom, actualBottom);
        assertTrue(actualMiddle);
        assertEquals(top, map.get("top"));

        map = new DocumentModelMapper<>(Middle.class).createMap(new Middle(true, top));

        assertEquals(2, map.size());

        actualMiddle = (boolean) map.get("middle");

        assertTrue(actualMiddle);
        assertEquals(top, map.get("top"));

        map = new DocumentModelMapper<>(Top.class).createMap(new Top(top));

        assertEquals(1, map.size());

        assertEquals(top, map.get("top"));
    }

    @Test
    public void test_createdMapShouldPlaceCorrectValuesInAHierarchy() {

    }
}