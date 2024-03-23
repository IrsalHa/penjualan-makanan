/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.viewmodel;

import com.vym.tugas.dao.MuserDao;
import com.vym.tugas.domain.Muser;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;

import java.math.BigInteger;

@Getter
@Setter
public class DashboardVm {
    private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
    private Muser oUser;
    private Long totalUser;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        try {
            totalUser = new MuserDao().totalUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
