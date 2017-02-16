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

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 月イベントクラス
 * <p>
 * 月イベントの情報を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@DiscriminatorValue(value = "5")
public class Monthly extends Daily {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 日
     */
    public static final int EVENT_TYPE_MONTHLY_DAY = 11;

    /**
     * 週
     */
    public static final int EVENT_TYPE_MONTHLY_WEEK = 12;

    /**
     * 日
     */
    @Column(name = "REP_DAY")
    private Integer repDay;

    /**
     * 週
     */
    @Column(name = "REP_WEEK")
    private Integer repWeek;

    /**
     * 無効日の扱い
     * <p>
     * 0:中止, 1:前にずらす, 2:後ろにずらす
     * </p>
     */
    @Column(name = "INVALID_MOVE")
    private Character invalidMove = '0';

    /**
     * コンストラクタ
     */
    public Monthly() {
        super();
        this.setEventClass(Event.EVENT_CLASS_MONTHLY);
    }

    /**
     * 日を取得する。
     *
     * @return 日
     */
    public Integer getRepDay() {
        return repDay;
    }

    /**
     * 日を設定する。
     *
     * @param repDay 日
     */
    public void setRepDay(final Integer repDay) {
        this.repDay = repDay;
    }

    /**
     * 週を取得する。
     *
     * @return 週
     */
    public Integer getRepWeek() {
        return repWeek;
    }

    /**
     * 週を設定する。
     *
     * @param repWeek 週
     */
    public void setRepWeek(final Integer repWeek) {
        this.repWeek = repWeek;
    }

    /**
     * 無効日の扱いを取得する。
     *
     * @return 無効日の扱い
     */
    public Character getInvalidMove() {
        return invalidMove;
    }

    /**
     * 無効日の扱いを設定する。
     *
     * @param invalidMove 無効日の扱い
     */
    public void setInvalidMove(final Character invalidMove) {
        this.invalidMove = invalidMove;
    }

    /**
     *
     * @return 文字列
     */
    @Override
    public String toString() {
        return "Monthly [No." + this.getEventNo() + "] " + this.getName();
    }

    /**
     * 他のイベントから中身をコピーする。
     *
     * @param src コピー元イベント
     */
    @Override
    public void copyContents(final Event src) {
        super.copyContents(src);
        if (src instanceof Monthly) {
            Monthly m = (Monthly) src;
            this.setRepDay(m.getRepDay());
            this.setRepWeek(m.getRepWeek());
            this.setInvalidMove(m.getInvalidMove());
        }
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(final Object object) {
        if (object == null || !(object.getClass().toString().equals(
                this.getClass().toString()))) {
            return false;
        }
        Monthly other = (Monthly) object;
        return (this.getEventNo() != null || other.getEventNo() == null)
                && (this.getEventNo() == null
                || this.getEventNo().equals(other.getEventNo()));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.repDay);
        hash = 97 * hash + Objects.hashCode(this.repWeek);
        hash = 97 * hash + Objects.hashCode(this.invalidMove);
        return hash;
    }

}
