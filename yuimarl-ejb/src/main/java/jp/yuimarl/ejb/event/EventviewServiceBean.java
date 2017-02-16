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
package jp.yuimarl.ejb.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jp.yuimarl.ejb.AbstractFacade;
import jp.yuimarl.ejb.EventviewService;
import jp.yuimarl.entity.event.Daily;
import jp.yuimarl.entity.event.Event;
import jp.yuimarl.entity.event.Eventview;
import jp.yuimarl.entity.event.EventviewSpread;
import jp.yuimarl.entity.event.Eventviewset;
import jp.yuimarl.entity.event.Holiday;
import jp.yuimarl.entity.event.Monthly;
import jp.yuimarl.entity.event.Weekly;
import jp.yuimarl.entity.event.Yearly;
import jp.yuimarl.util.YuimarlUtil;

/**
 * イベントビュー操作クラス
 *
 * @author Masahiro Nitta
 */
@Stateless
public class EventviewServiceBean extends AbstractFacade<Eventview>
        implements EventviewService {

    /**
     * EntityManager
     */
    @PersistenceContext(unitName = "yuimarlPU")
    private EntityManager em;

    /**
     * 追加日
     */
    private String[] includeDays;

    /**
     * 除外日
     */
    private String[] excludeDays;

    /**
     * 開始日
     */
    private Calendar firstDay;

    /**
     * 開始日
     */
    private Calendar calFrom = null;

    /**
     * 終了日
     */
    private Calendar calTo = null;

    /**
     * EventView No.
     */
    private int eventviewNo;

    /**
     * 年
     */
    private int year;

    /**
     * 年
     */
    private String sYear = null;

    /**
     * 休日のリスト
     */
    private final List<String> holidays = new ArrayList<>();

    /**
     * コンストラクタ
     */
    public EventviewServiceBean() {
    }

    /**
     * コンストラクタ
     *
     * @param em EntityManager
     */
    public EventviewServiceBean(final EntityManager em) {
        this.em = em;
    }

    /**
     * EntityManagerを取得する。
     *
     * @return EntityManager
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     *
     * @param eventviewNo EventView No.
     * @param termFrom 期間開始日
     * @param termTo 期間終了日
     * @return Eventのリスト
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Event> findByEventviewTerm(final int eventviewNo,
            final String termFrom, final String termTo) {
        Query q = em.createNamedQuery("Eventviewset.findByEventview");
        q.setParameter("eventview", eventviewNo);
        List<Eventviewset> list = q.getResultList();
        StringBuilder buf = new StringBuilder();
        boolean isFirst = true;
        for (Eventviewset ev : list) {
            if (isFirst) {
                isFirst = false;
            } else {
                buf.append(",");
            }
            buf.append(ev.getEventviewsetPK().getEventset());
        }

        if (isFirst) {
            return null;
        }

        q = em.createQuery(
                "SELECT e FROM Event e WHERE e.eventset.eventsetNo IN ("
                + buf.toString()
                + ") AND e.termFrom <= '" + termTo + "' AND e.termTo >= '"
                + termFrom
                + "' AND e.delFlg = '0' "
                + "ORDER BY e.eventClass, e.startTime, e.endTime");
        return q.getResultList();
    }

    /**
     * 指定された年の範囲の日本の休日を展開する。
     *
     * @param year1 年1
     * @param year2 年2
     */
    @Override
    public void spreadJapaneseHoliday(final int year1, final int year2) {
        for (int y = year1; y <= year2; y++) {
            spreadEvent(1, y);
        }
    }

    /**
     * 指定されたイベントビューNo.のイベントを、指定年の1年分、 EventviewSpreadに展開する。
     *
     * @param eventviewNo イベントビューNo.
     * @param year 年
     */
    @Override
    public void spreadEvent(final int eventviewNo, final int year) {
        this.eventviewNo = eventviewNo;
        this.year = year;
        this.sYear = Integer.toString(year);
        if (this.sYear.length() != 4) {
            return;
        }

        Query q = em.createNamedQuery("Eventview.findByEventviewNo");
        q.setParameter("eventviewNo", eventviewNo);
        Eventview ev = (Eventview) q.getSingleResult();
        if (ev == null) {
            return;
        }
        if (ev.getSpreadYears() != null
                && ev.getSpreadYears().contains(sYear)) {
            // すでに展開済み。
            return;
        }

        calFrom = Calendar.getInstance();
        calFrom.clear();
        calFrom.set(year, 0, 1);
        calFrom.add(Calendar.DATE, -10);

        calTo = Calendar.getInstance();
        calTo.clear();
        calTo.set(year + 1, 0, 1);
        calTo.add(Calendar.DATE, 9);

        // 既存の展開済みイベントを削除する。
        q = em.createNamedQuery("EventviewSpread.deleteBySpreadTerm");
        q.setParameter("eventviewNo", eventviewNo);
        q.setParameter("spreadDate1", sYear + "0000");
        q.setParameter("spreadDate2", sYear + "9999");
        q.executeUpdate();

        if (ev.getJapaneseHoliday() == '1') {
            spreadJapaneseHoliday();
        }

        List<Event> events = getEvents();
        if (events != null) {
            for (Event e : events) {
                switch (e.getEventClass()) {
                    case Event.EVENT_CLASS_MONTHLY:
                        if (e instanceof Monthly) {
                            monthlySpread((Monthly) e);
                        }
                        break;
                    case Event.EVENT_CLASS_YEARLY:
                        if (e instanceof Yearly) {
                            yearlySpread((Yearly) e);
                        }
                        break;
                    case Event.EVENT_CLASS_HOLIDAY:
                        if (e.getEventType()
                                == Holiday.EVENT_TYPE_MEMORIALDAY_REGULAR
                                && e instanceof Holiday) {
                            memorialdaySpread((Holiday) e);
                        }
                        break;
                    default:
                        eventSpread(e);
                        break;
                }

                // 除外日のリスト
                if (e.getExcludeDays() != null) {
                    excludeDays = e.getExcludeDays().split(",");
                    for (String ex : excludeDays) {
                        if (ex.length() == 8) {
                            q = em.createNamedQuery(
                                    "EventviewSpread.deleteBySpreadDate");
                            q.setParameter("eventNo", e.getEventNo());
                            q.setParameter("spreadDate", ex);
                            q.executeUpdate();
                        }
                    }
                }

                // 追加日のリスト
                if (e.getIncludeDays() != null) {
                    includeDays = e.getIncludeDays().split(",");
                    for (String id : includeDays) {
                        if (id.length() == 8) {
                            addEvent(e, id);
                        }
                    }
                }
            }
        }

        // イベントセットに展開年を追加する。
        if (ev.getSpreadYears() == null) {
            ev.setSpreadYears(sYear);
        } else {
            ev.setSpreadYears(ev.getSpreadYears() + "," + sYear);
        }
        em.merge(ev);
    }

    /**
     * 日本の休日・記念日を展開する。
     */
    @SuppressWarnings("unchecked")
    private void spreadJapaneseHoliday() {
        String date;
        // イベントセットに対応したイベントの情報を読み込む。
        Query q = em.createNamedQuery("Event.findByEventsetNo");
        q.setParameter("eventsetNo", 1);
        List<Event> events = q.getResultList();
        Holiday h1;
        EventviewSpread s1;
        EventviewSpread s2;
        Calendar cal;
        int dd;
        holidays.clear();

        for (Event e : events) {
            if (e.getEventClass() != Event.EVENT_CLASS_HOLIDAY
                    || !(e instanceof Holiday)) {
                // 休日・記念日のみ対応。
                continue;
            }
            h1 = (Holiday) e;
            switch (e.getEventType()) {
                case Holiday.EVENT_TYPE_HOLIDAY_REGULAR:
                case Holiday.EVENT_TYPE_MEMORIALDAY_REGULAR:
                    date = sYear + String.format("%02d%02d",
                            h1.getRepMonth(), h1.getRepDay());
                    if (h1.getTermFrom().compareTo(date) <= 0
                            && h1.getTermTo().compareTo(date) >= 0) {
                        addEvent(h1, date);
                        if (e.getEventType()
                                == Holiday.EVENT_TYPE_HOLIDAY_REGULAR) {
                            holidays.add(date);
                        }
                    }
                    break;
                case Holiday.EVENT_TYPE_HOLIDAY_IRREGULAR:
                    String[] days = h1.getIncludeDays().split(",");
                    for (String day : days) {
                        if (day.startsWith(sYear)) {
                            if (h1.getTermFrom().compareTo(day) <= 0
                                    && h1.getTermTo().compareTo(day) >= 0) {
                                addEvent(h1, day);
                                holidays.add(day);
                            }
                        }
                    }
                    break;
                case Holiday.EVENT_TYPE_HOLIDAY_HAPPYMONDAY:
                    cal = Calendar.getInstance();
                    cal.clear();
                    cal.set(year, h1.getRepMonth() - 1, 1);
                    dd = cal.get(Calendar.DAY_OF_WEEK);
                    // 第1月曜日の日を求める。
                    if (dd <= Calendar.MONDAY) {
                        dd = 3 - dd;
                    } else {
                        dd = 10 - dd;
                    }
                    // 第w月曜日の日を求める。
                    dd += (h1.getRepWeek() - 1) * 7;
                    date = sYear + String.format("%02d%02d",
                            h1.getRepMonth(), dd);
                    if (h1.getTermFrom().compareTo(date) <= 0
                            && h1.getTermTo().compareTo(date) >= 0) {
                        addEvent(h1, date);
                        holidays.add(date);
                    }
                    break;
                case Holiday.EVENT_TYPE_MEMORIALDAY_SUNDAY:
                    cal = Calendar.getInstance();
                    cal.clear();
                    cal.set(year, h1.getRepMonth() - 1, 1);
                    dd = cal.get(Calendar.DAY_OF_WEEK);
                    // 第1日曜日の日を求める。
                    if (dd > Calendar.SUNDAY) {
                        dd = 9 - dd;
                    }
                    // 第w日曜日の日を求める。
                    dd += (h1.getRepWeek() - 1) * 7;
                    date = sYear + String.format("%02d%02d",
                            h1.getRepMonth(), dd);
                    if (h1.getTermFrom().compareTo(date) <= 0
                            && h1.getTermTo().compareTo(date) >= 0) {
                        addEvent(h1, date);
                    }
                    break;
                default:
                    break;
            }
        }

        /*
         * 国民の休日、振替休日の判定・登録
         */
        // 展開した休日を検索
        q = em.createQuery("SELECT e FROM EventviewSpread e "
                + "WHERE e.eventviewSpreadPK.eventviewNo = " + eventviewNo
                + " AND e.eventType IN (" + Holiday.EVENT_TYPE_HOLIDAY_REGULAR
                + ", " + Holiday.EVENT_TYPE_HOLIDAY_IRREGULAR
                + ", " + Holiday.EVENT_TYPE_HOLIDAY_HAPPYMONDAY
                + ") AND e.eventviewSpreadPK.spreadDate > '" + sYear
                + "0000' AND e.eventviewSpreadPK.spreadDate < '"
                + sYear + "9999' ORDER BY e.eventviewSpreadPK.spreadDate"
        );

        List<EventviewSpread> spreads = q.getResultList();

        for (int i = 0; i < spreads.size(); i++) {
            s1 = spreads.get(i);
            // 国民の休日判定「その前日及び翌日が『国民の祝日』である日
            //（『国民の祝日』でない日に限る。）は、休日とする。」
            if ((i + 1) != spreads.size()) {
                cal = YuimarlUtil.strToCal(
                        s1.getEventviewSpreadPK().getSpreadDate());
                cal.add(Calendar.DATE, 2);
                date = YuimarlUtil.calToStr(cal);
                s2 = spreads.get(i + 1);
                // 2日後も祝日なら、間の日を国民の休日として登録する。
                if (s2.getEventviewSpreadPK().getSpreadDate().equals(date)) {
                    cal.add(Calendar.DATE, -1);
                    date = YuimarlUtil.calToStr(cal);
                    s2 = new EventviewSpread(eventviewNo, date,
                            s1.getEventviewSpreadPK().getEventsetNo(), 0);
                    s2.setName("国民の休日");
                    s2.setEventType(Holiday.EVENT_TYPE_HOLIDAY_NATIONAL);
                    em.persist(s2);
                    holidays.add(date);
                    continue;
                }
            }

            // 振替休日判定「『国民の祝日』が日曜日に当たるときは、
            // その日後においてその日に最も近い『国民の祝日』でない日を休日とする。」
            cal = YuimarlUtil.strToCal(s1.getEventviewSpreadPK()
                    .getSpreadDate());
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                cal.add(Calendar.DATE, 1);
                date = YuimarlUtil.calToStr(cal);
                // その日後においてその日に最も近い『国民の祝日』でない日を探す。
                for (int j = i + 1; j < spreads.size(); j++) {
                    s2 = spreads.get(j);
                    if (s2.getEventviewSpreadPK().getSpreadDate()
                            .equals(date)) {
                        cal.add(Calendar.DATE, 1);
                        date = YuimarlUtil.calToStr(cal);
                    } else {
                        break;
                    }
                }
                s2 = new EventviewSpread(eventviewNo, date,
                        s1.getEventviewSpreadPK().getEventsetNo(), 0);
                s2.setName("振替休日");
                s2.setEventClass(Event.EVENT_CLASS_HOLIDAY);
                s2.setEventType(Holiday.EVENT_TYPE_HOLIDAY_SUBSTITUTE);
                em.persist(s2);
                holidays.add(date);
            }
        }

    }

    /**
     * 指定されたEventviewNo.と期間のイベント配列を取得する。
     *
     * @return イベント配列
     */
    @SuppressWarnings("unchecked")
    private List<Event> getEvents() {
        // Eventviewが使用しているEventsetを取得する。
        Query q = em.createNamedQuery("Eventviewset.findByEventview");
        q.setParameter("eventview", eventviewNo);
        List<Eventviewset> list = q.getResultList();
        StringBuilder buf = new StringBuilder();
        boolean flg = false;

        // EventsetNo.の配列をカンマ区切り文字列にする。
        for (Eventviewset v : list) {
            if (flg) {
                buf.append(",");
            } else {
                flg = true;
            }
            buf.append(v.getEventviewsetPK().getEventset());
        }

        if (!flg) {
            return null;
        }

        // Eventsetのイベントを取得する。
        q = em.createQuery("SELECT e FROM Event e WHERE e.eventsetNo IN ("
                + buf.toString()
                + ") AND e.termFrom < '" + (year + 1)
                + "0110' AND e.termTo > '" + (year - 1)
                + "1220' AND e.delFlg = '0' "
                + "ORDER BY e.eventClass, e.startTime, e.endTime");

        return q.getResultList();
    }

    /**
     * イベントをEventviewSpreadに展開する。
     *
     * @param e Event
     */
    private void eventSpread(final Event e) {
        int diff;
        firstDay = null;
        int interval = 1;

        if ((e instanceof Daily) && !e.getTermFrom().equals("00000000")) {
            // イベントの有効期間の最初の日付
            firstDay = YuimarlUtil.strToCal(e.getTermFrom());
            if (((Daily) e).getRepInterval() != null
                    && ((Daily) e).getRepInterval() > 1) {
                interval = ((Daily) e).getRepInterval();

                switch (e.getEventClass()) {
                    case Event.EVENT_CLASS_WEEKLY:
                        // 最初の日を日曜日にする。
                        firstDay.add(Calendar.DATE,
                                1 - firstDay.get(Calendar.DAY_OF_WEEK));
                        break;
                    default:
                        break;
                }
            }
        }

        Calendar c1 = (Calendar) calFrom.clone();
        Calendar c2;
        String s1;
        String s2;

        while (c1.before(calTo)) {
            c1.add(Calendar.DATE, 1);
            s1 = YuimarlUtil.calToStr(c1);

            if (e.getTermFrom().compareTo(s1) > 0
                    || e.getTermTo().compareTo(s1) < 0) {
                // 有効期間外ならスキップ
                continue;
            }

            // 実施曜日の判定
            boolean exec = true;
            switch (c1.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    if (e.getSunday() == '0') {
                        exec = false;
                    }
                    break;
                case Calendar.MONDAY:
                    if (e.getMonday() == '0') {
                        exec = false;
                    }
                    break;
                case Calendar.TUESDAY:
                    if (e.getTuesday() == '0') {
                        exec = false;
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if (e.getWednesday() == '0') {
                        exec = false;
                    }
                    break;
                case Calendar.THURSDAY:
                    if (e.getThursday() == '0') {
                        exec = false;
                    }
                    break;
                case Calendar.FRIDAY:
                    if (e.getFriday() == '0') {
                        exec = false;
                    }
                    break;
                case Calendar.SATURDAY:
                    if (e.getSaturday() == '0') {
                        exec = false;
                    }
                    break;
                default:
                    break;
            }

            switch (e.getEventClass()) {
                case Event.EVENT_CLASS_SINGLE:
                    if (exec && e.getHoliday() == '0' && isHoliday(s1)) {
                        exec = false;
                    }
                    if (exec) {
                        if (s1.startsWith(sYear)) {
                            EventviewSpread es
                                    = new EventviewSpread(e, eventviewNo, s1);
                            if (!e.getTermFrom().equals(e.getTermTo())) {
                                es.setEventTerm('2');
                                if (e.getTermFrom().equals(s1)) {
                                    es.setEventTerm('1');
                                } else {
                                    es.setStartTime(" ");
                                }
                                if (e.getTermTo().equals(s1)) {
                                    es.setEventTerm('3');
                                } else {
                                    es.setEndTime(" ");
                                }
                            }
                            em.persist(es);
                        }
                    }
                    break;
                case Event.EVENT_CLASS_DAILY:
                    if (exec) {
                        Daily daily = (Daily) e;

                        if (interval > 1) {
                            // 最初の日からの差分
                            diff = YuimarlUtil.dateDiff(c1, firstDay);
                            diff = diff % interval;
                            if (diff == 0) {
                                if (e.getHoliday() == '0' && isHoliday(s1)) {
                                    switch (daily.getExceptMove()) {
                                        case '1':   // 前にずらす
                                            c2 = (Calendar) c1.clone();
                                            while (true) {
                                                c2.add(Calendar.DATE, -1);
                                                s2 = YuimarlUtil.calToStr(c2);
                                                if (!isHoliday(s2)) {
                                                    addEvent(daily, s2);
                                                    break;
                                                }
                                            }
                                            break;
                                        case '2':   // 後ろにずらす
                                            c2 = (Calendar) c1.clone();
                                            while (true) {
                                                c2.add(Calendar.DATE, 1);
                                                s2 = YuimarlUtil.calToStr(c2);
                                                if (!isHoliday(s2)) {
                                                    addEvent(daily, s2);
                                                    break;
                                                }
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    addEvent(daily, s1);
                                }
                            }
                        } else {
                            if (e.getHoliday() == '0' && isHoliday(s1)) {
                                exec = false;
                            }
                            if (exec) {
                                addEvent(daily, s1);
                            }
                        }
                    }
                    break;
                case Event.EVENT_CLASS_WEEKLY:
                    if (exec && e instanceof Weekly) {
                        Weekly weekly = (Weekly) e;

                        if (interval > 1) {
                            c2 = (Calendar) c1.clone();
                            // 日曜日にする。
                            c2.add(Calendar.DATE,
                                    1 - c2.get(Calendar.DAY_OF_WEEK));
                            // 最初の日からの差分
                            diff = YuimarlUtil.dateDiff(c2, firstDay);
                            diff = diff % (interval * 7);
                            if (diff != 0) {
                                exec = false;
                            }
                        }
                        if (exec) {
                            if (e.getHoliday() == '0' && isHoliday(s1)) {
                                switch (weekly.getExceptMove()) {
                                    case '1':   // 前にずらす
                                        c2 = (Calendar) c1.clone();
                                        while (true) {
                                            c2.add(Calendar.DATE, -1);
                                            s2 = YuimarlUtil.calToStr(c2);
                                            if (!isHoliday(s2)) {
                                                addEvent(weekly, s2);
                                                break;
                                            }
                                        }
                                        break;
                                    case '2':   // 後ろにずらす
                                        c2 = (Calendar) c1.clone();
                                        while (true) {
                                            c2.add(Calendar.DATE, 1);
                                            s2 = YuimarlUtil.calToStr(c2);
                                            if (!isHoliday(s2)) {
                                                addEvent(weekly, s2);
                                                break;
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                addEvent(weekly, s1);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * MonthlyイベントをEventviewSpreadに展開する。
     *
     * @param m Monthlyイベント
     */
    private void monthlySpread(final Monthly m) {
        Calendar c1 = (Calendar) calFrom.clone();
        c1.set(Calendar.DAY_OF_MONTH, 1);
        Calendar c2;
        int interval = 1;
        int d1 = 0;
        int d2;
        boolean first = true;
        boolean flg;
        String strWk = null;
        int dow = 0;
        int d;

        if (m.getTermFrom().equals("00000000")) {
            firstDay = null;
        } else {
            // イベントの有効期間の最初の日付
            firstDay = YuimarlUtil.strToCal(m.getTermFrom());
            d1 = firstDay.get(Calendar.YEAR) * 12
                    + firstDay.get(Calendar.MONTH);
            if (m.getRepInterval() != null) {
                interval = m.getRepInterval();
            }
        }

        if (m.getEventType() == Monthly.EVENT_TYPE_MONTHLY_WEEK) {
            if (m.getSunday() == '1') {
                dow = Calendar.SUNDAY;
            }
            if (m.getMonday() == '1') {
                dow = Calendar.MONDAY;
            }
            if (m.getTuesday() == '1') {
                dow = Calendar.TUESDAY;
            }
            if (m.getWednesday() == '1') {
                dow = Calendar.WEDNESDAY;
            }
            if (m.getThursday() == '1') {
                dow = Calendar.THURSDAY;
            }
            if (m.getFriday() == '1') {
                dow = Calendar.FRIDAY;
            }
            if (m.getSaturday() == '1') {
                dow = Calendar.SATURDAY;
            }
            if (dow == 0) {
                return;
            }
        }

        while (c1.before(calTo)) {
            if (first) {
                first = false;
            } else {
                c1.add(Calendar.MONTH, 1);
                c1.set(Calendar.DAY_OF_MONTH, 1);
            }

            if (interval > 1) {
                d2 = c1.get(Calendar.YEAR) * 12 + c1.get(Calendar.MONTH);
                d2 = d2 - d1;   // 月の差分
                if (d2 < 0) {
                    continue;
                } else {
                    d2 = d2 % interval;
                    if (d2 != 0) {
                        continue;
                    }
                }
            }

            c2 = (Calendar) c1.clone();

            if (m.getEventType() == Monthly.EVENT_TYPE_MONTHLY_DAY) {
                c2.set(Calendar.DAY_OF_MONTH, m.getRepDay());
            } else {
                d = c2.get(Calendar.DAY_OF_WEEK);
                if (d <= dow) {
                    d = dow - d + 1 + (m.getRepWeek() - 1) * 7;
                } else {
                    d = dow - d + 8 + (m.getRepWeek() - 1) * 7;
                }
                c2.set(Calendar.DAY_OF_MONTH, d);
            }

            flg = true;

            if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) {
                switch (m.getInvalidMove()) {
                    case '0':   // 中止
                        flg = false;
                        break;
                    case '1':   // 前にずらす
                        c2.set(Calendar.DAY_OF_MONTH, 1);
                        c2.add(Calendar.DATE, -1);
                        break;
                    case '2':   // 後ろにずらす
                        c2.set(Calendar.DAY_OF_MONTH, 1);
                        break;
                    default:
                        break;
                }
                if (!flg) {
                    continue;
                }
            }

            while (flg) {
                strWk = YuimarlUtil.calToStr(c2);

                switch (c2.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.SUNDAY:
                        if (m.getSunday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.MONDAY:
                        if (m.getMonday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.TUESDAY:
                        if (m.getTuesday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.WEDNESDAY:
                        if (m.getWednesday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.THURSDAY:
                        if (m.getThursday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.FRIDAY:
                        if (m.getFriday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.SATURDAY:
                        if (m.getSaturday() == '0') {
                            flg = false;
                        }
                        break;
                    default:
                        break;
                }

                if (m.getHoliday() == '0') {
                    if (isHoliday(strWk)) {
                        flg = false;
                    }
                }
                if (flg) {
                    break;
                }

                switch (m.getExceptMove()) {
                    case '0':   // 中止
                        break;
                    case '1':   // 前にずらす
                        c2.add(Calendar.DATE, -1);
                        flg = true;
                        break;
                    case '2':   // 後ろにずらす
                        c2.add(Calendar.DATE, 1);
                        flg = true;
                        break;
                    default:
                        break;
                }

                if (flg && m.getEventType()
                        == Monthly.EVENT_TYPE_MONTHLY_WEEK) {
                    strWk = YuimarlUtil.calToStr(c2);
                    break;
                }
            }

            if (!flg) {
                continue;
            }

            if (m.getTermFrom().compareTo(strWk) > 0
                    || m.getTermTo().compareTo(strWk) < 0) {
                // 有効期間外ならスキップ
                continue;
            }
            addEvent(m, strWk);
        }
    }

    /**
     * YearlyイベントをEventviewSpreadに展開する。
     *
     * @param y Yearlyイベント
     */
    private void yearlySpread(final Yearly y) {
        Calendar c1;
        Calendar c2;
        int interval = 1;
        int y0 = 0;
        int d2;
        boolean flg;
        String strWk = null;
        int dow = 0;
        int d;

        if (y.getTermFrom().equals("00000000")) {
            firstDay = null;
        } else {
            // イベントの有効期間の最初の日付
            firstDay = YuimarlUtil.strToCal(y.getTermFrom());
            y0 = firstDay.get(Calendar.YEAR);
            if (y.getRepInterval() != null) {
                interval = y.getRepInterval();
            }
        }

        if (y.getEventType() == Monthly.EVENT_TYPE_MONTHLY_WEEK) {
            if (y.getSunday() == '1') {
                dow = Calendar.SUNDAY;
            }
            if (y.getMonday() == '1') {
                dow = Calendar.MONDAY;
            }
            if (y.getTuesday() == '1') {
                dow = Calendar.TUESDAY;
            }
            if (y.getWednesday() == '1') {
                dow = Calendar.WEDNESDAY;
            }
            if (y.getThursday() == '1') {
                dow = Calendar.THURSDAY;
            }
            if (y.getFriday() == '1') {
                dow = Calendar.FRIDAY;
            }
            if (y.getSaturday() == '1') {
                dow = Calendar.SATURDAY;
            }
            if (dow == 0) {
                return;
            }
        }

        int y1 = calFrom.get(Calendar.YEAR);
        int y2 = calTo.get(Calendar.YEAR);

        for (int yy = y1; yy <= y2; yy++) {
            c1 = Calendar.getInstance();
            c1.clear();
            c1.set(yy, y.getRepMonth() - 1, 1);

            if (interval > 1) {
                d2 = c1.get(Calendar.YEAR);
                d2 = d2 - y0;   // 年の差分
                if (d2 < 0) {
                    continue;
                } else {
                    d2 = d2 % interval;
                    if (d2 != 0) {
                        continue;
                    }
                }
            }

            c2 = (Calendar) c1.clone();

            if (y.getEventType() == Monthly.EVENT_TYPE_MONTHLY_DAY) {
                c2.set(Calendar.DAY_OF_MONTH, y.getRepDay());
            } else {
                d = c2.get(Calendar.DAY_OF_WEEK);
                if (d <= dow) {
                    d = dow - d + 1 + (y.getRepWeek() - 1) * 7;
                } else {
                    d = dow - d + 8 + (y.getRepWeek() - 1) * 7;
                }
                c2.set(Calendar.DAY_OF_MONTH, d);
            }

            flg = true;

            if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) {
                switch (y.getInvalidMove()) {
                    case '0':   // 中止
                        flg = false;
                        break;
                    case '1':   // 前にずらす
                        c2.set(Calendar.DAY_OF_MONTH, 1);
                        c2.add(Calendar.DATE, -1);
                        break;
                    case '2':   // 後ろにずらす
                        c2.set(Calendar.DAY_OF_MONTH, 1);
                        break;
                    default:
                        break;
                }
                if (!flg) {
                    continue;
                }
            }

            while (flg) {
                strWk = YuimarlUtil.calToStr(c2);

                switch (c2.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.SUNDAY:
                        if (y.getSunday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.MONDAY:
                        if (y.getMonday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.TUESDAY:
                        if (y.getTuesday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.WEDNESDAY:
                        if (y.getWednesday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.THURSDAY:
                        if (y.getThursday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.FRIDAY:
                        if (y.getFriday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.SATURDAY:
                        if (y.getSaturday() == '0') {
                            flg = false;
                        }
                        break;
                    default:
                        break;
                }

                if (y.getHoliday() == '0') {
                    if (isHoliday(strWk)) {
                        flg = false;
                    }
                }
                if (flg) {
                    break;
                }

                switch (y.getExceptMove()) {
                    case '0':   // 中止
                        break;
                    case '1':   // 前にずらす
                        c2.add(Calendar.DATE, -1);
                        flg = true;
                        break;
                    case '2':   // 後ろにずらす
                        c2.add(Calendar.DATE, 1);
                        flg = true;
                        break;
                    default:
                        break;
                }

                if (flg && y.getEventType()
                        == Monthly.EVENT_TYPE_MONTHLY_WEEK) {
                    strWk = YuimarlUtil.calToStr(c2);
                    break;
                }
            }

            if (!flg) {
                continue;
            }

            if (y.getTermFrom().compareTo(strWk) > 0
                    || y.getTermTo().compareTo(strWk) < 0) {
                // 有効期間外ならスキップ
                continue;
            }
            addEvent(y, strWk);
        }
    }

    /**
     * 記念日イベントをEventviewSpreadに展開する。
     *
     * @param m 記念日
     */
    private void memorialdaySpread(final Holiday m) {
        Calendar c1;
        Calendar c2;
        int y0 = 0;
        boolean flg;
        String strWk = null;

        if (m.getTermFrom().equals("00000000")) {
            firstDay = null;
        } else {
            // イベントの有効期間の最初の日付
            firstDay = YuimarlUtil.strToCal(m.getTermFrom());
            y0 = firstDay.get(Calendar.YEAR);
        }

        int y1 = calFrom.get(Calendar.YEAR);
        int y2 = calTo.get(Calendar.YEAR);

        for (int yy = y1; yy <= y2; yy++) {
            c1 = Calendar.getInstance();
            c1.clear();
            c1.set(yy, m.getRepMonth() - 1, 1);
            c2 = (Calendar) c1.clone();
            c2.set(Calendar.DAY_OF_MONTH, m.getRepDay());
            flg = true;

            if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) {
                switch (m.getInvalidMove()) {
                    case '0':   // 中止
                        flg = false;
                        break;
                    case '1':   // 前にずらす
                        c2.set(Calendar.DAY_OF_MONTH, 1);
                        c2.add(Calendar.DATE, -1);
                        break;
                    case '2':   // 後ろにずらす
                        c2.set(Calendar.DAY_OF_MONTH, 1);
                        break;
                    default:
                        break;
                }
                if (!flg) {
                    continue;
                }
            }

            while (flg) {
                strWk = YuimarlUtil.calToStr(c2);

                switch (c2.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.SUNDAY:
                        if (m.getSunday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.MONDAY:
                        if (m.getMonday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.TUESDAY:
                        if (m.getTuesday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.WEDNESDAY:
                        if (m.getWednesday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.THURSDAY:
                        if (m.getThursday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.FRIDAY:
                        if (m.getFriday() == '0') {
                            flg = false;
                        }
                        break;
                    case Calendar.SATURDAY:
                        if (m.getSaturday() == '0') {
                            flg = false;
                        }
                        break;
                    default:
                        break;
                }

                if (m.getHoliday() == '0') {
                    if (isHoliday(strWk)) {
                        flg = false;
                    }
                }
                if (flg) {
                    break;
                }

                switch (m.getExceptMove()) {
                    case '0':   // 中止
                        break;
                    case '1':   // 前にずらす
                        c2.add(Calendar.DATE, -1);
                        flg = true;
                        break;
                    case '2':   // 後ろにずらす
                        c2.add(Calendar.DATE, 1);
                        flg = true;
                        break;
                    default:
                        break;
                }
            }

            if (!flg) {
                continue;
            }

            if (m.getTermFrom().compareTo(strWk) > 0
                    || m.getTermTo().compareTo(strWk) < 0) {
                // 有効期間外ならスキップ
                continue;
            }

            if (strWk.startsWith(sYear)) {
                EventviewSpread es = new EventviewSpread(m, eventviewNo, strWk);
                if (m.getLabel() != null
                        && m.getLabelStart() != null && y0 > 0) {
                    es.setName(es.getName()
                            + "(" + (yy - y0 + m.getLabelStart())
                            + m.getLabel() + ")");
                }
                em.persist(es);
            }
        }
    }

    /**
     * EventviewSpreadを登録する。
     *
     * @param e Event
     * @param date 日付
     */
    private void addEvent(final Event e, final String date) {
        if (date.startsWith(sYear)) {
            EventviewSpread es = new EventviewSpread(e, eventviewNo, date);
            em.persist(es);
        }
    }

    /**
     * 指定された日付が休日かを返す。
     *
     * @param date 日付
     * @return true:休日
     */
    private boolean isHoliday(final String date) {
        return holidays.stream().anyMatch((s) -> (s.equals(date)));
    }
}
