package henesys.connection.crypto;

/**
 * Created on 15/12/2023
 * @author Desc
 *
 * This encryption is used for all versions before v149.2 in GMS.
 */
public class ShandaCrypto {

    /**
     * Encrypts the data using the Shanda encryption method.
     *  @param data The data to encrypt.
     *  @return The encrypted data.
     * */
    public static byte[] encryptData(byte[] data) {
        for (int j = 0; j < 6; j++) {
            byte remember = 0;
            byte dataLength = (byte) (data.length & 0xFF);
            if (j % 2 == 0) {
                for (int i = 0; i < data.length; i++) {
                    byte cur = data[i];
                    cur = BitTools.rollLeft(cur, 3);
                    cur += dataLength;
                    cur ^= remember;
                    remember = cur;
                    cur = BitTools.rollRight(cur, (int) dataLength & 0xFF);
                    cur = ((byte) ((~cur) & 0xFF));
                    cur += 0x48;
                    dataLength--;
                    data[i] = cur;
                }
            } else {
                for (int i = data.length - 1; i >= 0; i--) {
                    byte cur = data[i];
                    cur = BitTools.rollLeft(cur, 4);
                    cur += dataLength;
                    cur ^= remember;
                    remember = cur;
                    cur ^= 0x13;
                    cur = BitTools.rollRight(cur, 3);
                    dataLength--;
                    data[i] = cur;
                }
            }
        }
        return data;
    }

    /**
     * Decrypts the data using the Shanda encryption method.
     *  @param data The data to decrypt.
     *  @return The decrypted data.
     * */
    public static byte[] decryptData(byte[] data) {
        for (int j = 1; j <= 6; j++) {
            byte remember = 0;
            byte dataLength = (byte) (data.length & 0xFF);
            byte nextRemember;
            if (j % 2 == 0) {
                for (int i = 0; i < data.length; i++) {
                    byte cur = data[i];
                    cur -= 0x48;
                    cur = ((byte) ((~cur) & 0xFF));
                    cur = BitTools.rollLeft(cur, (int) dataLength & 0xFF);
                    nextRemember = cur;
                    cur ^= remember;
                    remember = nextRemember;
                    cur -= dataLength;
                    cur = BitTools.rollRight(cur, 3);
                    data[i] = cur;
                    dataLength--;
                }
            } else {
                for (int i = data.length - 1; i >= 0; i--) {
                    byte cur = data[i];
                    cur = BitTools.rollLeft(cur, 3);
                    cur ^= 0x13;
                    nextRemember = cur;
                    cur ^= remember;
                    remember = nextRemember;
                    cur -= dataLength;
                    cur = BitTools.rollRight(cur, 4);
                    data[i] = cur;
                    dataLength--;
                }
            }
        }
        return data;
    }

}
