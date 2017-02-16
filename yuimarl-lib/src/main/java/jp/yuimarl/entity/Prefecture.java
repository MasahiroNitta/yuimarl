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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 都道府県クラス
 *
 * @author Masahiro Nitta
 */
@Entity
@Table(name = "YM_PREFECTURE")
@NamedQueries({
    @NamedQuery(name = "Prefecture.findAll",
            query = "SELECT p FROM Prefecture p WHERE p.delFlg = '0' "
                    + "ORDER BY p.prefectureNo"),
    @NamedQuery(name = "Prefecture.findByPrefectureNo",
            query = "SELECT p FROM Prefecture p "
                    + "WHERE p.delFlg = '0' "
                    + "AND p.prefectureNo = :prefectureNo")})
public class Prefecture implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 都道府県No.
     */
    @Id
    @Column(name = "PREFECTURE_NO")
    private Integer prefectureNo;

    /**
     * 名前
     */
    @Column(length = 10)
    private String name;

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
    public Prefecture() {
    }

    /**
     * 都道府県No.を取得する。
     *
     * @return 都道府県No.
     */
    public Integer getPrefectureNo() {
        return this.prefectureNo;
    }

    /**
     * 都道府県No.を設定する。
     *
     * @param prefectureNo 都道府県No.
     */
    public void setPrefectureNo(final Integer prefectureNo) {
        this.prefectureNo = prefectureNo;
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
