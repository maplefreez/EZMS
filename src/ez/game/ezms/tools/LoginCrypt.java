package ez.game.ezms.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginCrypt {
    protected static final int extralength = 6;
    private static final String[] Alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static final String[] Number = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static String Generate_13DigitAsiasoftPassport() {
        StringBuilder sb = new StringBuilder();
        sb.append(Alphabet[RandomHelper.nextInt (Alphabet.length)]);
        for (int i = 0; i < 11; i++) {
            sb.append(Number[RandomHelper.nextInt(Number.length)]);
        }
        sb.append(Alphabet[RandomHelper.nextInt(Alphabet.length)]);
        return sb.toString();
    }

    private static String toSimpleHexString(byte[] bytes) {
        return HexTool.toString(bytes).replace(" ", "").toLowerCase();
    }

    private static String hashWithDigest(String in, String digest) {
        try {
            MessageDigest Digester = MessageDigest.getInstance(digest);
            Digester.update(in.getBytes("UTF-8"), 0, in.length());
            byte[] sha1Hash = Digester.digest();
            return toSimpleHexString(sha1Hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Hashing the password failed", ex);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding the string failed", e);
        }
    }

    /**
     * 目前使用这个函数进行密码加密，存在
     * 数据库中的密码都是密文。
     *
     * @param in
     * @return
     */
    public static String hexSha1(String in) {
        return hashWithDigest(in, "SHA-1");
    }

    public static String hexSha512(String in) {
        return hashWithDigest(in, "SHA-512");
    }

    public static boolean checkSha1Hash(String hash, String password) {
        return hash.equals(hexSha1(password));
    }

    public static boolean checkSaltedSha512Hash(String hash, String password, String salt) {
        return hash.equals(makeSaltedSha512Hash(password, salt));
    }

    public static String makeSaltedSha512Hash(String password, String salt) {
        return hexSha512(new StringBuilder().append(password).append(salt).toString());
    }

    public static String makeSalt() {
        byte[] salt = new byte[16];
//        RandomHelper.nextBytes (salt);
        return toSimpleHexString(salt);
    }

    public static String rand_s(String in) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(RandomHelper.nextBoolean() ? Alphabet[
                    RandomHelper.nextInt(Alphabet.length)] :
                    Number[RandomHelper.nextInt(Number.length)]);
        }
        return new StringBuilder().append(sb.toString()).append(in).toString();
    }

    public static String rand_r(String in) {
        return in.substring(6, 134);
    }

}
