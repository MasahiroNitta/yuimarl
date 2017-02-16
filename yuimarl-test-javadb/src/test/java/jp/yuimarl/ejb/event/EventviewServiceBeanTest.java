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
package jp.yuimarl.ejb.event;

import java.io.File;
import java.io.FileInputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * イベントビュー操作テストクラス
 *
 * @author Masahiro Nitta
 */
public class EventviewServiceBeanTest extends DBTestCase {

    EntityManager em;
    EventviewServiceBean eventviewServiceBean;

    public EventviewServiceBeanTest(String name) {
        super(name);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.apache.derby.jdbc.ClientDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:derby://localhost:1527/yuimarl");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "app");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "app");
        // System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "" );
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("xml/YUIMARL_DB_INIT_DATA.xml"));
    }

    /**
     *
     * @throws Exception
     */
    @Before
    @Override
    public void setUp() throws Exception {
        IDatabaseConnection connection = null;
        try {
            super.setUp();
            connection = getConnection();

            //テストデータを投入する
            IDataSet dataSet
                    = new FlatXmlDataSetBuilder().build(new FileInputStream("xml/YUIMARL_DB_INIT_DATA.xml"));
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        // "testJavaDbPU" の設定は、yuimarl-lib/src/main/resource/META-INF/persistence.xml で行う。
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testJavaDbPU");
        em = emf.createEntityManager();
        eventviewServiceBean = new EventviewServiceBean(em);
    }

    /**
     *
     */
    @After
    @Override
    public void tearDown() {
        em.close();
    }

    /**
     * spreadEvent() テスト
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testSpreadEvent() throws Exception {
        EntityTransaction et = em.getTransaction();
        et.begin();
        // EventviewSpreadの展開を実行する。
        eventviewServiceBean.spreadEvent(1, 2014);
        //eventviewServiceBean.spreadEvent(2, 2014);
        //eventviewServiceBean.spreadEvent(1, 2015);
        et.commit();

        // テスト実行後のYM_EVENTVIEW_SPREADテーブルの内容を取得する。
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("YM_EVENTVIEW_SPREAD");

        // YM_EVENTVIEW_SPREADテーブルの期待値をロードする。
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("xml/expectedDataSet1.xml"));
        ITable expectedTable = expectedDataSet.getTable("YM_EVENTVIEW_SPREAD");

        // YM_EVENTVIEW_SPREADテーブルの実際の値と期待値を比較する。
        Assertion.assertEquals(expectedTable, actualTable);
    }

}
