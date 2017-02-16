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
import javax.persistence.Embeddable;

/**
 * イベントビュー展開主キークラス
 * <p>
 * イベントビュー展開クラスの主キー情報を保持するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Embeddable
public class EventviewSpreadPK implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * イベントビューNo.
     */
    @Basic(optional = false)
    @Column(name = "EVENTVIEW_NO", nullable = false)
    private int eventviewNo;

    /**
     * 展開日付
     * <p>
     * yyyyMMdd
     * </p>
     */
    @Basic(optional = false)
    @Column(name = "SPREAD_DATE", length = 8, nullable = false)
    private String spreadDate;

    /**
     * イベントセットNo.
     */
    @Basic(optional = false)
    @Column(name = "EVENTSET_NO", nullable = false)
    private int eventsetNo;

    /**
     * イベントNo.
     */
    @Basic(optional = false)
    @Column(name = "EVENT_NO", nullable = false)
    private int eventNo;

    /**
     * コンストラクタ
     */
    public EventviewSpreadPK() {
    }

    /**
     * コンストラクタ
     *
     * @param eventviewNo イベントビューNo.
     * @param spreadDate 展開日付
     * @param eventsetNo イベントセットNo.
     * @param eventNo イベントNo.
     */
    public EventviewSpreadPK(final int eventviewNo, final String spreadDate,
            final int eventsetNo, final int eventNo) {
        this.eventviewNo = eventviewNo;
        this.spreadDate = spreadDate;
        this.eventsetNo = eventsetNo;
        this.eventNo = eventNo;
    }

    /**
     * イベントビューNo.を取得する。
     *
     * @return イベントビューNo.
     */
    public int getEventviewNo() {
        return eventviewNo;
    }

    /**
     * イベントビューNo.を設定する。
     *
     * @param eventviewNo イベントビューNo.
     */
    public void setEventviewNo(final int eventviewNo) {
        this.eventviewNo = eventviewNo;
    }

    /**
     * 展開日付を取得する。
     *
     * @return 展開日付
     */
    public String getSpreadDate() {
        return spreadDate;
    }

    /**
     * 展開日付を設定する。
     *
     * @param spreadDate 展開日付
     */
    public void setSpreadDate(final String spreadDate) {
        this.spreadDate = spreadDate;
    }

    /**
     * イベントセットNo.を取得する。
     *
     * @return イベントセットNo.
     */
    public int getEventsetNo() {
        return eventsetNo;
    }

    /**
     * イベントセットNo.を設定する。
     *
     * @param eventsetNo イベントセットNo.
     */
    public void setEventsetNo(final int eventsetNo) {
        this.eventsetNo = eventsetNo;
    }

    /**
     * イベントNo.を取得する。
     *
     * @return イベントNo.
     */
    public int getEventNo() {
        return eventNo;
    }

    /**
     * イベントNo.を設定する。
     *
     * @param eventNo イベントNo.
     */
    public void setEventNo(final int eventNo) {
        this.eventNo = eventNo;
    }

}
