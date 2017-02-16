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
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.YuimarlUser;
import jp.yuimarl.util.JsfUtil;
import jp.yuimarl.util.YuimarlUtil;

/**
 * パスワード変更制御クラス
 *
 * @author Masahiro Nitta
 */
@Named(value = "changePasswordController")
@RequestScoped
public class ChangePasswordController implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * バンドル
     */
    private static final String BUNDLE = "bundles.Bundle";

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(
            ChangePasswordController.class.getName());

    /**
     * YuimarlUserService
     */
    @EJB
    private YuimarlUserService userService;

    /**
     * 現在のパスワード
     */
    private String currentPassword = null;

    /**
     * 新パスワード
     */
    private String newPassword1 = null;

    /**
     * 新パスワード（再度）
     */
    private String newPassword2 = null;

    /**
     * 現在のパスワードを取得する。
     *
     * @return 現在のパスワード
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * 現在のパスワードを設定する。
     *
     * @param currentPassword 現在のパスワード
     */
    public void setCurrentPassword(final String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * 新パスワードを取得する。
     *
     * @return 新パスワード
     */
    public String getNewPassword1() {
        return newPassword1;
    }

    /**
     * 新パスワードを設定する。
     *
     * @param newPassword1 新パスワード
     */
    public void setNewPassword1(final String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    /**
     * 新パスワード（再度）を取得する。
     *
     * @return 新パスワード
     */
    public String getNewPassword2() {
        return newPassword2;
    }

    /**
     * 新パスワード（再度）を設定する。
     *
     * @param newPassword2 新パスワード
     */
    public void setNewPassword2(final String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    /**
     * パスワード変更画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareEditPassword() {
        this.currentPassword = "";
        this.newPassword1 = "";
        this.newPassword2 = "";
        return "/yuimarlUser/EditPassword?faces-redirect=true";
    }

    /**
     * 更新を実行する。
     *
     * @return 画面遷移先
     */
    public String update() {
        YuimarlUser user = YuimarlUtil.getLoginUser(userService);
        if (user == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("user.userId.exist"));
            return null;
        }
        String passMd5 = YuimarlUtil.hashMD5(this.currentPassword);
        //String passMd5 = YuimarlUtil.generateMD5(this.currentPassword);
        if (!user.getPassword().equals(passMd5)) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("CurrentPasswordError"));
            return null;
        }
        if (!this.newPassword1.equals(this.newPassword2)) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("user.passwordAgain"));
            return null;
        }
        if (this.newPassword1.length() < 4) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("user.password"));
            return null;
        }
        passMd5 = YuimarlUtil.hashMD5(this.newPassword1);
        //passMd5 = YuimarlUtil.generateMD5(this.newPassword1);
        user.setPassword(passMd5);
        user.setUpdateUser(user);
        user.setUpdateTime(new Date());
        userService.edit(user);
        LOGGER.log(Level.INFO, "Password changed YuimarlUser No.:{0}, ID:{1}.",
                new Object[]{user.getUserNo(), user.getUserId()});
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                .getString("UserUpdated"));
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
