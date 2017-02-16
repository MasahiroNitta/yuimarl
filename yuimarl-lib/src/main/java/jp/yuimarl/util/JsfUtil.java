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
/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package jp.yuimarl.util;

import java.util.List;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

/**
 *
 * @author masan
 */
public final class JsfUtil {

    /**
     * コンストラクタ
     */
    private JsfUtil() {

    }

    /**
     * リソースバンドルから文字列を取得する。
     *
     * @param bundle バンドル
     * @param message キー
     * @return 文字列
     */
    public static String getStringFromBundle(final String bundle,
            final String message) {
        return ResourceBundle.getBundle(bundle).getString(message);
    }

    /**
     * 文字列のリストからSelectItemの配列を生成する。
     *
     * @param entities リスト
     * @param selectOne true:先頭に空リストを入れる
     * @return SelectItemの配列
     */
    public static SelectItem[] getSelectItems(final List<?> entities,
            final boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    /**
     * エラーメッセージを追加する。
     *
     * @param ex 例外
     * @param defaultMsg デフォルトメッセージ
     */
    public static void addErrorMessage(final Exception ex,
            final String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    /**
     * エラーメッセージを追加する。
     *
     * @param messages メッセージの配列
     */
    public static void addErrorMessages(final List<String> messages) {
        messages.stream().forEach((message) -> {
            addErrorMessage(message);
        });
    }

    /**
     * エラーメッセージを追加する。
     *
     * @param msg メッセージ
     */
    public static void addErrorMessage(final String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    /**
     * エラーメッセージを追加する。
     *
     * @param context コンテキスト
     * @param msg メッセージ
     */
    public static void addErrorMessage(final FacesContext context,
            final String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                msg, msg);
        context.addMessage(null, facesMsg);
    }

    /**
     * エラーメッセージを追加する。
     *
     * @param msg メッセージ
     */
    public static void addSuccessMessage(final String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                msg, msg);
        // FacesContext.getCurrentInstance().getMessageList().clear();
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    /**
     * リクエストパラメータを取得する。
     *
     * @param key キー
     * @return リクエストパラメータ
     */
    public static String getRequestParameter(final String key) {
        return FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get(key);
    }

    /**
     *
     * @param requestParameterName パラメータ
     * @param converter コンバータ
     * @param component コンポーネント
     * @return オブジェクト
     */
    public static Object getObjectFromRequestParameter(
            final String requestParameterName, final Converter converter,
            final UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(),
                component, theId);
    }

}
