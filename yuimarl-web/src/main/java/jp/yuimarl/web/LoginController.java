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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.YuimarlUser;
import jp.yuimarl.util.YuimarlUtil;

/**
 * ログイン制御クラス
 *
 * @author Masahiro Nitta
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {
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
            = Logger.getLogger(LoginController.class.getName());

    /**
     * YuimarlUserService
     */
    @EJB
    private YuimarlUserService userService;

    /**
     * ユーザーID
     */
    private String userId = "";

    /**
     * パスワード
     */
    private String password = "";

    /**
     * ログイン状態
     */
    private boolean logged = false;

    /**
     * ユーザー名
     */
    private String userName = "";

    /**
     * ユーザー権限
     */
    private String authMap = null;

    /**
     * ユーザーIDを取得する。
     *
     * @return ユーザーID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * ユーザーIDを設定する。
     *
     * @param userId ユーザーID
     */
    public void setUserId(final String userId) {
        this.userId = userId;
    }

    /**
     * パスワードを取得する。
     *
     * @return パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードを設定する。
     *
     * @param password パスワード
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * ログイン状態を取得する。
     *
     * @return true:ログイン済, false:未ログイン
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * ユーザー名を取得する。
     *
     * @return ユーザー名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * ユーザー名を設定する。
     *
     * @param userName ユーザー名
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * 人/組織/物 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthPartyView() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_PARTY_VIEW)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * 人/組織/物 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthPartyCreate() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_PARTY_CREATE)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * 人/組織/物 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthPartyUpdate() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_PARTY_UPDATE)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * 人/組織/物 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthPartyDelete() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_PARTY_DELETE)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * ユーザー 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthUserView() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_USER_VIEW)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * ユーザー 登録権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthUserCreate() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_USER_CREATE)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * ユーザー 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthUserUpdate() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_USER_UPDATE)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * ユーザー 削除権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthUserDelete() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_USER_DELETE)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * ユーザー権限 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthUserAuthView() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_VIEW)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * ユーザー権限 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthUserAuthUpdate() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_USER_AUTH_UPDATE)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * 物品カテゴリー 照会権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthGoodsCategoryView() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_VIEW)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * 物品カテゴリー 更新権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthGoodsCategoryUpdate() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_UPDATE)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * システムメニュー表示権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthSystemMenuView() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_USER_VIEW)
                == YuimarlUser.AUTH_ENABLE
                || authMap.charAt(YuimarlUser.AUTH_GOODS_CATEGORY_VIEW)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * パスワード変更権限を取得する。
     *
     * @return true:権限あり, false:権限なし
     */
    public boolean isAuthUserPassword() {
        if (authMap == null
                || authMap.length() != YuimarlUser.AUTH_MAP_LENGTH) {
            return false;
        }
        return (authMap.charAt(YuimarlUser.AUTH_PASSWORD_CHANGE)
                == YuimarlUser.AUTH_ENABLE);
    }

    /**
     * ログインを実行する。
     *
     * @return 画面遷移先
     */
    public String login() {
        logged = false;
        LOGGER.log(Level.INFO, "*** LoginController.login");
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        HttpServletRequest request
                = (HttpServletRequest) externalContext.getRequest();
        try {
            //ログイン処理
            request.login(this.userId, this.password);

            //ユーザIDからアカウント情報を取得
            //Account account = accountFacade.findByUserId(userId);
            YuimarlUser user = userService.getYuimarlUserByUserId(this.userId);

            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap()
                    .put("loginUserId", this.userId);
            logged = true;
            userName = user.getParty().getName();
            authMap = user.getAuthMap();
            //logger.info("User logged in.");
            YuimarlUtil.writeLog(LOGGER, "User logged in.");

            return "/main/index?faces-redirect=true";
        } catch (ServletException e) {
            String msg = ResourceBundle.getBundle(BUNDLE)
                    .getString("LoginError");
            FacesMessage facesMsg
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return null;
        }
    }

    /**
     * ログアウトを実行する。
     *
     * @return 画面遷移先
     */
    public String logout() {
        ExternalContext externalContext
                = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request
                = (HttpServletRequest) externalContext.getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            String msg = ResourceBundle.getBundle(BUNDLE)
                    .getString("SystemError");
            FacesMessage facesMsg = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            return null;
        }
        logged = false;
        userName = "";
        this.userId = "";
        this.password = "";
        return "/main/index?faces-redirect=true";
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
