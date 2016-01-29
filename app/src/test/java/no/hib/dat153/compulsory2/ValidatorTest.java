package no.hib.dat153.compulsory2;


import org.junit.Test;

import no.hib.dat153.compulsory2.utils.Validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorTest {

    @Test
    public void testValidNames() {
        String [] validNames = {
                "Didrik", "Viljar", "Ståle", "Ole-Johan Dahl",
                "Katrine", "Karoline", "Lise", "Gøril", "Hanne"
        };
        for(String name : validNames) {
            assertTrue(Validator.validName(name));
        }
    }

    @Test
    public void testInvalidNames() {
        String [] invalidNames = {
                " Hansen", null, "", "Dids ", "Dumbdash-", ":123",
                "Didrik3 Aubert", "St0le", "G ørild."
        };
        for(String name : invalidNames) {
            assertFalse(Validator.validName(name));
        }
    }
}
