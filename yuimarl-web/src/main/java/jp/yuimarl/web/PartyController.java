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
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import jp.yuimarl.ejb.GoodsCategoryService;
import jp.yuimarl.ejb.OrganizationCategoryService;
import jp.yuimarl.ejb.PartyService;
import jp.yuimarl.ejb.PartyRelationService;
import jp.yuimarl.ejb.PrefectureService;
import jp.yuimarl.ejb.YuimarlUserService;
import jp.yuimarl.entity.Address;
import jp.yuimarl.entity.Corporation;
import jp.yuimarl.entity.Goods;
import jp.yuimarl.entity.Organization;
import jp.yuimarl.entity.OrganizationCategory;
import jp.yuimarl.entity.Party;
import jp.yuimarl.entity.PartyRelation;
import jp.yuimarl.entity.Person;
import jp.yuimarl.entity.Prefecture;
import jp.yuimarl.entity.YuimarlUser;
import jp.yuimarl.util.JsfUtil;
import jp.yuimarl.util.YuimarlPagination;
import jp.yuimarl.util.YuimarlUtil;

/**
 * Party制御クラス
 *
 * @author Masahiro Nitta
 */
@Named(value = "partyController")
@SessionScoped
public class PartyController implements Serializable {

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
            = Logger.getLogger(PartyController.class.getName());

    /**
     * PartyService
     */
    @EJB
    private PartyService partyService;

    /**
     * YuimarlUserService
     */
    @EJB
    private YuimarlUserService userService;

    /**
     * PrefectureService
     */
    @EJB
    private PrefectureService prefectureService;

    /**
     * OrganizationCategoryService
     */
    @EJB
    private OrganizationCategoryService organizationCategoryService;

    /**
     * GoodsCategoryService
     */
    @EJB
    private GoodsCategoryService goodsCategoryService;

    /**
     * PartyRelationService
     */
    @EJB
    private PartyRelationService partyRelationService;

    /**
     * Party
     */
    private Party current = null;

    /**
     * PartyRelation
     */
    private PartyRelation curRel = null;

    /**
     * DataModel
     */
    private DataModel items = null;

    /**
     * PartyRelationのリスト
     */
    private List<PartyRelation> relList = null;

    /**
     * PartyRelationのリスト
     */
    private List<PartyRelation> relPageList = null;

    /**
     * Partyのリスト
     */
    private List<Party> mPartyList = null;

    /**
     * YuimarlPagination
     */
    private YuimarlPagination pagination;

    /**
     * YuimarlPagination
     */
    private YuimarlPagination relPagination;

    /**
     * 性別選択リスト
     */
    private List<SelectItem> genderList;

    /**
     * 都道府県選択リスト
     */
    private List<SelectItem> prefectureList;

    /**
     * 組織種類選択リスト
     */
    private List<SelectItem> organizationCategoryList;

    /**
     * 法人種類選択リスト
     */
    private List<SelectItem> corporationCategoryList;

    /**
     * Party関連種別選択リスト
     */
    private List<SelectItem> relationTypeList;

    /**
     * Party種別選択リスト
     */
    private List<SelectItem> partyTypeList;

    /**
     * 口座種別選択リスト
     */
    private List<SelectItem> accountTypeList;

    /**
     * 登録画面であるか
     * <p>
     * true:登録画面, false:編集画面
     * </p>
     */
    private boolean modeCreate = false;

    /**
     * dynatreeのツリー用タグ
     */
    private String goodsCategoryTree;

    /**
     * 検索用Party No.
     */
    private Integer searchPartyNo;

    /**
     * Partyダイアログ用Party No.
     */
    private Integer dlgPartyNo;

    /**
     * 検索用名前
     */
    private String searchPartyName;

    /**
     * 検索用Party種別
     */
    private String[] partyTypes;

    /**
     * 選択された一覧の行番号
     */
    private Integer lineNo = 0;

    /**
     * Party関連編集画面のParty1のParty No.
     */
    private Integer party1No;

    /**
     * Party関連編集画面のParty2のParty No.
     */
    private Integer party2No;

    /**
     * 削除確認の選択結果
     */
    private String conf;

    /**
     * 検索用のJPQL条件文
     */
    private String searchCondition = "";

    /**
     * Party検索結果JSON
     */
    private String partyJson;

//    /**
//     *
//     */
//    private UISelectBoolean partyType1 = null;
//
//    /**
//     *
//     */
//    private UISelectBoolean partyType2 = null;
//
//    /**
//     *
//     */
//    private UISelectBoolean partyType3 = null;
//
//    /**
//     *
//     */
//    private UISelectBoolean partyType4 = null;
    /**
     * コンストラクタ
     */
    public PartyController() {
    }

    /**
     * 選択されているPartyを取得する。
     *
     * @return Party
     */
    public Party getSelected() {
        if (current == null) {
            current = new Party();
        }
        return current;
    }

    /**
     * 選択されているParty関連を取得する。
     *
     * @return Party関連
     */
    public PartyRelation getRelSelected() {
        if (curRel == null) {
            curRel = new PartyRelation();
        }
        return curRel;
    }

    /**
     * Party一覧画面のDataModelを取得する。
     *
     * @return DataModel
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    /**
     *
     * @return Partyのリスト
     */
    public List<Party> getmPartyList() {
        return mPartyList;
    }

    /**
     * Party関連一覧画面のデータのリスト（全体）を取得する。
     *
     * @return PartyRelationのリスト
     */
    public List<PartyRelation> getRelList() {
        return relList;
    }

    /**
     * Party関連一覧画面のデータのリスト（ページ内）を取得する。
     *
     * @return PartyRelationのリスト
     */
    public List<PartyRelation> getRelPageList() {
        return relPageList;
    }

    /**
     * Party一覧画面の一覧ページ制御クラスのインスタンスを取得する。
     *
     * @return Party一覧画面の一覧ページ制御クラスのインスタンス
     */
    public YuimarlPagination getPagination() {
        if (pagination == null) {
            pagination = new PaginationSub(YuimarlPagination.DEFAULT_SIZE, 0);
        }
        return pagination;
    }

    /**
     * ページ制御クラス
     */
    static class PaginationSub extends YuimarlPagination {

        /**
         * コンストラクタ
         *
         * @param pageSize 一覧表示行数
         * @param itemsCoun データ全体の件数
         */
        public PaginationSub(final int pageSize, final int itemsCoun) {
            super(pageSize, itemsCoun);
        }

        /**
         * ページのDataModelを取得する。
         *
         * @return ページのDataModel
         */
        @Override
        public DataModel createPageDataModel() {
            return new ListDataModel();
        }
    }

    /**
     * Party関連一覧画面の一覧ページ制御クラスのインスタンスを取得する。
     *
     * @return Party関連一覧画面の一覧ページ制御クラスのインスタンス
     */
    public YuimarlPagination getRelPagination() {
        return relPagination;
    }

    /**
     * 選択されているPartyのParty種別を取得する。
     *
     * @return Party種別
     */
    public Character getPartyType() {
        return current.getPartyType();
    }

    /**
     * 選択されているPartyが組織であるかを取得する。
     *
     * @return true:組織
     */
    public boolean isOrganization() {
        return (current instanceof Organization);
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
     * dynatreeのツリー用タグを取得する。
     *
     * @return dynatreeのツリー用タグ
     */
    public String getGoodsCategoryTree() {
        return goodsCategoryTree;
    }

    /**
     * 選択されている物品のカテゴリNo.を取得する。
     *
     * @return 選択されている物品のカテゴリNo.
     */
    public Integer getCategoryNo() {
        if (!(current instanceof Goods)) {
            return null;
        }
        Goods goods = (Goods) current;
        if (goods.getCategory() == null) {
            return null;
        }
        return goods.getCategory().getCategoryNo();
    }

    /**
     * 選択されている物品のカテゴリNo.を設定する。
     *
     * @param categoryNo 選択されている物品のカテゴリNo.
     */
    public void setCategoryNo(final Integer categoryNo) {
        if (!(current instanceof Goods)) {
            return;
        }
        Goods goods = (Goods) current;
        if (categoryNo == null || categoryNo == 0) {
            goods.setCategory(null);
        } else {
            goods.setCategory(
                    goodsCategoryService.findByCategoryNo(categoryNo));
        }
    }

    /**
     * 選択されているParty関連のParty関連種別を取得する。
     *
     * @return 選択されているParty関連のParty関連種別
     */
    public int getRelationType() {
        return getRelSelected().getRelationType();
    }

    /**
     * 選択されているParty関連のParty関連種別を設定する。
     *
     * @param relationType 選択されているParty関連のParty関連種別
     */
    public void setRelationType(final int relationType) {
        getRelSelected().setRelationType(relationType);
    }

    /**
     * 検索用Party No.を取得する。
     *
     * @return 検索用Party No.
     */
    public Integer getSearchPartyNo() {
        return searchPartyNo;
    }

    /**
     * 検索用Party No.を設定する。
     *
     * @param searchPartyNo 検索用Party No.
     */
    public void setSearchPartyNo(final Integer searchPartyNo) {
        this.searchPartyNo = searchPartyNo;
    }

    /**
     * Partyダイアログ用Party No.を取得する。
     *
     * @return Partyダイアログ用Party No.
     */
    public Integer getDlgPartyNo() {
        return dlgPartyNo;
    }

    /**
     * PartyダイアログParty No.を設定する。
     *
     * @param dlgPartyNo PartyダイアログParty No.
     */
    public void setDlgPartyNo(final Integer dlgPartyNo) {
        this.dlgPartyNo = dlgPartyNo;
    }

    /**
     * 検索用名前を取得する。
     *
     * @return 検索用名前
     */
    public String getSearchPartyName() {
        return searchPartyName;
    }

    /**
     * 検索用名前を設定する。
     *
     * @param searchPartyName 検索用名前
     */
    public void setSearchPartyName(final String searchPartyName) {
        this.searchPartyName = searchPartyName;
    }

    /**
     * 検索用Party種別を取得する。
     *
     * @return 検索用Party種別
     */
    public String[] getPartyTypes() {
        if (partyTypes == null) {
            return new String[0];
        }
        return (String[]) partyTypes.clone();
    }

    /**
     * 検索用Party種別を設定する。
     *
     * @param partyTypes 検索用Party種別
     */
    public void setPartyTypes(final String[] partyTypes) {
        if (partyTypes == null) {
            this.partyTypes = null;
        } else {
            this.partyTypes = (String[]) partyTypes.clone();
        }
    }

    /**
     * 選択された一覧の行番号を取得する。
     *
     * @return 選択された一覧の行番号を
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
     * Party関連編集画面のParty1のParty No.を取得する。
     *
     * @return Party関連編集画面のParty1のParty No.
     */
    public Integer getParty1No() {
        return party1No;
    }

    /**
     * Party関連編集画面のParty1のParty No.を設定する。
     *
     * @param party1No Party関連編集画面のParty1のParty No.
     */
    public void setParty1No(final Integer party1No) {
        this.party1No = party1No;
    }

    /**
     * Party関連編集画面のParty2のParty No.を取得する。
     *
     * @return Party関連編集画面のParty2のParty No.
     */
    public Integer getParty2No() {
        return party2No;
    }

    /**
     * Party関連編集画面のParty2のParty No.を設定する。
     *
     * @param party2No Party関連編集画面のParty2のParty No.
     */
    public void setParty2No(final Integer party2No) {
        this.party2No = party2No;
    }

    /**
     * Party関連編集画面のParty1の名前をAjaxで取得する。
     *
     * @return Party関連編集画面のParty1の名前
     */
    public String getParty1Name() {
        if (party1No == null || party1No == 0) {
            return "";
        }
        Party p1 = partyService.getPartyByPartyNo(party1No);
        if (p1 == null) {
            return "";
        }
        return p1.getName();
    }

    /**
     * Party関連編集画面のParty2の名前をAjaxで取得する。
     *
     * @return Party関連編集画面のParty2の名前
     */
    public String getParty2Name() {
        if (party2No == null || party2No == 0) {
            return "";
        }
        Party p2 = partyService.getPartyByPartyNo(party2No);
        if (p2 == null) {
            return "";
        }
        return p2.getName();
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
     * Party関連一覧画面のデータ件数を取得する。
     *
     * @return Party関連一覧画面のデータ件数
     */
    public int getRelListCount() {
        if (relList == null) {
            return 0;
        }
        return relList.size();
    }

    /**
     * 性別選択リストを取得する。
     *
     * @return 性別選択リスト
     */
    public List<SelectItem> getGenderList() {
        if (genderList == null) {
            genderList = new ArrayList<>();
            genderList.add(new SelectItem("1", "男性"));
            genderList.add(new SelectItem("2", "女性"));
            genderList.add(new SelectItem("0", "(不明)"));
        }
        return genderList;
    }

    /**
     * 性別選択リストを設定する。
     *
     * @param genderList 性別選択リスト
     */
    public void setGenderList(final List<SelectItem> genderList) {
        this.genderList = genderList;
    }

    /**
     * 都道府県選択リストを取得する。
     *
     * @return 都道府県選択リスト
     */
    public List<SelectItem> getPrefectureList() {
        if (prefectureList == null) {
            prefectureList = new ArrayList<>();
            prefectureList.add(new SelectItem("0", "(不明)"));
            List<Prefecture> list = prefectureService.findAll();
            list.stream().forEach((p) -> {
                prefectureList.add(
                        new SelectItem(p.getPrefectureNo().toString(),
                                p.getName()));
            });
        }
        return prefectureList;
    }

    /**
     * 都道府県選択リストを設定する。
     *
     * @param prefectureList 都道府県選択リスト
     */
    public void setPrefectureList(final List<SelectItem> prefectureList) {
        this.prefectureList = prefectureList;
    }

    /**
     * 口座種別選択リストを取得する。
     *
     * @return 口座種別選択リスト
     */
    public List<SelectItem> getAccountTypeList() {
        if (accountTypeList == null) {
            accountTypeList = new ArrayList<>();
            accountTypeList.add(new SelectItem("1", "普通"));
            accountTypeList.add(new SelectItem("2", "当座"));
            accountTypeList.add(new SelectItem("0", "(不明)"));
        }
        return accountTypeList;
    }

    /**
     * 口座種別選択リストを設定する。
     *
     * @param accountTypeList 口座種別選択リスト
     */
    public void setAccountTypeList(final List<SelectItem> accountTypeList) {
        this.accountTypeList = accountTypeList;
    }

    /**
     * 都道府県No.を取得する。
     *
     * @return 都道府県No.
     */
    public int getPrefectureNo() {
        if (current.getAddress().getPrefecture() == null) {
            return 0;
        }
        return current.getAddress().getPrefecture().getPrefectureNo();
    }

    /**
     * 都道府県No.を設定する。
     *
     * @param prefectureNo 都道府県No.
     */
    public void setPrefectureNo(final int prefectureNo) {
        if (prefectureNo == 0) {
            current.getAddress().setPrefecture(null);
        }
        current.getAddress().setPrefecture(prefectureService
                .getPrefectureByPrefectureNo(prefectureNo));
    }

    /**
     * 組織種類選択リストを取得する。
     *
     * @return 組織種類選択リスト
     */
    public List<SelectItem> getOrganizationCategoryList() {
        if (organizationCategoryList == null) {
            organizationCategoryList = new ArrayList<>();
            organizationCategoryList.add(new SelectItem("0", "(不明)"));
            List<OrganizationCategory> list = organizationCategoryService
                    .findAllOrganization();
            list.stream().forEach((c) -> {
                organizationCategoryList.add(new SelectItem(
                        c.getCategoryNo().toString(), c.getName()));
            });
        }
        return organizationCategoryList;
    }

    /**
     * 法人種類選択リストを取得する。
     *
     * @return 法人種類選択リスト
     */
    public List<SelectItem> getCorporationCategoryList() {
        if (corporationCategoryList == null) {
            corporationCategoryList = new ArrayList<>();
            corporationCategoryList.add(new SelectItem("0", "(不明)"));
            List<OrganizationCategory> list = organizationCategoryService
                    .findAllCorporation();
            list.stream().forEach((c) -> {
                corporationCategoryList.add(new SelectItem(
                        c.getCategoryNo().toString(), c.getName()));
            });
        }
        return corporationCategoryList;
    }

    /**
     * 組織種類No.を取得する。
     *
     * @return 組織種類No.
     */
    public int getOrganizationCategoryNo() {
        if (!(current instanceof Organization)
                || ((Organization) current).getCategory() == null) {
            return 0;
        }
        return ((Organization) current).getCategory().getCategoryNo();
    }

    /**
     * 組織種類No.を設定する。
     *
     * @param categoryNo 組織種類No.
     */
    public void setOrganizationCategoryNo(final int categoryNo) {
        if (!(current instanceof Organization)) {
            return;
        }
        Organization org = (Organization) current;
        if (categoryNo == 0) {
            org.setCategory(null);
        }
        org.setCategory(organizationCategoryService
                .getOrganizationCategoryByCategoryNo(categoryNo));
    }

    /**
     * Party関連種別選択リストを取得する。
     *
     * @return Party関連種別選択リスト
     */
    public List<SelectItem> getRelationTypeList() {
        if (relationTypeList == null) {
            relationTypeList = new ArrayList<>();
            relationTypeList.add(new SelectItem("0", "(不明)"));
            for (int i = 0; i < PartyRelation.RELATION_TYPES1.size(); i++) {
                relationTypeList.add(new SelectItem(String.valueOf(i + 1),
                        PartyRelation.RELATION_TYPES1.get(i) + "←→"
                        + PartyRelation.RELATION_TYPES2.get(i)));
            }
        }
        return relationTypeList;
    }

    /**
     * Party関連種別選択リストを設定する。
     *
     * @param relationTypeList Party関連種別選択リスト
     */
    public void setRelationTypeList(
            final List<SelectItem> relationTypeList) {
        this.relationTypeList = relationTypeList;
    }

    /**
     * Party種別選択リストを取得する。
     *
     * @return Party種別選択リスト
     */
    public List<SelectItem> getPartyTypeList() {
        if (partyTypeList == null) {
            partyTypeList = new ArrayList<>();
            partyTypeList.add(new SelectItem("1", "人"));
            partyTypeList.add(new SelectItem("2", "組織"));
            partyTypeList.add(new SelectItem("3", "法人"));
            partyTypeList.add(new SelectItem("4", "物品"));
        }
        return partyTypeList;
    }

    /**
     *
     * @return Party検索結果JSON
     */
    public String getPartyJson() {
        return partyJson;
    }

    /**
     *
     * @param partyJson Party検索結果JSON
     */
    public void setPartyJson(final String partyJson) {
        this.partyJson = partyJson;
    }

    /**
     * Party種別選択リストを設定する。
     *
     * @param partyTypeList Party種別選択リスト
     */
    public void setPartyTypeList(final List<SelectItem> partyTypeList) {
        this.partyTypeList = partyTypeList;
    }

//    /**
//     *
//     * @return
//     */
//    public UISelectBoolean getPartyType1() {
//        return partyType1;
//    }
//
//    /**
//     *
//     * @param partyType1
//     */
//    public void setPartyType1(UISelectBoolean partyType1) {
//        this.partyType1 = partyType1;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public UISelectBoolean getPartyType2() {
//        return partyType2;
//    }
//
//    /**
//     *
//     * @param partyType2
//     */
//    public void setPartyType2(UISelectBoolean partyType2) {
//        this.partyType2 = partyType2;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public UISelectBoolean getPartyType3() {
//        return partyType3;
//    }
//
//    /**
//     *
//     * @param partyType3
//     */
//    public void setPartyType3(UISelectBoolean partyType3) {
//        this.partyType3 = partyType3;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public UISelectBoolean getPartyType4() {
//        return partyType4;
//    }
//
//    /**
//     *
//     * @param partyType4
//     */
//    public void setPartyType4(final UISelectBoolean partyType4) {
//        this.partyType4 = partyType4;
//    }
    /**
     * Party一覧画面のDataModelを初期化する。
     */
    private void recreateModel() {
        items = null;
    }

    /**
     * Party一覧画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareList() {
        initPagination();
        return "/party/List?faces-redirect=true";
    }

    /**
     * Party一覧画面の先頭ページに遷移する。
     *
     * @return 画面遷移先
     */
    public String pageTop() {
        getPagination().topPage();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の先頭ページに遷移する。（モバイル用）
     *
     * @return 画面遷移先
     */
    public String pageTopM() {
        getPagination().topPage();
        items = getPagination().createPageDataModel();
        itemsToList();
        return null;
    }

    /**
     * Party一覧画面の1ページ前に遷移する。
     *
     * @return 画面遷移先
     */
    public String pagePrevious() {
        getPagination().previousPage();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の1ページ前に遷移する。（モバイル用）
     *
     * @return 画面遷移先
     */
    public String pagePreviousM() {
        getPagination().previousPage();
        items = getPagination().createPageDataModel();
        itemsToList();
        return null;
    }

    /**
     * Party一覧画面の2ページ前に遷移する。
     *
     * @return 画面遷移先
     */
    public String pagePrevious2() {
        getPagination().previous2Page();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の1ページ後に遷移する。
     *
     * @return 画面遷移先
     */
    public String pageNext() {
        getPagination().nextPage();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の1ページ後に遷移する。（モバイル用）
     *
     * @return 画面遷移先
     */
    public String pageNextM() {
        getPagination().nextPage();
        items = getPagination().createPageDataModel();
        itemsToList();
        return null;
    }

    /**
     * Party一覧画面の2ページ後に遷移する。
     *
     * @return 画面遷移先
     */
    public String pageNext2() {
        getPagination().next2Page();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の最終ページに遷移する。
     *
     * @return 画面遷移先
     */
    public String pageLast() {
        getPagination().lastPage();
        recreateModel();
        return null;
    }

    /**
     * Party一覧画面の最終ページに遷移する。（モバイル用）
     *
     * @return 画面遷移先
     */
    public String pageLastM() {
        getPagination().lastPage();
        items = getPagination().createPageDataModel();
        itemsToList();
        return null;
    }

    /**
     * Party表示画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareView() {
        current = (Party) getItems().getRowData();
        YuimarlUtil.setCurrentParty(current);
        return "View?faces-redirect=true";
    }

    /**
     * 人の登録画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String preparePersonCreate() {
        Person p = new Person();
        p.setAddress(new Address());
        p.setGender(0);
        current = p;
        modeCreate = true;
        return "/party/Edit?faces-redirect=true";
    }

    /**
     * 組織の登録画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareOrganizationCreate() {
        Organization o = new Organization();
        o.setAddress(new Address());
        current = o;
        modeCreate = true;
        return "/party/Edit?faces-redirect=true";
    }

    /**
     * 法人の登録画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareCorporationCreate() {
        Corporation c = new Corporation();
        c.setAddress(new Address());
        current = c;
        modeCreate = true;
        return "/party/Edit?faces-redirect=true";
    }

    /**
     * 物品の登録画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareGoodsCreate() {
        Goods g = new Goods();
        g.setAddress(new Address());
        goodsCategoryTree = goodsCategoryService.makeTag(false);
        current = g;
        modeCreate = true;
        return "/party/Edit?faces-redirect=true";
    }

    /**
     * Partyの編集画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareEdit() {
        modeCreate = false;
        current = (Party) getItems().getRowData();
        if (current.getAddress() == null) {
            current.setAddress(new Address());
        }
        if (current instanceof Goods) {
            goodsCategoryTree = goodsCategoryService.makeTag(false);
        }
        return "Edit?faces-redirect=true";
    }

    /**
     * Party関連一覧画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareRelationList() {
        current = (Party) getItems().getRowData();
        initRelList();

        return "RelList?faces-redirect=true";
    }

    /**
     * Party関連一覧画面の先頭ページに遷移する。
     *
     * @return 画面遷移先
     */
    public String relPageTop() {
        relPagination.topPage();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の1ページ前に遷移する。
     *
     * @return 画面遷移先
     */
    public String relPagePrevious() {
        relPagination.previousPage();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の2ページ前に遷移する。
     *
     * @return 画面遷移先
     */
    public String relPagePrevious2() {
        relPagination.previous2Page();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の1ページ後に遷移する。
     *
     * @return 画面遷移先
     */
    public String relPageNext() {
        relPagination.nextPage();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の2ページ後に遷移する。
     *
     * @return 画面遷移先
     */
    public String relPageNext2() {
        relPagination.next2Page();
        relPageChange();
        return null;
    }

    /**
     * Party関連一覧画面の最終ページに遷移する。
     *
     * @return 画面遷移先
     */
    public String relPageLast() {
        relPagination.lastPage();
        relPageChange();
        return null;
    }

    /**
     * Party関連の登録画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareRelCreate() {
        PartyRelation r = new PartyRelation();
        r.setRelationType(1);
        curRel = r;
        this.party1No = null;
        this.party2No = null;
        modeCreate = true;
        return "RelEdit?faces-redirect=true";
    }

    /**
     * Party関連の編集画面に遷移する。
     *
     * @return 画面遷移先
     */
    public String prepareRelEdit() {
        modeCreate = false;
        curRel = relPageList.get(this.lineNo);
        this.party1No = curRel.getParty1().getPartyNo();
        this.party2No = curRel.getParty2().getPartyNo();
        return "RelEdit?faces-redirect=true";
    }

    /**
     * Party一覧画面の一覧ページ制御クラスのインスタンスを生成する。
     */
    private void initPagination() {
        /*
        pagination = new YuimarlPagination(YuimarlPagination.DEFAULT_SIZE,
                partyService.condCount(searchCondition)) {

            @Override
            public DataModel createPageDataModel() {
                return new ListDataModel(partyService.findCondRange(
                        searchCondition, new int[]{getPageFirstItem(),
                            getPageFirstItem() + getPageSize()}));
            }
        };
         */
        pagination = new PaginationSub3(YuimarlPagination.DEFAULT_SIZE,
                partyService.condCount(searchCondition), partyService,
                searchCondition);
        items = pagination.createPageDataModel();
    }

    /**
     * ページ制御クラス
     */
    static class PaginationSub3 extends YuimarlPagination {

        /**
         * PartyService
         */
        private final PartyService partyService;
        /**
         * 検索条件
         */
        private String searchCondition = "";

        /**
         * コンストラクタ
         *
         * @param pageSize 一覧表示行数
         * @param itemsCoun データ全体の件数
         * @param partyService PartyService
         * @param searchCondition 検索条件
         */
        public PaginationSub3(final int pageSize, final int itemsCoun,
                final PartyService partyService, final String searchCondition) {
            super(pageSize, itemsCoun);
            this.partyService = partyService;
            this.searchCondition = searchCondition;
        }

        /**
         * ページのDataModelを取得する。
         *
         * @return ページのDataModel
         */
        @Override
        public DataModel createPageDataModel() {
            return new ListDataModel<>(partyService.findCondRange(
                    searchCondition, new int[]{getPageFirstItem(),
                        getPageFirstItem() + getPageSize()}));
        }
    }

    /**
     * Party関連一覧画面のデータを取得し、データのリストを構成する。
     */
    private void initRelList() {
        relList = partyRelationService.findByParty(current);
        // 一覧ページ制御クラスのインスタンスを生成する。
        relPagination = new PaginationSub2(YuimarlPagination.DEFAULT_SIZE,
                relList.size());
        relPageChange();
    }

    /**
     * ページ制御クラス2
     */
    static class PaginationSub2 extends YuimarlPagination {

        /**
         * コンストラクタ
         *
         * @param pageSize 一覧表示行数
         * @param itemsCoun データ全体の件数
         */
        public PaginationSub2(final int pageSize, final int itemsCoun) {
            super(pageSize, itemsCoun);
        }

        /**
         * ページのDataModelを取得する。
         *
         * @return ページのDataModel
         */
        @Override
        public DataModel createPageDataModel() {
            return null;
        }
    }

    /**
     * Party関連一覧画面の、データのリスト（ページ内）を構成する。
     */
    private void relPageChange() {
        relPageList = new ArrayList<>();
        int j = relPagination.getPageFirstItem();
        for (int i = 0; i < YuimarlPagination.DEFAULT_SIZE; i++) {
            if (j >= relList.size()) {
                break;
            }
            relPageList.add(relList.get(j));
            j++;
        }
    }

    /**
     * Partyの登録を行う。
     *
     * @return 画面遷移先
     */
    public String create() {
        try {
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setRegistUser(user);
            current.setRegistTime(new Date());
            partyService.create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("PartyCreated"));
            LOGGER.log(Level.INFO, "Created party No.:{0}, Name:{1}.",
                    new Object[]{current.getPartyNo(), current.getName()});
            initPagination();
            return "View?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Partyの更新を行う。
     *
     * @return 画面遷移先
     */
    public String update() {
        try {
            LOGGER.log(Level.INFO, "Updating party No.:{0}, Name:{1}.",
                    new Object[]{current.getPartyNo(), current.getName()});
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            current.setUpdateUser(user);
            current.setUpdateTime(new Date());
            partyService.edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("PartyUpdated"));
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
            return null;
        }
    }

    /**
     * Partyの削除を行う。
     *
     * @return 画面遷移先
     */
    public String destroy() {
        try {
            if (this.conf.equals("0")) {
                // 削除確認がOKでなければ、削除しない。
                return null;
            }
            current = (Party) getItems().getRowData();
            // このPartyがYuimarlUserで使用されていれば、削除できない。
            YuimarlUser u = userService.getYuimarlUserByPartyNo(
                    current.getPartyNo());
            if (u != null) {
                JsfUtil.addErrorMessage("This party is used by YuimarlUser: "
                        + u.getUserId() + ".");
                return null;
            }
            u = YuimarlUtil.getLoginUser(userService);
            current.setUpdateUser(u);
            current.setUpdateTime(new Date());
            LOGGER.log(Level.INFO, "Deleted party No.:{0}, Name:{1}.",
                    new Object[]{current.getPartyNo(), current.getName()});
            // PartyとParty関連を削除する。
            partyService.removePartyAndRelation(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("PartyDeleted"));
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
            return null;
        }
    }

    /**
     * Party関連の登録を行う。
     *
     * @return 画面遷移先
     */
    public String relCreate() {
        try {
            Party p1 = partyService.getPartyByPartyNo(party1No);
            if (p1 == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                        .getString("user.partyNo"));
                return null;
            }
            Party p2 = partyService.getPartyByPartyNo(party2No);
            if (p2 == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                        .getString("user.partyNo"));
                return null;
            }
            if (Objects.equals(party1No, party2No)) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                        .getString("party1equal2"));
                return null;
            }
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            curRel.setParty1(p1);
            curRel.setParty2(p2);
            curRel.setRegistUser(user);
            curRel.setRegistTime(new Date());
            partyRelationService.create(curRel);
            LOGGER.log(Level.INFO, "Created partyRelation No.:{0}.",
                    curRel.getRelationNo());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("PartyCreated"));
            initRelList();
            return "RelList?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Party関連の更新を行う。
     *
     * @return 画面遷移先
     */
    public String relUpdate() {
        try {
            Party p1 = partyService.getPartyByPartyNo(party1No);
            if (p1 == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                        .getString("user.partyNo"));
                return null;
            }
            Party p2 = partyService.getPartyByPartyNo(party2No);
            if (p2 == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                        .getString("user.partyNo"));
                return null;
            }
            if (Objects.equals(party1No, party2No)) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE)
                        .getString("party1equal2"));
                return null;
            }
            LOGGER.log(Level.INFO, "Updating partyRelation No.:{0}.",
                    curRel.getRelationNo());
            YuimarlUser user = YuimarlUtil.getLoginUser(userService);
            curRel.setParty1(p1);
            curRel.setParty2(p2);
            curRel.setUpdateUser(user);
            curRel.setUpdateTime(new Date());
            partyRelationService.edit(curRel);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("PartyUpdated"));
            initRelList();
            return "RelList?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Party関連の削除を行う。
     *
     * @return 画面遷移先
     */
    public String relDestroy() {
        try {
            if (this.conf.equals("0")) {
                // 削除確認がOKでなければ、削除しない。
                return null;
            }
            curRel = relPageList.get(this.lineNo);
            LOGGER.log(Level.INFO, "Deleting partyRelation No.:{0}.",
                    curRel.getRelationNo());
            partyRelationService.remove(curRel);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE)
                    .getString("PartyDeleted"));
            initRelList();
            return "RelList?faces-redirect=true";
        } catch (OptimisticLockException e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE)
                    .getString("OptimisticLockException"));
            return null;
        } catch (EJBException e) {
            if (e.getCausedByException() != null
                    && e.getCausedByException().getClass().getTypeName()
                    .equals(
                            "javax.persistence.OptimisticLockException")) {
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
            return null;
        }
    }

    /**
     * 検索を実行する。
     *
     * @return 画面遷移先
     */
    public String search() {
        // 検索用のJPQL条件文を生成する。
        searchCondition = "";
        if (searchPartyNo != null && searchPartyNo != 0) {
            searchCondition = "AND p.partyNo = " + searchPartyNo;
        } else {
            if (searchPartyName != null
                    && searchPartyName.trim().length() > 0) {
                searchCondition = "AND p.name LIKE '%"
                        + searchPartyName.trim().replaceAll("'", "''") + "%'";
            }
            if (partyTypes != null && partyTypes.length > 0
                    && partyTypes.length != 4) {
                searchCondition += "AND p.partyType IN (";
                for (int i = 0; i < partyTypes.length; i++) {
                    if (i > 0) {
                        searchCondition += ",";
                    }
                    searchCondition += "'" + partyTypes[i] + "'";
                }
                searchCondition += ")";
            }
        }

        initPagination();
        return null;
    }

    /**
     * アイテムの配列をリストにセットする。
     */
    private void itemsToList() {
        Iterator it = items.iterator();
        if (mPartyList == null) {
            mPartyList = new ArrayList<>();
        } else {
            mPartyList.clear();
        }
        while (it.hasNext()) {
            mPartyList.add((Party) it.next());
        }
    }

    /**
     * AjaxでPartyの検索を行い、結果をJSONで返す。
     *
     * @param event AjaxBehaviorEvent
     * @throws AbortProcessingException プロセス例外
     */
    public void ajaxPartySearch(final AjaxBehaviorEvent event)
            throws AbortProcessingException {
        Party p = partyService.getPartyByPartyNo(this.dlgPartyNo);
        if (p == null) {
            this.partyJson = "";
            return;
        }
        this.partyJson = p.partyToJson();
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
