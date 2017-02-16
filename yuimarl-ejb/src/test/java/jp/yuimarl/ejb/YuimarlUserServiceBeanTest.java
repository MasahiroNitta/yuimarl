/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2017 BitFarm Corporation
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import jp.yuimarl.entity.Party;
import jp.yuimarl.entity.YuimarlUser;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * AchieveUserServiceBeanテストクラス
 *
 * @author Masahiro Nitta
 */
public class YuimarlUserServiceBeanTest {

    private static EntityManager em;
    private static PartyServiceBean partyServiceBean;
    private static YuimarlUserServiceBean yuimarlUserServiceBean;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // テスト用のH2インメモリDBを作成。
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testH2PU");
        em = emf.createEntityManager();
        partyServiceBean = new PartyServiceBean(em);
        yuimarlUserServiceBean = new YuimarlUserServiceBean(em);
        EntityTransaction et = em.getTransaction();
        et.begin();

        Party party;
        YuimarlUser user;
        party = new Party();
        party.setName("山田 太郎");
        party.setNameKana("やまだ たろう");
        party.setMailAddress1("tyamada@mail.com");
        partyServiceBean.create(party);

        user = new YuimarlUser();
        user.setParty(party);
        user.setUserId("U00001");
        user.setPassword("password");
        yuimarlUserServiceBean.create(user);

        party = new Party();
        party.setName("殿馬 一人");
        party.setNameKana("とのま かずと");
        party.setMailAddress1("ktonoma@mail.com");
        partyServiceBean.create(party);

        user = new YuimarlUser();
        user.setParty(party);
        user.setUserId("U00002");
        user.setPassword("password");
        yuimarlUserServiceBean.create(user);

        et.commit();
    }

    @AfterClass
    public static void tearDownClass() {
        em.close();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() {
    }

    @Test
    public final void testCount() {
        int count = yuimarlUserServiceBean.count();
        assertEquals(2, count);
    }

    @Test
    public final void testGetYuimarlUserByUserId() {
        YuimarlUser user;
        user = yuimarlUserServiceBean.getYuimarlUserByUserId("U00001");
        assertNotNull(user);
        assertEquals("山田 太郎", user.getParty().getName());
        user = yuimarlUserServiceBean.getYuimarlUserByUserId("U00002");
        assertNotNull(user);
        assertEquals(43, user.getParty().getPartyNo().intValue());
        assertEquals("殿馬 一人", user.getParty().getName());
    }
    
}
