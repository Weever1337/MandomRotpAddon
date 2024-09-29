package uk.meow.weever.rotp_mandom.util;

@Deprecated
public class TextFormatter {
    public static String wordCorrect(int number, String p1, String p2, String p3) {
        String numberStr = String.valueOf(number);
        String ld = numberStr.length() >= 2 ? numberStr.substring(numberStr.length() - 2) : numberStr;

        String caseKey;
        if (ld.length() > 1 && ld.charAt(0) == '1') {
            caseKey = "p3";
        } else {
            char lastDigit = ld.charAt(ld.length() - 1);
            switch (lastDigit) {
                case '1':
                    caseKey = "p1";
                    break;
                case '2':
                case '3':
                case '4':
                    caseKey = "p2";
                    break;
                default:
                    caseKey = "p3";
                    break;
            }
        }

        switch (caseKey) {
            case "p1":
                return p1;
            case "p2":
                return p2;
            default:
                return p3;
        }
    }
}
