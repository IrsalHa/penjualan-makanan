/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.viewmodel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.vym.tugas.dao.MuserDao;
import com.vym.tugas.domain.Muser;
import com.vym.tugas.validator.MuserValidator;
import com.vym.utils.db.StoreHibernateUtil;
import com.vym.utils.helper.MessageBox;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@Getter
@Setter
public class MuserFormVm {
    private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
    private Muser oUser;
    private Muser muser;
    private Boolean isEdit = false;
    private String title;

    @Wire
    private Grid grid;
    @Wire
    private Window winMuser;
    @Wire
    private Div divPassword;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("isEdit") Boolean isEdit,  @ExecutionArgParam("obj") Muser obj) {
        Selectors.wireComponents(view, this, false);
        try {
            title = "User Add";

            if(isEdit != null){
                this.isEdit = isEdit;
                this.muser = obj;
                title = this.muser.getName() + " Edit";
            }

            doReset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotifyChange("*")
    public void doReset() {

        if(!isEdit){
            muser = new Muser();
        }else{
            divPassword.detach();
        }

    }

    @Command
    public void doSave() {
        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();

                if(!isEdit) {
                    muser.setPassword(BCrypt.withDefaults().hashToString(12, muser.getPassword().toCharArray()));
                }

                new MuserDao().saveOrUpdate(session, muser);
                MessageBox.success(muser.getName() + " Berhasil di Simpan!");
                session.getTransaction().commit();
                Events.postEvent(new Event("onClose", winMuser, null));
            } catch (Exception e) {
                session.getTransaction().rollback();
                e.printStackTrace();
                MessageBox.error(muser.getName() + " Gagal di Simpan!");
            }
        }
    }

    public Validator getValidator() {
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                new MuserValidator(isEdit).validate(ctx);
            }
        };
    }


}
