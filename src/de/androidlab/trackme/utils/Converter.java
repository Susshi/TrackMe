package de.androidlab.trackme.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.telephony.PhoneNumberUtils;

public class Converter {
    
    private static String[] countryCodes = new String[]
        {
            "0", "1", "7", "20", "27", "30", "31", "32", "33", "34", "36", "39", 
            "39", "40", "41", "43", "44", "45", "46", "47", "48", "49", "51", "53", 
            "54", "55", "56", "56", "57", "58", "60", "61", "61", "62", "63", "64", 
            "65", "66", "81", "82", "84", "86", "90", "91", "92", "93", "94", "95", 
            "98", "212", "213", "216", "218", "220", "221", "222", "223", "224", 
            "225", "226", "227", "228", "229", "230", "231", "232", "233", "234", 
            "235", "236", "237", "238", "239", "239", "240", "241", "242", "242", 
            "243", "244", "245", "246", "246", "247", "248", "249", "250", "251", 
            "252", "253", "254", "255", "255", "256", "257", "258", "260", "261", 
            "262", "263", "264", "264", "265", "266", "267", "268", "268", "268", 
            "269", "269", "284", "290", "291", "297", "298", "299", "340", "345", 
            "350", "351", "352", "353", "354", "355", "356", "357", "358", "359", 
            "370", "371", "372", "373", "374", "375", "376", "377", "378", "380", 
            "381", "381", "385", "386", "387", "389", "420", "421", "423", "441", 
            "473", "500", "501", "502", "503", "504", "505", "506", "507", "508", 
            "509", "521", "522", "523", "524", "525", "526", "527", "528", "590", 
            "591", "592", "593", "594", "595", "596", "596", "597", "598", "599", 
            "599", "649", "664", "670", "670", "671", "672", "672", "672", "673", 
            "674", "675", "676", "677", "678", "679", "680", "681", "682", "683", 
            "684", "685", "686", "687", "688", "689", "690", "691", "692", "758", 
            "767", "784", "787", "939", "808", "808", "809", "809", "850", "852", 
            "853", "855", "856", "868", "869", "869", "870", "871", "872", "873", 
            "874", "876", "878", "880", "881", "886", "960", "961", "962", "963", 
            "964", "965", "966", "967", "968", "970", "971", "972", "973", "974", 
            "975", "976", "977", "992", "993", "994", "995", "996", "998", "5399", 
            "8816", "8817", "88216"
        };
    
    public static String normalizeNumber(String number) {
        String normalizedNumber = number;
        if (number.startsWith("+")) {
            normalizedNumber = normalizedNumber.substring(1);
            normalizedNumber = PhoneNumberUtils.stripSeparators(normalizedNumber);
            for (String code : countryCodes) {
                if (normalizedNumber.startsWith(code) == true) {
                    normalizedNumber = "0" + normalizedNumber.substring(code.length());
                    break;
                }
            }
        }
        return normalizedNumber;
    }
    
    public static String calculateHash(String source) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.update(source.getBytes());
            byte[] bytes = digest.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }       
            return sb.toString(); 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
