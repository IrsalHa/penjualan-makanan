/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 21 - 11 - 2023
 */

package com.vym.utils.db;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppCleanup;
import org.zkoss.zk.ui.util.WebAppInit;

public class HibernateListener implements WebAppInit, WebAppCleanup {

    @Override
    public void init(WebApp wapp){
    	
    	try {
            StoreHibernateUtil.getSessionFactory();
    	}catch (Exception e) {
			e.printStackTrace();
		}

    }

    @Override
    public void cleanup(WebApp wapp) throws Exception {
    	try {
            StoreHibernateUtil.getSessionFactory().close();
    	}catch (Exception e) {
			e.printStackTrace();
		}

    }
}
