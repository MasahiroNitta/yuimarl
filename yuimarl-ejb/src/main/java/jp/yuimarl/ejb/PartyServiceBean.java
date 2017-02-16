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
import jp.yuimarl.entity.Party;

/**
 * Party操作クラス
 *
 * @author Masahiro Nitta
 */
@Stateless
public class PartyServiceBean extends AbstractFacade<Party>
        implements PartyService {

    /**
     * EntityManager
     */
    @PersistenceContext(unitName = "yuimarlPU")
    private EntityManager em;

    /**
     * コンストラクタ
     */
    public PartyServiceBean() {
        super(Party.class);
    }

    /**
     * コンストラクタ
     *
     * @param em EntityManager
     */
    public PartyServiceBean(final EntityManager em) {
        super(Party.class);
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
                "SELECT COUNT(p) FROM  Party p WHERE p.delFlg = '0'");
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * 検索条件を指定して件数を取得する。
     *
     * @param cond 検索条件
     * @return 件数
     */
    @Override
    public int condCount(final String cond) {
        Query q = em.createQuery(
                "SELECT COUNT(p) FROM  Party p WHERE p.delFlg = '0' " + cond);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * 範囲を指定してPartyを取得する。
     *
     * @param range 範囲
     * @return Partyのリスト
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Party> findRange(final int[] range) {
        Query q = em.createNamedQuery("Party.findAll");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * Party種別を指定してPartyを取得する。
     *
     * @param partyType Party種別
     * @return Partyのリスト
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Party> findByPartyType(final Character partyType) {
        Query createNamedQuery = em.createNamedQuery("Party.findByPartyType");

        createNamedQuery.setParameter("partyType", partyType);
        return createNamedQuery.getResultList();
    }

    /**
     * Party No.を指定してPartyを取得する。
     *
     * @param partyNo Party No.
     * @return Party
     */
    @Override
    public Party getPartyByPartyNo(final Integer partyNo) {
        Query createNamedQuery = em.createNamedQuery("Party.findByPartyNo");

        createNamedQuery.setParameter("partyNo", partyNo);

        if (createNamedQuery.getResultList().size() > 0) {
            return (Party) createNamedQuery.getSingleResult();
        }

        return null;
    }

    /**
     * 検索条件と範囲を指定してPartyを取得する。
     *
     * @param condition 検索条件
     * @param range 範囲
     * @return Partyのリスト
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Party> findCondRange(final String condition,
            final int[] range) {
        Query q = em.createQuery("SELECT p FROM Party p WHERE p.delFlg = '0' "
                + condition + " ORDER BY p.nameKana");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * PartyとParty関連を削除する。
     * <p>
     * Partyは論理削除。Party関連は物理削除。
     * </p>
     * @param party Party
     */
    @Override
    public void removePartyAndRelation(final Party party) {
        Query q;
        q = em.createQuery(
                "DELETE FROM PartyRelation r WHERE r.party1 = :party1")
                .setParameter("party1", party);
        q.executeUpdate();
        q = em.createQuery(
                "DELETE FROM PartyRelation r WHERE r.party2 = :party2")
                .setParameter("party2", party);
        q.executeUpdate();
        party.setDelFlg('1');
        em.merge(party);
    }
}
