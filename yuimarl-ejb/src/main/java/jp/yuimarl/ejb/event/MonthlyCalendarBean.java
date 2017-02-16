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
import javax.persistence.EntityManager;
import javax.persistence.Query;
import jp.yuimarl.entity.event.DayInfo;
import jp.yuimarl.entity.event.EventviewSpread;
import jp.yuimarl.util.YuimarlUtil;

/**
 * 月カレンダー操作クラス
 * <p>
 * 月単位でイベントの情報を検索し、日付情報の配列を取得するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
public class MonthlyCalendarBean {

    /**
     * EntityManager
     */
    private final EntityManager em;

    /**
     * DayInfoのリスト
     */
    private List<DayInfo> days = null;

    /**
     * 開始日
     */
    private Calendar calFrom;

    /**
     * 終了日
     */
    private Calendar calTo;

    /**
     * 開始日
     */
    private String termFrom;

    /**
     * 終了日
     */
    private String termTo;

    /**
     * コンストラクタ
     *
     * @param em EntityManager
     */
    public MonthlyCalendarBean(final EntityManager em) {
        this.em = em;
    }

    /**
     * 月カレンダー生成処理
     * <p>
     * 指定されたEventview、年、月のイベントを取得し、DayInfoの配列を返す。<br>
     * 取得する範囲は、指定年月の前後10日を含める。（月のカレンダーは、前後の日付が表示されるのと、前後にずれるイベントがあるため。）
     * </p>
     *
     * @param eventviewNo EventviewNo.
     * @param year 年
     * @param month 月
     * @return DayInfoの配列
     */
    @SuppressWarnings("unchecked")
    public List<DayInfo> loadMonthCalendar(final int eventviewNo,
            final int year, final int month) {
        days = new ArrayList<>();
        DayInfo di;
        calFrom = Calendar.getInstance();
        calFrom.clear();
        calFrom.set(year, month - 1, 1);
        calTo = (Calendar) calFrom.clone();
        // 取得範囲の最初の日付は、指定月の1日から10日前とする。
        calFrom.add(Calendar.DATE, -10);
        calTo.add(Calendar.MONTH, 1);
        // 取得範囲の最後の日付は、指定月の翌月10日とする。
        calTo.add(Calendar.DATE, 9);
        termFrom = YuimarlUtil.calToStr(calFrom);
        termTo = YuimarlUtil.calToStr(calTo);
        Calendar cal = (Calendar) calFrom.clone();

        // 取得範囲の各日付について、DayInfoを作成し、daysリストに格納する。
        while (cal.compareTo(calTo) <= 0) {
            di = new DayInfo((Calendar) cal.clone());
            days.add(di);
            cal.add(Calendar.DATE, 1);
        }

        Query q = em.createNamedQuery("EventviewSpread.findBySpreadTerm");
        q.setParameter("eventviewNo", eventviewNo);
        q.setParameter("spreadDate1", termFrom);
        q.setParameter("spreadDate2", termTo);
        List<EventviewSpread> events = q.getResultList();
        int i = 0;
        di = days.get(i);

        for (EventviewSpread e : events) {
            while (!(e.getEventviewSpreadPK().getSpreadDate()
                    .equals(di.getYmd()))) {
                i++;
                di = days.get(i);
            }
            di.getEvents().add(e);
        }

        return days;
    }
}
