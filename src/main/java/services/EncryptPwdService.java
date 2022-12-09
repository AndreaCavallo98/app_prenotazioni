package services;

import org.apache.commons.codec.digest.DigestUtils;

public class EncryptPwdService {

    public static String encryptSHA2(String testoChiaro){
        String key = DigestUtils.sha256Hex(testoChiaro).toUpperCase();
        return key;
    }
    public static boolean checkSHA2(String password, String testoChiaro){
        if (password.equals(encryptSHA2(testoChiaro).toUpperCase()))
            return true;
        else
            return false;
    }

}


