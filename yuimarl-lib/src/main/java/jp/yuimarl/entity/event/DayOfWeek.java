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

/**
 * 曜日列挙型
 *
 * @author Masahiro Nitta
 */
public enum DayOfWeek {

    /**
     * 日曜日
     */
    Sunday("日曜日", 1),
    /**
     * 月曜日
     */
    Monday("月曜日", 2),
    /**
     * 火曜日
     */
    Tuesday("火曜日", 3),
    /**
     * 水曜日
     */
    Wednesday("水曜日", 4),
    /**
     * 木曜日
     */
    Thursday("木曜日", 5),
    /**
     * 金曜日
     */
    Friday("金曜日", 6),
    /**
     * 土曜日
     */
    Saturday("土曜日", 7);

    /**
     * ラベル
     */
    private final String label;

    /**
     * 日
     */
    private final int value;

    /**
     * コンストラクタ
     *
     * @param label ラベル
     * @param value 日
     */
    DayOfWeek(final String label, final int value) {
        this.label = label;
        this.value = value;
    }

    /**
     * 名称取得メソッド
     * @return 名称
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * 値取得メソッド
     * @return 値
     */
    public int getValue() {
        return this.value;
    }
}
