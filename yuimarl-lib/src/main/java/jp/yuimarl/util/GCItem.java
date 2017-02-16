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
package jp.yuimarl.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 物品カテゴリークラス
 * <p>
 * 物品カテゴリー画面で、物品カテゴリーの階層構造を保持するために使用する。
 * </p>
 *
 * @author Masahiro Nitta
 */
public class GCItem {

    /**
     * 物品カテゴリNo.
     */
    private Integer categoryNo = 0;
    /**
     * 名前
     */
    private String name = "";
    /**
     * 名前よみ
     */
    private String nameKana = "";
    /**
     * 親カテゴリ
     */
    private int parent = 0;
    /**
     * 階層レベル
     */
    private int level = 0;
    /**
     * 子カテゴリのリスト
     */
    private List<GCItem> children = new ArrayList<>();

    /**
     *
     * @return 物品カテゴリNo.
     */
    public Integer getCategoryNo() {
        return categoryNo;
    }

    /**
     *
     * @param categoryNo 物品カテゴリNo.
     */
    public void setCategoryNo(final Integer categoryNo) {
        this.categoryNo = categoryNo;
    }

    /**
     *
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name 名前
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return 名前よみ
     */
    public String getNameKana() {
        return nameKana;
    }

    /**
     *
     * @param nameKana 名前よみ
     */
    public void setNameKana(final String nameKana) {
        this.nameKana = nameKana;
    }

    /**
     *
     * @return 親カテゴリ
     */
    public int getParent() {
        return parent;
    }

    /**
     *
     * @param parent 親カテゴリ
     */
    public void setParent(final int parent) {
        this.parent = parent;
    }

    /**
     *
     * @return 階層レベル
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @param level 階層レベル
     */
    public void setLevel(final int level) {
        this.level = level;
    }

    /**
     *
     * @return 子カテゴリのリスト
     */
    public List<GCItem> getChildren() {
        return children;
    }

    /**
     *
     * @param children 子カテゴリのリスト
     */
    public void setChildren(final List<GCItem> children) {
        this.children = children;
    }

}
