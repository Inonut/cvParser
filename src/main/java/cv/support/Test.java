package cv.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Dragos on 12/20/2016.
 */
public class Test {

    public static final Pattern DIACRITICS_AND_FRIENDS
            = Pattern.compile("[\\p{javaISOControl}]+");


    private static String stripDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }

    public static void main(String[] args) throws IOException {
        //System.out.println(stripDiacritics("03/2002–07/2002"));
//        System.out.println(Encoder.encode("03/2002–07/2002".getBytes()));

        final Charset windowsCharset = Charset.forName("windows-1252");
        final Charset utfCharset = Charset.forName("UTF-8");
        final CharBuffer windowsEncoded = windowsCharset.decode(ByteBuffer.wrap(new byte[] {(byte) 0x91}));
        final byte[] utfEncoded = utfCharset.encode(windowsEncoded).array();
        System.out.println(new String(utfEncoded, utfCharset.displayName()));
    }

}

class Encoder {
    static Map<Integer, Integer> mapTiny = new HashMap<Integer, Integer>() {
        public Integer get(Object key) {
            Integer code = super.get(key);
            if (code == null)
                code = (Integer) key;
            return code;
        }
    };

    static {
        mapTiny.put(128,8364);
        mapTiny.put(130,8218);
        mapTiny.put(131,402);
        mapTiny.put(132,8222);
        mapTiny.put(133,8230);
        mapTiny.put(134,8224);
        mapTiny.put(135,8225);
        mapTiny.put(136,710);
        mapTiny.put(137,8240);
        mapTiny.put(138,352);
        mapTiny.put(139,8249);
        mapTiny.put(140,338);
        mapTiny.put(142,381);
        mapTiny.put(145,8216);
        mapTiny.put(146,8217);
        mapTiny.put(147,8220);
        mapTiny.put(148,8221);
        mapTiny.put(149,8226);
        mapTiny.put(150,8211);
        mapTiny.put(151,8212);
        mapTiny.put(152,732);
        mapTiny.put(153,8482);
        mapTiny.put(154,353);
        mapTiny.put(155,8250);
        mapTiny.put(156,339);
        mapTiny.put(158,382);
        mapTiny.put(159,376);
    }


    public static String encode(byte[] b) throws IOException {

        ByteArrayInputStream in = new ByteArrayInputStream(b);
        ByteArrayOutputStream convFileByteArray = new ByteArrayOutputStream();
        int i = in.read();
        while (i != -1) {
            convFileByteArray.write(new byte[] { (byte) (mapTiny.get(i) & 0xff), (byte) ((mapTiny.get(i) >> 8) & 0xff) });
            i = in.read();
        }
        return Base64.getEncoder().encodeToString(convFileByteArray.toByteArray());
    }

}
