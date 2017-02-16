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

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import static jp.yuimarl.entity.event.Event.EVENT_CLASS_SINGLE;

/**
 * イベントビュー展開クラス
 * <p>
 * イベントビュー展開の情報を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "YM_EVENTVIEW_SPREAD")
@NamedQueries({
    @NamedQuery(name = "EventviewSpread.findAll",
            query = "SELECT e FROM EventviewSpread e"),
    @NamedQuery(name = "EventviewSpread.findBySpreadTerm",
            query = "SELECT e FROM EventviewSpread e "
                    + "WHERE e.eventviewSpreadPK.eventviewNo = :eventviewNo "
                    + "AND e.eventviewSpreadPK.spreadDate >= :spreadDate1 "
                    + "AND e.eventviewSpreadPK.spreadDate <= :spreadDate2 "
                    + "ORDER BY e.eventviewSpreadPK.spreadDate, "
                    + "e.startTime, e.endTime, e.eventClass"),
    @NamedQuery(name = "EventviewSpread.deleteBySpreadDate",
            query = "DELETE FROM EventviewSpread e "
                    + "WHERE e.eventviewSpreadPK.eventNo = :eventNo "
                    + "AND e.eventviewSpreadPK.spreadDate = :spreadDate"),
    @NamedQuery(name = "EventviewSpread.deleteBySpreadTerm",
            query = "DELETE FROM EventviewSpread e "
                    + "WHERE e.eventviewSpreadPK.eventviewNo = :eventviewNo "
                    + "AND e.eventviewSpreadPK.spreadDate > :spreadDate1 "
                    + "AND e.eventviewSpreadPK.spreadDate < :spreadDate2")})
public class EventviewSpread implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * イベント展開主キー
     */
    @EmbeddedId
    private EventviewSpreadPK eventviewSpreadPK;

    /**
     * 開始時刻
     * <p>
     * HHmm<br/>
     * " ":終日
     * </p>
     */
    @Column(name = "START_TIME", length = 4)
    private String startTime = " ";

    /**
     * 終了時刻
     * <p>
     * HHmm<br/>
     * " ":終日
     * </p>
     */
    @Column(name = "END_TIME", length = 4)
    private String endTime = " ";

    /**
     * イベント型
     * <p>
     * 1:休日・記念日, 2:単イベント, 3:日イベント, 4:週イベント, 5:月イベント, 6:年イベント, 7:タスク
     * </p>
     */
    @Basic(optional = false)
    @Column(name = "EVENT_CLASS")
    private Character eventClass = EVENT_CLASS_SINGLE;

    /**
     * 詳細種別
     */
    @Column(name = "EVENT_TYPE")
    private Integer eventType = 0;

    /**
     * 名前
     */
    @Column(length = 50)
    private String name;

    /**
     * 複数日イベント
     * <p>
     * 0:単日, 1:開始日, 2:中間, 3:終了日
     * </p>
     */
    @Basic(optional = false)
    @Column(name = "EVENT_TERM")
    private Character eventTerm = '0';

    /**
     * 表示色
     * <p>
     * 16進数RGB, 例: 白=FFFFFF
     * </p>
     */
    @Column(length = 6)
    private String color;

    /**
     * コンストラクタ
     */
    public EventviewSpread() {
    }

    /**
     * コンストラクタ
     *
     * @param eventviewNo イベントビューNo.
     * @param spreadDate 展開日
     * @param eventsetNo イベントセットNo.
     * @param eventNo イベントNo.
     */
    public EventviewSpread(final int eventviewNo, final String spreadDate,
            final int eventsetNo, final int eventNo) {
        this.eventviewSpreadPK = new EventviewSpreadPK(
                eventviewNo, spreadDate, eventsetNo, eventNo);
    }

    /**
     * コンストラクタ
     *
     * @param event イベント
     * @param eventviewNo イベントビューNo.
     * @param spreadDate 展開日
     */
    public EventviewSpread(final Event event, final int eventviewNo,
            final String spreadDate) {
        this.eventviewSpreadPK = new EventviewSpreadPK(eventviewNo, spreadDate,
                event.getEventsetNo(), event.getEventNo());
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.eventClass = event.getEventClass();
        this.eventType = event.getEventType();
        this.name = event.getName();
        this.color = event.getColor();
    }

    /**
     * イベントビュー展開主キーを取得する。
     *
     * @return イベントビュー展開主キー
     */
    public EventviewSpreadPK getEventviewSpreadPK() {
        return eventviewSpreadPK;
    }

    /**
     * イベントビュー展開主キーを設定する。
     *
     * @param eventviewSpreadPK イベントビュー展開主キー
     */
    public void setEventviewSpreadPK(
            final EventviewSpreadPK eventviewSpreadPK) {
        this.eventviewSpreadPK = eventviewSpreadPK;
    }

    /**
     * 開始時刻を取得する。
     *
     * @return 開始時刻
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 開始時刻を設定する。
     *
     * @param startTime 開始時刻
     */
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    /**
     * 終了時刻を取得する。
     *
     * @return 終了時刻
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 終了時刻を設定する。
     *
     * @param endTime 終了時刻
     */
    public void setEndTime(final String endTime) {
        this.endTime = endTime;
    }

    /**
     * イベント型を取得する。
     *
     * @return イベント型
     */
    public Character getEventClass() {
        return eventClass;
    }

    /**
     * イベント型を設定する。
     *
     * @param eventClass イベント型
     */
    public void setEventClass(final Character eventClass) {
        this.eventClass = eventClass;
    }

    /**
     * 詳細種別を取得する。
     *
     * @return 詳細種別
     */
    public Integer getEventType() {
        return eventType;
    }

    /**
     * 詳細種別を設定する。
     *
     * @param eventType 詳細種別
     */
    public void setEventType(final Integer eventType) {
        this.eventType = eventType;
    }

    /**
     * 名前を取得する。
     *
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定する。
     *
     * @param name 名前
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * 複数日イベントを取得する。
     *
     * @return 複数日イベント
     */
    public Character getEventTerm() {
        return eventTerm;
    }

    /**
     * 複数日イベントを設定する。
     *
     * @param eventTerm 複数日イベント
     */
    public void setEventTerm(final Character eventTerm) {
        this.eventTerm = eventTerm;
    }

    /**
     * 表示色を取得する。
     *
     * @return 表示色
     */
    public String getColor() {
        return color;
    }

    /**
     * 表示色を設定する。
     *
     * @param color 表示色
     */
    public void setColor(final String color) {
        this.color = color;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "EventviewSpread / " + this.getStartTime() + " / "
                + this.getEndTime() + " / " + this.getName();
    }

}
