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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import jp.yuimarl.entity.YuimarlUser;

/**
 * イベントビューセットクラス
 * <p>
 * イベントビューとイベントセットの関連情報を管理するクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "YM_EVENTVIEWSET")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventviewset.findAll",
            query = "SELECT e FROM Eventviewset e"),
    @NamedQuery(name = "Eventviewset.findByEventview",
            query = "SELECT e FROM Eventviewset e "
                    + "WHERE e.eventviewsetPK.eventview = :eventview"),
    @NamedQuery(name = "Eventviewset.findByEventset",
            query = "SELECT e FROM Eventviewset e "
                    + "WHERE e.eventviewsetPK.eventset = :eventset")})
public class Eventviewset implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * イベントビューセット主キー
     */
    @EmbeddedId
    private EventviewsetPK eventviewsetPK;

    /**
     * イベントビュー
     */
    @JoinColumn(name = "EVENTVIEW",
            referencedColumnName = "EVENTVIEW_NO",
            nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Eventview eventview;

    /**
     * イベントセット
     */
    @JoinColumn(name = "EVENTSET",
            referencedColumnName = "EVENTSET_NO",
            nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Eventset eventset;

    /**
     * オーナー権限
     * <p>
     * 1:オーナー, 0:オーナーでない
     * </p>
     */
    @Column(name = "AUTH_OWNER")
    private Character authOwner;

    /**
     * 更新権限
     * <p>
     * 1:更新可, 0:更新不可
     * </p>
     */
    @Column(name = "AUTH_UPDATE")
    private Character authUpdate;

    /**
     * 登録ユーザー
     */
    @JoinColumn(name = "REGIST_USER", referencedColumnName = "USER_NO")
    @ManyToOne
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
    @JoinColumn(name = "UPDATE_USER", referencedColumnName = "USER_NO")
    @ManyToOne
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
    public Eventviewset() {
    }

    /**
     * コンストラクタ
     *
     * @param eventviewsetPK イベントビューセット主キー
     */
    public Eventviewset(final EventviewsetPK eventviewsetPK) {
        this.eventviewsetPK = eventviewsetPK;
    }

    /**
     * コンストラクタ
     *
     * @param eventviewNo イベントビューNo.
     * @param eventsetNo イベントセットNo.
     */
    public Eventviewset(final int eventviewNo, final int eventsetNo) {
        this.eventviewsetPK = new EventviewsetPK(eventviewNo, eventsetNo);
    }

    /**
     * イベントビューセット主キーを取得する。
     *
     * @return イベントビューセット主キー
     */
    public EventviewsetPK getEventviewsetPK() {
        return eventviewsetPK;
    }

    /**
     * イベントビューセット主キーを設定する。
     *
     * @param eventviewsetPK イベントビューセット主キー
     */
    public void setEventviewsetPK(final EventviewsetPK eventviewsetPK) {
        this.eventviewsetPK = eventviewsetPK;
    }

    /**
     * イベントビューを取得する。
     *
     * @return イベントビュー
     */
    public Eventview getEventview() {
        return eventview;
    }

    /**
     * イベントビューを設定する。
     *
     * @param eventview イベントビュー
     */
    public void setEventview(final Eventview eventview) {
        this.eventview = eventview;
    }

    /**
     * イベントセットを取得する。
     *
     * @return イベントセット
     */
    public Eventset getEventset() {
        return eventset;
    }

    /**
     * イベントセットを設定する。
     *
     * @param eventset イベントセット
     */
    public void setEventset(final Eventset eventset) {
        this.eventset = eventset;
    }

    /**
     * オーナー権限を取得する。
     *
     * @return オーナー権限
     */
    public Character getAuthOwner() {
        return authOwner;
    }

    /**
     * オーナー権限を設定する。
     *
     * @param authOwner オーナー権限
     */
    public void setAuthOwner(final Character authOwner) {
        this.authOwner = authOwner;
    }

    /**
     * 更新権限を取得する。
     *
     * @return 更新権限
     */
    public Character getAuthUpdate() {
        return authUpdate;
    }

    /**
     * 更新権限を設定する。
     *
     * @param authUpdate 更新権限
     */
    public void setAuthUpdate(final Character authUpdate) {
        this.authUpdate = authUpdate;
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
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventviewsetPK != null ? eventviewsetPK.hashCode() : 0);
        return hash;
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Eventviewset)) {
            return false;
        }
        Eventviewset other = (Eventviewset) object;
        return (this.eventviewsetPK != null || other.eventviewsetPK == null)
                && (this.eventviewsetPK == null
                || this.eventviewsetPK.equals(other.eventviewsetPK));
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "jp.yuimarl.entity.Eventviewset[ eventviewsetPK="
                + eventviewsetPK + " ]";
    }

}
