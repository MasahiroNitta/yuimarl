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
package jp.yuimarl.ejb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author markito
 * @author Masahiro Nitta
 * @param <T>
 */
public abstract class AbstractFacade<T> {

    /**
     * エンティティクラス
     */
    private Class<T> entityClass;

    /**
     * コンストラクタ
     */
    public AbstractFacade() {

    }

    /**
     * コンストラクタ
     *
     * @param entityClass エンティティクラス
     */
    public AbstractFacade(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * EntityManagerを取得する。
     *
     * @return EntityManager
     */
    protected abstract EntityManager getEntityManager();

    /**
     * エンティティを作成する。
     *
     * @param entity エンティティ
     */
    public void create(final T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * エンティティを更新する。
     *
     * @param entity エンティティ
     */
    public void edit(final T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * エンティティを削除する。
     *
     * @param entity エンティティ
     */
    public void remove(final T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * エンティティを検索する。
     *
     * @param id ID
     * @return エンティティ
     */
    public T find(final Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * すべてのエンティティを検索する。
     *
     * @return エンティティのリスト
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                .getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * 範囲指定してエンティティを検索する。
     *
     * @param range 範囲
     * @return エンティティのリスト
     */
    @SuppressWarnings("unchecked")
    public List<T> findRange(final int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                .getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * 範囲指定してエンティティを検索する。
     *
     * @param range 範囲
     * @param query 条件
     * @return エンティティのリスト
     */
    @SuppressWarnings("unchecked")
    public List<T> findRange(final int[] range, final CriteriaQuery query) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     *
     * @return CriteriaBuilder
     */
    public CriteriaBuilder getCriteriaBuilder() {
        return getEntityManager().getCriteriaBuilder();
    }

    /**
     * 件数を取得する。
     *
     * @return 件数
     */
    @SuppressWarnings("unchecked")
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager()
                .getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
