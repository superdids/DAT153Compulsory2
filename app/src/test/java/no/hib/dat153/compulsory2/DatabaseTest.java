package no.hib.dat153.compulsory2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

    private static final String DIDRIK = "Didrik";
    private static final String ANDREA = "Andrea";
    private static final String HILDE = "Hilde";

    private Person didrik = new Person(DIDRIK, "Test");
    private Person andrea = new Person(ANDREA, "Test");
    private Person hilde = new Person(HILDE, "Test");
    private ApplicationDatabase myDB = mock(ApplicationDatabase.class);

    private Person [] persons = { didrik, andrea, hilde };
    private ArrayList<Person> personList = new ArrayList<>(Arrays.asList(persons));

    @Before
    public void setup() {
        //myDB = mock(ApplicationDatabase.class);
        when(myDB.fetchAll()).thenReturn(personList);
        /*
        when(myDB.find(DIDRIK)).thenReturn(didrik);
        when(myDB.find(ANDREA)).thenReturn(andrea);
        when(myDB.find(HILDE)).thenReturn(hilde);
        when(myDB.exists(DIDRIK)).thenReturn(true);
        when(myDB.exists(ANDREA)).thenReturn(true);
        when(myDB.exists(HILDE)).thenReturn(true);*/
    }

    @After
    public void teardown() {
        myDB.clearDB();
    }

    @Test
    public void testFind() {
        when(myDB.find(DIDRIK)).thenReturn(didrik);
        assertEquals(myDB.find(DIDRIK), didrik);
        assertEquals(myDB.find("Some random name that doesn't exist"), null);
    }

    @Test
    public void testExists() {
        when(myDB.exists(DIDRIK)).thenReturn(true);
        when(myDB.exists(ANDREA)).thenReturn(true);
        assertTrue(myDB.exists(DIDRIK));
        assertTrue(myDB.exists(ANDREA));
    }

    @Test
    public void testRemove() {

        when(myDB.exists(ANDREA)).thenReturn(true);
        assertTrue(myDB.exists(ANDREA));
        myDB.deletePerson(ANDREA);
        //assertFalse(myDB.exists(ANDREA));
        assertTrue(myDB.fetchAll().size() == 3);
    }

    @Test
    public void testFetchAll() {
        assertEquals(myDB.fetchAll(), personList);
        assertEquals(myDB.fetchAll().size(), 3);
    }

    @Test
    public void testFetchAllAfterRemoval() {
        assertEquals(myDB.fetchAll().size(), 3);
        myDB.deletePerson(HILDE);
        //assertFalse(myDB.exists(HILDE));
        //assertEquals(myDB.fetchAll().size(), 2);
    }

    @Test
    public void testRemoveAllEntries() {
        assertEquals(myDB.fetchAll().size(), 3);
        myDB.clearDB();
      //  assertEquals(myDB.fetchAll().size(), 0);
    }
}