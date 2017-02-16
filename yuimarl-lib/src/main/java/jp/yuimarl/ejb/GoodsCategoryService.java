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
import jp.yuimarl.entity.GoodsCategory;

/**
 * 物品カテゴリー操作インターフェース
 *
 * @author Masahiro Nitta
 */
@Local
public interface GoodsCategoryService {

    /**
     * 登録する。
     *
     * @param entity GoodsCategory
     */
    void create(GoodsCategory entity);

    /**
     * 更新する。
     *
     * @param entity GoodsCategory
     */
    void edit(GoodsCategory entity);

    /**
     * 削除する。
     *
     * @param entity GoodsCategory
     */
    void remove(GoodsCategory entity);

    /**
     * 検索する。
     *
     * @param range 範囲
     * @return GoodsCategoryのリスト
     */
    List<GoodsCategory> findRange(int[] range);

    /**
     * すべての物品カテゴリーを取得する。
     *
     * @return すべての物品カテゴリー
     */
    List<GoodsCategory> findAll();

    /**
     * カテゴリNo.を指定して物品カテゴリーを取得する。
     *
     * @param categoryNo カテゴリNo.
     * @return GoodsCategory
     */
    GoodsCategory findByCategoryNo(Integer categoryNo);

    /**
     * dynatreeのツリー用タグを生成する。
     *
     * @param keyPlusKana true:カナ名を付加する
     * @return ツリー用タグ
     */
    String makeTag(boolean keyPlusKana);

    /**
     * 物品カテゴリーを削除する。
     * <p>
     * 削除される物品カテゴリーを使用している物品のカテゴリーのクリアも行う。
     * </p>
     *
     * @param gc 物品カテゴリー
     */
    void deleteWithGoods(GoodsCategory gc);
}
