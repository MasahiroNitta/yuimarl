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

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 組織種類クラス
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "YM_ORG_CATEGORY")
@NamedQueries({
    @NamedQuery(name = "OrganizationCategory.findAllOrganization",
            query = "SELECT c FROM OrganizationCategory c "
                    + "WHERE c.delFlg = '0' AND c.partyType = '2' "
                    + "ORDER BY c.categoryNo"),
    @NamedQuery(name = "OrganizationCategory.findAllCorporation",
            query = "SELECT c FROM OrganizationCategory c "
                    + "WHERE c.delFlg = '0' AND c.partyType = '3' "
                    + "ORDER BY c.categoryNo"),
    @NamedQuery(name = "OrganizationCategory.findByCategoryNo",
            query = "SELECT c FROM OrganizationCategory c "
                    + "WHERE c.delFlg = '0' AND c.categoryNo = :categoryNo")})
public class OrganizationCategory implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 種類No.
     */
    @Id
    @Column(name = "CATEGORY_NO")
    private Integer categoryNo;

    /**
     * 名前
     */
    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * Party種別
     * <p>
     * 1:人, 2:組織, 3:法人, 4:物品
     * </p>
     */
    @Basic(optional = false)
    @Column(name = "PARTY_TYPE", nullable = false)
    private String partyType;

    /**
     * 削除フラグ
     * <p>
     * 0:有効, 1:削除済み
     * </p>
     */
    @Column(name = "DEL_FLG")
    private Character delFlg = '0';

    /**
     * コンストラクタ
     */
    public OrganizationCategory() {
    }

    /**
     * 種類No.を取得する。
     *
     * @return 種類No.
     */
    public Integer getCategoryNo() {
        return categoryNo;
    }

    /**
     * 種類No.を設定する。
     *
     * @param categoryNo 種類No.
     */
    public void setCategoryNo(final Integer categoryNo) {
        this.categoryNo = categoryNo;
    }

    /**
     * 名前を取得する。
     *
     * @return 名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * 名前を設定する。
     *
     * @param name 名前
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Party種別を取得する。
     *
     * @return Party種別
     */
    public String getPartyType() {
        return partyType;
    }

    /**
     * Party種別を設定する。
     *
     * @param partyType Party種別
     */
    public void setPartyType(final String partyType) {
        this.partyType = partyType;
    }

    /**
     * 削除フラグを取得する。
     *
     * @return delFlg 削除フラグ
     */
    public Character getDelFlg() {
        return delFlg;
    }

    /**
     * 削除フラグを設定する。
     *
     * @param delFlg 削除フラグ
     */
    public void setDelFlg(final Character delFlg) {
        this.delFlg = delFlg;
    }

}
