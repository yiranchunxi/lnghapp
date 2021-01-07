package com.siasun.rtd.lngh.http.prefs;

public class StringUtils {
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public StringUtils() {
    }

    public static String bytesToString(byte[] bytes) {
        return bytesToString(bytes, 0, bytes.length);
    }

    public static String bytesToString(byte[] bytes, int len) {
        return bytesToString(bytes, 0, len);
    }

    public static String bytesToString(byte[] bytes, int pos, int len) {
        return new String(bytes, pos, len);
    }

    public static String bytesToHexString(byte[] bytes, String separator) {
        StringBuilder sb = new StringBuilder();
        byte[] var3 = bytes;
        int var4 = bytes.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            sb.append(String.format("%02X" + separator, b & 255));
        }

        return sb.toString();
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src != null && src.length > 0) {
            for(int i = 0; i < src.length; ++i) {
                int v = src[i] & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }

                stringBuilder.append(hv);
            }

            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    public static String bytesToHexString(byte[] d, int s, int n) {
        char[] ret = new char[n * 2];
        int e = s + n;
        int x = 0;

        for(int i = s; i < e; ++i) {
            byte v = d[i];
            ret[x++] = HEX[15 & v >> 4];
            ret[x++] = HEX[15 & v];
        }

        return new String(ret);
    }

    public static short bytesToShort(byte[] bytes) {
        return (short)(((bytes[0] & 255) << 8) + (bytes[1] & 255));
    }

    public static byte[] shortToBytes(short value) {
        return new byte[]{(byte)(value >>> 8), (byte)value};
    }

    public static int bytesToInt(byte[] bytes) {
        int ret = 0;
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            ret <<= 8;
            ret |= b & 255;
        }

        return ret;
    }

    public static int bytesToInt(byte[] bytes, int s, int n) {
        int ret = 0;
        int e = s + n;

        for(int i = s; i < e; ++i) {
            ret <<= 8;
            ret |= bytes[i] & 255;
        }

        return ret;
    }

    public static byte[] intToBytes(int n) {
        return new byte[]{(byte)(255 & n >>> 24), (byte)(255 & n >>> 16), (byte)(255 & n >>> 8), (byte)(255 & n)};
    }

    public static byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for(int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    public static int getSW1SW2(byte[] data) {
        if (data.length < 2) {
            return 0;
        } else {
            int sw1sw2 = data[data.length - 2] << 8 & '\uff00';
            sw1sw2 |= data[data.length - 1] & 255;
            return sw1sw2;
        }
    }

    public static byte[] stripSW1SW2(byte[] data) {
        if (data.length < 2) {
            return new byte[0];
        } else {
            byte[] strippedData = new byte[data.length - 2];

            for(int i = 0; i < data.length - 2; ++i) {
                strippedData[i] = data[i];
            }

            return strippedData;
        }
    }

    public static String fen2Yuan(String fen) {
        float yuan = (float)Integer.valueOf(fen) / 100.0F;
        return String.format("%.2f", yuan);
    }

    public static String hexFen2Yuan(String fen) {
        float yuan = (float)Integer.valueOf(fen, 16) / 100.0F;
        return String.format("%.2f", yuan);
    }

    public static String yuan2Fen(String yuan) {
        return String.valueOf((int)(Float.valueOf(yuan) * 100.0F));
    }

    public static String moneyFenInt2YuanString(int amount) {
        if (amount < 0) {
            return "  -";
        } else {
            int intPart = amount / 100;
            int floatPart = amount % 100;
            int jiao = floatPart / 10;
            int fen = floatPart % 10;
            return intPart + "." + jiao + "" + fen + "元";
        }
    }

    public static boolean isInList(String tag, String[] list) {
        boolean result = false;
        if (list != null && tag != null) {
            String[] var3 = list;
            int var4 = list.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String atag = var3[var5];
                if (tag.equals(atag)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public static String phoneNumSecret(String phoneNum) {
        try {
            return phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, 11);
        } catch (Exception var2) {
            return phoneNum;
        }
    }

    public static String studentNumberSecret(String studentNumber) {
        try {
            if (studentNumber.length() == 0) {
                return "";
            } else {
                String star;
                int i;
                if (studentNumber.length() < 6) {
                    star = "";

                    for(i = 0; i < studentNumber.length() - 2; ++i) {
                        star = star + "*";
                    }

                    return studentNumber.substring(0, 1) + star + studentNumber.substring(studentNumber.length() - 1, studentNumber.length());
                } else {
                    star = "";

                    for(i = 0; i < studentNumber.length() - 5; ++i) {
                        star = star + "*";
                    }

                    return studentNumber.substring(0, 2) + "****" + studentNumber.substring(studentNumber.length() - 3, studentNumber.length());
                }
            }
        } catch (Exception var3) {
            return studentNumber;
        }
    }

    public static String nameSecret(String name) {
        try {
            return "*" + name.substring(1);
        } catch (Exception var2) {
            return name;
        }
    }

    public static String orderSnSecret(String orderSn) {
        try {
            return "*****" + orderSn.substring(orderSn.length() - 5);
        } catch (Exception var2) {
            return orderSn;
        }
    }

}
