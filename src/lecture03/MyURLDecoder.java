package lecture03;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MyURLDecoder {
    /**
     * 영문 소문자를 영문 대문자로 변경하는 패턴
     * - ASCII 코드 상, 영문 대문자는 0x41 부터, 소문자는 0x61 부터 시작함.
     * - 따라서 0xDF 와 AND연산(&)을 하면, 0x20을 떨궈서 소문자를 대문자로 치환 가능
     * @param b1
     * @param b2
     * @return
     */
    private static int hex2int(byte b1, byte b2) {
        int digit;
        if (b1 >= 'A') {
            digit = (b1 & 0xDF) - 'A' + 10;
        } else {
            digit = (b1 - '0');
        }
        digit *= 16;

        if (b2 >= 'A') {
            digit += (b2 & 0xDF) - 'A' + 10;
        } else {
            digit += (b2 - '0');
        }

        return digit;
    }

    public static String decode(String src, String enc) throws UnsupportedEncodingException {
        byte[] srcBytes = src.getBytes("ISO_8859_1");       // 인코딩된 문자열 src 를 바이트열로 변환. ISO_8859_1 설정해서 ASCII 코드로 변환
        byte[] destBytes = new byte[srcBytes.length];

        int destIdx = 0;
        for (int srcIdx = 0; srcIdx < srcBytes.length; srcIdx++) {
            // %23 과 같이 인코딩된 문자의 경우 '%' 뒤의 2개 문자를 16진수로 인식해서 int형으로 변환(hex2int)한다. 변환 후 인덱스는 2 증가.
            if (srcBytes[srcIdx] == (byte) '%') {
                destBytes[destIdx] = (byte) hex2int(srcBytes[srcIdx + 1], srcBytes[srcIdx + 2]);
                srcIdx += 2;
            } else {
                destBytes[destIdx] = srcBytes[srcIdx];
            }
            destIdx++;
        }

        byte[] destBytes2 = Arrays.copyOf(destBytes, destIdx);

        return new String(destBytes2, enc);
    }
}
