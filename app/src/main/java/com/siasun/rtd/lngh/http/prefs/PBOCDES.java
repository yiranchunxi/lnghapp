package com.siasun.rtd.lngh.http.prefs;

import java.util.Arrays;

public class PBOCDES {
    private static final byte[] IP = new byte[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
    private static final byte[] FP = new byte[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
    private static final byte[] E = new byte[]{32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};
    private static final byte[][] S = new byte[][]{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}, {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}, {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}, {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}, {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}, {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}, {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}, {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};
    private static final byte[] P = new byte[]{16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};
    private static final byte[] PC1 = new byte[]{57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};
    private static final byte[] PC2 = new byte[]{14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};
    private static final byte[] rotations = new byte[]{1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private static final byte[] ZERO_IV = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
    public PBOCDES() {
    }

    private static long IP(long src) {
        return permute(IP, 64, src);
    }

    private static long FP(long src) {
        return permute(FP, 64, src);
    }

    private static long E(int src) {
        return permute(E, 32, (long)src & 4294967295L);
    }

    private static int P(int src) {
        return (int)permute(P, 32, (long)src & 4294967295L);
    }

    private static long PC1(long src) {
        return permute(PC1, 64, src);
    }

    private static long PC2(long src) {
        return permute(PC2, 56, src);
    }

    private static long permute(byte[] table, int srcWidth, long src) {
        long dst = 0L;

        for(int i = 0; i < table.length; ++i) {
            int srcPos = srcWidth - table[i];
            dst = dst << 1 | src >> srcPos & 1L;
        }

        return dst;
    }

    private static byte S(int boxNumber, byte src) {
        src = (byte)(src & 32 | (src & 1) << 4 | (src & 30) >> 1);
        return S[boxNumber - 1][src];
    }

    private static long getLongFromBytes(byte[] ba, int offset) {
        long l = 0L;

        for(int i = 0; i < 8; ++i) {
            byte value;
            if (offset + i < ba.length) {
                value = ba[offset + i];
            } else {
                value = 0;
            }

            l = l << 8 | (long)value & 255L;
        }

        return l;
    }

    private static void getBytesFromLong(byte[] ba, int offset, long l) {
        for(int i = 7; i > -1 && offset + i < ba.length; --i) {
            ba[offset + i] = (byte)((int)(l & 255L));
            l >>= 8;
        }

    }

    private static void get4BytesFromLong(byte[] ba4, long l) {
        l >>= 32;
        ba4[3] = (byte)((int)(l & 255L));
        l >>= 8;
        ba4[2] = (byte)((int)(l & 255L));
        l >>= 8;
        ba4[1] = (byte)((int)(l & 255L));
        l >>= 8;
        ba4[0] = (byte)((int)(l & 255L));
    }

    private static int feistel(int r, long subkey) {
        long e = E(r);
        long x = e ^ subkey;
        int dst = 0;

        for(int i = 0; i < 8; ++i) {
            dst >>>= 4;
            int s = S(8 - i, (byte)((int)(x & 63L)));
            dst |= s << 28;
            x >>= 6;
        }

        return P(dst);
    }

    private static long[] createSubkeys(long key) {
        long[] subkeys = new long[16];
        key = PC1(key);
        int c = (int)(key >> 28);
        int d = (int)(key & 268435455L);

        for(int i = 0; i < 16; ++i) {
            if (rotations[i] == 1) {
                c = c << 1 & 268435455 | c >> 27;
                d = d << 1 & 268435455 | d >> 27;
            } else {
                c = c << 2 & 268435455 | c >> 26;
                d = d << 2 & 268435455 | d >> 26;
            }

            long cd = ((long)c & 4294967295L) << 28 | (long)d & 4294967295L;
            subkeys[i] = PC2(cd);
        }

        return subkeys;
    }

    public static long encryptBlock(long m, long[] subkeys) {
        long ip = IP(m);
        int l = (int)(ip >> 32);
        int r = (int)(ip & 4294967295L);

        for(int i = 0; i < 16; ++i) {
            int previous_l = l;
            l = r;
            r = previous_l ^ feistel(r, subkeys[i]);
        }

        long rl = ((long)r & 4294967295L) << 32 | (long)l & 4294967295L;
        long fp = FP(rl);
        return fp;
    }

    public static long encryptBlock(long m, long key) {
        long[] subkeys = createSubkeys(key);
        return encryptBlock(m, subkeys);
    }

    public static void encryptBlock(byte[] message, int messageOffset, byte[] ciphertext, int ciphertextOffset, byte[] key) {
        long m = getLongFromBytes(message, messageOffset);
        long k = getLongFromBytes(key, 0);
        long c = encryptBlock(m, k);
        getBytesFromLong(ciphertext, ciphertextOffset, c);
    }

    public static byte[] encrypt(byte[] message, byte[] key) {
        byte[] ciphertext = new byte[message.length];

        for(int i = 0; i < message.length; i += 8) {
            encryptBlock(message, i, ciphertext, i, key);
        }

        return ciphertext;
    }

    public static byte[] encrypt(byte[] challenge, String password) {
        return encrypt(challenge, passwordToKey(password));
    }

    private static byte[] passwordToKey(String password) {
        byte[] pwbytes = password.getBytes();
        byte[] key = new byte[8];

        for(int i = 0; i < 8; ++i) {
            if (i >= pwbytes.length) {
                key[i] = 0;
            } else {
                byte b = pwbytes[i];
                byte b2 = 0;

                for(int j = 0; j < 8; ++j) {
                    b2 = (byte)(b2 << 1);
                    b2 = (byte)(b2 | b & 1);
                    b = (byte)(b >>> 1);
                }

                key[i] = b2;
            }
        }

        return key;
    }

    private static int charToNibble(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        } else if (c >= 'a' && c <= 'f') {
            return 10 + c - 97;
        } else {
            return c >= 'A' && c <= 'F' ? 10 + c - 65 : 0;
        }
    }

    public static byte[] parseBytes(String s) {
        return parseBytes(s, false);
    }

    public static byte[] parseBytes(String s, boolean lp0) {
        s = s.replace(" ", "");
        if (lp0 && s.length() % 2 != 0) {
            s = "0" + s;
        }

        byte[] ba = new byte[s.length() / 2];
        if (s.length() % 2 > 0) {
            s = s + '0';
        }

        for(int i = 0; i < s.length(); i += 2) {
            ba[i / 2] = (byte)(charToNibble(s.charAt(i)) << 4 | charToNibble(s.charAt(i + 1)));
        }

        return ba;
    }

    public static String hex(byte[] bytes) {
        return hex(bytes, bytes.length);
    }

    public static String hex(byte[] bytes, int len) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < len; ++i) {
            sb.append(String.format("%02X", bytes[i]));
        }

        return sb.toString();
    }

    public static String convertStringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();

        for(int i = 0; i < chars.length; ++i) {
            hex.append(Integer.toHexString(chars[i]));
        }

        return hex.toString();
    }

    public static byte[] encryptCBC(byte[] message, byte[] key, byte[] iv) {
        byte[] ciphertext = new byte[message.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = getLongFromBytes(iv, 0);
        return encryptCBC(message, 0, message.length, k, previousCipherBlock, ciphertext);
    }

    public static byte[] encryptCBC(byte[] message, int offset, int len, long k, long iv, byte[] cipherBuff) {
        long previousCipherBlock = iv;

        for(int i = offset; i < len; i += 8) {
            long messageBlock = getLongFromBytes(message, i);
            long cipherBlock = encryptBlock(messageBlock ^ previousCipherBlock, k);
            getBytesFromLong(cipherBuff, i, cipherBlock);
            previousCipherBlock = cipherBlock;
        }

        return cipherBuff;
    }

    public static long decryptBlock(long c, long[] subkeys) {
        long ip = IP(c);
        int l = (int)(ip >> 32);
        int r = (int)(ip & 4294967295L);

        for(int i = 15; i > -1; --i) {
            int previous_l = l;
            l = r;
            r = previous_l ^ feistel(r, subkeys[i]);
        }

        long rl = ((long)r & 4294967295L) << 32 | (long)l & 4294967295L;
        long fp = FP(rl);
        return fp;
    }

    public static long decryptBlock(long c, long key) {
        long[] subkeys = createSubkeys(key);
        return decryptBlock(c, subkeys);
    }

    public static void decryptBlock(byte[] ciphertext, int ciphertextOffset, byte[] message, int messageOffset, byte[] key) {
        long c = getLongFromBytes(ciphertext, ciphertextOffset);
        long k = getLongFromBytes(key, 0);
        long m = decryptBlock(c, k);
        getBytesFromLong(message, messageOffset, m);
    }

    public static byte[] decrypt(byte[] ciphertext, byte[] key) {
        byte[] message = new byte[ciphertext.length];

        for(int i = 0; i < ciphertext.length; i += 8) {
            decryptBlock(ciphertext, i, message, i, key);
        }

        return message;
    }

    public static byte[] decryptCBC(byte[] ciphertext, byte[] key, long iv) {
        byte[] message = new byte[ciphertext.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = iv;

        for(int i = 0; i < ciphertext.length; i += 8) {
            long cipherBlock = getLongFromBytes(ciphertext, i);
            long messageBlock = decryptBlock(cipherBlock, k);
            messageBlock ^= previousCipherBlock;
            getBytesFromLong(message, i, messageBlock);
            previousCipherBlock = cipherBlock;
        }

        return message;
    }

    public static long des_ede(long[] skL, long[] skR, long m, boolean enc) {
        long c = 0L;
        if (enc) {
            c = encryptBlock(m, skL);
            c = decryptBlock(c, skR);
            c = encryptBlock(c, skL);
            return c;
        } else {
            c = decryptBlock(m, skL);
            c = encryptBlock(c, skR);
            c = decryptBlock(c, skL);
            return c;
        }
    }

    public static long des_ede(long kL, long kR, long m, boolean enc) {
        long c = 0L;
        if (enc) {
            c = encryptBlock(m, kL);
            c = decryptBlock(c, kR);
            c = encryptBlock(c, kL);
            return c;
        } else {
            c = decryptBlock(m, kL);
            c = encryptBlock(c, kR);
            c = decryptBlock(c, kL);
            return c;
        }
    }

    public static String pboc3DESMACHex(String data, String key16) {
        byte[] m = parseBytes(data);
        byte[] k = parseBytes(key16);
        byte[] mac = new byte[4];
        pboc3DESMAC(k, m, mac);
        return hex(mac);
    }

    public static void pboc3DESMAC(byte[] mackey16, byte[] input, byte[] mac) {
        long kL = getLongFromBytes(mackey16, 0);
        long[] skL = createSubkeys(kL);
        long kR = getLongFromBytes(mackey16, 8);
        int pad_0_cnt = 8 - (input.length + 1) % 8;
        if (pad_0_cnt == 8) {
            pad_0_cnt = 0;
        }

        byte[] mdata = new byte[input.length + 1 + pad_0_cnt];
        Arrays.fill(mdata, (byte)0);
        System.arraycopy(input, 0, mdata, 0, input.length);
        mdata[input.length] = -128;
        long iv = 0L;
        long b = 0L;

        for(int i = 0; i < mdata.length; i += 8) {
            b = getLongFromBytes(mdata, i);
            iv ^= b;
            iv = encryptBlock(iv, skL);
        }

        iv = decryptBlock(iv, kR);
        iv = encryptBlock(iv, skL);
        get4BytesFromLong(mac, iv);
    }

    public static StringBuffer padding_ISO9796M2Hex(StringBuffer sb) {
        sb.append("80");
        int pad_count = 8 - sb.length() / 2 % 8;
        if (pad_count == 8) {
            pad_count = 0;
        }

        for(int i = 0; i < pad_count; ++i) {
            sb.append("00");
        }

        return sb;
    }

    public static byte[] padding_ISO9796M2(byte[] input, boolean appLD) {
        byte[] pinput;
        int pad_0_count;
        if (appLD) {
            pad_0_count = 8 - (2 + input.length) % 8;
            if (pad_0_count == 8) {
                pad_0_count = 0;
            }

            pinput = new byte[2 + input.length + pad_0_count];
            Arrays.fill(pinput, (byte)0);
            pinput[0] = (byte)input.length;
            System.arraycopy(input, 0, pinput, 1, input.length);
            pinput[input.length + 1] = -128;
        } else {
            pad_0_count = 8 - (1 + input.length) % 8;
            if (pad_0_count == 8) {
                pad_0_count = 0;
            }

            pinput = new byte[1 + input.length + pad_0_count];
            Arrays.fill(pinput, (byte)0);
            System.arraycopy(input, 0, pinput, 0, input.length);
            pinput[input.length] = -128;
        }

        return pinput;
    }

    public static byte[] padding_ISO9796M2Long(byte[] input, boolean appLD) {
        byte[] pinput;
        int pad_0_count;
        if (appLD) {
            pad_0_count = 8 - (3 + input.length) % 8;
            if (pad_0_count == 8) {
                pad_0_count = 0;
            }

            pinput = new byte[3 + input.length + pad_0_count];
            Arrays.fill(pinput, (byte)0);
            pinput[0] = (byte)(input.length >> 8);
            pinput[1] = (byte)(input.length & 255);
            System.arraycopy(input, 0, pinput, 2, input.length);
            pinput[input.length + 2] = -128;
        } else {
            pad_0_count = 8 - (1 + input.length) % 8;
            if (pad_0_count == 8) {
                pad_0_count = 0;
            }

            pinput = new byte[1 + input.length + pad_0_count];
            Arrays.fill(pinput, (byte)0);
            System.arraycopy(input, 0, pinput, 0, input.length);
            pinput[input.length] = -128;
        }

        return pinput;
    }

    public static byte[] pboc3DESEncrypt(byte[] key16, byte[] input, boolean appLD, boolean pad) {
        byte[] pinput;
        if (pad) {
            pinput = padding_ISO9796M2(input, appLD);
        } else {
            if (input.length % 8 != 0) {
                return null;
            }

            pinput = input;
        }

        byte[] output = new byte[pinput.length];
        long kL = getLongFromBytes(key16, 0);
        long kR = getLongFromBytes(key16, 8);
        long[] skL = createSubkeys(kL);
        long[] skR = createSubkeys(kR);
        long m = 0L;
        long c = 0L;

        for(int i = 0; i < pinput.length; i += 8) {
            m = getLongFromBytes(pinput, i);
            c = des_ede(skL, skR, m, true);
            getBytesFromLong(output, i, c);
        }

        return output;
    }

    public static byte[] pboc3DESEncryptForLong(byte[] key16, byte[] input, boolean appLD, boolean pad) {
        byte[] pinput;
        if (pad) {
            pinput = padding_ISO9796M2Long(input, appLD);
        } else {
            if (input.length % 8 != 0) {
                return null;
            }

            pinput = input;
        }

        byte[] output = new byte[pinput.length];
        long kL = getLongFromBytes(key16, 0);
        long kR = getLongFromBytes(key16, 8);
        long[] skL = createSubkeys(kL);
        long[] skR = createSubkeys(kR);
        long m = 0L;
        long c = 0L;

        for(int i = 0; i < pinput.length; i += 8) {
            m = getLongFromBytes(pinput, i);
            c = des_ede(skL, skR, m, true);
            getBytesFromLong(output, i, c);
        }

        return output;
    }

    public static String pboc3DESEncryptHex(String key16, String input, boolean appLD, boolean pad) {
        if (appLD && input.length() > 510) {
            return null;
        } else {
            byte[] k = parseBytes(key16);
            byte[] inp = parseBytes(input);
            return hex(pboc3DESEncrypt(k, inp, appLD, pad));
        }
    }

    public static String pboc3DESEncryptHex2(String key16, String input, boolean appLD, boolean pad) {
        if (appLD && input.length() > 1024) {
            return null;
        } else {
            byte[] k = parseBytes(key16);
            byte[] inp = parseBytes(input);
            return hex(pboc3DESEncryptForLong(k, inp, appLD, pad));
        }
    }

    public static byte[] pboc3DESDecrypt(byte[] key16, byte[] input) {
        long kL = getLongFromBytes(key16, 0);
        long kR = getLongFromBytes(key16, 8);
        long[] skL = createSubkeys(kL);
        long[] skR = createSubkeys(kR);
        byte[] output = new byte[input.length];
        long m = 0L;
        long c = 0L;

        for(int i = 0; i < input.length; i += 8) {
            m = getLongFromBytes(input, i);
            c = des_ede(skL, skR, m, false);
            getBytesFromLong(output, i, c);
        }

        return output;
    }

    public static String pboc3DESDecryptHex(String key16, String input) {
        byte[] k = parseBytes(key16);
        byte[] inp = parseBytes(input);
        return hex(pboc3DESDecrypt(k, inp));
    }

    public static byte[] pboc3DESDiversify(byte[] mk_key16, byte[] divisor) {
        long kL = getLongFromBytes(mk_key16, 0);
        long kR = getLongFromBytes(mk_key16, 8);
        long m = getLongFromBytes(divisor, 0);
        long cL = des_ede(kL, kR, m, true);
        long cR = des_ede(kL, kR, ~m, true);
        byte[] dk = new byte[mk_key16.length];
        getBytesFromLong(dk, 0, cL);
        getBytesFromLong(dk, 8, cR);
        return dk;
    }

    public static String pboc3DESDiversifyHex(String mk_key16, String divisor) {
        if (divisor.length() != 16) {
            return null;
        } else {
            byte[] divB = parseBytes(divisor);
            byte[] mk = parseBytes(mk_key16);
            return hex(pboc3DESDiversify(mk, divB));
        }
    }

    public static byte[] pboc3DESDiversifyTowLevel(byte[] mk_key16, byte[] divisor1, byte[] divisor2) {
        return pboc3DESDiversify(pboc3DESDiversify(mk_key16, divisor1), divisor2);
    }

    public static String pboc3DESDiversifyTowLevelHex(String mk_key16, String divisor1, String divisor2) {
        byte[] mk = parseBytes(mk_key16);
        byte[] div1 = parseBytes(divisor1);
        byte[] div2 = parseBytes(divisor2);
        return hex(pboc3DESDiversify(pboc3DESDiversify(mk, div1), div2));
    }

    public static String pboc3DESEncryptForLongWithString(String key16, String input) {
        return hex(pboc3DESEncryptForLong(parseBytes(key16), input.getBytes(), true, true));
    }

    public static String pboc3DESDecryptWhitString(String key16, String input) {
        byte[] deCode = pboc3DESDecrypt(parseBytes(key16), parseBytes(input));
        byte[] len = new byte[]{deCode[0], deCode[1]};
        int lenght = Integer.parseInt(StringUtils.bytesToHexString(len), 16);
        byte[] value = new byte[lenght];
        System.arraycopy(deCode, 2, value, 0, lenght);
        return new String(value);
    }

    public static String pboc3DESDecryptWithByte(byte[] key16, byte[] input) {
        byte[] deCode = pboc3DESDecrypt(key16, input);
        byte[] len = new byte[]{deCode[0], deCode[1]};
        int lenght = Integer.parseInt(StringUtils.bytesToHexString(len), 16);
        byte[] value = new byte[lenght];
        System.arraycopy(deCode, 2, value, 0, lenght);
        return new String(value);
    }

}
