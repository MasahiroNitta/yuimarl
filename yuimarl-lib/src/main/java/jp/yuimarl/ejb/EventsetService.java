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
import jp.yuimarl.entity.event.Eventset;

/**
 * Eventset操作インターフェース
 *
 * @author Masahiro Nitta
 */
@Local
public interface EventsetService {

    /**
     * 登録する。
     *
     * @param entity エンティティ
     */
    void create(Eventset entity);

    /**
     * 更新する。
     *
     * @param entity エンティティ
     */
    void edit(Eventset entity);

    /**
     * 削除する。
     *
     * @param entity エンティティ
     */
    void remove(Eventset entity);

    /**
     * 検索する。
     *
     * @param range 範囲
     * @return Eventsetのリスト
     */
    List<Eventset> findRange(int[] range);

    /**
     * すべての件数を取得する。
     *
     * @return 件数
     */
    int count();

    /**
     * 検索条件を指定して件数を取得する。
     *
     * @param cond 検索条件
     * @return 件数
     */
    int condCount(String cond);

    /**
     * 検索条件を指定してプロジェクト件数を取得する。
     *
     * @param cond 検索条件
     * @return 件数
     */
    int condProjectCount(String cond);

    /**
     * イベントセットNo.を指定してEventsetを取得する。
     *
     * @param eventsetNo イベントセットNo.
     * @return Eventset
     */
    Eventset getEventsetByEventsetNo(Integer eventsetNo);

    /**
     * 検索条件と範囲を指定してEventsetを取得する。
     *
     * @param condition 検索条件
     * @param range 範囲
     * @return Eventsetのリスト
     */
    List<Eventset> findCondRange(String condition, int[] range);

    /**
     * 検索条件と範囲を指定してプロジェクトを取得する。
     *
     * @param condition 検索条件
     * @param range 範囲
     * @return Eventsetのリスト
     */
    List<Eventset> findProjectCondRange(String condition, int[] range);

    /**
     *
     * @param eventset Eventset
     * @return JSON
     */
     String eventsetDaysJson(Eventset eventset);
}
