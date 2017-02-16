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
package jp.yuimarl.ejb;

import java.util.List;
import javax.ejb.Local;
import jp.yuimarl.entity.YuimarlUser;

/**
 * Yuimarlユーザー操作インターフェース
 *
 * @author Masahiro Nitta
 */
@Local
public interface YuimarlUserService {

    /**
     * 件数を取得する。
     *
     * @return 件数
     */
    int count();

    /**
     * 登録する。
     *
     * @param entity YuimarlUser
     */
    void create(YuimarlUser entity);

    /**
     * 更新する。
     *
     * @param entity YuimarlUser
     */
    void edit(YuimarlUser entity);

    /**
     * 検索する。
     *
     * @param range 範囲
     * @return エンティティのリスト
     */
    List<YuimarlUser> findRange(int[] range);

    /**
     * ユーザーIDを指定してYuimarlユーザーを取得する。
     *
     * @param userId ユーザーID
     * @return YuimarlUser
     */
    YuimarlUser getYuimarlUserByUserId(String userId);

    /**
     * Party No.を指定してYuimarlユーザーを取得する。
     *
     * @param partyNo Party No.
     * @return YuimarlUser
     */
    YuimarlUser getYuimarlUserByPartyNo(Integer partyNo);

}
