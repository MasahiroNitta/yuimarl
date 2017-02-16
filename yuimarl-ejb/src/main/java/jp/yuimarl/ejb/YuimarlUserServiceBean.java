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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ejb.Stateless;
import jp.yuimarl.entity.YuimarlUser;

/**
 * Yuimarlユーザー操作クラス
 *
 * @author Masahiro Nitta
 */
@Stateless
public class YuimarlUserServiceBean extends AbstractFacade<YuimarlUser>
        implements YuimarlUserService {

    /**
     * EntityManager
     */
    @PersistenceContext(unitName = "yuimarlPU")
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public YuimarlUserServiceBean() {
        super(YuimarlUser.class);
    }

    /**
     * コンストラクタ
     *
     * @param em EntityManager
     */
    public YuimarlUserServiceBean(final EntityManager em) {
        this.em = em;
    }

    /**
     * EntityManagerを取得する。
     *
     * @return EntityManager
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * すべての件数を取得する。
     *
     * @return 件数
     */
    @Override
    public int count() {
        Query q = em.createQuery(
                "SELECT COUNT(y) FROM  YuimarlUser y WHERE y.delFlg = '0'");
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * 範囲を指定してYuimarlユーザーを取得する。
     *
     * @param range 範囲
     * @return YuimarlUserのリスト
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<YuimarlUser> findRange(final int[] range) {
        Query q = em.createNamedQuery("YuimarlUser.findAll");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * ユーザーIDを指定してYuimarlユーザーを取得する。
     *
     * @param userId ユーザーID
     * @return YuimarlUser
     */
    @Override
    public YuimarlUser getYuimarlUserByUserId(final String userId) {
        Query createNamedQuery = em.createNamedQuery(
                "YuimarlUser.findByUserId");

        createNamedQuery.setParameter("userId", userId);

        if (createNamedQuery.getResultList().size() > 0) {
            return (YuimarlUser) createNamedQuery.getSingleResult();
        }

        return null;
    }

    /**
     * Party No.を指定してYuimarlユーザーを取得する。
     *
     * @param partyNo Party No.
     * @return YuimarlUser
     */
    @Override
    public YuimarlUser getYuimarlUserByPartyNo(final Integer partyNo) {
        Query createNamedQuery = em.createNamedQuery(
                "YuimarlUser.findByPartyNo");

        createNamedQuery.setParameter("partyNo", partyNo);

        if (createNamedQuery.getResultList().size() > 0) {
            return (YuimarlUser) createNamedQuery.getSingleResult();
        }

        return null;
    }
}
