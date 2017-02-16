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
package jp.yuimarl.entity.event;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.XmlRootElement;
import jp.yuimarl.entity.Party;
import jp.yuimarl.entity.YuimarlUser;

/**
 * イベントセットクラス
 * <p>
 * イベントの集合を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "YM_EVENTSET")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventset.findAll",
            query = "SELECT e FROM Eventset e WHERE e.delFlg = '0'"),
    @NamedQuery(name = "Eventset.findByEventsetNo",
            query = "SELECT e FROM Eventset e "
                    + "WHERE e.eventsetNo = :eventsetNo AND e.delFlg = '0'"),
    @NamedQuery(name = "Eventset.findByName",
            query = "SELECT e FROM Eventset e "
                    + "WHERE e.name = :name AND e.delFlg = '0'")})
public class Eventset implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * プロジェクト
     */
    public static final char EVENTSET_TYPE_PROJECT = '1';

    /**
     * イベントセットNo.
     * <p>
     * 1:日本の休日・記念日
     * </p>
     */
    @TableGenerator(name = "EVENTSET_NO_GEN", table = "YM_GENERATED_ID",
            pkColumnName = "KEY_NAME", valueColumnName = "VALUE",
            pkColumnValue = "EVENTSET_NO_MAX", allocationSize = 1)
    @Id
    @Basic(optional = false)
    @Column(name = "EVENTSET_NO")
    private Integer eventsetNo;

    /**
     * イベントセット型
     * <p>
     * 1:プロジェクト
     * </p>
     */
    @Basic(optional = false)
    @Column(name = "EVENTSET_TYPE")
    private Character eventsetType = '0';

    /**
     * 名前
     */
    @Column(length = 100)
    private String name;

    /**
     * 期間開始日
     * <p>
     * yyyyMMdd
     * </p>
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "TERM_FROM")
    private Date termFrom;

    /**
     * 期間終了日
     * <p>
     * yyyyMMdd
     * </p>
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "TERM_TO")
    private Date termTo;

    /**
     * クライアント
     */
    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT", referencedColumnName = "PARTY_NO")
    private Party client;

    /**
     * プロデューサー
     */
    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCER", referencedColumnName = "PARTY_NO")
    private Party producer;

    /**
     * 日曜日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "SUNDAY")
    private Character sunday = '1';

    /**
     * 土曜日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "SATURDAY")
    private Character saturday = '1';

    /**
     * 祝日
     * <p>
     * 1:実施, 0:実施しない
     * </p>
     */
    @Column(name = "HOLIDAY")
    private Character holiday = '1';

    /**
     * 稼動日のリスト
     * <p>
     * yyyyMMdd,yyyyMMdd,・・・
     * </p>
     */
    @Column(name = "INCLUDE_DAYS", length = 2048)
    private String includeDays;

    /**
     * 非稼動日のリスト
     * <p>
     * yyyyMMdd,yyyyMMdd,・・・
     * </p>
     */
    @Column(name = "EXCLUDE_DAYS", length = 2048)
    private String excludeDays;

    /**
     * メモ
     */
    @Column(length = 4096)
    private String memo;

    /**
     * 更新権限ユーザーのリスト
     * <p>
     * User No.,User No.,・・・
     * </p>
     */
    @Column(name = "UPDATE_AUTH", length = 2048)
    private String updateAuth;

    /**
     * オーナー
     */
    @JoinColumn(name = "OWNER", referencedColumnName = "USER_NO")
    @ManyToOne
    private YuimarlUser owner;

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
    @Column(name = "REGIST_TIME")
    @Temporal(TemporalType.TIMESTAMP)
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
    @Column(name = "UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
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
    public Eventset() {
    }

    /**
     * コンストラクタ
     *
     * @param eventsetNo イベントセットNo.
     */
    public Eventset(final Integer eventsetNo) {
        this.eventsetNo = eventsetNo;
    }

    /**
     * イベントセットNo.を取得する。
     *
     * @return イベントセットNo.
     */
    public Integer getEventsetNo() {
        return eventsetNo;
    }

    /**
     * イベントセットNo.を設定する。
     *
     * @param eventsetNo イベントセットNo.
     */
    public void setEventsetNo(final Integer eventsetNo) {
        this.eventsetNo = eventsetNo;
    }

    /**
     * イベントセット型を取得する。
     *
     * @return イベントセット型
     */
    public Character getEventsetType() {
        return eventsetType;
    }

    /**
     * イベントセット型を設定する。
     *
     * @param eventsetType イベントセット型
     */
    public void setEventsetType(final Character eventsetType) {
        this.eventsetType = eventsetType;
    }

    /**
     * 名前を取得する。
     *
     * @return 名前
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
     * 期間開始日を取得する。
     *
     * @return 期間開始日
     */
    public Date getTermFrom() {
        if (termFrom == null) {
            return null;
        }
        return (Date) termFrom.clone();
    }

    /**
     * 期間開始日を設定する。
     *
     * @param termFrom 期間開始日
     */
    public void setTermFrom(final Date termFrom) {
        if (termFrom == null) {
            this.termFrom = null;
        } else {
            this.termFrom = (Date) termFrom.clone();
        }
    }

    /**
     * 期間終了日を取得する。
     *
     * @return 期間終了日
     */
    public Date getTermTo() {
        if (termTo == null) {
            return null;
        }
        return (Date) termTo.clone();
    }

    /**
     * 期間終了日を設定する。
     *
     * @param termTo 期間終了日
     */
    public void setTermTo(final Date termTo) {
        if (termTo == null) {
            this.termTo = null;
        } else {
            this.termTo = (Date) termTo.clone();
        }
    }

    /**
     * クライアントを取得する。
     *
     * @return クライアント
     */
    public Party getClient() {
        return client;
    }

    /**
     * クライアントを設定する。
     *
     * @param client クライアント
     */
    public void setClient(final Party client) {
        this.client = client;
    }

    /**
     * プロデューサーを取得する。
     *
     * @return プロデューサー
     */
    public Party getProducer() {
        return producer;
    }

    /**
     * プロデューサーを設定する。
     *
     * @param producer プロデューサー
     */
    public void setProducer(final Party producer) {
        this.producer = producer;
    }

    /**
     * 日曜日を取得する。
     *
     * @return 日曜日
     */
    public Character getSunday() {
        return sunday;
    }

    /**
     * 日曜日を設定する。
     *
     * @param sunday 日曜日
     */
    public void setSunday(final Character sunday) {
        this.sunday = sunday;
    }

    /**
     * 土曜日を取得する。
     *
     * @return 土曜日
     */
    public Character getSaturday() {
        return saturday;
    }

    /**
     * 土曜日を設定する。
     *
     * @param saturday 土曜日
     */
    public void setSaturday(final Character saturday) {
        this.saturday = saturday;
    }

    /**
     * 祝日を取得する。
     *
     * @return 祝日
     */
    public Character getHoliday() {
        return holiday;
    }

    /**
     * 祝日を設定する。
     *
     * @param holiday 祝日
     */
    public void setHoliday(final Character holiday) {
        this.holiday = holiday;
    }

    /**
     * 稼動日のリストを取得する。
     *
     * @return 稼動日のリスト
     */
    public String getIncludeDays() {
        return includeDays;
    }

    /**
     * 稼動日のリストを設定する。
     *
     * @param includeDays 稼動日のリスト
     */
    public void setIncludeDays(final String includeDays) {
        this.includeDays = includeDays;
    }

    /**
     * 非稼動日のリストを取得する。
     *
     * @return 非稼動日のリスト
     */
    public String getExcludeDays() {
        return excludeDays;
    }

    /**
     * 非稼動日のリストを設定する。
     *
     * @param excludeDays 非稼動日のリスト
     */
    public void setExcludeDays(final String excludeDays) {
        this.excludeDays = excludeDays;
    }

    /**
     * メモを取得する。
     *
     * @return メモ
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
     * 更新権限ユーザーのリストを取得する。
     *
     * @return 更新権限ユーザーのリスト
     */
    public String getUpdateAuth() {
        return updateAuth;
    }

    /**
     * 更新権限ユーザーのリストを設定する。
     *
     * @param updateAuth 更新権限ユーザーのリスト
     */
    public void setUpdateAuth(final String updateAuth) {
        this.updateAuth = updateAuth;
    }

    /**
     * 削除フラグを取得する。
     *
     * @return 削除フラグ
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
     * 登録日時を取得する。
     *
     * @return 登録日時
     */
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
    public void setRegistTime(final Date registTime) {
        if (registTime == null) {
            this.registTime = null;
        } else {
            this.registTime = (Date) registTime.clone();
        }
    }

    /**
     * 更新日時を取得する。
     *
     * @return 更新日時
     */
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
    public Integer getVersionNo() {
        return versionNo;
    }

    /**
     * バージョン番号を設定する。
     *
     * @param versionNo バージョン番号
     */
    public void setVersionNo(final Integer versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * 更新ユーザーを取得する。
     *
     * @return 更新ユーザー
     */
    public YuimarlUser getUpdateUser() {
        return updateUser;
    }

    /**
     * 更新ユーザーを設定する。
     *
     * @param updateUser 更新ユーザー
     */
    public void setUpdateUser(final YuimarlUser updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 登録ユーザーを取得する。
     *
     * @return 登録ユーザー
     */
    public YuimarlUser getRegistUser() {
        return registUser;
    }

    /**
     * 登録ユーザーを設定する。
     *
     * @param registUser 登録ユーザー
     */
    public void setRegistUser(final YuimarlUser registUser) {
        this.registUser = registUser;
    }

    /**
     * オーナーを取得する。
     *
     * @return オーナー
     */
    public YuimarlUser getOwner() {
        return owner;
    }

    /**
     * オーナーを設定する。
     *
     * @param owner オーナー
     */
    public void setOwner(final YuimarlUser owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventsetNo != null ? eventsetNo.hashCode() : 0);
        return hash;
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Eventset)) {
            return false;
        }
        Eventset other = (Eventset) object;
        return (this.eventsetNo != null || other.eventsetNo == null)
                && (this.eventsetNo == null
                || this.eventsetNo.equals(other.eventsetNo));
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "jp.yuimarl.entity.Eventset[ eventsetNo=" + eventsetNo + " ]";
    }

}
