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
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 住所クラス
 * <p>
 * 住所のデータを保持する埋め込みクラス。
 * </p>
 *
 * @author Masahiro Nitta
 */
@Embeddable
public class Address implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 郵便番号
     */
    @Column(name = "ZIP_CODE", length = 10)
    private String zipCode;

    /**
     * 都道府県
     */
    @ManyToOne
    @JoinColumn(name = "PREFECTURE", referencedColumnName = "PREFECTURE_NO")
    private Prefecture prefecture;

    /**
     * 市町村
     */
    @Column(length = 10)
    private String city;

    /**
     * 住所1
     */
    @Column(length = 50)
    private String address1;

    /**
     * 住所2
     */
    @Column(length = 50)
    private String address2;

    /**
     * 郵便番号を取得する。
     *
     * @return 郵便番号
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 郵便番号を設定する。
     *
     * @param zipCode 郵便番号
     */
    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * 都道府県を取得する。
     *
     * @return Prefecture
     */
    public Prefecture getPrefecture() {
        return prefecture;
    }

    /**
     * 都道府県の名前を取得する。
     *
     * @return 都道府県の名前
     */
    public String getPrefectureName() {
        if (prefecture == null) {
            return "";
        }
        return prefecture.getName();
    }

    /**
     * 都道府県を設定する。
     *
     * @param prefecture Prefecture
     */
    public void setPrefecture(final Prefecture prefecture) {
        this.prefecture = prefecture;
    }

    /**
     * 市町村を取得する。
     *
     * @return 市町村
     */
    public String getCity() {
        return city;
    }

    /**
     * 市町村を設定する。
     *
     * @param city 市町村
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * 住所1を取得する。
     *
     * @return address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * 住所1を設定する。
     *
     * @param address1
     *            セットする address1
     */
    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    /**
     * 住所2を取得する。
     *
     * @return address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * 住所2を設定する。
     *
     * @param address2
     *            セットする address2
     */
    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

}
