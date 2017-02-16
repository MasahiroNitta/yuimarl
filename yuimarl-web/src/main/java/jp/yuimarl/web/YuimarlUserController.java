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
package jp.yuimarl.web;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import jp.yuimarl.ejb.PartyService;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.Party;
import jp.yuimarl.entity.YuimarlUser;
import jp.yuimarl.util.JsfUtil;
import jp.yuimarl.util.YuimarlPagination;
import jp.yuimarl.util.YuimarlUtil;

/**
 * Yuimarlユーザー制御クラス
 *
 * @author Masahiro Nitta
 */
@Named(value = "yuimarlUserController")
@SessionScoped
public class YuimarlUserController implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * BUNDLE
     */
    private static final String BUNDLE = "bundles.Bundle";

    /**
     * Logger
     */
    private static final Logger LOGGER
            = Logger.getLogger(YuimarlUserController.class.getName());

    /**
     * YuimarlUserService
     */
    @EJB
    private YuimarlUserService userService;

    /**
     * PartyService
     */
    @EJB
    private PartyService partyService;

    /**
     * YuimarlUser
     */
    private YuimarlUser current = null;

    /**
     * DataModel
     */
    private DataModel items = null;

    /**
     * ユーザーのリスト
     */
    private List<YuimarlUser> userList = null;

    /**
     * 一覧画面の一覧ページ制御クラス
     */
    private YuimarlPagination pagination;

    /**
     * 登録画面であるか
     * <p>
     * true:登録画面, false:編集画面
     * </p>
     */
    private boolean modeCreate = false;

    /**
     * 編集画面であるかを取得する。
     * <p>
     * true:編集画面, false:照会画面
     * </p>
     */
    private boolean modeUpdate = false;

    /**
     * パスワード
     */
    private String password1 = "";

    /**
     * パスワード（再度）
     */
    private String password2 = "";

    /**
     * Party No.
     */
    private Integer partyNo = null;

    /**
     * パスワード変更権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean authPw = false;

    /**
     * ユーザー 照会権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth1V = false;

    /**
     * ユーザー 登録権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth1C = false;

    /**
     * ユーザー 更新権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth1U = false;

    /**
     * ユーザー 削除権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth1D = false;

    /**
     * ユーザー権限 照会権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth2V = false;

    /**
     * ユーザー権限 登録権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth2C = false;

    /**
     * ユーザー権限 更新権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth2U = false;

    /**
     * ユーザー権限 削除権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth2D = false;

    /**
     * 物品カテゴリー 照会権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth3V = false;

    /**
     * 物品カテゴリー 登録権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth3C = false;

    /**
     * 物品カテゴリー 更新権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth3U = false;

    /**
     * 物品カテゴリー 削除権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth3D = false;

    /**
     * 人/組織/物 照会権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth4V = false;

    /**
     * 人/組織/物 登録権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth4C = false;

    /**
     * 人/組織/物 更新権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth4U = false;

    /**
     * 人/組織/物 削除権限
     * <p>
     * true:権限あり, false:権限なし
     * </p>
     */
    private boolean auth4D = false;

    /**
     * 権限登録ユーザー
     */
    private String authRegUser = "";

    /**
     * 権限登録日時
     */
    private Date authRegTime = null;

    /**
     * 権限更新ユーザー
     */
    private String authUpdUser = "";

    /**
     * 権限更新日時
     */
    private Date authUpdTime = null;

    /**
     * 選択された一覧の行番号
     */
    private Integer lineNo = 0;

    /**
     * 削除確認の選択結果
     * <p>
     * 1: OK, 0:Cancel
     * </p>
     */
    private String conf;

    /**
     * コンストラクタ
     */
    public YuimarlUserController() {
    }

    /**
     * ユーザーのリストを取得する。
     *
     * @return ユーザーのリスト
     */
    public List<YuimarlUser> getUserList() {
        return userList;
    }

    /**
     * 登録画面であるかを取得する。
     *
     * @return true:登録画面, false:編集画面
     */
    public boolean isModeCreate() {
        return modeCreate;
    }

    /**
     * 編集画面であるかを取得する。
     *
     * @return true:編集画面, false:照会画面
     */
    public boolean isModeUpdate() {
        return modeUpdate;
    }

    /**
     * パスワードを取得する。
     *
     * @return パスワード
     */
    public String getPassword1() {
        return password1;
    }

    /**
     * パスワードを設定する。
     *
     * @param password1 パスワード
     */
    public void setPassword1(final String password1) {
        this.password1 = password1;
    }

    /**
     * パスワード（再度）を取得する。
     *
     * @return パスワード（再度）
     */
    public String getPassword2() {
        return password2;
    }

    /**
     * パスワード（再度）を設定する。
     *
     * @param password2 パスワード（再度）
     */
    public void setPassword2(final String password2) {
        this.password2 = password2;
    }

    /**
     * Party No.を取得する。
     *
     * @return Party No.
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
     * 名前を取得する。
     *
     * @return 名前
     */
    public String getPartyName() {
        if (partyNo == null || partyNo == 0) {
            return "";
        }
        Party p = partyService.getPartyByPartyNo(partyNo);
        if (p == null) {
            return "";
        }
        return p.getName();
    }

    /**
     * パスワード変更権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthPw() {
        return authPw;
    }

    /**
     * パスワード変更権限を設定する。
     *
     * @param authPw パスワード変更権限
     */
    public void setAuthPw(final boolean authPw) {
        this.authPw = authPw;
    }

    /**
     * ユーザー 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth1V() {
        return auth1V;
    }

    /**
     * ユーザー 照会権限を設定する。
     *
     * @param auth1V ユーザー 照会権限
     */
    public void setAuth1V(final boolean auth1V) {
        this.auth1V = auth1V;
    }

    /**
     * ユーザー 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth1C() {
        return auth1C;
    }

    /**
     * ユーザー 登録権限を設定する。
     *
     * @param auth1C ユーザー 登録権限
     */
    public void setAuth1C(final boolean auth1C) {
        this.auth1C = auth1C;
    }

    /**
     * ユーザー 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth1U() {
        return auth1U;
    }

    /**
     * ユーザー 更新権限を設定する。
     *
     * @param auth1U ユーザー 更新権限
     */
    public void setAuth1U(final boolean auth1U) {
        this.auth1U = auth1U;
    }

    /**
     * ユーザー 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth1D() {
        return auth1D;
    }

    /**
     * ユーザー 削除権限を設定する。
     *
     * @param auth1D ユーザー 削除権限
     */
    public void setAuth1D(final boolean auth1D) {
        this.auth1D = auth1D;
    }

    /**
     * ユーザー権限 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth2V() {
        return auth2V;
    }

    /**
     * ユーザー権限 照会権限を設定する。
     *
     * @param auth2V ユーザー権限 照会権限
     */
    public void setAuth2V(final boolean auth2V) {
        this.auth2V = auth2V;
    }

    /**
     * ユーザー権限 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth2C() {
        return auth2C;
    }

    /**
     * ユーザー権限 登録権限を設定する。
     *
     * @param auth2C ユーザー権限 登録権限
     */
    public void setAuth2C(final boolean auth2C) {
        this.auth2C = auth2C;
    }

    /**
     * ユーザー権限 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth2U() {
        return auth2U;
    }

    /**
     * ユーザー権限 更新権限を設定する。
     *
     * @param auth2U ユーザー権限 更新権限
     */
    public void setAuth2U(final boolean auth2U) {
        this.auth2U = auth2U;
    }

    /**
     * ユーザー権限 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth2D() {
        return auth2D;
    }

    /**
     * ユーザー権限 削除権限を設定する。
     *
     * @param auth2D ユーザー権限 削除権限
     */
    public void setAuth2D(final boolean auth2D) {
        this.auth2D = auth2D;
    }

    /**
     * 物品カテゴリー 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth3V() {
        return auth3V;
    }

    /**
     * 物品カテゴリー 照会権限を設定する。
     *
     * @param auth3V 物品カテゴリー 照会権限
     */
    public void setAuth3V(final boolean auth3V) {
        this.auth3V = auth3V;
    }

    /**
     * 物品カテゴリー 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth3C() {
        return auth3C;
    }

    /**
     * 物品カテゴリー 登録権限を設定する。
     *
     * @param auth3C 物品カテゴリー 登録権限
     */
    public void setAuth3C(final boolean auth3C) {
        this.auth3C = auth3C;
    }

    /**
     * 物品カテゴリー 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth3U() {
        return auth3U;
    }

    /**
     * 物品カテゴリー 更新権限を設定する。
     *
     * @param auth3U 物品カテゴリー 更新権限
     */
    public void setAuth3U(final boolean auth3U) {
        this.auth3U = auth3U;
    }

    /**
     * 物品カテゴリー 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth3D() {
        return auth3D;
    }

    /**
     * 物品カテゴリー 削除権限を設定する。
     *
     * @param auth3D 物品カテゴリー 削除権限
     */
    public void setAuth3D(final boolean auth3D) {
        this.auth3D = auth3D;
    }

    /**
     * 人/組織/物 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth4V() {
        return auth4V;
    }

    /**
     * 人/組織/物 照会権限を設定する。
     *
     * @param auth4V 人/組織/物 照会権限
     */
    public void setAuth4V(final boolean auth4V) {
        this.auth4V = auth4V;
    }

    /**
     * 人/組織/物 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth4C() {
        return auth4C;
    }

    /**
     * 人/組織/物 登録権限を設定する。
     *
     * @param auth4C 人/組織/物 登録権限
     */
    public void setAuth4C(final boolean auth4C) {
        this.auth4C = auth4C;
    }

    /**
     * 人/組織/物 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth4U() {
        return auth4U;
    }

    /**
     * 人/組織/物 更新権限を設定する。
     *
     * @param auth4U 人/組織/物 更新権限
     */
    public void setAuth4U(final boolean auth4U) {
        this.auth4U = auth4U;
    }

    /**
     * 人/組織/物 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuth4D() {
        return auth4D;
    }

    /**
     * 人/組織/物 削除権限を設定する。
     *
     * @param auth4D 人/組織/物 削除権限
     */
    public void setAuth4D(final boolean auth4D) {
        this.auth4D = auth4D;
    }

    /**
     * 権限登録ユーザーを取得する。
     *
     * @return 権限登録ユーザー
     */
    public String getAuthRegUser() {
        return authRegUser;
    }

    /**
     * 権限登録ユーザーを設定する。
     *
     * @param authRegUser 権限登録ユーザー
     */
    public void setAuthRegUser(final String authRegUser) {
        this.authRegUser = authRegUser;
    }

    /**
     * 権限登録日時を取得する。
     *
     * @return 権限登録日時
     */
    public Date getAuthRegTime() {
        if (authRegTime == null) {
            return null;
        }
        return (Date) authRegTime.clone();
    }

    /**
     * 権限登録日時を設定する。
     *
     * @param authRegTime 権限登録日時
     */
    public void setAuthRegTime(final Date authRegTime) {
        if (authRegTime == null) {
            this.authRegTime = null;
        } else {
            this.authRegTime = (Date) authRegTime.clone();
        }
    }

    /**
     * 権限更新ユーザーを取得する。
     *
     * @return 権限更新ユーザー
     */
    public String getAuthUpdUser() {
        return authUpdUser;
    }

    /**
     * 権限更新ユーザーを設定する。
     *
     * @param authUpdUser 権限更新ユーザー
     */
    public void setAuthUpdUser(final String authUpdUser) {
        this.authUpdUser = authUpdUser;
    }

    /**
     * 権限更新日時を取得する。
     *
     * @return 権限更新日時
     */
    public Date getAuthUpdTime() {
        if (authUpdTime == null) {
            return null;
        }
        return (Date) authUpdTime.clone();
    }

    /**
     * 権限更新日時を設定する。
     *
     * @param authUpdTime 権限更新日時
     */
    public void setAuthUpdTime(final Date authUpdTime) {
        if (authUpdTime == null) {
            this.authUpdTime = null;
        } else {
            this.authUpdTime = (Date) authUpdTime.clone();
        }
    }

    /**
     * 選択された一覧の行番号を取得する。
     *
     * @return 選択された一覧の行番号
     */
    public Integer getLineNo() {
        return lineNo;
    }

    /**
     * 選択された一覧の行番号を設定する。
     *
     * @param lineNo 選択された一覧の行番号
     */
    public void setLineNo(final Integer lineNo) {
        this.lineNo = lineNo;
    }

    /**
     * 削除確認の選択結果を取得する。
     *
     * @return 1: OK, 0:Cancel
     */
    public String getConf() {
        return conf;
    }

    /**
     * 削除確認の選択結果を設定する。
     *
     * @param conf 削除確認の選択結果
     */
    public void setConf(final String conf) {
        this.conf = conf;
    }

    /**
     * 選択されているユーザーを取得する。
     *
     * @return 選択されているユーザー
     */
    public YuimarlUser getSelected() {
        if (current == null) {
            current = new YuimarlUser();
        }
        return current;
    }

    /**
     * 一覧画面の一覧ページ制御クラスのインスタンスを取得する。
     *
     * @return 一覧画面の一覧ページ制御クラスのインスタンス
     */
    public YuimarlPagination getPagination() {
        if (pagination == null) {
            initPagination();
        }
        return pagination;
    }

    /**
     * 一覧画面のDataModelを取得する。
     *
     * @return 一覧画面のDataModel
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    /**
     * 一覧画面の一覧ページ制御クラスのインスタンスを初期化する。
     */
    private void initPagination() {
/*
        pagination = new YuimarlPagination(YuimarlPagination.DEFAULT_SIZE,
                userService.count()) {

            @Override
            public DataModel createPageDataModel() {
                return new ListDataModel(userService.findRange(
                        new int[]{getPageFirstItem(),
                            getPageFirstItem() + getPageSize()}));
            }
        };
*/
        pagination = new PaginationSub(YuimarlPagination.DEFAULT_SIZE,
                userService.count(), userService);
        itemsToList();
    }

    /**
     * ページ制御クラス
     */
    static class PaginationSub extends YuimarlPagination {
        /**
         * YuimarlUserService
         */
        private final YuimarlUserService userService;
        /**
         * コンストラクタ
         *
         * @param pageSize 一覧表示行数
         * @param itemsCoun データ全体の件数
         * @param userService YuimarlUserService
         */
        public PaginationSub(final int pageSize, final int itemsCoun,
                final YuimarlUserService userService) {
            super(pageSize, itemsCoun);
            this.userService = userService;
        }

        /**
         * ページのDataModelを取得する。
         *
         * @return ページのDataModel
         */
        @Override
        public DataModel createPageDataModel() {
            return new ListDataModel<>(userService.findRange(
                    new int[]{getPageFirstItem(),
                        getPageFirstItem() + getPageSize()}));
        }
    }

    /**
     * アイテムの配列をリストにセットする。
     */
    private void itemsToList() {
        items = pagination.createPageDataModel();
        Iterator it = items.iterator();
        if (userList == null) {
            userList = new ArrayList<>();
        } else {
            userList.clear();
        }
        while (it.hasNext()) {
            userList.add((YuimarlUser) it.next());
        }
    }

    /**
     * 一覧画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareList() {
        initPagination();
        return "/yuimarlUser/List?faces-redirect=true";
    }

    /**
     * 一覧画面の先頭ページに遷移する。
     *
     * @return 画面遷移先
     */
    public String pageTop() {
        getPagination().topPage();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の1ページ前に遷移する。
     *
     * @return 画面遷移先
     */
    public String pagePrevious() {
        getPagination().previousPage();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の2ページ前に遷移する。
     *
     * @return 画面遷移先
     */
    public String pagePrevious2() {
        getPagination().previous2Page();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の1ページ後に遷移する。
     *
     * @return 画面遷移先
     */
    public String pageNext() {
        getPagination().nextPage();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の2ページ後に遷移する。
     *
     * @return 画面遷移先
     */
    public String pageNext2() {
        getPagination().next2Page();
        itemsToList();
        return null;
    }

    /**
     * 一覧画面の最終ページに遷移する。
     *
     * @return 画面遷移先
     */
    public String pageLast() {
        getPagination().lastPage();
        itemsToList();
        return null;
    }

    /**
     * 表示画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareView() {
        current = userList.get(this.lineNo);
        return "View?faces-redirect=true";
    }

    /**
     * 登録画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareCreate() {
        current = new YuimarlUser();
        modeCreate = true;
        password1 = "";
        password2 = "";
        partyNo = null;
        return "Edit?faces-redirect=true";
    }

    /**
     * 権限画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareAuth() {
        modeUpdate = true;
        current = userList.get(this.lineNo);
        return prepareAuthSub();
    }

    /**
     * 権限画面の設定を行う。
     *
     * @return 画面遷移先
     */
    private String prepareAuthSub() {
        authPw = false;
        auth1V = false;
        auth1C = false;
        auth1U = false;
        auth1D = false;
        auth2V = false;
        auth2C = false;
        auth2U = false;
        auth2D = false;
        auth3V = false;
        auth3C = false;
        auth3U = false;
        auth3D = false;
        auth4V = false;
        auth4C = false;
        auth4U = false;
        auth4D = false;
        authRegUser = "";
        authRegTime = null;
        authUpdUser = "";
        authUpdTime = null;

        String authMap = current.getAuthMap();
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return "Authority?faces-redirect=true";
        }

        authPw = (authMap.charAt(YuimarlUser.AUTH_PASSWORD_CHANGE)
                == YuimarlUser.AUTH_ENABLE);
        auth1V = (authMap.charAt(YuimarlUser.AUTH_USER_VIEW)
                == YuimarlUser.AUTH_ENABLE);
        auth1C = (authMap.charAt(YuimarlUser.AUTH_USER_CREATE)
                == YuimarlUser.AUTH_ENABLE);
        auth1U = (authMap.charAt(YuimarlUser.AUTH_USER_UPDATE)
                == YuimarlUser.AUTH_ENABLE);
        auth1D = (authMap.charAt(YuimarlUser.AUTH_USER_DELETE)
                == YuimarlUser.AUTH_ENABLE);
        auth2V = (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_VIEW)
                == YuimarlUser.AUTH_ENABLE);
        auth2C = (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_CREATE)
                == YuimarlUser.AUTH_ENABLE);
        auth2U = (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_UPDATE)
                == YuimarlUser.AUTH_ENABLE);
        auth2D = (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_DELETE)
                == YuimarlUser.AUTH_ENABLE);
        auth3V = (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_VIEW)
                == YuimarlUser.AUTH_ENABLE);
        auth3C = (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_CREATE)
                == YuimarlUser.AUTH_ENABLE);
        auth3U = (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_UPDATE)
                == YuimarlUser.AUTH_ENABLE);
        auth3D = (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_DELETE)
                == YuimarlUser.AUTH_ENABLE);
        auth4V = (authMap.charAt(YuimarlUser.AUTH_PARTY_VIEW)
                == YuimarlUser.AUTH_ENABLE);
        auth4C = (authMap.charAt(YuimarlUser.AUTH_PARTY_CREATE)
                == YuimarlUser.AUTH_ENABLE);
        auth4U = (authMap.charAt(YuimarlUser.AUTH_PARTY_UPDATE)
                == YuimarlUser.AUTH_ENABLE);
        auth4D = (authMap.charAt(YuimarlUser.AUTH_PARTY_DELETE)
                == YuimarlUser.AUTH_ENABLE);

        if (current.getAuthRegistUser() != null) {
            authRegUser = current.getAuthRegistUser().getUserId() + " "
                    + current.getAuthRegistUser().getParty().getName();
            authRegTime = current.getAuthRegistTime();
        }
        if (current.getAuthUpdateUser() != null) {
            authUpdUser = current.getAuthUpdateUser().getUserId() + " "
                    + current.getAuthUpdateUser().getParty().getName();
            authUpdTime = current.getAuthUpdateTime();
        }

        return "Authority?faces-redirect=true";
    }

    /**
     * 登録を行う。
     *
     * @return 画面遷移先
     */
    public String create() {
        try {
            Party p = checkInput();
            if (p == null) {
                return null;
            }
            String hash = YuimarlUtil.hashMD5(password1);
            current.setPassword(hash);
            current.setParty(p);
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setRegistUser(user);
            current.setRegistTime(new Date());
            userService.create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("UserCreated"));
            LOGGER.log(Level.INFO, "Created YuimarlUser No.:{0}, ID:{1}.",
                    new Object[]{current.getUserNo(), current.getUserId()});
            initPagination();
            return "View?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("PersistenceErrorOccured"));
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * 編集画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareEdit() {
        modeCreate = false;
        current = userList.get(this.lineNo);
        if (current.getParty() == null) {
            partyNo = null;
        } else {
            partyNo = current.getParty().getPartyNo();
        }
        password1 = "";
        password2 = "";
        return "Edit?faces-redirect=true";
    }

    /**
     * 更新を行う。
     *
     * @return 画面遷移先
     */
    public String update() {
        try {
            Party p = checkInput();
            if (p == null) {
                return null;
            }
            String hash = YuimarlUtil.hashMD5(password1);
            current.setPassword(hash);
            //String passMd5 = YuimarlUtil.generateMD5(password1);
            //current.setPassword(password1);
            current.setParty(p);
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setUpdateUser(user);
            current.setUpdateTime(new Date());
            userService.edit(current);
            LOGGER.log(Level.INFO, "Updated YuimarlUser No.:{0}, ID:{1}.",
                    new Object[]{current.getUserNo(), current.getUserId()});
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("UserUpdated"));
            initPagination();
            return "View?faces-redirect=true";
        } catch (OptimisticLockException e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("OptimisticLockException"));
            return null;
        } catch (EJBException e) {
            if (e.getCausedByException() != null
                    && e.getCausedByException().getClass().getTypeName()
                    .equals("javax.persistence.OptimisticLockException")) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                        .getString("OptimisticLockException"));
            } else {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                        .getString("PersistenceErrorOccured"));
            }
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("PersistenceErrorOccured"));
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * ユーザー権限を更新する。
     *
     * @return 画面遷移先
     */
    public String authUpdate() {
        StringBuilder buf = new StringBuilder();
        auth1V = (auth1V || auth1C || auth1U || auth1D);
        auth2V = (auth2V || auth2C || auth2U || auth2D);
        auth3V = (auth3V || auth3C || auth3U || auth3D);
        auth4V = (auth4V || auth4C || auth4U || auth4D);
        buf.append(authPw ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth1V ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth1C ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth1U ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth1D ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth2V ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth2C ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth2U ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth2D ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth3V ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth3C ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth3U ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth3D ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth4V ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth4C ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth4U ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);
        buf.append(auth4D ? YuimarlUser.AUTH_ENABLE : YuimarlUser.AUTH_DISABLE);

        if (current.getAuthMap() == null
                || current.getAuthMap().length() == 0) {
            current.setAuthRegistUser(YuimarlUtil.getLoginUser(userService));
            current.setAuthRegistTime(new Date());
        } else {
            current.setAuthUpdateUser(YuimarlUtil.getLoginUser(userService));
            current.setAuthUpdateTime(new Date());
        }
        current.setAuthMap(buf.toString());
        userService.edit(current);

        LOGGER.log(Level.INFO, "Updated YuimarlUser Authority ID:{0}.",
                current.getUserId());
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                .getString("UserAuthUpdated"));
        prepareAuthSub();
        modeUpdate = false;
        return null;
    }

    /**
     * 編集画面の入力内容をチェックする。
     *
     * @return 正常:Partyのインスタンス, 不正:null
     */
    private Party checkInput() {
        if (current.getUserId().length() < 3) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("user.userId"));
            return null;
        }
        if (!password1.equals(password2)) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("user.passwordAgain"));
            return null;
        }
        if (password1.length() < 4) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("user.password"));
            return null;
        }
        // User IDの存在チェック
        YuimarlUser user = userService.getYuimarlUserByUserId(
                current.getUserId());
        if (user != null) {
            if (modeCreate || !Objects.equals(user.getUserNo(),
                    current.getUserNo())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                        .getString("user.userId.exist"));
                return null;
            }
        }
        Party p = partyService.getPartyByPartyNo(partyNo);
        if (p == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("user.partyNo"));
            return null;
        }
        return p;
    }

    /**
     * 論理削除を行う。
     *
     * @return 画面遷移先
     */
    public String destroy() {
        if (this.conf.equals("0")) {
            return null;
        }
        try {
            current.setDelFlg('1');
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setUpdateUser(user);
            current.setUpdateTime(new Date());
            userService.edit(current);
            LOGGER.log(Level.INFO, "Deleted YuimarlUser No.:{0}, ID:{1}.",
                    new Object[]{current.getUserNo(), current.getUserId()});
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("UserDeleted"));
            initPagination();
            return "List?faces-redirect=true";
        } catch (OptimisticLockException e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("OptimisticLockException"));
            return null;
        } catch (EJBException e) {
            if (e.getCausedByException() != null
                    && e.getCausedByException().getClass().getTypeName()
                    .equals("javax.persistence.OptimisticLockException")) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                        .getString("OptimisticLockException"));
            } else {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                        .getString("PersistenceErrorOccured"));
            }
            return null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("PersistenceErrorOccured"));
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * 直列化
     *
     * @param s ObjectOutputStream
     * @throws IOException 入出力例外
     */
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        // customized serialization code
    }

    /**
     * 直列化復元
     *
     * @param s ObjectInputStream
     * @throws IOException 入出力例外
     * @throws ClassNotFoundException クラスが見つからない
     */
    private void readObject(final ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        // customized deserialization code
        // followed by code to update the object, if necessary
    }
}
