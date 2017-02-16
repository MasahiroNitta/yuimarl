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
 * 休日・記念日イベントクラス
 * <p>
 * 休日・記念日イベントの情報を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@DiscriminatorValue(value = "1")
public class Holiday extends Yearly {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 毎年の休日
     */
    public static final int EVENT_TYPE_HOLIDAY_REGULAR = 1;
    /**
     * 不定期の休日
     */
    public static final int EVENT_TYPE_HOLIDAY_IRREGULAR = 2;
    /**
     * ハッピーマンデー
     */
    public static final int EVENT_TYPE_HOLIDAY_HAPPYMONDAY = 3;
    /**
     * 国民の休日
     */
    public static final int EVENT_TYPE_HOLIDAY_NATIONAL = 4;
    /**
     * 振替休日
     */
    public static final int EVENT_TYPE_HOLIDAY_SUBSTITUTE = 5;
    /**
     * 毎年の記念日(月/日)
     */
    public static final int EVENT_TYPE_MEMORIALDAY_REGULAR = 6;
    /**
     * 毎年のM月第W週日曜日
     */
    public static final int EVENT_TYPE_MEMORIALDAY_SUNDAY = 7;

    /**
     * ラベル
     */
    @Column(length = 10)
    private String label;

    /**
     * ラベル開始数値
     */
    @Column(name = "LABEL_START")
    private Integer labelStart;

    /**
     * コンストラクタ
     */
    public Holiday() {
        super();
        this.setEventClass(Event.EVENT_CLASS_HOLIDAY);
    }

    /**
     * ラベルを取得する。
     *
     * @return ラベル
     */
    public String getLabel() {
        return label;
    }

    /**
     * ラベルを設定する。
     *
     * @param label ラベル
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * ラベル開始数値を取得する。
     *
     * @return ラベル開始数値
     */
    public Integer getLabelStart() {
        return labelStart;
    }

    /**
     * ラベル開始数値を設定する。
     *
     * @param labelStart ラベル開始数値
     */
    public void setLabelStart(final Integer labelStart) {
        this.labelStart = labelStart;
    }

    /**
     *
     * @return 文字列
     */
    @Override
    public String toString() {
        return "Holiday [No." + this.getEventNo() + "] " + this.getName();
    }

    /**
     * 他のイベントから中身をコピーする。
     *
     * @param src コピー元イベント
     */
    @Override
    public void copyContents(final Event src) {
        super.copyContents(src);
        if (src instanceof Holiday) {
            Holiday h = (Holiday) src;
            this.setLabel(h.getLabel());
            this.setLabelStart(h.getLabelStart());
        }
    }

    /**
     * 休日かを返す。
     *
     * @return true:休日, false:休日でない
     */
    public boolean isHoliday() {
        return this.getEventType() >= EVENT_TYPE_HOLIDAY_REGULAR
                && this.getEventType() <= EVENT_TYPE_HOLIDAY_SUBSTITUTE;
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
        Holiday other = (Holiday) object;
        return (this.getEventNo() != null || other.getEventNo() == null)
                && (this.getEventNo() == null
                || this.getEventNo().equals(other.getEventNo()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.label);
        hash = 41 * hash + Objects.hashCode(this.labelStart);
        return hash;
    }
}
