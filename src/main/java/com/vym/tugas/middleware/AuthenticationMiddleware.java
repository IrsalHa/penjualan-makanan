/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 20 - 3 - 2024
 */

package com.vym.tugas.middleware;

import com.vym.tugas.domain.Muser;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Initiator;

import java.util.Map;

public class AuthenticationMiddleware implements Initiator {
    @Override
    public void doInit(Page page, Map<String, Object> map) throws Exception {
        org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
        Muser oUser = (Muser) zkSession.getAttribute("oUser");
        if (oUser == null) {
            Executions.sendRedirect("/");
        }
    }
}
