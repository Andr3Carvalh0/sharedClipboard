package pt.andre.projecto.Model.Utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Security {

    public static String hashString(String word){
        return DigestUtils.sha256Hex(word);
    }

}
