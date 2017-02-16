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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 法人クラス
 * <p>
 * 組織クラスから継承。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Entity
@DiscriminatorValue(value = "3")
public class Corporation extends Organization {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * URL
     */
    @Column(length = 60)
    private String url;

    /**
     * 資本金
     */
    private Long capital;

    /**
     * 決算月
     */
    @Column(name = "ACCOUNT_MONTH")
    private Integer accountMonth;

    /**
     * 取引先フラグ
     * <p>
     * 1:取引先
     * </p>
     */
    @Column(name = "PARTNER_FLG")
    private Character partnerFlg = '0';

    /**
     * 銀行
     */
    @Column(length = 40)
    private String bank;

    /**
     * 支店
     */
    @Column(length = 40)
    private String branch;

    /**
     * 口座種別
     * <p>
     * 1:普通, 2:当座
     * </p>
     */
    @Column(name = "ACCOUNT_TYPE")
    private Character accountType = '0';

    /**
     * 口座
     */
    @Column(length = 40)
    private String account;

    /**
     * 締日
     * <p>
     * 31:月末
     * </p>
     */
    private Integer cutoff;

    /**
     * コンストラクタ
     */
    public Corporation() {
        super();
        this.setPartyType(Party.PARTY_TYPE_CORPORATION);
    }

    /**
     * URLを取得する。
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * URLを設定する。
     *
     * @param url セットする url
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * 資本金を取得する。
     *
     * @return capital
     */
    public Long getCapital() {
        return capital;
    }

    /**
     * 資本金を設定する。
     *
     * @param capital セットする capital
     */
    public void setCapital(final Long capital) {
        this.capital = capital;
    }

    /**
     * 資本金を取得する。
     *
     * @return accountMonth
     */
    public Integer getAccountMonth() {
        return accountMonth;
    }

    /**
     * 資本金を設定する。
     *
     * @param accountMonth セットする accountMonth
     */
    public void setAccountMonth(final Integer accountMonth) {
        this.accountMonth = accountMonth;
    }

    /**
     * 取引先フラグを取得する。
     *
     * @return partnerFlg
     */
    public Character getPartnerFlg() {
        if (partnerFlg == null) {
            return '0';
        }
        return partnerFlg;
    }

    /**
     * 取引先フラグを取得する。
     *
     * @return partner
     */
    public boolean getPartner() {
        if (partnerFlg == null) {
            return false;
        }
        return (partnerFlg == '1');
    }

    /**
     * 取引先名を取得する。
     *
     * @return 取引先名
     */
    public String getPartnerAsString() {
        if (this.partnerFlg == null) {
            return "";
        }

        if (this.partnerFlg == '1') {
            return "取引先";
        }
        return "";
    }

    /**
     * 取引先フラグを設定する。
     *
     * @param partnerFlg セットする partnerFlg
     */
    public void setPartnerFlg(final Character partnerFlg) {
        this.partnerFlg = partnerFlg;
    }

    /**
     * 取引先フラグを設定する。
     *
     * @param partner セットする partner
     */
    public void setPartner(final boolean partner) {
        if (partner) {
            this.partnerFlg = '1';
        } else {
            this.partnerFlg = '0';
        }
    }

    /**
     * 銀行を取得する。
     *
     * @return bank
     */
    public String getBank() {
        return bank;
    }

    /**
     * 銀行を設定する。
     *
     * @param bank セットする bank
     */
    public void setBank(final String bank) {
        this.bank = bank;
    }

    /**
     * 支店を取得する。
     *
     * @return branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * 支店を設定する。
     *
     * @param branch セットする branch
     */
    public void setBranch(final String branch) {
        this.branch = branch;
    }

    /**
     * 口座種別を取得する。
     *
     * @return accountType
     */
    public Character getAccountType() {
        return accountType;
    }

    /**
     * 口座種別名を取得する。
     *
     * @return 口座種別名
     */
    public String getAccountTypeAsString() {
        if (this.accountType == null) {
            return "";
        }

        switch (this.accountType) {
            case '1':
                return "普通";
            case '2':
                return "当座";
            default:
                return "";
        }
    }

    /**
     * 口座種別を設定する。
     *
     * @param accountType セットする accountType
     */
    public void setAccountType(final Character accountType) {
        this.accountType = accountType;
    }

    /**
     * 口座を取得する。
     *
     * @return account
     */
    public String getAccount() {
        return account;
    }

    /**
     * 口座を設定する。
     *
     * @param account セットする account
     */
    public void setAccount(final String account) {
        this.account = account;
    }

    /**
     * 締日を取得する。
     *
     * @return cutoff
     */
    public Integer getCutoff() {
        return cutoff;
    }

    /**
     * 締日を設定する。
     *
     * @param cutoff セットする cutoff
     */
    public void setCutoff(final Integer cutoff) {
        this.cutoff = cutoff;
    }

}
