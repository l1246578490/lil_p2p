package com.dj.p2p.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.math.BigDecimal;

/**
 *  * 
 *  * @ClassName: ArithDouble
 *  * @Description: 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
 *  * @author lil
 *  *
 *  
 */
public class ArithDouble {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 2;


    // 这个类不能实例化
    private ArithDouble() {
    }


    /**
     * 提供精确的加法运算。
     * @param v1            被加数
     * @param v2            加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }


    /**
     * 提供精确的减法运算。
     * 参数V1减去V2
     * @param v1            被减数
     * @param v2            减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 提供精确的减法运算。
     * 参数V1减去V2
     * @param v1            被减数
     * @param v2            减数
     * @return 两个参数的差
     */
    public static String sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    /**
     * @param @param  v1 被减数
     * @param @param  v2 减数
     * @param @param  scale 表示表示需要精确到小数点以后几位。
     * @param @return    设定文件 
     * @return String    返回类型 
     * @throws
     * @Title: sub 
     * @Description: 提供精确的减法运算。
     */
    public static String sub(String v1, String v2, int scale) {
        return scale(sub(v1, v2), scale);
    }

    /**
     * 提供精确的乘法运算。
     * @param v1            被乘数
     * @param v2            乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *  参数S1除以S2
     *
     * @param v1            被除数
     * @param v2            除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字向上取整。
     * 参数S1除以S2
     *
     * @param v1               被除数
     * @param v2               除数
     * @param scale            表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * 参数S1除以S2
     *
     * @param v1               被除数
     * @param v2               除数
     * @return 两个参数的商
     */
    public static double div5up(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v                需要四舍五入的数字
     * @param scale            小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     *  
     *
     * @param a
     * @param b
     * @return int    返回类型 
     * @throws
     * @Title: compareTo 
     * @Description: 在数字上小于、等于或大于 val 时，返回 -1、0 或 1。
     */
    public static int compareTo(double a, double b) {
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        return data1.compareTo(data2);
    }

    /**
     *  
     *
     * @param a
     * @param b
     * @return int    返回类型 
     * @throws
     * @Title: compareTo 
     * @Description: 在数字上小于、等于或大于 val 时，返回 -1、0 或 1。
     */


    public static int compareTo(String a, String b) {
        BigDecimal data1 = new BigDecimal(a == null ? "0" : a.trim());
        BigDecimal data2 = new BigDecimal(b == null ? "0" : b.trim());
        return data1.compareTo(data2);
    }

    /**
     *  
     * 功能：<br/>
     * 判断是否是数字
     */
    public static boolean isNumber(String temp) {
        boolean flag = false;
        if (temp == null || temp.equals("")) return flag;
        try {
            Double.valueOf(temp);// 字符型转换为double类型。
            flag = true;
        } catch (NumberFormatException e) {
            flag = false;
        }
        return flag;
    }

    /**
     *  
     *
     * @param s1            被减数
     * @param s2            减数
     * @return 两个参数的差
     * @throws
     * @Title: subInteger 
     * 提供精确的减法运算。参数V1减去V2
     *  
     */
    public static String subInteger(String s1, String s2) {
        int a = 0;
        int b = 0;
        if (StringUtils.isNotEmpty(s1 != null ? s1.trim() : "")) a = Integer.valueOf(s1 != null ? s1.trim() : "");
        if (StringUtils.isNotEmpty(s2 != null ? s2.trim() : "")) b = Integer.valueOf(s2 != null ? s2.trim() : "");
        return (a - b) + "";
    }

    /**
     *  
     *
     * @param @param  s1
     * @param @param  s2
     * @param @return    设定文件 
     * @return Integer    返回类型 
     * @throws
     * @Title: add 
     * @Description: 将两个整数的字符串相加
     */
    public static String add(String s1, String s2) {
        int a = 0;
        int b = 0;
        if (StringUtils.isNotEmpty(s1 != null ? s1.trim() : "")) a = Integer.valueOf(s1 != null ? s1.trim() : "");
        if (StringUtils.isNotEmpty(s2 != null ? s2.trim() : "")) b = Integer.valueOf(s2 != null ? s2.trim() : "");
        return (a + b) + "";
    }


    /**
     *      * 两个String数相加 返回String 保留小说点
     *      * @param v1
     *      * @param v2
     *      * @return Double
     *      
     */
    public static String add(String v1, String v2, int scale) {
        BigDecimal b1 = new BigDecimal((v1 == null || v1.trim().equals("")) ? "0.0" : v1.trim());
        BigDecimal b2 = new BigDecimal((v2 == null || v2.trim().equals("")) ? "0.0" : v2.trim());
        Double d = b1.add(b2).doubleValue();
        return scale(d.toString(), scale);
    }

    /**
     *      * 并保留scale位小数
     *      * @param v1
     *      * @param scale
     *      * @return Double
     *      
     */
    public static String scale(String v1, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("保留小数点位数的值不能小于0");
        }
        BigDecimal b1 = new BigDecimal(v1);
        return b1.setScale(scale, BigDecimal.ROUND_UP).toString();
    }

    /**
     *  
     *
     * @param @param  s1
     * @param @param  s2
     * @param @return    设定文件 
     * @return String    返回类型 
     * @throws
     * @Title: addDouble 
     * @Description: 两个数相减，参数S1减去S2
     */
    public static String subDouble(String s1, String s2) {
        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        b1.subtract(b2);
        return b1.subtract(b2).toString();
    }


    public static void main(String agre[]) {
        double a = 1.53255;
        double b = 1.3;
// System.out.println(ArithDouble.sub(a, b));
// for(int i=0;i<16;i++){
// System.out.println("i/4="+i/4+" i%4="+i%4);
// }
// ProExceptionDetail pe=new ProExceptionDetail();
// pe.setMaxOperator("<=");
// pe.setMaxWeight("55");
// pe.setMinOperator(">");
// pe.setMinWeight("40");
        System.out.println(ArithDouble.sub(a, b));
    }
}
