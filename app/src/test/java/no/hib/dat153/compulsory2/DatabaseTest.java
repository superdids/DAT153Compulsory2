package no.hib.dat153.compulsory2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;

import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

    private static final String STAALE = "Ståle";
    private static final String DIDRIK = "Didrik";
    private static final String ANDREA = "Andrea";
    private static final String HILDE = "Hilde";

    private Person staale = new Person(STAALE, "Test");
    private Person didrik = new Person(DIDRIK, "Test");
    private Person andrea = new Person(ANDREA, "Test");
    private Person hilde = new Person(HILDE, "Test");

    //private ApplicationDatabase myDB; // = mock(ApplicationDatabase.class);

    @Mock
    private ApplicationDatabase myDB;

    private Person [] persons = { staale, didrik, andrea, hilde };
    private ArrayList<Person> personList = new ArrayList<>(Arrays.asList(persons));

    @Before
    public void setup() {
       // myDB = mock(ApplicationDatabase.class);
        when(myDB.fetchAll()).thenReturn(personList);
       /* when(myDB.find(STAALE)).thenReturn(staale);
        when(myDB.find(DIDRIK)).thenReturn(didrik);
        when(myDB.find(ANDREA)).thenReturn(andrea);
        when(myDB.find(HILDE)).thenReturn(hilde);

        when(myDB.exists(STAALE)).thenReturn(true);
        when(myDB.exists(DIDRIK)).thenReturn(true);
        when(myDB.exists(ANDREA)).thenReturn(true);
        when(myDB.exists(HILDE)).thenReturn(true);*/
    }


    @Test
    public void testFind() {
        assertEquals(myDB.find(STAALE), staale);
        assertEquals(myDB.find(DIDRIK), didrik);
        assertEquals(myDB.find(ANDREA), andrea);
        assertEquals(myDB.find(HILDE), hilde);
        assertEquals(myDB.find("Some random name that doesn't exist"), null);
    }

    @Test
    public void testExists() {
        assertTrue(myDB.exists(STAALE));
        assertTrue(myDB.exists(DIDRIK));
        assertTrue(myDB.exists(ANDREA));
        assertTrue(myDB.exists(HILDE));
    }

    @Test
    public void testRemove() {

        //when(myDB.exists(ANDREA)).thenReturn(true);
        assertTrue(myDB.exists(ANDREA));
        myDB.deletePerson(ANDREA);
        assertEquals(myDB.find(ANDREA), null);
        //when(myDB.exists(ANDREA)).thenReturn(false);
        //assertFalse(myDB.exists(ANDREA));
        assertTrue(myDB.fetchAll().size() == 3);
    }

    @Test
    public void testFetchAll() {
        assertEquals(myDB.fetchAll(), personList);
        assertEquals(myDB.fetchAll().size(), 4);
    }

    @Test
    public void testFetchAllAfterRemoval() {
        when(myDB.fetchAll()).thenReturn(personList);
        assertEquals(myDB.fetchAll().size(), 4);
        doAnswer(new Answer<ArrayList<Person>>() {
            @Override
            public ArrayList<Person> answer(InvocationOnMock invocation) throws Throwable {
                ArrayList<Person> tmp = personList;
                int index = -1;
                for(int x = 0; x < tmp.size(); x++) {
                    if(tmp.get(x).getName().equals(HILDE)) { index = x; break; }
                }
                if(index != -1) tmp.remove(index);
                return tmp;
            }
        }).when(myDB).deletePerson(HILDE);
        //myDB.deletePerson(HILDE);
        //assertFalse(myDB.exists(HILDE));
        assertEquals(myDB.fetchAll().size(), 3);
    }

    @Test
    public void testRemoveAllEntries() {
        //when(myDB.fetchAll()).thenReturn(personList);
        //assertEquals(myDB.fetchAll().size(), 3);
         /*doAnswer(new Answer<ArrayList<Person>>() {
             @Override
             public ArrayList<Person> answer(InvocationOnMock invocation) throws Throwable {
                 return new ArrayList<>();
             }
         }).when(myDB).clearDB();*/
        myDB.clearDB();
        assertEquals(myDB.fetchAll().size(), 4);
    }
}