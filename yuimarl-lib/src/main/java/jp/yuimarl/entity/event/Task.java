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
 * タスククラス
 * <p>
 * タスクの情報を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@DiscriminatorValue(value = "7")
public class Task extends Event {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 日数
     */
    @Column(name = "DAY_COUNT")
    private Integer dayCount;

    /**
     * 先行タスク
     */
    @Column(name = "PRE_TASK", length = 256)
    private String preTask;

    /**
     * 進捗率
     */
    private Integer progress;

    /**
     * 完了フラグ
     * <p>
     * 1:完了済み
     * </p>
     */
    private Character finished = '0';

    /**
     * コンストラクタ
     */
    public Task() {
        super();
        this.setEventClass(Event.EVENT_CLASS_TASK);
    }

    /**
     * 日数を取得する。
     *
     * @return 日数
     */
    public Integer getDayCount() {
        return dayCount;
    }

    /**
     * 日数を設定する。
     *
     * @param dayCount 日数
     */
    public void setDayCount(final Integer dayCount) {
        this.dayCount = dayCount;
    }

    /**
     * 先行タスクを取得する。
     *
     * @return 先行タスク
     */
    public String getPreTask() {
        return preTask;
    }

    /**
     * 先行タスクを設定する。
     *
     * @param preTask 先行タスク
     */
    public void setPreTask(final String preTask) {
        this.preTask = preTask;
    }

    /**
     * 進捗率を取得する。
     *
     * @return 進捗率
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * 進捗率を設定する。
     *
     * @param progress 進捗率
     */
    public void setProgress(final Integer progress) {
        this.progress = progress;
    }

    /**
     * 完了フラグを取得する。
     *
     * @return 完了フラグ
     */
    public Character getFinished() {
        return finished;
    }

    /**
     * 完了フラグを設定する。
     *
     * @param finished 完了フラグ
     */
    public void setFinished(final Character finished) {
        this.finished = finished;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Task [No." + this.getEventNo() + "] " + this.getName();
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
        Task other = (Task) object;
        return (this.getEventNo() != null || other.getEventNo() == null)
                && (this.getEventNo() == null
                || this.getEventNo().equals(other.getEventNo()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.dayCount);
        hash = 79 * hash + Objects.hashCode(this.preTask);
        hash = 79 * hash + Objects.hashCode(this.progress);
        hash = 79 * hash + Objects.hashCode(this.finished);
        return hash;
    }

}
