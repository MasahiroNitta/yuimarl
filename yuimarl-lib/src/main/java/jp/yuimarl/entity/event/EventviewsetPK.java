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
 * イベントビューセット主キークラス
 * <p>
 * イベントビューセットクラスの主キー情報を保持するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Embeddable
public class EventviewsetPK implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * イベントビューNo.
     */
    @Basic(optional = false)
    @Column(name = "EVENTVIEW", nullable = false)
    private int eventview;

    /**
     * イベントセットNo.
     */
    @Basic(optional = false)
    @Column(name = "EVENTSET", nullable = false)
    private int eventset;

    /**
     * コンストラクタ
     */
    public EventviewsetPK() {
    }

    /**
     * コンストラクタ
     *
     * @param eventview イベントビューNo.
     * @param eventset イベントセットNo.
     */
    public EventviewsetPK(final int eventview, final int eventset) {
        this.eventview = eventview;
        this.eventset = eventset;
    }

    /**
     * イベントビューNo.を取得する。
     *
     * @return イベントビューNo.
     */
    public int getEventview() {
        return eventview;
    }

    /**
     * イベントビューNo.を設定する。
     *
     * @param eventview イベントビューNo.
     */
    public void setEventview(final int eventview) {
        this.eventview = eventview;
    }

    /**
     * イベントセットNo.を取得する。
     *
     * @return イベントセットNo.
     */
    public int getEventset() {
        return eventset;
    }

    /**
     * イベントセットNo.を設定する。
     *
     * @param eventset イベントセットNo.
     */
    public void setEventset(final int eventset) {
        this.eventset = eventset;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) eventview;
        hash += (int) eventset;
        return hash;
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof EventviewsetPK)) {
            return false;
        }
        EventviewsetPK other = (EventviewsetPK) object;
        if (this.eventview != other.eventview) {
            return false;
        }
        return this.eventset == other.eventset;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "jp.yuimarl.entity.EventviewsetPK[ eventview="
                + eventview + ", eventset=" + eventset + " ]";
    }

}
