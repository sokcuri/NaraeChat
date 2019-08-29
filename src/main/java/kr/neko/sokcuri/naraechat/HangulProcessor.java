package kr.neko.sokcuri.naraechat;

import com.google.common.base.Splitter;

import java.util.List;

public class HangulProcessor {
    static String chosungTable = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";
    static String jungsungTable = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ";
    static String jongsungTable = "\u0000ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ";
    static List<String> jungsungCombiTable = Splitter.on(",").splitToList(",,,,,,,,,ㅗㅏ,ㅗㅐ,ㅗㅣ,,,ㅜㅓ,ㅜㅔ,ㅜㅣ,,,ㅡㅣ,ㅣ");
    static List<String> jongsungCombiTable = Splitter.on(",").splitToList(",,,ㄱㅅ,,ㄴㅈ,ㄴㅎ,,,ㄹㄱ,ㄹㅁ,ㄹㅂ,ㄹㅅ,ㄹㅌ,ㄹㅍ,ㄹㅎ,,,ㅂㅅ,,,,,,,,,");

    public static boolean isJaeum(char c) {
        return c >= 0x3131 && c <= 0x314E;
    }

    public static boolean isMoeum(char c) {
        return c >= 0x314F && c <= 0x3163;
    }

    public static boolean isChosung(char c) {
        return getChosung(c) != -1;
    }

    public static boolean isJungsung(char c) {
        return getJungsung(c) != -1;
    }

    public static boolean isJungsung(char p, char c) {
        return getJungsung(p, c) != -1;
    }

    public static boolean isJongsung(char c) {
        return getJongsung(c) != -1;
    }

    public static boolean isJongsung(char p, char c) {
        return getJongsung(p, c) != -1;
    }

    public static int getChosung(char c) {
        return chosungTable.indexOf(c);
    }

    public static int getJungsung(char c) {
        return jungsungTable.indexOf(c);
    }

    public static int getJungsung(char p, char c) {
        int jung = ((p - 0xAC00) % (21 * 28)) / 28;

        for (int i = 0; i < jungsungCombiTable.size(); i++) {
            char[] tbl = jungsungCombiTable.get(i).toCharArray();
            if (tbl.length == 2 && tbl[0] == jungsungTable.charAt(jung) && tbl[1] == c) {
                return i;
            }
        }
        return -1;
    }

    public static int getJongsung(char c) {
        return jongsungTable.indexOf(c);
    }

    public static int getJongsung(char p, char c) {
        int jong = ((p - 0xAC00) % (21 * 28)) % 28;

        for (int i = 0; i < jongsungCombiTable.size(); i++) {
            char[] tbl = jongsungCombiTable.get(i).toCharArray();
            if (tbl.length == 2 && tbl[0] == jongsungTable.charAt(jong) && tbl[1] == c) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isHangulSyllables(char c) {
        return c >= 0xAC00 && c <= 0xD7AF;
    }

    public static boolean isHangulCharacter(char c) {
        return isJaeum(c) || isMoeum(c) || isHangulSyllables(c);
    }

    public static char synthesizeHangulCharacter(int cho, int jung, int jong) {
        return (char)('가' + cho * 28 * 21 + jung * 28 + jong);
    }
}
