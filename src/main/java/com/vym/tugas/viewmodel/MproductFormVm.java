/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.viewmodel;

import com.vym.tugas.dao.McategoryDao;
import com.vym.tugas.dao.MproductDao;
import com.vym.tugas.domain.Mcategory;
import com.vym.tugas.domain.Mproduct;
import com.vym.tugas.validator.McategoryValidator;
import com.vym.tugas.validator.MproductValidator;
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
import org.zkoss.zul.*;

@Getter
@Setter
public class MproductFormVm {
    private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
    private Boolean isEdit = false;
    private String title;
    private Mproduct mproduct;
    private Mcategory mcategory;

    @Wire
    private Grid grid;
    @Wire
    private Window winMproduct;
    @Wire
    private Combobox cbMcategory;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("isEdit") Boolean isEdit, @ExecutionArgParam("obj") Mproduct obj) {
        Selectors.wireComponents(view, this, false);
        try {
            title = "+ Product";

            if (isEdit != null) {
                this.isEdit = isEdit;
                this.mproduct = obj;
                this.mcategory = obj.getMcategory();
                title = this.mproduct.getName() + " Edit";
            }

            doReset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotifyChange("*")
    public void doReset() {

        if (!isEdit) {
            mproduct = new Mproduct();
        }else{
            Comboitem combobox = new Comboitem();
            combobox.setLabel(this.mproduct.getMcategory().getName());
            combobox.setValue(this.mproduct.getMcategory());
            cbMcategory.appendChild(combobox);
            cbMcategory.setSelectedItem(combobox);
        }

    }

    public ListModelList<Mcategory> getListmcategory() {
        ListModelList<Mcategory> lm = null;
        try {
            lm = new ListModelList<>(new McategoryDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lm;
    }

    @Command
    public void doSave() {
        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                mproduct.setMcategory(mcategory);
                new MproductDao().saveOrUpdate(session, mproduct);
                MessageBox.success(mproduct.getName() + " Berhasil di Simpan!");
                session.getTransaction().commit();
                Events.postEvent(new Event("onClose", winMproduct, null));
            } catch (Exception e) {
                session.getTransaction().rollback();
                e.printStackTrace();
                MessageBox.error(mproduct.getName() + " Gagal di Simpan!");
            }
        }
    }

    public Validator getValidator() {
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                new MproductValidator().validate(ctx, cbMcategory);
            }
        };
    }


}
