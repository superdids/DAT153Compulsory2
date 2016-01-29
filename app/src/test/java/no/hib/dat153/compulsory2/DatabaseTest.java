package no.hib.dat153.compulsory2;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import junit.framework.TestResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.hib.dat153.compulsory2.persistence.ApplicationDatabase;
import no.hib.dat153.compulsory2.persistence.Person;
import no.hib.dat153.compulsory2.utils.ApplicationUtils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest  {

    //private Context context = mock(Context.class);

    @Mock
    private ApplicationDatabase myDB;

    //@Before
    //public void setup() {
    //    myDB = new ApplicationDatabase(context, null, null, 1);
    //}

    @After
    public void finish() {
        myDB.clearDB();
    }

    @Test
    public void testAddEntry() {
        myDB.addPerson(new Person("test", "test"));
        myDB.addPerson(new Person("test2", "test"));
        Person person = myDB.find("test");
        assertNull(person);
        assertTrue(myDB.fetchAll().size() == 0);
    }

    /*
    private ApplicationDatabase myDB;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        myDB = new ApplicationDatabase(context, null, null, 1);
    }

    @Override
    public void tearDown() throws Exception {
        myDB.close();
        super.tearDown();
    }*/

}