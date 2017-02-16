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
package jp.yuimarl.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 物品クラス
 * <p>
 * Partyクラスから継承。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@DiscriminatorValue(value = "4")
public class Goods extends Party {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 種類
     */
    @ManyToOne
    @JoinColumn(name = "CATEGORY_NO", referencedColumnName = "CATEGORY_NO")
    private GoodsCategory category;

    /**
     * コンストラクタ
     */
    public Goods() {
        super();
        this.setPartyType(Party.PARTY_TYPE_GOODS);
    }

    /**
     * 種類を取得する。
     *
     * @return category
     */
    public GoodsCategory getCategory() {
        return category;
    }

    /**
     * 種類を設定する。
     *
     * @param category セットする category
     */
    public void setCategory(final GoodsCategory category) {
        this.category = category;
    }

    /**
     * カテゴリー番号を取得する。
     *
     * @return カテゴリー番号
     */
    public Integer getCategoryNo() {
        if (category == null) {
            return 0;
        }
        return category.getCategoryNo();
    }

    /**
     * 種類名を取得する。
     *
     * @return
     */
    @Override
    public String getCategoryName() {
        GoodsCategory gc = this.getCategory();
        if (gc == null) {
            return "物品";
        }

        return gc.getName();
    }

}
