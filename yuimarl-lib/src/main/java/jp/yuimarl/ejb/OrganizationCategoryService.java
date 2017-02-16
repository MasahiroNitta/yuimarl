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
import jp.yuimarl.entity.OrganizationCategory;

/**
 * 組織種別操作インターフェース
 *
 * @author Masahiro Nitta
 */
@Local
public interface OrganizationCategoryService {

    /**
     * 登録する。
     *
     * @param entity OrganizationCategory
     */
    void create(OrganizationCategory entity);

    /**
     * 更新する。
     *
     * @param entity OrganizationCategory
     */
    void edit(OrganizationCategory entity);

    /**
     * 削除する。
     *
     * @param entity OrganizationCategory
     */
    void remove(OrganizationCategory entity);

    /**
     * 検索する。
     *
     * @param range 範囲
     * @return OrganizationCategoryのリスト
     */
    List<OrganizationCategory> findRange(int[] range);

    /**
     * すべての組織種別を取得する。
     *
     * @return すべての組織種別
     */
    List<OrganizationCategory> findAllOrganization();

    /**
     * すべての法人種別を取得する。
     *
     * @return すべての法人種別
     */
    List<OrganizationCategory> findAllCorporation();

    /**
     * カテゴリNo.を指定して組織種別を取得する。
     *
     * @param categoryNo カテゴリNo.
     * @return OrganizationCategory
     */
    OrganizationCategory getOrganizationCategoryByCategoryNo(
            Integer categoryNo);

}
