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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * 物品カテゴリクラス
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "YM_GOODS_CATEGORY")
@NamedQueries({
    @NamedQuery(name = "GoodsCategory.findAll",
            query = "SELECT g FROM GoodsCategory g "
                    + "WHERE g.delFlg = '0' ORDER BY g.nameKana"),
    @NamedQuery(name = "GoodsCategory.findByCategoryNo",
            query = "SELECT g FROM GoodsCategory g "
                    + "WHERE g.delFlg = '0' AND g.categoryNo = :categoryNo")})
public class GoodsCategory implements EntityBasic, Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * カテゴリNo.
     */
    @TableGenerator(name = "CATEGORY_NO_GEN", table = "YM_GENERATED_ID",
            pkColumnName = "KEY_NAME", valueColumnName = "VALUE",
            pkColumnValue = "CATEGORY_NO_MAX", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "CATEGORY_NO_GEN")
    @Column(name = "CATEGORY_NO")
    private Integer categoryNo;

    /**
     * 名前
     */
    @Column(length = 50)
    private String name;

    /**
     * 名前ヨミ
     */
    @Column(name = "NAME_KANA", length = 50)
    private String nameKana;

    /**
     * 親カテゴリ
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_CATEGORY", referencedColumnName = "CATEGORY_NO")
    private GoodsCategory parentCategory;

    /**
     * 削除フラグ
     * <p>
     * 0:有効, 1:削除済み
     * </p>
     */
    @Column(name = "DEL_FLG")
    private Character delFlg = '0';

    /**
     * 登録ユーザー
     */
    @ManyToOne
    @JoinColumn(name = "REGIST_USER", referencedColumnName = "USER_NO")
    private YuimarlUser registUser;

    /**
     * 登録日時
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGIST_TIME")
    private Date registTime;

    /**
     * 更新ユーザー
     */
    @ManyToOne
    @JoinColumn(name = "UPDATE_USER", referencedColumnName = "USER_NO")
    private YuimarlUser updateUser;

    /**
     * 更新日時
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     * バージョン番号
     */
    @Version
    @Column(name = "VERSION_NO")
    private Integer versionNo;

    /**
     * コンストラクタ
     */
    public GoodsCategory() {
    }

    /**
     * カテゴリNo.を取得する。
     *
     * @return カテゴリNo.
     */
    public Integer getCategoryNo() {
        return this.categoryNo;
    }

    /**
     * カテゴリNo.を設定する。
     *
     * @param categoryNo カテゴリNo.
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
     * 名前ヨミを取得する。
     *
     * @return 名前ヨミ
     */
    public String getNameKana() {
        return this.nameKana;
    }

    /**
     * 名前ヨミを設定する。
     *
     * @param nameKana 名前ヨミ
     */
    public void setNameKana(final String nameKana) {
        this.nameKana = nameKana;
    }

    /**
     * 親カテゴリを取得する。
     *
     * @return 親カテゴリ
     */
    public GoodsCategory getParentCategory() {
        return this.parentCategory;
    }

    /**
     * 親カテゴリを設定する。
     *
     * @param parentCategory 親カテゴリ
     */
    public void setParentCategory(final GoodsCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    /**
     * 親カテゴリNo.を取得する。
     *
     * @return 親カテゴリNo.
     */
    public int getParentCategoryNo() {
        GoodsCategory p = this.getParentCategory();
        if (p == null) {
            return 0;
        }
        return p.categoryNo;
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

    /**
     * 登録ユーザーを取得する。
     *
     * @return registUser 登録ユーザー
     */
    @Override
    public YuimarlUser getRegistUser() {
        return registUser;
    }

    /**
     * 登録ユーザーを設定する。
     *
     * @param registUser 登録ユーザー
     */
    @Override
    public void setRegistUser(final YuimarlUser registUser) {
        this.registUser = registUser;
    }

    /**
     * 登録日時を取得する。
     *
     * @return registTime 登録日時
     */
    @Override
    public Date getRegistTime() {
        if (registTime == null) {
            return null;
        }
        return (Date) registTime.clone();
    }

    /**
     * 登録日時を設定する。
     *
     * @param registTime 登録日時
     */
    @Override
    public void setRegistTime(final Date registTime) {
        if (registTime == null) {
            this.registTime = null;
        } else {
            this.registTime = (Date) registTime.clone();
        }
    }

    /**
     * 更新ユーザーを取得する。
     *
     * @return updateUser 更新ユーザー
     */
    @Override
    public YuimarlUser getUpdateUser() {
        return updateUser;
    }

    /**
     * 更新ユーザーを設定する。
     *
     * @param updateUser 更新ユーザー
     */
    @Override
    public void setUpdateUser(final YuimarlUser updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 更新日時を取得する。
     *
     * @return updateTime 更新日時
     */
    @Override
    public Date getUpdateTime() {
        if (updateTime == null) {
            return null;
        }
        return (Date) updateTime.clone();
    }

    /**
     * 更新日時を設定する。
     *
     * @param updateTime 更新日時
     */
    @Override
    public void setUpdateTime(final Date updateTime) {
        if (updateTime == null) {
            this.updateTime = null;
        } else {
            this.updateTime = (Date) updateTime.clone();
        }
    }

    /**
     * バージョン番号を取得する。
     *
     * @return バージョン番号
     */
    @Override
    public Integer getVersionNo() {
        return this.versionNo;
    }

    /**
     * バージョン番号を設定する。
     *
     * @param versionNo バージョン番号
     */
    @Override
    public void setVersionNo(final Integer versionNo) {
        this.versionNo = versionNo;
    }
}
