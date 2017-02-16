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
import java.util.ResourceBundle;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import jp.yuimarl.qualifiers.ValidEmail;
import jp.yuimarl.qualifiers.ValidPhoneNumber;
import jp.yuimarl.util.YuimarlUtil;

/**
 * パーティクラス
 * <p>
 * 人、組織、法人、物品の親クラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "YM_PARTY")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PARTY_TYPE",
        discriminatorType = DiscriminatorType.CHAR)
@NamedQueries({
    @NamedQuery(name = "Party.findAll",
            query = "SELECT p FROM Party p WHERE p.delFlg = '0' "
                    + "ORDER BY p.nameKana"),
    @NamedQuery(name = "Party.findByPartyType",
            query = "SELECT p FROM Party p "
                    + "WHERE p.delFlg = '0' AND p.partyType = :partyType "
                    + "ORDER BY p.nameKana"),
    @NamedQuery(name = "Party.findByPartyNo",
            query = "SELECT p FROM Party p "
                    + "WHERE p.delFlg = '0' AND p.partyNo = :partyNo")})
public class Party implements EntityBasic, Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 人
     */
    public static final char PARTY_TYPE_PERSON = '1';
    /**
     * 組織
     */
    public static final char PARTY_TYPE_ORGANIZATION = '2';
    /**
     * 法人
     */
    public static final char PARTY_TYPE_CORPORATION = '3';
    /**
     * 物品
     */
    public static final char PARTY_TYPE_GOODS = '4';

    /**
     * BUNDLE
     */
    private static final String BUNDLE = "bundles.Bundle";

    /**
     * Party No.
     */
    @TableGenerator(name = "PARTY_NO_GEN", table = "YM_GENERATED_ID",
            pkColumnName = "KEY_NAME", valueColumnName = "VALUE",
            pkColumnValue = "PARTY_NO_MAX", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PARTY_NO_GEN")
    @Column(name = "PARTY_NO")
    private Integer partyNo;

    /**
     * Party種別
     * <p>
     * 1:人, 2:組織, 3:法人, 4:物品
     * </p>
     */
    @Column(name = "PARTY_TYPE")
    private Character partyType;

    /**
     * 名前
     */
    @Column(length = 50)
    @NotNull(message = "{party.name}")
    private String name;

    /**
     * 名前ヨミ
     */
    @Column(name = "NAME_KANA", length = 50)
    @NotNull(message = "{party.nameKana}")
    private String nameKana;

    /**
     * 住所
     */
    @Embedded
    private Address address;

    /**
     * 電話番号
     */
    @ValidPhoneNumber(message = "{party.phoneNo}")
    @Column(name = "PHONE_NO", length = 20)
    private String phoneNo;

    /**
     * FAX番号
     */
    @ValidPhoneNumber(message = "{party.faxNo}")
    @Column(name = "FAX_NO", length = 20)
    private String faxNo;

    /**
     * メールアドレス1
     */
    @ValidEmail(message = "{party.email}")
    @Column(name = "MAIL_ADDRESS1", length = 50)
    private String mailAddress1;

    /**
     * メールアドレス2
     */
    @ValidEmail(message = "{party.email}")
    @Column(name = "MAIL_ADDRESS2", length = 50)
    private String mailAddress2;

    /**
     * 開始年月日
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    /**
     * 終了年月日
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    /**
     * メモ
     */
    @Column(length = 4096)
    private String memo;

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
     * Party No.を取得する。
     *
     * @return partyNo Party No.
     */
    public Integer getPartyNo() {
        return partyNo;
    }

    /**
     * Party No.を設定する。
     *
     * @param partyNo Party No.
     */
    public void setPartyNo(final Integer partyNo) {
        this.partyNo = partyNo;
    }

    /**
     * Party種別を取得する。
     *
     * @return partyType Party種別
     */
    public Character getPartyType() {
        return partyType;
    }

    /**
     * Party種別の名前を取得する。
     *
     * @return Party種別の名前
     */
    public String getPartyTypeName() {
        if (this.partyType == null) {
            return "";
        }
        switch (this.partyType) {
            case PARTY_TYPE_PERSON:
                return "人";
            case PARTY_TYPE_ORGANIZATION:
                return "組織";
            case PARTY_TYPE_CORPORATION:
                return "法人";
            case PARTY_TYPE_GOODS:
                return "物品";
            default:
                return "";
        }
    }

    /**
     * Party種別を設定する。
     *
     * @param partyType Party種別
     */
    public void setPartyType(final Character partyType) {
        this.partyType = partyType;
    }

    /**
     * 種類名を取得する。
     *
     * @return 種類名
     */
    public String getCategoryName() {
        return null;
    }

    /**
     * 名前を取得する。
     *
     * @return name 名前
     */
    public String getName() {
        return name;
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
     * @return nameKana 名前ヨミ
     */
    public String getNameKana() {
        return nameKana;
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
     * 住所を取得する。
     *
     * @return address 住所
     */
    public Address getAddress() {
        return address;
    }

    /**
     * 住所を設定する。
     *
     * @param address 住所
     */
    public void setAddress(final Address address) {
        this.address = address;
    }

    /**
     * 電話番号を取得する。
     *
     * @return phoneNo 電話番号
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * 電話番号を設定する。
     *
     * @param phoneNo 電話番号
     */
    public void setPhoneNo(final String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * FAX番号を取得する。
     *
     * @return faxNo FAX番号
     */
    public String getFaxNo() {
        return faxNo;
    }

    /**
     * FAX番号を設定する。
     *
     * @param faxNo FAX番号
     */
    public void setFaxNo(final String faxNo) {
        this.faxNo = faxNo;
    }

    /**
     * メールアドレス1を取得する。
     *
     * @return mailAddress1 メールアドレス1
     */
    public String getMailAddress1() {
        return mailAddress1;
    }

    /**
     * メールアドレス1を設定する。
     *
     * @param mailAddress1 メールアドレス1
     */
    public void setMailAddress1(final String mailAddress1) {
        this.mailAddress1 = mailAddress1;
    }

    /**
     * メールアドレス2を取得する。
     *
     * @return mailAddress2 メールアドレス2
     */
    public String getMailAddress2() {
        return mailAddress2;
    }

    /**
     * メールアドレス2を設定する。
     *
     * @param mailAddress2 メールアドレス2
     */
    public void setMailAddress2(final String mailAddress2) {
        this.mailAddress2 = mailAddress2;
    }

    /**
     * 開始年月日を取得する。
     *
     * @return startDate 開始年月日
     */
    public Date getStartDate() {
        if (startDate == null) {
            return null;
        }
        return (Date) startDate.clone();
    }

    /**
     * 開始年月日を設定する。
     *
     * @param startDate 開始年月日
     */
    public void setStartDate(final Date startDate) {
        if (startDate == null) {
            this.startDate = null;
        } else {
            this.startDate = (Date) startDate.clone();
        }
    }

    /**
     * 終了年月日を取得する。
     *
     * @return endDate 終了年月日
     */
    public Date getEndDate() {
        if (endDate == null) {
            return null;
        }
        return (Date) endDate.clone();
    }

    /**
     * 終了年月日を設定する。
     *
     * @param endDate 終了年月日
     */
    public void setEndDate(final Date endDate) {
        if (endDate == null) {
            this.endDate = null;
        } else {
            this.endDate = (Date) endDate.clone();
        }
    }

    /**
     * メモを取得する。
     *
     * @return memo メモ
     */
    public String getMemo() {
        return memo;
    }

    /**
     * メモを設定する。
     *
     * @param memo メモ
     */
    public void setMemo(final String memo) {
        this.memo = memo;
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
     * @param versionno バージョン番号
     */
    @Override
    public void setVersionNo(final Integer versionno) {
        this.versionNo = versionno;
    }

    /**
     * 開始年月日の和暦年を取得する。
     *
     * @return 開始年月日の和暦年
     */
    public String getStartDateWareki() {
        return YuimarlUtil.dateToWareki(this.getStartDate(), '（');
    }

    /**
     * 終了年月日の和暦年を取得する。
     *
     * @return 終了年月日の和暦年
     */
    public String getEndDateWareki() {
        return YuimarlUtil.dateToWareki(this.getEndDate(), '（');
    }

    /**
     * PartyをJSONに変換する。
     *
     * @return JSON
     */
    public String partyToJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("partyNo", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_partyNo"))
                .add("value", getPartyNo()));
        builder.add("partyType", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("ListPartyTitle_partyType"))
                .add("value", getPartyType()));
        builder.add("name", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_name"))
                .add("value", YuimarlUtil.nullToStr(getName())));
        builder.add("nameKana", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_nameKana"))
                .add("value", YuimarlUtil.nullToStr(getNameKana())));
        builder.add("zipCode", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_zipCode"))
                .add("value", YuimarlUtil.nullToStr(
                        getAddress().getZipCode())));
        builder.add("prefecture", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_prefecture"))
                .add("value", YuimarlUtil.nullToStr(
                        getAddress().getPrefectureName())));
        builder.add("city", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_city"))
                .add("value", YuimarlUtil.nullToStr(getAddress().getCity())));
        builder.add("address1", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_address1"))
                .add("value", YuimarlUtil.nullToStr(
                        getAddress().getAddress1())));
        builder.add("address2", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_address2"))
                .add("value", YuimarlUtil.nullToStr(
                        getAddress().getAddress2())));
        builder.add("phoneNo", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_phoneNo"))
                .add("value", YuimarlUtil.nullToStr(getPhoneNo())));
        builder.add("faxNo", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_faxNo"))
                .add("value", YuimarlUtil.nullToStr(getFaxNo())));
        builder.add("mailAddress1", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_mailAddress1"))
                .add("value", YuimarlUtil.nullToStr(getMailAddress1())));
        builder.add("mailAddress2", Json.createObjectBuilder()
                .add("label", ResourceBundle.getBundle(BUNDLE)
                        .getString("PartyTitle_mailAddress2"))
                .add("value", YuimarlUtil.nullToStr(getMailAddress2())));

        if (this instanceof Person) {
            Person p = (Person) this;
            builder.add("lastName", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_lastName"))
                    .add("value", YuimarlUtil.nullToStr(p.getLastName())));
            builder.add("firstName", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_firstName"))
                    .add("value", YuimarlUtil.nullToStr(p.getFirstName())));
            builder.add("lastNameKana", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_lastNameKana"))
                    .add("value", YuimarlUtil.nullToStr(p.getLastNameKana())));
            builder.add("firstNameKana", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_firstNameKana"))
                    .add("value", YuimarlUtil.nullToStr(
                            p.getFirstNameKana())));
            builder.add("gender", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_gender"))
                    .add("value", YuimarlUtil.nullToStr(
                            p.getGenderAsString())));
            builder.add("cellPhoneNumber", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_cellPhoneNumber"))
                    .add("value", YuimarlUtil.nullToStr(
                            p.getCellPhoneNumber())));
            builder.add("birthDay", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_birthDay"))
                    .add("value", YuimarlUtil.dtToStr1(p.getBirthday())
                            + "  " + p.getStartDateWareki()));
            builder.add("dateOfDeath", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_dateOfDeath"))
                    .add("value", YuimarlUtil.dtToStr1(p.getDateOfDeath())
                            + "  " + p.getEndDateWareki()));
        }

        if (this instanceof Organization) {
            Organization o = (Organization) this;
            builder.add("organizationCategory", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_organizationCategory"))
                    .add("value", YuimarlUtil.nullToStr(o.getCategoryName())));
            builder.add("personCount", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_personCount"))
                    .add("value", YuimarlUtil.intToStr(o.getPersonCount())));
            builder.add("establishDate", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_establishDate"))
                    .add("value", YuimarlUtil.dtToStr1(o.getEstablishDate())
                            + "  " + o.getStartDateWareki()));
            builder.add("abolitionDate", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_abolitionDate"))
                    .add("value", YuimarlUtil.dtToStr1(o.getAbolitionDate())
                            + "  " + o.getEndDateWareki()));
            if (this instanceof Corporation) {
                Corporation c = (Corporation) this;
                builder.add("url", Json.createObjectBuilder()
                        .add("label", ResourceBundle.getBundle(BUNDLE)
                                .getString("PartyTitle_url"))
                        .add("value", YuimarlUtil.nullToStr(c.getUrl())));
                builder.add("capital", Json.createObjectBuilder()
                        .add("label", ResourceBundle.getBundle(BUNDLE)
                                .getString("PartyTitle_capital"))
                        .add("value", YuimarlUtil.longToStr(c.getCapital())));
                builder.add("accountMonth", Json.createObjectBuilder()
                        .add("label", ResourceBundle.getBundle(BUNDLE)
                                .getString("PartyTitle_accountMonth"))
                        .add("value", YuimarlUtil
                                .intToStr(c.getAccountMonth())));
            }
        }

        if (this instanceof Goods) {
            Goods g = (Goods) this;
            builder.add("category", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_category"))
                    .add("value", YuimarlUtil.nullToStr(g.getCategoryName())));
            builder.add("startDate", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_startDate"))
                    .add("value", YuimarlUtil.dtToStr1(getStartDate())
                            + "  " + g.getStartDateWareki()));
            builder.add("endDate", Json.createObjectBuilder()
                    .add("label", ResourceBundle.getBundle(BUNDLE)
                            .getString("PartyTitle_endDate"))
                    .add("value", YuimarlUtil.dtToStr1(getEndDate())
                            + "  " + g.getEndDateWareki()));
        }
        return builder.build().toString();
    }

}
