package com.example.todolistapplication.Utils;

import com.example.todolistapplication.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.element.ShapeElement;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SecurityUtil {
    /**
     * 使用SHA256进行加密
     * @param str 需要加密的字符串
     * @return
     */
    public static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


    /**
     * 核实密码的复杂程度
     * @param password 用户输入的密码
     * @return 是否足够复杂
     */
    public static boolean checkPasswordComplexity(String password){
        int[] cnt = new int[4]; // 符号的种类  至少两种
        Arrays.fill(cnt, 0);
        for(int i=0; i<password.length(); i++){
            char c = password.charAt(i);

            if(Character.isDigit(c)){
                cnt[0] = 1;
            }else if(Character.isLowerCase(c)){
                cnt[1] = 1;
            }else if(Character.isUpperCase(c)){
                cnt[2] = 1;
            }else{
                cnt[3] = 1;
            }
        }

        return Arrays.stream(cnt).sum() >= 2;
//        if(Arrays.stream(cnt).sum() < 2){
////                    Text text = (Text) findComponentById(ResourceTable.Id_RegisterPasswordError);
//
//            return false;
//        }
    }

    public static boolean checkPasswordLength(String password){
        return password.length() >= 6 && password.length() <= 16;
    }


}
