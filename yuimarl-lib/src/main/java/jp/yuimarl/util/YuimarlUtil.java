/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 BitFarm Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jp.yuimarl.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.Party;
import jp.yuimarl.entity.YuimarlUser;

/**
 * ユーティリティクラス
 *
 * @author Masahiro Nitta
 */
public final class YuimarlUtil {

    /**
     * パスワード生成用文字列1
     */
    private static final String PASS_CHARS1
            = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    /**
     * パスワード生成用文字列2
     */
    private static final String PASS_CHARS2
            = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * コンストラクタ
     */
    private YuimarlUtil() {
    }

    /**
     * 文字列からMD5ハッシュ値を生成する。
     *
     * @param str 文字列
     * @return MD5ハッシュ値
     */
    public static String hashMD5(final String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(str.getBytes("UTF-8"));

            BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException nsae) {
            return null;
        }
    }

    /**
     * 文字列をハッシュ化（SHA-256）して返す。
     *
     * @param value 文字列
     * @return ハッシュ化された文字列
     */
    public static String hashSHA256(final String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(md.digest())
                    .toLowerCase(Locale.ENGLISH);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ログインユーザーを取得する。
     *
     * @param userService YuimarlUserService
     * @return ログインユーザー
     */
    public static YuimarlUser getLoginUser(
            final YuimarlUserService userService) {
        String userId = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get("loginUserId");
        if (userId == null || userId.equals("")) {
            return null;
        }
        YuimarlUser user = userService.getYuimarlUserByUserId(userId);
        return user;
    }

    /**
     *
     * @param party Party
     */
    public static void setCurrentParty(final Party party) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .put("currentParty", party);
    }

    /**
     *
     * @return Party
     */
    public static Party getCurrentParty() {
        return (Party) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap()
                .get("currentParty");
    }

    /**
     * ログファイルにログを出力する。
     * <p>
     * GlassFishの場合、出力されるログファイルは、glassfish/domains/domain1/logs/server.log
     * </p>
     *
     * @param logger Logger
     * @param msg 出力メッセージ
     */
    public static void writeLog(final Logger logger, final String msg) {
        String userId = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .get("loginUserId");
        HttpServletRequest servletRequest
                = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        logger.log(Level.INFO, "{0} | IP:{1}/{2} | User:{3} | {4}",
                new Object[]{
                    Thread.currentThread().getStackTrace()[2].getMethodName(),
                    servletRequest.getRemoteAddr(),
                    servletRequest.getLocalAddr(),
                    userId, msg});
    }

    /**
     * 日付を文字列(yyyy/MM/dd)に変換する。
     *
     * @param dt 日付
     * @return 文字列
     */
    public static String dtToStr1(final Date dt) {
        if (dt == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(dt);
    }

    /**
     * 日付を文字列(yyyyMMdd)に変換する。
     *
     * @param dt 日付
     * @return 文字列
     */
    public static String dtToStr2(final Date dt) {
        if (dt == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(dt);
    }

    /**
     * 日付を文字列(yyyy-MM-ddTHH:mm:ss.SSS+09:00)に変換する。
     *
     * @param dt 日付
     * @return 文字列
     */
    public static String dtToStr3(final Date dt) {
        if (dt == null) {
            return "";
        }
        SimpleDateFormat sdf
                = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+09:00'");
        return sdf.format(dt);
    }

    /**
     * 日付を文字列(yyyyMMddHHmmss)に変換する。
     *
     * @param dt 日付
     * @return 文字列
     */
    public static String dtToStr4(final Date dt) {
        if (dt == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(dt);
    }

    /**
     * 日付を文字列(yyyyMMddHHmm)に変換する。
     *
     * @param dt 日付
     * @return 文字列
     */
    public static String dtToStr5(final Date dt) {
        if (dt == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return sdf.format(dt);
    }

    /**
     * 文字列がNullなら空文字に変換する。
     *
     * @param str 文字列
     * @return 文字列
     */
    public static String nullToStr(final String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    /**
     * Integerを文字列に変換する。
     *
     * @param val Integer
     * @return 文字列
     */
    public static String intToStr(final Integer val) {
        if (val == null) {
            return "";
        }
        return Integer.toString(val);
    }

    /**
     * Longを文字列に変換する。
     *
     * @param val Long
     * @return 文字列
     */
    public static String longToStr(final Long val) {
        if (val == null) {
            return "";
        }
        return Long.toString(val);
    }

    /**
     * Date型の日付の和暦年を返す。
     *
     * @param dt Date型の日付
     * @param parenthesis 括弧: '（' または '['
     * @return 和暦年
     */
    public static String dateToWareki(final Date dt,
            final Character parenthesis) {
        if (dt == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return dateToWareki2(cal, parenthesis);
    }

    /**
     * Calendar型の日付の和暦年を返す。
     *
     * @param cal Calendar型の日付
     * @param parenthesis 括弧: '（' または '['
     * @return 和暦年
     */
    public static String dateToWareki2(final Calendar cal,
            final Character parenthesis) {
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int md = m * 100 + d;
        int g;  // 1:明治, 2:大正, 3:昭和, 4:平成
        int wy;

        if (y < 1868) {
            // 明治元年より前は非対応。
            return "";
        }
        // 明治: 1868年1月1日～1912年7月29日
        // 大正: 1912年7月30日～1926年12月24日
        // 昭和: 1926年12月25日～1989年1月7日
        // 平成: 1989年1月8日～
        switch (y) {
            case 1912:  // 大正元年
                if (md >= 730) {
                    g = 2;
                    wy = y - 1911;
                } else {
                    g = 1;
                    wy = y - 1867;
                }
                break;
            case 1926:  // 昭和元年
                if (md >= 1225) {
                    g = 3;
                    wy = y - 1925;
                } else {
                    g = 2;
                    wy = y - 1911;
                }
                break;
            case 1989:  // 平成元年
                if (md >= 108) {
                    g = 4;
                    wy = y - 1988;
                } else {
                    g = 3;
                    wy = y - 1925;
                }
                break;
            default:
                if (y < 1912) {
                    g = 1;
                    wy = y - 1867;
                } else if (y < 1926) {
                    g = 2;
                    wy = y - 1911;
                } else if (y < 1989) {
                    g = 3;
                    wy = y - 1925;
                } else {
                    g = 4;
                    wy = y - 1988;
                }
                break;
        }

        StringBuilder buf = new StringBuilder();
        if (parenthesis != null) {
            switch (parenthesis) {
                case '（':
                    buf.append("（");
                    break;
                case '[':
                    buf.append("[");
                    break;
                default:
                    break;
            }
        }
        switch (g) {
            case 1:
                buf.append("明治");
                break;
            case 2:
                buf.append("大正");
                break;
            case 3:
                buf.append("昭和");
                break;
            case 4:
                buf.append("平成");
                break;
            default:
                break;
        }
        buf.append(wy).append("年");
        if (parenthesis != null) {
            switch (parenthesis) {
                case '（':
                    buf.append("）");
                    break;
                case '[':
                    buf.append("]");
                    break;
                default:
                    break;
            }
        }
        return buf.toString();
    }

    /**
     * 日付文字列(yyyyMMdd)をCalendarに変換する。
     *
     * @param str 日付文字列(yyyyMMdd)
     * @return Calendar
     */
    public static Calendar strToCal(final String str) {
        if (str == null || str.length() != 8) {
            return null;
        }
        int y = Integer.parseInt(str.substring(0, 4));
        int m = Integer.parseInt(str.substring(4, 6));
        int d = Integer.parseInt(str.substring(6));
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(y, m - 1, d);
        return cal;
    }

    /**
     * Calendarを日付文字列(yyyyMMdd)に変換する。
     *
     * @param cal Calendar
     * @return 日付文字列(yyyyMMdd)
     */
    public static String calToStr(final Calendar cal) {
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d%02d%02d", y, m, d);
    }

    /**
     * 2つのCalendarの日付の差を返す。
     *
     * @param cal1 Calendar
     * @param cal2 Calendar
     * @return 日付の差
     */
    public static int dateDiff(final Calendar cal1, final Calendar cal2) {
        long diffTime = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        return (int) (diffTime / 1000 / 60 / 60 / 24);
    }

    /**
     * 日付文字列を取得する。
     *
     * @param date 日付
     * @return 日付文字列
     */
    public static String toDateStr(final String date) {
        if (date == null || date.length() != 8) {
            return null;
        }
        // 2015-02-25
        String ret = date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                + date.substring(6, 8);
        return ret;
    }

    /**
     * 日付と時間から日時文字列を取得する。
     *
     * @param date 日付
     * @param time 時間
     * @return 日時文字列
     */
    public static String toDateTimeStr(final String date, final String time) {
        if (date == null || time == null || date.length() != 8
                || time.length() != 4) {
            return null;
        }
        // 2015-02-25T11:58:00+09:00
        String ret = date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
                + date.substring(6, 8) + "T" + time.substring(0, 2) + ":"
                + time.substring(2, 4) + ":00+09:00";
        return ret;
    }

    /**
     * 指定された桁数のパスワードを生成する。（英字大文字26文字、小文字26文字、数字10文字）
     *
     * @param length 桁数
     * @return パスワード
     */
    public static String generatePassword1(final int length) {
        Random rnd = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = rnd.nextInt(62);
            buf.append(PASS_CHARS1.charAt(num));
        }
        return buf.toString();
    }

    /**
     * 指定された桁数のパスワードを生成する。（英字大文字26文字、数字10文字）
     *
     * @param length 桁数
     * @return パスワード
     */
    public static String generatePassword2(final int length) {
        Random rnd = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = rnd.nextInt(36);
            buf.append(PASS_CHARS2.charAt(num));
        }
        return buf.toString();
    }
}
