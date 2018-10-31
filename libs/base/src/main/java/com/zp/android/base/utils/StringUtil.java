package com.zp.android.base.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;


import com.zp.android.base.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by zhaopan on 15/8/12 16:45
 * e-mail: kangqiao610@gmail.com
 * 字符串操作工具包
 */
public class StringUtil {
    private static final String TAG = "StringUtil";

    private static final String DEFAULT_LETTER = "#";

    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|170|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    //电话号码实际长度.默认为11.
    private static final int PHONE_NUMBER_LEN = 11;
    //电话号码间的分隔符 eg: "185-1161-1085" 或 "185 1161 1085"
    private static final String PHONE_SEPARATOR = "\\-| ";
    //目前经纪宝电话号码的匹配规则, 以+或+86或86开头(可有可无), 以1开头的11位电话号码, 其中号码间以 PHONE_SEPARATOR 常量规定的分隔符分隔(可有可无).
    private static Pattern startWith1And11length = Pattern.compile("^(\\+|\\+86|86)?(1)([\\d|" + PHONE_SEPARATOR + "]{10,})$");
    //昵称匹配，（仅可使用中英文字、数值、_和-）
    private static final String NICKNAME_REGEX = "[\u4E00-\u9FA5\\w\\-]{1,15}";

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String JUST_NOW = getString(R.string.base_just_now);
    private static final String ONE_SECOND_AGO = getString(R.string.base_second_ago);
    private static final String ONE_MINUTE_AGO = getString(R.string.base_minute_ago);
    private static final String ONE_HOUR_AGO = getString(R.string.base_hour_ago);
    private static final String ONE_DAY_AGO = getString(R.string.base_day_ago);
    private static final String ONE_MONTH_AGO = getString(R.string.base_month_ago);
    private static final String ONE_YEAR_AGO = getString(R.string.base_year_ago);

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

 /*   public static String getString(int resId) {
        try {
            return App.getApp().getResources().getString(resId);
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }*/

    public static String getString(int resId, Object... params) {
        return String.format(getString(resId), params);
    }

    public static CharSequence htmlFormat(String content) {
        return Html.fromHtml(content);
    }

    public static String redWrap(String content) {
        return "<font color='#F65757'>" + content + "</font>";
    }

    public static String greenWrap(String content) {
        return "<font color='#2EC39B'>" + content + "</font>";
    }

    public static String yellowWrap(String content) {
        return "<font color='#FFBB53'>" + content + "</font>";
    }

    /**
     * 判断子符串是否为空或空子符串.
     *
     * @param str
     * @return
     */
    public static boolean isTrimEmpty(CharSequence str) {
        return null == str || str.toString().trim().length() == 0;
    }

    public static boolean notTrimEmpty(CharSequence str){
        return !isTrimEmpty(str);
    }

    /**
     * 判断子符串是否为空或空子符串.
     *
     * @param str
     * @return
     */
    public static boolean isTrimEmpty(String str) {
        return null == str || str.trim().length() == 0;
    }

    public static String trim(CharSequence str) {
        return isTrimEmpty(str) ? "" : str.toString().trim();
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime() {
        return getDataTime("HH:mm");
    }

    //对日期格式的转换成（"yyyy-MM-dd"）格式的方法
    public static String convertDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(str);
            java.sql.Date d1 = new java.sql.Date(d.getTime());

            return sdf.format(d1);

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }


    /**
     * 毫秒值转换为mm:ss
     *
     * @param ms
     * @author kymjs
     */
    public static String timeFormat(int ms) {
        StringBuilder time = new StringBuilder();
        time.delete(0, time.length());
        ms /= 1000;
        int s = ms % 60;
        int min = ms / 60;
        if (min < 10) {
            time.append(0);
        }
        time.append(min).append(":");
        if (s < 10) {
            time.append(0);
        }
        time.append(s);
        return time.toString();
    }

    /**
     * 将字符串转位日期类型
     *
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    public static String getUpToNow(String date) {
        if(isEmpty(date)) return "";
        Date ddd = DateUtil.milliToDate(date);
        return getUpToNow(ddd.getTime());
    }

    /**
     * 转换时间方法 转成几分钟、几小时前的格式
     *
     * @param date
     * @return
     */
    public static String getUpToNow(long date) {
        long delta = new Date().getTime() - date;
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 1 ? JUST_NOW : seconds + ONE_SECOND_AGO);
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    public static String getUpToNow2(String date) {
        if(isEmpty(date)) return "";
        Date ddd = DateUtil.milliToDate(date);
        return getUpToNow2(ddd.getTime());
    }

    public static String getUpToNow2(long date) {
        long delta = new Date().getTime() - date;
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 1 ? JUST_NOW : seconds + ONE_SECOND_AGO);
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        return DateUtil.DateToString(new Date(date), DateUtil.DateStyle.YYYY_MM_DD_HH_MM_SS_MY1);
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean notEmpty(String input) {
        return !isEmpty(input);
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone2(String phoneNum) {
        if (phoneNum == null || phoneNum.trim().length() == 0)
            return false;
        return phone.matcher(phoneNum).matches();
    }

    /**
     * 判断电话号码是否匹配.
     *
     * @param phoneNum
     * @return
     */
    public static boolean isPhone(String phoneNum) {
        return !"".equals(extractPhoneNumber(phoneNum));

    }

    public static String extractPhoneNumber(String phoneNum) {
        return extractPhoneNumber(phoneNum, false);
    }

    /**
     * 截取用户的电话号码,
     * 建议向服务端发送时, 采用此方法先截取号码再发送.
     *
     * @param phoneNum
     * @param withNONP 是否保留前缀.
     * @return 如果phoneNum不符合匹配规则, 则直接返回 空串"".
     */
    public static String extractPhoneNumber(String phoneNum, boolean withNONP) {
        if (TextUtils.isEmpty(phoneNum)) {
            return "";
        }
        Matcher m = startWith1And11length.matcher(phoneNum);
        if (m.matches()) {
            String pnumb = m.group(2) + m.group(3);
            pnumb = pnumb.replaceAll(PHONE_SEPARATOR, "");
            if (pnumb.length() == PHONE_NUMBER_LEN) {
                if (withNONP) {
                    String prefix = m.group(1);
                    prefix = isTrimEmpty(prefix) ? "" : prefix;
                    return prefix + pnumb;
                } else {
                    return pnumb;
                }
            }
        }
        return "";
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static Pattern DIGIT = Pattern.compile("\\d+");
    public static boolean isDigit(String str){
        return DIGIT.matcher(str).matches();
    }

    /**
     * 获取AppKey
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Activity aty) {
        TelephonyManager tm = (TelephonyManager) aty.getSystemService(Activity.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(aty, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        return tm.getDeviceId();
    }

    /**
     * MD5加密
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * KJ加密
     */
    public static String KJencrypt(String str) {
        char[] cstr = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : cstr) {
            hex.append((char) (c + 5));
        }
        return hex.toString();
    }

    /**
     * KJ解密
     */
    public static String KJdecipher(String str) {
        char[] cstr = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char c : cstr) {
            hex.append((char) (c - 5));
        }
        return hex.toString();
    }

    /**
     * 生成客户表中的cid（客户编号）
     *
     * @return
     */
    public static String generateCID() {
        //return CommonParam.getInstance().getUserId() + generateID();
        return "";
    }

    /**
     * 生成ID, 使用UUID.
     *
     * @return
     */
    public static String generateID() {
        String str = UUID.randomUUID().toString();
        return str.replace("-", "");
    }


    public static boolean limitStrEN(String content, int least, int limit) {
        if (content == null || content.trim().length() == 0)
            return false;
        Pattern p = Pattern.compile("\\w{" + least + "," + limit + "}");
        return p.matcher(content).matches();
    }

    public static boolean limitStrZH(String content, int least, int limit) {
        if (content == null || content.trim().length() == 0)
            return false;
        Pattern p = Pattern.compile(".{" + least + "," + limit + "}");
        return p.matcher(content).matches();
    }


    public static boolean limitNickName(String nickname) {
        if (nickname == null || nickname.trim().length() == 0)
            return false;
        Pattern p = Pattern.compile(NICKNAME_REGEX);
        return p.matcher(nickname).matches();
    }


    /**
     * 简单密码列表
     */
    private static ArrayList<String> simplePasswords = new ArrayList<>();

    static {
        simplePasswords.add("abc123");
        simplePasswords.add("123qwe");
        simplePasswords.add("xiaoming");
        simplePasswords.add("12345678");
        simplePasswords.add("iloveyou");
        simplePasswords.add("admin1");
        simplePasswords.add("qq123456");
        simplePasswords.add("taobao");
        simplePasswords.add("wang1234");
        simplePasswords.add("5201314");
        simplePasswords.add("a1b2c3");
        simplePasswords.add("p@ssword");
        simplePasswords.add("123123");
        simplePasswords.add("123321");
        simplePasswords.add("password");
        simplePasswords.add("passwd");
        simplePasswords.add("qwerty");
        simplePasswords.add("qweasd");
    }

    public static boolean isSimplePassword(String str) {
        if (str != null && !"".equals(str)) {
            if (simplePasswords.contains(str)) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否符合密码规则
     * 必须为6~12位必须有字母、可有数字或符号的组合
     *
     * @param str 传入的密码
     * @return
     */
    public static boolean isMatchPasswordRule(String str) {
        if (str != null && !"".equals(str)) {
            // 6~12位字符，至少包含数字.字母.符号中的2种
            String regStr1 = "(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,12}$";
            String regStr2 = ".*[a-zA-Z]{1}.*";
            String str2 = str;
            Matcher m1 = null;
            Pattern p1 = null;
            Matcher m2 = null;
            Pattern p2 = null;
            p1 = Pattern.compile(regStr1);
            m1 = p1.matcher(str);
            p2 = Pattern.compile(regStr2);
            m2 = p2.matcher(str2);

            if ((m1.matches())) {
                if ((m2.matches())) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是连续的数字或者字母
     * 如：12345678，abcdef，ABCDEF
     *
     * @param str
     * @return
     */
    public static boolean isAllNumberOrLetter(String str) {
        if (str != null && !"".equals(str)) {
            // 6~12位字符，至少包含数字.字母.符号中的2种
            String regStr1 = "[0-9]+";
            String regStr2 = "[a-zA-Z]+";
            Matcher m1 = null;
            Matcher m2 = null;
            Pattern p1 = null;
            Pattern p2 = null;
            p1 = Pattern.compile(regStr1);
            m1 = p1.matcher(str);

            p2 = Pattern.compile(regStr2);
            m2 = p2.matcher(str);
            if (m1.matches() || m2.matches()) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否连续出现2个以上的相同字符
     * 如：12345678，abcdef，ABCDEF
     *
     * @param str
     * @return
     */
    public static boolean isSameCharacterMoreThanThree(String str) {
        if (str != null && !"".equals(str)) {
            String regStr1 = "^.*?([a-zA-Z\\d])\\1\\1.*?$";
            Matcher m1 = null;
            Pattern p1 = null;
            p1 = Pattern.compile(regStr1);
            m1 = p1.matcher(str);

            if ((m1.matches())) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否至少包含字母、数字、符号三者中的两类字符
     *
     * @param str
     * @return 符合条件true
     */
    public static boolean twoTypeOfPassword(String str) {
        if (str != null && !"".equals(str)) {
            String regStr1 = "(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,}";
            Matcher m1 = null;
            Pattern p1 = null;
            p1 = Pattern.compile(regStr1);
            m1 = p1.matcher(str);

            if ((m1.matches())) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (TextUtils.isEmpty(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (!isNotEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }


    public static int convertFirstLetter(String key) {
        if (!StringUtil.isEmpty(key)) {
            char firstKey = key.toUpperCase().charAt(0);
            if (isCaseAtoZ(firstKey)) {
                return firstKey - 'A';
            } else {
                return 0;
            }
        }
        return 0;
    }

    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            } else {

            }
        }

        if (buf == null) {
            return source; //如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) { //这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    public static boolean isCaseAtoZ(char c) {
        return 'A' <= c && c <= 'Z';
    }

    /*public static String getFirstLetter(String str) {
        return getFirstLetter(str, DEFAULT_LETTER, true);
    }

    public static String getFirstLetter(String str, String def) {
        return getFirstLetter(str, def, true);
    }*/

    /**
     * 将参数(@param str)的第一个字符转换为ASICC字符, 并取转换后的Char数组的第一个字符.
     * 例如: "你好" => "N" ,   "_你好" => "#"//如果def="#"时.
     *
     * @param messageContent 初始的字符(中文, 英文, 特殊符号...)
     * @param def     当转换后的ASICC码字符是大于'Z'时, 统一用def代替.
     * @param useDef  当都不匹配时, 是否使用中默认字符.
     * @return
     */
    /*public static String getFirstLetter(String messageContent, String def, boolean useDef) {
        //如果默认的为空, 取定义的.
        if (isTrimEmpty(def)) {
            def = DEFAULT_LETTER;
        }

        //如果content是空的直接用默认的.
        if (isTrimEmpty(messageContent)) {
            return def;
        }

        //转换首字母.
        messageContent = messageContent.trim().toUpperCase();
        char firstChar = messageContent.charAt(0);
        String firstStr = String.valueOf(firstChar);

        //判断是否是英文的A-Z
        if (isCaseAtoZ(firstChar)) {
            return firstStr;
        }

        //转换成中文的A-Z
        if (firstStr.matches("[\\u4e00-\\u9fa5]")) {
            // 擦，Pinyin4j.jar里有个io流没关！！！！！！！
            String[] strings = PinyinHelper.toHanyuPinyinStringArray(firstChar);
            if (strings.length > 0) {
                return String.valueOf(strings[0].charAt(0)).toUpperCase();
            }
        }

        //不是英文, 也不是中文, 是否用默认的.
        if (useDef) {
            return def;
        }

        //还是选用原生的首字符吧.
        return firstStr;
    }*/

    /**
     * inner class
     * 功能：实现汉语拼音序比较
     */
    /*public static int compare(String o1, String o2) {
        return getFirstLetter(o1).compareTo(getFirstLetter(o2));
    }*/

    /*public static String redMoneyIfNeed(double money) {
        if (0 > money) {
            return redWrap(Arith.valueOfMoney(money));
        } else {
            return Arith.valueOfMoney(money);
        }
    }*/

    /**
     * 以下来自https://github.com/motianhuo/wechat/tree/wechat2.0
     */

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param data 要转换的字节数组
     * @return 转换后的结果
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    /**
     * 将给定的字符串中所有给定的关键字标红
     *
     * @param sourceString 给定的字符串
     * @param keyword      给定的关键字
     * @return 返回的是带Html标签的字符串，在使用时要通过Html.fromHtml()转换为Spanned对象再传递给TextView对象
     */
    public static String keywordMadeRed(String sourceString, String keyword) {
        String result = "";
        if (sourceString != null && !"".equals(sourceString.trim())) {
            if (keyword != null && !"".equals(keyword.trim())) {
                result = sourceString.replaceAll(keyword,
                        "<font color=\"red\">" + keyword + "</font>");
            } else {
                result = sourceString;
            }
        }
        return result;
    }

    /**
     * 为给定的字符串添加HTML红色标记，当使用Html.fromHtml()方式显示到TextView 的时候其将是红色的
     *
     * @param string 给定的字符串
     * @return
     */
    public static String addHtmlRedFlag(String string) {
        return "<font color=\"red\">" + string + "</font>";
    }


}