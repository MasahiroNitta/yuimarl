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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jp.yuimarl.entity.OrganizationCategory;

/**
 * 組織種別操作クラス
 *
 * @author Masahiro Nitta
 */
@Stateless
public class OrganizationCategoryServiceBean
        extends AbstractFacade<OrganizationCategory>
        implements OrganizationCategoryService {

    /**
     * EntityManager
     */
    @PersistenceContext(unitName = "yuimarlPU")
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public OrganizationCategoryServiceBean() {
        super(OrganizationCategory.class);
    }

    /**
     * EntityManagerを取得する。
     *
     * @return
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * すべての組織種別を取得する。
     *
     * @return すべての組織種別
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<OrganizationCategory> findAllOrganization() {
        Query createNamedQuery
                = em.createNamedQuery(
                        "OrganizationCategory.findAllOrganization");
        return createNamedQuery.getResultList();
    }

    /**
     * すべての法人種別を取得する。
     *
     * @return すべての法人種別
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<OrganizationCategory> findAllCorporation() {
        Query createNamedQuery
                = em.createNamedQuery(
                        "OrganizationCategory.findAllCorporation");
        return createNamedQuery.getResultList();
    }

    /**
     * カテゴリNo.を指定して組織種別を取得する。
     *
     * @param categoryNo カテゴリNo.
     * @return OrganizationCategory
     */
    @Override
    public OrganizationCategory getOrganizationCategoryByCategoryNo(
            final Integer categoryNo) {
        Query createNamedQuery
                = em.createNamedQuery("OrganizationCategory.findByCategoryNo");

        createNamedQuery.setParameter("categoryNo", categoryNo);

        if (createNamedQuery.getResultList().size() > 0) {
            return (OrganizationCategory) createNamedQuery.getSingleResult();
        }

        return null;
    }
}
