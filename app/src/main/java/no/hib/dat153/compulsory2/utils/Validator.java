package no.hib.dat153.compulsory2.utils;

/**
 * Created by didrik on 27.01.16.
 */
public class Validator {

    public static boolean validName(String name) {
        return name != null &&
                name.matches("[a-zæøåA-ZÆØÅ]+([ '-][a-zæøåA-ZÆØÅ]+)*")
                && name.length() <= 25;
    }
}
