package com.cxytiandi.foxmock.agent.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


public class MD5 {
    private static int DIGITS_SIZE = 16;
    private static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static Map<Character, Integer> rDigits = new HashMap<>(16);

    static {
        for (int i = 0; i < digits.length; ++i) {
            rDigits.put(digits[i], i);
        }
    }

    private static MD5 me = new MD5();
    private MessageDigest mHasher;
    private ReentrantLock opLock = new ReentrantLock();

    private MD5() {
        try {
            mHasher = MessageDigest.getInstance("md5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static MD5 getInstance() {
        return me;
    }

    public String getMD5String(String content) {
        return bytes2string(hash(content));
    }

    public String getMD5String(byte[] content) {
        return bytes2string(hash(content));
    }

    public byte[] getMD5Bytes(byte[] content) {
        return hash(content);
    }

    /**
     * 对字符串进行md5
     *
     * @param str
     * @return md5 byte[16]
     */
    public byte[] hash(String str) {
        opLock.lock();
        try {
            byte[] bt = mHasher.digest(str.getBytes("UTF-8"));
            if (null == bt || bt.length != DIGITS_SIZE) {
                throw new IllegalArgumentException("md5 need");
            }
            return bt;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unsupported utf-8 encoding", e);
        } finally {
            opLock.unlock();
        }
    }

    /**
     * 对二进制数据进行md5
     *
     * @param data
     * @return md5 byte[16]
     */
    public byte[] hash(byte[] data) {
        opLock.lock();
        try {
            byte[] bt = mHasher.digest(data);
            if (null == bt || bt.length != DIGITS_SIZE) {
                throw new IllegalArgumentException("md5 need");
            }
            return bt;
        } finally {
            opLock.unlock();
        }
    }

    /**
     * 将一个字节数组转化为可见的字符串
     *
     * @param bt
     * @return
     */
    public String bytes2string(byte[] bt) {
        int l = bt.length;

        char[] out = new char[l << 1];

        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = digits[(0xF0 & bt[i]) >>> 4];
            out[j++] = digits[0x0F & bt[i]];
        }

        return new String(out);
    }
}
