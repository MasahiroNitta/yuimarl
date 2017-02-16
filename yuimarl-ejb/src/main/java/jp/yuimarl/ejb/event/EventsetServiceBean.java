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

import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jp.yuimarl.ejb.AbstractFacade;
import jp.yuimarl.ejb.EventsetService;
import jp.yuimarl.entity.event.Event;
import jp.yuimarl.entity.event.Eventset;
import jp.yuimarl.entity.event.EventviewSpread;
import jp.yuimarl.entity.event.Holiday;
import jp.yuimarl.util.YuimarlUtil;

/**
 * イベントセット操作クラス
 *
 * @author Masahiro Nitta
 */
@Stateless
public class EventsetServiceBean extends AbstractFacade<Eventset>
        implements EventsetService {

    /**
     * EntityManager
     */
    @PersistenceContext(unitName = "yuimarlPU")
    private EntityManager em;

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
     * 検索条件を指定して件数を取得する。
     *
     * @param cond 検索条件
     * @return 件数
     */
    @Override
    public int condCount(final String cond) {
        Query q = em.createQuery(
                "SELECT COUNT(e) FROM Eventset e WHERE e.delFlg = '0' "
                        + cond);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * 検索条件を指定してプロジェクト件数を取得する。
     *
     * @param cond 検索条件
     * @return プロジェクト件数
     */
    @Override
    public int condProjectCount(final String cond) {
        Query q = em.createQuery(
                "SELECT COUNT(e) FROM Eventset e "
                        + "WHERE e.eventsetType = '1' AND e.delFlg = '0' "
                        + cond);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * イベントセットNo.を指定してEventsetを取得する。
     *
     * @param eventsetNo イベントセットNo.
     * @return Eventset
     */
    @Override
    public Eventset getEventsetByEventsetNo(final Integer eventsetNo) {
        Query createNamedQuery = em.createNamedQuery(
                "Eventset.findByEventsetNo");

        createNamedQuery.setParameter("eventsetNo", eventsetNo);

        if (createNamedQuery.getResultList().size() > 0) {
            return (Eventset) createNamedQuery.getSingleResult();
        }

        return null;
    }

    /**
     * 検索条件と範囲を指定してEventsetを取得する。
     *
     * @param condition 検索条件
     * @param range 範囲
     * @return Eventsetのリスト
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Eventset> findCondRange(final String condition,
            final int[] range) {
        Query q = em.createQuery(
                "SELECT e FROM Eventset e WHERE e.delFlg = '0' "
                        + condition + " ORDER BY e.termFrom DESC");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * 検索条件と範囲を指定してプロジェクトを取得する。
     *
     * @param condition 検索条件
     * @param range 範囲
     * @return Eventsetのリスト
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Eventset> findProjectCondRange(final String condition,
            final int[] range) {
        Query q = em.createQuery(
                "SELECT e FROM Eventset e "
                        + "WHERE e.eventsetType = '1' AND e.delFlg = '0' "
                        + condition + " ORDER BY e.termFrom DESC");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     *
     * @param eventset Eventset
     * @return JSON
     */
    @Override
    @SuppressWarnings("unchecked")
    public String eventsetDaysJson(final Eventset eventset) {
        if (eventset == null || eventset.getTermFrom() == null
                || eventset.getTermTo() == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Query q;
        EventviewSpread e;
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(eventset.getTermFrom());
        Calendar calTo = Calendar.getInstance();
        calTo.setTime(eventset.getTermTo());
        //Calendar calFrom = YuimarlUtil.strToCal(eventset.getTermFrom());
        //Calendar calTo = YuimarlUtil.strToCal(eventset.getTermTo());
        Calendar cal = (Calendar) calFrom.clone();
        q = em.createNamedQuery("EventviewSpread.findBySpreadTerm");
        q.setParameter("eventviewNo", 1);
        q.setParameter("spreadDate1",
                YuimarlUtil.dtToStr2(eventset.getTermFrom()));
        q.setParameter("spreadDate2",
                YuimarlUtil.dtToStr2(eventset.getTermTo()));
        List<EventviewSpread> events = q.getResultList();
        int i = 0;
        String dt1, dt2;
        boolean isFirst = true;
        boolean isHoliday;
        boolean isRest;
        sb.append("[");
        while (cal.compareTo(calTo) <= 0) {
            dt2 = YuimarlUtil.calToStr(cal);
            isHoliday = false;
            isRest = false;
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(",");
            }

            for (int j = i; j < events.size(); j++) {
                e = events.get(j);
                dt1 = e.getEventviewSpreadPK().getSpreadDate();
                if (dt1.compareTo(dt2) > 0) {
                    break;
                }
                if (dt1.equals(dt2)) {
                    if (e.getEventClass().equals(Event.EVENT_CLASS_HOLIDAY)) {
                        if (e.getEventType()
                                >= Holiday.EVENT_TYPE_HOLIDAY_REGULAR
                                && e.getEventType()
                                <= Holiday.EVENT_TYPE_HOLIDAY_SUBSTITUTE) {
                            isHoliday = true;
                            if (eventset.getHoliday() == '0') {
                                isRest = true;
                            }
                        }
                    }
                } else {
                    i = j;
                }
            }

            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    if (eventset.getSunday() == '0') {
                        isRest = true;
                    }
                    break;
                case Calendar.SATURDAY:
                    if (eventset.getSaturday() == '0') {
                        isRest = true;
                    }
                    break;
                default:
                    break;
            }

            if (!isRest && eventset.getExcludeDays() != null
                    && eventset.getExcludeDays().length() > 0) {
                String[] list = eventset.getExcludeDays().split(",");
                for (String s : list) {
                    if (s.equals(dt2)) {
                        isRest = true;
                        break;
                    }
                }
            }

            if (isRest && eventset.getIncludeDays() != null
                    && eventset.getIncludeDays().length() > 0) {
                String[] list = eventset.getIncludeDays().split(",");
                for (String s : list) {
                    if (s.equals(dt2)) {
                        isRest = false;
                        break;
                    }
                }
            }

            sb.append("{\"y\": ").append(cal.get(Calendar.YEAR))
                    .append(", \"m\": ").append(cal.get(Calendar.MONTH) + 1)
                    .append(", \"d\": ").append(cal.get(Calendar.DAY_OF_MONTH))
                    .append(", \"w\": ").append(cal.get(Calendar.DAY_OF_WEEK))
                    .append(", \"h\": ");
            if (isHoliday) {
                sb.append("true");
            } else {
                sb.append("false");
            }
            sb.append(", \"rest\": ");
            if (isRest) {
                sb.append("true");
            } else {
                sb.append("false");
            }
            sb.append("}");
            cal.add(Calendar.DATE, 1);
        }

        sb.append("]");
        return sb.toString();
    }

}
