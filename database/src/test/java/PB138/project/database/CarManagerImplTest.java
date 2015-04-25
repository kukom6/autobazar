package PB138.project.database;

import org.exist.xmldb.XQueryService;
import org.junit.*;
import org.junit.Test;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CarManagerImplTest {

    private CarManagerImpl manager;

    private Collection collection;

    private static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    private static final String PREFIX = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";
    @Before
    public void setUp() throws Exception {
        try {
            Class c = Class.forName(DRIVER);
            Database database = (Database) c.newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);
            Collection parent = DatabaseManager.getCollection(PREFIX,"admin","test123");
            CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
            mgt.createCollection("cars");
            parent.close();
            collection = DatabaseManager.getCollection(PREFIX + "cars", "admin", "test123");

            XMLResource resource = (XMLResource)collection.createResource("cars.xml", "XMLResource");
            resource.setContent("<cars></cars>");
            collection.storeResource(resource);

            resource = (XMLResource)collection.createResource("data.xml", "XMLResource");
            resource.setContent("<data><car-next-id>1</car-next-id></data>");
            collection.storeResource(resource);


        } catch (XMLDBException e) {
            System.err.println("XML:DB Exception occurred " + e.errorCode + " " + e.getMessage());
        }

        manager = new CarManagerImpl(collection);
    }

    @After
    public void tearDown() throws Exception {
        //Collection parent = DatabaseManager.getCollection(PREFIX,"admin","test123");
        //CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");

        //mgt.removeCollection(PREFIX + "cars");
       // parent.close();
        collection.close();
    }

    @Test
    public void testCreateCar() throws Exception {
        Car testCar = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");

        manager.createCar(testCar);
        Long carId = testCar.getId();

        Car result = manager.getCarById(carId);

        assertEquals(testCar, result);
        assertNotSame(testCar, result);
        assertDeepEquals(testCar, result);
   }
/*
    @Test
    public void testGetCarById() throws Exception {

    }

    @Test
    public void testGetAllCars() throws Exception {

    }

    @Test
    public void testGetCarsByManufacturer() throws Exception {

    }

    @Test
    public void testGetCarsByColor() throws Exception {

    }

    @Test
    public void testGetCarsByKmLessThan() throws Exception {

    }

    @Test
    public void testGetCarsByKmMoreThan() throws Exception {

    }

    @Test
    public void testGetCarsByKm() throws Exception {

    }

    @Test
    public void testUpdateCar() throws Exception {

    }

    @Test
    public void testDeleteCar() throws Exception {

    }*/

    private void assertDeepEquals(Car expected, Car actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getManufacturer(), actual.getManufacturer());
        assertEquals(expected.getKm(), actual.getKm());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getColor(), actual.getColor());
        assertEquals(expected.getDescription(), actual.getDescription());
    }


}