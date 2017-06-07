package pt.andre.projecto.Model.Utils;

import org.apache.commons.codec.digest.DigestUtils;

/*
* Class responsible to handle every security problem(may not be the best word to describe this class)
* */
public class Security {

    //Returns the sha256 value of the @param word
    public static String hashString(String word){
        return DigestUtils.sha256Hex(word);
    }

}
