package no.hib.dat153.compulsory2.persistence;

/**
 * The person class represents the name of the person and an URI
 * string to an image located on the device.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class Person {

    private String name;
    private String uriString;

    /**
     * Constructor assigning values to the field variables
     * @param name Name of person
     * @param uriString URI string of where the image is located on the device
     */
    public Person(String name, String uriString) {
        this.name = name;
        this.uriString = uriString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUriString() { return uriString; }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    /**
     * Checks if a person has empty entries. "Empty" in this context
     * is defined as either or both of the field variables containing
     * a null reference.
     * @return False if either or both field variables contain a null
     * reference, true otherwise.
     */
    public boolean isNotEmpty() {
        return name != null && uriString != null;
    }

    /**
     * toString implementation of the person.
     * @return Name of the person.
     */
    @Override
    public String toString() {
        return name;
    }
}
