/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 28 - 11 - 2023
 */

package com.vym.tugas.viewmodel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.vym.tugas.dao.MuserDao;
import com.vym.tugas.domain.Muser;
import com.vym.utils.helper.MessageBox;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;

@Getter
@Setter
public class AuthentificationVm {
    private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();

    private String userid;
    private String password;
    private String lblMessage;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);

        if (zkSession.getAttribute("oUser") != null) {
            Executions.sendRedirect("/view/index.zul");
        }

    }

    @Command
    @NotifyChange("lblMessage")
    public void doLogin() {
        try {
            if (userid == null || password == null) {
                lblMessage = "Please fill the username and password field";
                MessageBox.error(lblMessage);
                return;
            }

            Muser user = new MuserDao().where("userid = '" + userid + "'");

            if (user == null) {
                lblMessage = "Invalid UserId / Password";
                return;
            }

            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

            if (!result.verified) {
                lblMessage = "Invalid UserId / Password";
                return;
            }

            zkSession.setAttribute("oUser", user);
            Executions.sendRedirect("/view/index.zul");
            lblMessage = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
