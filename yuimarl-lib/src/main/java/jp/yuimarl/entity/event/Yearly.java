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
 * 年イベントクラス
 * <p>
 * 年イベントの情報を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@DiscriminatorValue(value = "6")
public class Yearly extends Monthly {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 月
     */
    @Column(name = "REP_MONTH")
    private Integer repMonth;

    /**
     * コンストラクタ
     */
    public Yearly() {
        super();
        this.setEventClass(Event.EVENT_CLASS_YEARLY);
    }

    /**
     * 月を取得する。
     *
     * @return 月
     */
    public Integer getRepMonth() {
        return repMonth;
    }

    /**
     * 月を設定する。
     *
     * @param repMonth 月
     */
    public void setRepMonth(final Integer repMonth) {
        this.repMonth = repMonth;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Yearly [No." + this.getEventNo() + "] " + this.getName();
    }

    /**
     * 他のイベントから中身をコピーする。
     *
     * @param src コピー元イベント
     */
    @Override
    public void copyContents(final Event src) {
        super.copyContents(src);
        if (src instanceof Yearly) {
            Yearly y = (Yearly) src;
            this.setRepMonth(y.getRepMonth());
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
        Yearly other = (Yearly) object;
        return (this.getEventNo() != null || other.getEventNo() == null)
                && (this.getEventNo() == null
                || this.getEventNo().equals(other.getEventNo()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.repMonth);
        return hash;
    }

}
