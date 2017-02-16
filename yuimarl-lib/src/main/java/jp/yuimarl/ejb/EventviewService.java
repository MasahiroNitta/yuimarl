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
package jp.yuimarl.ejb;

import java.util.List;
import javax.ejb.Local;
import jp.yuimarl.entity.event.Event;

/**
 * Eventview操作インターフェース
 *
 * @author Masahiro Nitta
 */
@Local
public interface EventviewService {

    /**
     * 指定された年の範囲の日本の休日を展開する。
     *
     * @param year1 年1
     * @param year2 年2
     */
    void spreadJapaneseHoliday(int year1, int year2);

    /**
     *
     * @param eventviewNo EventView No.
     * @param termFrom 期間開始日
     * @param termTo 期間終了日
     * @return Eventのリスト
     */
    List<Event> findByEventviewTerm(int eventviewNo, String termFrom,
            String termTo);

    /**
     * 指定されたイベントビューNo.のイベントを、指定年の1年分、
     * EventviewSpreadに展開する。
     *
     * @param eventviewNo イベントビューNo.
     * @param year 年
     */
    void spreadEvent(int eventviewNo, int year);
}
