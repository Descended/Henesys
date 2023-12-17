package henesys.connection.crypto;

/**
 * Created on 15/12/2023
 * @author Desc
 *
 * This encryption is used for all versions before v149.2 in GMS.
 */
public class ShandaCrypto {

    private static final byte ROLL_LEFT_ENCRYPT = 3;
    private static final byte ROLL_RIGHT_ENCRYPT = 4;
    private static final byte ROLL_LEFT_DECRYPT = 4;
    private static final byte ROLL_RIGHT_DECRYPT = 3;
    private static final byte ADDITION_CONSTANT = 0x48;
    private static final byte XOR_CONSTANT = 0x13;

    /**
     * Encrypts the data using the Shanda encryption method.
     *  @param data The data to encrypt.
     *  @return The encrypted data.
     * */
    public static byte[] encrypt(byte[] data) {
        for (int j = 0; j < 6; j++) {
            if (j % 2 == 0) {
                data = transformData(data, ROLL_LEFT_ENCRYPT, ADDITION_CONSTANT, true);
            } else {
                data = transformData(data, ROLL_RIGHT_ENCRYPT, XOR_CONSTANT, false);
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
            if (j % 2 == 0) {
                data = transformData(data, ROLL_RIGHT_DECRYPT, ADDITION_CONSTANT, true);
            } else {
                data = transformData(data, ROLL_LEFT_DECRYPT, XOR_CONSTANT, false);
            }
        }
        return data;
    }

    /**
     * Transforms the data using the Shanda encryption method.
     *  @param data The data to transform.
     *  @param rollValue The value to roll the data.
     *  @param constant The constant to use.
     *  @param isEncrypt Whether the data is being encrypted.
     *  @return The transformed data.
     * */
    private static byte[] transformData(byte[] data, int rollValue, byte constant, boolean isEncrypt) {
        byte remember = 0;
        byte dataLength = (byte) (data.length & 0xFF);
        int start = isEncrypt ? 0 : data.length - 1;
        int end = isEncrypt ? data.length : -1;
        int step = isEncrypt ? 1 : -1;

        for (int i = start; isEncrypt ? i < end : i > end; i += step) {
            byte current = data[i];
            current = BitTools.rollLeft(current, rollValue);
            current += isEncrypt ? dataLength : constant;
            current ^= remember;
            remember = current;
            current ^= isEncrypt ? constant : dataLength;
            current = BitTools.rollRight(current, rollValue);
            data[i] = current;
            dataLength--;
        }
        return data;
    }
}
