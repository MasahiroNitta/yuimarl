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
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import jp.yuimarl.util.YuimarlUtil;

/**
 * パーティ関連クラス
 * <p>
 * パーティ間の関連情報を保持する。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "YM_PARTY_RELATION")
public class PartyRelation implements EntityBasic, Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 組織←→創立者
     */
    public static final Integer RELATION_TYPE_FOUNDER = 1;
    /**
     * 組織←→代表者
     */
    public static final Integer RELATION_TYPE_DELEGATE = 2;
    /**
     * 親←→子
     */
    public static final Integer RELATION_TYPE_PARENT = 3;
    /**
     * きょうだい←→きょうだい
     */
    public static final Integer RELATION_TYPE_BROTHER = 4;
    /**
     * 夫←→妻
     */
    public static final Integer RELATION_TYPE_COUPLE = 5;
    /**
     * 親戚←→親戚
     */
    public static final Integer RELATION_TYPE_RELATIVE = 6;
    /**
     * パートナー←→パートナー
     */
    public static final Integer RELATION_TYPE_PARTNER = 7;
    /**
     * 友人←→友人
     */
    public static final Integer RELATION_TYPE_FRIEND = 8;
    /**
     * 取引先←→取引先
     */
    public static final Integer RELATION_TYPE_ACQUAINTANCE = 9;
    /**
     * 取引先←→取引担当者
     */
    public static final Integer RELATION_TYPE_CONTACT = 10;
    /**
     * 勤務先←→勤務者
     */
    public static final Integer RELATION_TYPE_EMPLOY = 11;
    /**
     * 所属先←→所属者
     */
    public static final Integer RELATION_TYPE_BELONG = 12;
    /**
     * 購入先←→顧客
     */
    public static final Integer RELATION_TYPE_CUSTOMER = 13;
    /**
     * 所有者←→所有物
     */
    public static final Integer RELATION_TYPE_OWNER = 14;

    /**
     * 関連1
     */
    public static final List<String> RELATION_TYPES1 = Collections
            .unmodifiableList(new LinkedList<String>() {
                {
                    add("組織");
                    add("組織");
                    add("親");
                    add("きょうだい");
                    add("夫");
                    add("親戚");
                    add("パートナー");
                    add("友人");
                    add("取引先");
                    add("取引先");
                    add("勤務先");
                    add("所属先");
                    add("購入先");
                    add("所有者");
                }
            });

    /**
     * 関連2
     */
    public static final List<String> RELATION_TYPES2 = Collections
            .unmodifiableList(new LinkedList<String>() {
                {
                    add("創立者");
                    add("代表者");
                    add("子");
                    add("きょうだい");
                    add("妻");
                    add("親戚");
                    add("パートナー");
                    add("友人");
                    add("取引先");
                    add("取引担当者");
                    add("勤務者");
                    add("所属者");
                    add("顧客");
                    add("所有");
                }
            });

    /**
     * Party関連No.
     */
    @TableGenerator(name = "RELATION_NO_GEN", table = "YM_GENERATED_ID",
            pkColumnName = "KEY_NAME", valueColumnName = "VALUE",
            pkColumnValue = "RELATION_NO_MAX", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "RELATION_NO_GEN")
    @Column(name = "RELATION_NO")
    private Integer relationNo;

    /**
     * Party関連種別
     */
    @Column(name = "RELATION_TYPE")
    private Integer relationType;

    /**
     * Party1
     */
    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY1", referencedColumnName = "PARTY_NO")
    private Party party1;

    /**
     * 役割1
     */
    @Column(length = 30)
    private String role1;

    /**
     * Party2
     */
    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY2", referencedColumnName = "PARTY_NO")
    private Party party2;

    /**
     * 役割2
     */
    @Column(length = 30)
    private String role2;

    /**
     * 開始日
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "TERM_FROM")
    private Date termFrom;

    /**
     * 終了日
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "TERM_TO")
    private Date termTo;

    /**
     * 登録ユーザー
     */
    @ManyToOne(targetEntity = YuimarlUser.class, fetch = FetchType.LAZY)
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
    @ManyToOne(targetEntity = YuimarlUser.class, fetch = FetchType.LAZY)
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
     * Party関連No.を取得する。
     *
     * @return relationNo Party関連No.
     */
    public Integer getRelationNo() {
        return relationNo;
    }

    /**
     * Party関連No.を設定する。
     *
     * @param relationNo Party関連No.
     */
    public void setRelationNo(final Integer relationNo) {
        this.relationNo = relationNo;
    }

    /**
     * Party関連種別を取得する。
     *
     * @return relationType Party関連種別
     */
    public Integer getRelationType() {
        return relationType;
    }

    /**
     * Party関連種別を設定する。
     *
     * @param relationType Party関連種別
     */
    public void setRelationType(final Integer relationType) {
        this.relationType = relationType;
    }

    /**
     * Party1を取得する。
     *
     * @return party1 Party1
     */
    public Party getParty1() {
        return party1;
    }

    /**
     * Party1を設定する。
     *
     * @param party1 Party1
     */
    public void setParty1(final Party party1) {
        this.party1 = party1;
    }

    /**
     * 役割1を取得する。
     *
     * @return role1 役割1
     */
    public String getRole1() {
        return role1;
    }

    /**
     * 役割1を設定する。
     *
     * @param role1 役割1
     */
    public void setRole1(final String role1) {
        this.role1 = role1;
    }

    /**
     * Party2を取得する。
     *
     * @return party2 Party2
     */
    public Party getParty2() {
        return party2;
    }

    /**
     * Party2を設定する。
     *
     * @param party2 Party2
     */
    public void setParty2(final Party party2) {
        this.party2 = party2;
    }

    /**
     * 役割2を取得する。
     *
     * @return role2 役割2
     */
    public String getRole2() {
        return role2;
    }

    /**
     * 役割2を設定する。
     *
     * @param role2 役割2
     */
    public void setRole2(final String role2) {
        this.role2 = role2;
    }

    /**
     * 開始日を取得する。
     *
     * @return termFrom 開始日
     */
    public Date getTermFrom() {
        if (termFrom == null) {
            return null;
        }
        return (Date) termFrom.clone();
    }

    /**
     * 開始日を設定する。
     *
     * @param termFrom 開始日
     */
    public void setTermFrom(final Date termFrom) {
        if (termFrom == null) {
            this.termFrom = null;
        } else {
            this.termFrom = (Date) termFrom.clone();
        }
    }

    /**
     * 終了日を取得する。
     *
     * @return termTo 終了日
     */
    public Date getTermTo() {
        if (termTo == null) {
            return null;
        }
        return (Date) termTo.clone();
    }

    /**
     * 終了日を設定する。
     *
     * @param termTo 終了日
     */
    public void setTermTo(final Date termTo) {
        if (termTo == null) {
            this.termTo = null;
        } else {
            this.termTo = (Date) termTo.clone();
        }
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
     * @return versionNo バージョン番号
     */
    @Override
    public Integer getVersionNo() {
        return versionNo;
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

    /**
     * Party関連種別名を取得する。
     *
     * @return Party関連種別名
     */
    public String getRelationTypeName() {
        if (this.relationType == null) {
            return null;
        }
        int i = this.relationType - 1;
        return (RELATION_TYPES1.get(i) + "←→" + RELATION_TYPES2.get(i));
    }

    /**
     * 開始日の和暦年を取得する。
     *
     * @return 開始日の和暦年
     */
    public String getTermFromWareki() {
        return YuimarlUtil.dateToWareki(this.termFrom, '（');
    }

    /**
     * 終了日の和暦年を取得する。
     *
     * @return 終了日の和暦年
     */
    public String getTermToWareki() {
        return YuimarlUtil.dateToWareki(this.termTo, '（');
    }
}
