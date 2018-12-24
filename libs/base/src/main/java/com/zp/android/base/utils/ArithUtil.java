package com.zp.android.base.utils;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by zhaopan on 2018/4/18.
 */
public final class ArithUtil {

    // 默认除法运算精度
    static int DEF_DIV_SCALE = 10;

    static String DEF_VOLUME_UNIT = "k";
    static String DEF_ARITH_FORMAT = "#.####";
    static String DEF_NUMBER_FORMAT = "#,##,###,####.##";
    static String DEF_MONEY_FORMAT = "#,##,###,####.00";

    static DecimalFormat SCALE_2_FORMAT = new DecimalFormat("0.00");
    static DecimalFormat SCALE_3_FORMAT = new DecimalFormat("0.000");
    static DecimalFormat SCALE_4_FORMAT = new DecimalFormat("0.0000");

    static int defaultRoundMode = BigDecimal.ROUND_DOWN;

    private ArithUtil() {
    }

    public static void setDefaultRoundMode(int mode) {
        defaultRoundMode = mode;
    }

    public static boolean isInvalidDouble(double d) {
        return Double.isNaN(d) || Double.isInfinite(d);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        //ObjectUtil.assertExpression(scale > 0, "this scale is required; it must be gt zero");
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        //ObjectUtil.assertExpression(scale > 0, "this scale is required; it must be gt zero");
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale, int roundingMode) {
        //ObjectUtil.assertExpression(scale > 0, "this scale is required; it must be gt zero");
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, roundingMode).doubleValue();
    }

    /**
     * 提供精确的类型转换(Float)
     *
     * @param v 需要被转换的数字
     * @return 返回转换结果
     */
    public static float convertsToFloat(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.floatValue();
    }

    /**
     * 提供精确的类型转换(Int)不进行四舍五入
     *
     * @param v 需要被转换的数字
     * @return 返回转换结果
     */
    public static int convertsToInt(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.intValue();
    }

    /**
     * 提供精确的类型转换(Long)
     *
     * @param v 需要被转换的数字
     * @return 返回转换结果
     */
    public static long convertsToLong(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.longValue();
    }

    /**
     * 返回两个数中大的一个值
     *
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 返回两个数中大的一个值
     */
    public static double returnMax(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.max(b2).doubleValue();
    }

    /**
     * 返回两个数中小的一个值
     *
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 返回两个数中小的一个值
     */
    public static double returnMin(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.min(b2).doubleValue();
    }

    /**
     * 精确比较两个数字
     *
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
     */
    public static int compareTo(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2);
    }

    /**
     * 获取数字小数位数
     *
     * @param number 数字.
     * @return 小数位数
     */
    public static int getDecimals(double number) {
        String numStr = Double.toString(number);//new BigDecimal(number).toPlainString();
        int dotIndex = numStr.indexOf(".");
        if (dotIndex > 0) {
            return numStr.length() - dotIndex - 1;
        } else {
            return 0;
        }
    }

    public static String keepXDecimal(Object numobj, int x) {
        return keepXDecimal(numobj, x, null);
    }

    public static String keepXDecimal(Object numobj, int x, RoundingMode mode) {
        String formatStr = "";
        x = Math.max(0, Math.min(x, 10));
        if (x > 0) formatStr += ".";
        while (x-- > 0) {
            formatStr += "0";
        }
        double num = toDouble(numobj);
        if (isInvalidDouble(num) || num == 0) {
            return "0"+formatStr;
        }
        formatStr = "0"+formatStr;

        BigDecimal bigDecimal = new BigDecimal(num);
        if(null != mode){
            bigDecimal.setScale(x, mode);
        } else {
            bigDecimal.setScale(x, defaultRoundMode);
        }
        return new DecimalFormat(formatStr).format(bigDecimal);
    }

    public static String keep2Decimal(Object val){
        double num = toDouble(val);
        if (isInvalidDouble(num)) {
            return "0.00";
        }
        BigDecimal bigDecimal = new BigDecimal(num);
        return SCALE_2_FORMAT.format(bigDecimal);
    }

    public static String keep3Decimal(Object val){
        double num = toDouble(val);
        if (isInvalidDouble(num)) {
            return "0.000";
        }
        BigDecimal bigDecimal = new BigDecimal(num);
        return SCALE_3_FORMAT.format(bigDecimal);
    }

    public static String keep4Decimal(Object val){
        double num = toDouble(val);
        if (isInvalidDouble(num)) {
            return "0.0000";
        }
        BigDecimal bigDecimal = new BigDecimal(num);
        return SCALE_4_FORMAT.format(bigDecimal);
    }

    public static String formatK(Object val){
        double num = toDouble(val) / 1000;
        if (isInvalidDouble(num)) {
            return "0";
        }
        return keep2Decimal(num) + DEF_VOLUME_UNIT;
    }

    public static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof CharSequence) {
            String stringValue = value.toString();
            if ("true".equalsIgnoreCase(stringValue)) {
                return true;
            } else if ("false".equalsIgnoreCase(stringValue)) {
                return false;
            } else if ("1".equalsIgnoreCase(stringValue)) {
                return true;
            } else if ("0".equalsIgnoreCase(stringValue)) {
                return false;
            }
        } else if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        return null;
    }

    public static Double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof CharSequence) {
            return Double.valueOf(value.toString());
        }
        return null;
    }

    public static Integer toInteger(Object value, Integer def) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof CharSequence) {
            return Integer.valueOf(value.toString());
        }
        return def;
    }

    public static Integer toInteger(Object value) {
        return toInteger(value, null);
    }

    public static Long toLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof CharSequence) {
            return Long.valueOf(value.toString());
        }
        return null;
    }

    public static String toString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }

    public static String getFormatedLegalTender(BigDecimal amount) {
        if (amount == null) {
            return "";
        }
        return amount.setScale(2, RoundingMode.DOWN).toString();
    }

    public static String getFormatedAmount(BigDecimal amount) {
        if (amount == null) {
            return "";
        }
        return amount.setScale(5, RoundingMode.DOWN).toString();
    }

//    public static String getFormatedAmount(BalanceCoin coinType, BigInteger bigIntAmount) {
//        if (coinType == null || bigIntAmount == null) {
//            return "0";
//        }
//        return getFormatedAmount(coinType, bigIntAmount, coinType.getDecimal());
//    }
//
//    public static String getFormatedAmount(BalanceCoin coinType, BigInteger bigIntAmount, int accuracy) {
//        return getFormatedAmount(coinType, bigIntAmount, accuracy, false);
//    }
//
//    public static String getFormatedAmount(BalanceCoin coinType, BigInteger bigIntAmount, int accuracy, boolean useGrouping) {
//        if (coinType == null || bigIntAmount == null) {
//            return "0";
//        }
//        String strUnit = "1";
//        for (int i = 0; i < coinType.getDecimal(); i ++) {
//            strUnit += "0";
//        }
//        NumberFormat nformat = NumberFormat.getNumberInstance();
//        nformat.setGroupingUsed(useGrouping);//逗号
//        nformat.setMaximumFractionDigits(accuracy);
//        return nformat.format(new BigDecimal(bigIntAmount).divide(new BigDecimal(strUnit)));
//    }
//
//    public static BigInteger reverseFormatedAmount(BalanceCoin coinType, String amoutStr) {
//        if (coinType == null || TextUtils.isEmpty(amoutStr)) {
//            return BigInteger.ZERO;
//        }
//        String strUnit = "1";
//        for (int i = 0; i < coinType.getDecimal(); i ++) {
//            strUnit += "0";
//        }
//        return new BigDecimal(amoutStr).multiply(new BigDecimal(strUnit)).toBigInteger();
////        if (!amoutStr.contains(".")) {
////            return (new BigInteger(amoutStr)).;
////        } else {
////            String[] array = amoutStr.split("\\.");
////            if (array.length > 1) {
////                if (array[1].length() > balanceCoin.getUnitExponent()) {
////                    array[1] = array[1].substring(balanceCoin.getUnitExponent(), array[1].length()-1);
////                }
////                int patchZeroCount = balanceCoin.getUnitExponent() - array[1].length();
////                for (int i = 0; i < patchZeroCount; i ++) {
////                    array[1] += "0";
////                }
////
////                return new BigInteger(array[0] + array[1]);
////            } else {
////                return new BigInteger(array[0]);
////            }
////        }
//    }

    public static String formatNumber(double value) {
        return formatNumber(value, getDecimals(value), defaultRoundMode);
    }

    public static String formatNumber(Double value, int scale) {
        return formatNumber(value, scale, defaultRoundMode);
    }

    public static String formatNumber(Double value, int scale, int mode) {
        if(null == value) value = 0d;
        if (value < 0) return "-" + formatNumber(-value, scale, mode);

        StringBuilder formatStr = new StringBuilder();
        if (scale > 0) {
            formatStr.append(".");
            int x = scale;
            while (x-- > 0) {
                formatStr.append("0");
            }
        }

        if (isInvalidDouble(value) || value == 0) {
            return "0" + formatStr.toString();
        }

        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal.setScale(scale, mode);
        DecimalFormat format = new DecimalFormat(",##0" + formatStr.toString());
        format.setRoundingMode(RoundingMode.valueOf(mode));
        return format.format(bigDecimal);
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatEnNum(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatEnNum(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatEnNum(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String formatEnNum(double value, int scale) {
        return formatEnNum(value, scale, defaultRoundMode);
    }

    public static String formatEnNum(double value, int scale, int mode) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Double.MIN_VALUE) return "";
        if (value < 0) return "-" + formatEnNum(-value, scale, mode);

        BigDecimal data = BigDecimal.valueOf(value).setScale(scale, mode);
        if (value < 1000) return data.stripTrailingZeros().toPlainString(); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(data.longValue());
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        return data.divide(new BigDecimal(divideBy), scale, mode)
                .stripTrailingZeros()
                .toPlainString() + suffix;
    }

}
