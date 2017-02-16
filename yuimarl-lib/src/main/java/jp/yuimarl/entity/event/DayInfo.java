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
package jp.yuimarl.entity.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日情報クラス
 * <p>
 * 1日分の情報を保持するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
public class DayInfo {

    /**
     * カレンダー
     */
    private Calendar cal;

    /**
     * 年
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;

    /**
     * 日
     */
    private Integer day;

    /**
     * 曜日
     */
    private Integer dayOfWeek;

    /**
     * 休日
     * <p>
     * true:休日, false:休日でない
     * </p>
     */
    private boolean holiday = false;

    /**
     * 年月日
     * <p>
     * yyyyMMdd
     * </p>
     */
    private String ymd;

    /**
     * イベントのリスト
     */
    private List<EventviewSpread> events = new ArrayList<>();

    /**
     * コンストラクタ
     */
    public DayInfo() {
    }

    /**
     * コンストラクタ
     *
     * @param cal Calendar
     */
    public DayInfo(final Calendar cal) {
        this.cal = cal;
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH) + 1;
        this.day = cal.get(Calendar.DAY_OF_MONTH);
        this.dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        this.ymd = String.format("%04d%02d%02d",
                this.year, this.month, this.day);
    }

    /**
     * カレンダーを取得する。
     *
     * @return カレンダー
     */
    public Calendar getCal() {
        return cal;
    }

    /**
     * カレンダーを設定する。
     *
     * @param cal カレンダー
     */
    public void setCal(final Calendar cal) {
        this.cal = cal;
    }

    /**
     * 年を取得する。
     *
     * @return 年
     */
    public Integer getYear() {
        return year;
    }

    /**
     * 年を設定する。
     *
     * @param year 年
     */
    public void setYear(final Integer year) {
        this.year = year;
    }

    /**
     * 月を取得する。
     *
     * @return 月
     */
    public Integer getMonth() {
        return month;
    }

    /**
     * 月を設定する。
     *
     * @param month 月
     */
    public void setMonth(final Integer month) {
        this.month = month;
    }

    /**
     * 日を取得する。
     *
     * @return 日
     */
    public Integer getDay() {
        return day;
    }

    /**
     * 日を設定する。
     *
     * @param day 日
     */
    public void setDay(final Integer day) {
        this.day = day;
    }

    /**
     * 曜日を取得する。
     *
     * @return 曜日
     */
    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * 曜日を設定する。
     *
     * @param dayOfWeek 曜日
     */
    public void setDayOfWeek(final Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * 休日を取得する。
     *
     * @return true:休日
     */
    public boolean isHoliday() {
        return holiday;
    }

    /**
     * 休日を設定する。
     *
     * @param holiday true:休日
     */
    public void setHoliday(final boolean holiday) {
        this.holiday = holiday;
    }

    /**
     * 年月日を取得する。
     *
     * @return 年月日
     */
    public String getYmd() {
        return ymd;
    }

    /**
     * 年月日を設定する。
     *
     * @param ymd 年月日
     */
    public void setYmd(final String ymd) {
        this.ymd = ymd;
    }

    /**
     * イベントのリストを取得する。
     *
     * @return EventviewSpreadのリスト
     */
    public List<EventviewSpread> getEvents() {
        return events;
    }

    /**
     * イベントのリストを設定する。
     *
     * @param events EventviewSpreadのリスト
     */
    public void setEvents(final List<EventviewSpread> events) {
        this.events = events;
    }

    /**
     * 日情報をプリントする。
     */
    public void print() {
        System.out.print("*** DayInfo ");
        System.out.print(String.format("%04d-%02d-%02d(%d)",
                year, month, day, dayOfWeek));
        if (holiday) {
            System.out.print(" Holiday");
        }
        System.out.println();
        events.stream().forEach((e) -> {
            System.out.println("    " + e.toString());
        });
    }
}
