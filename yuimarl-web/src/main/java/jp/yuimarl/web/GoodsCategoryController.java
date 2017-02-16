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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.stream.JsonParser;
import jp.yuimarl.ejb.GoodsCategoryService;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.GoodsCategory;
import jp.yuimarl.entity.YuimarlUser;
import jp.yuimarl.util.GCItem;
import jp.yuimarl.util.JsfUtil;
import jp.yuimarl.util.YuimarlUtil;

/**
 * 物品カテゴリ制御クラス
 *
 * @author Masahiro Nitta
 */
@Named(value = "goodsCategoryController")
@RequestScoped
public class GoodsCategoryController implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * バンドル
     */
    private static final String BUNDLE = "bundles.Bundle";

    /**
     * GoodsCategoryService
     */
    @EJB
    private GoodsCategoryService goodsCategoryService;

    /**
     * YuimarlUserService
     */
    @EJB
    private YuimarlUserService userService;

    /**
     * ツリー生成用タグ
     */
    private String treeTag;

    /**
     * ツリー内容のJSON
     */
    private String json;

    /**
     * YuimarlUser
     */
    private YuimarlUser user;

    /**
     * ツリー生成用タグを取得する。
     *
     * @return ツリー生成用タグ
     */
    public String getTreeTag() {
        return treeTag;
    }

    /**
     * ツリー生成用タグを設定する。
     *
     * @param treeTag ツリー生成用タグ
     */
    public void setTreeTag(final String treeTag) {
        this.treeTag = treeTag;
    }

    /**
     * ツリー内容のJSONを取得する。
     *
     * @return ツリー内容のJSON
     */
    public String getJson() {
        return json;
    }

    /**
     * ツリー内容のJSONを設定する。
     *
     * @param json ツリー内容のJSON
     */
    public void setJson(final String json) {
        this.json = json;
    }

    /**
     * 編集画面準備
     *
     * @return 画面遷移先
     */
    public String prepareEdit() {
        // dynatreeのツリー用タグを生成する。
        treeTag = goodsCategoryService.makeTag(true);
        return "/goodsCategory/Edit";
    }

    /**
     * 保存ボタン押下時の動作
     *
     * @return 画面遷移先
     */
    public String update() {
        try {
            user = YuimarlUtil.getLoginUser(userService);
            GCItem item = null;
            GCItem parent;
            // レベルの系譜を保持する配列
            List<GCItem> itemList = new ArrayList<>();
            List<GCItem> genealogy = new ArrayList<>();
            String key = null;
            String wk;
            int idx;
            int level = 0;

            // ツリーの内容がjsonに格納されているので、解析を行う。
            JsonParser parser = Json.createParser(new StringReader(this.json));

            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case START_OBJECT:
                        level++;
                        item = new GCItem();
                        item.setLevel(level);
                        if (genealogy.size() < level) {
                            genealogy.add(item);
                        } else {
                            genealogy.set(level - 1, item);
                        }
                        if (level > 1) {
                            parent = genealogy.get(level - 2);
                            parent.getChildren().add(item);
                            item.setParent(parent.getCategoryNo());
                        }
                        itemList.add(item);
                        break;
                    case END_OBJECT:
                        level--;
                        break;
                    case KEY_NAME:
                        key = parser.getString();
                        break;
                    case VALUE_STRING:
                        if (level > 1) {
                            if (key != null) {
                                switch (key) {
                                    case "title":
                                        item.setName(parser.getString());
                                        break;
                                    case "key":
                                        wk = parser.getString();
                                        idx = wk.indexOf(',');
                                        item.setCategoryNo(
                                                Integer.parseInt(
                                                        wk.substring(0, idx)));
                                        item.setNameKana(wk.substring(idx + 1));
                                        if (item.getCategoryNo() == 0) {
                                            // カテゴリNo.が0の場合、新規登録する。
                                            GoodsCategory g
                                                    = new GoodsCategory();
                                            g.setName(item.getName());
                                            g.setNameKana(item.getNameKana());
                                            g.setRegistUser(user);
                                            g.setRegistTime(new Date());
                                            goodsCategoryService.create(g);
                                            item.setCategoryNo(
                                                    g.getCategoryNo());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            matchingUpdate(itemList);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("PartyUpdated"));
            return prepareEdit();
        } catch (NumberFormatException e) {
            e.printStackTrace(System.err);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * ユーザーが編集したリストと、DBに保存されているデータを比較し、
     * 異なっている場合に変更をDBに反映する。
     *
     * @param itemList GCItemのリスト
     */
    private void matchingUpdate(final List<GCItem> itemList) {
        boolean exist;
        List<GoodsCategory> list = goodsCategoryService.findAll();
        for (GoodsCategory g : list) {
            exist = false;
            for (GCItem item : itemList) {
                if (Objects.equals(g.getCategoryNo(), item.getCategoryNo())) {
                    exist = true;
                    if (!g.getName().equals(item.getName())
                            || !g.getNameKana().equals(item.getNameKana())
                            || g.getParentCategoryNo() != item.getParent()) {
                        // 同じカテゴリNo.で、内容が異なっている場合は、更新する。
                        g.setName(item.getName());
                        g.setNameKana(item.getNameKana());
                        if (item.getParent() == 0) {
                            g.setParentCategory(null);
                        } else {
                            for (GoodsCategory p : list) {
                                if (p.getCategoryNo() == item.getParent()) {
                                    g.setParentCategory(p);
                                    break;
                                }
                            }
                        }
                        g.setUpdateUser(user);
                        g.setUpdateTime(new Date());
                        goodsCategoryService.edit(g);
                    }
                    break;
                }
            }
            if (!exist) {
                // リストになくなった場合は、論理削除する。
                g.setUpdateUser(user);
                g.setUpdateTime(new Date());
                goodsCategoryService.deleteWithGoods(g);
            }
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
