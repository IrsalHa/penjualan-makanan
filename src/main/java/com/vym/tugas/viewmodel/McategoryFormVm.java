/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.viewmodel;

import com.vym.tugas.dao.McategoryDao;
import com.vym.tugas.domain.Mcategory;
import com.vym.tugas.validator.McategoryValidator;
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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Window;

@Getter
@Setter
public class McategoryFormVm {
    private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
    private Boolean isEdit = false;
    private String title;
    private Mcategory mcategory;

    @Wire
    private Grid grid;
    @Wire
    private Window winMcategory;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("isEdit") Boolean isEdit, @ExecutionArgParam("obj") Mcategory obj) {
        Selectors.wireComponents(view, this, false);
        try {
            title = "+ Category";

            if (isEdit != null) {
                this.isEdit = isEdit;
                this.mcategory = obj;
                title = this.mcategory.getName() + " Edit";
            }

            doReset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotifyChange("*")
    public void doReset() {

        if (!isEdit) {
            mcategory = new Mcategory();
        }

    }

    @Command
    public void doSave() {
        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                new McategoryDao().saveOrUpdate(session, mcategory);
                MessageBox.success(mcategory.getName() + " Berhasil di Simpan!");
                session.getTransaction().commit();
                Events.postEvent(new Event("onClose", winMcategory, null));
            } catch (Exception e) {
                session.getTransaction().rollback();
                e.printStackTrace();
                MessageBox.error(mcategory.getName() + " Gagal di Simpan!");
            }
        }
    }

    public Validator getValidator() {
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                new McategoryValidator().validate(ctx);
            }
        };
    }


}
