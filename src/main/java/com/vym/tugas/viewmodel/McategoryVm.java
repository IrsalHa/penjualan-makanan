/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.viewmodel;

import com.vym.tugas.dao.McategoryDao;
import com.vym.tugas.domain.Mcategory;
import com.vym.tugas.model.McategoryListModel;
import com.vym.tugas.model.MuserListModel;
import com.vym.utils.db.StoreHibernateUtil;
import com.vym.utils.helper.MessageBox;
import org.hibernate.Session;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class McategoryVm {
    private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
    @Wire
    private Grid grid;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        try {
            renderTable();
            doReset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renderTable() {
        grid.setRowRenderer(new RowRenderer<Mcategory>() {
            @Override
            public void render(Row row, Mcategory data, int index) {
                row.getChildren().add(new Label(String.valueOf(index + 1)));

                List<Menuitem> menuitemList = new ArrayList<>();

                Menuitem menuitemEdit = new Menuitem();
                menuitemEdit.setLabel("Edit");
                menuitemEdit.setIconSclass("z-icon-pencil");
                menuitemEdit.addEventListener(Events.ON_CLICK, event -> {
                    doOpenEdit(data);
                });
                menuitemList.add(menuitemEdit);

                Menuitem menuitemDelete = new Menuitem();
                menuitemDelete.setLabel("Delete");
                menuitemDelete.setIconSclass("z-icon-trash");
                menuitemDelete.addEventListener(Events.ON_CLICK, event -> {
                    doDeleteConfirmation(data);
                });
                menuitemList.add(menuitemDelete);

                row.getChildren().add(renderBtnAction(menuitemList));
                row.getChildren().add(new Label(data.getName()));
            }
        });
    }

    @NotifyChange("*")
    public void doReset() {
        grid.setModel(new McategoryListModel(0, 999999999, "0=0", "mcategory_pk"));
    }

    @Command
    public void doOpenAdd() {
        Window win = (Window) Executions.createComponents("/view/mcategory/form.zul", null, null);
        win.doModal();
        win.setWidth("60%");
        win.setClosable(true);

        win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                doReset();
                BindUtils.postNotifyChange(null, null, McategoryVm.this, "*");

            }

        });
    }

    @Command
    public void doOpenEdit(Mcategory mcategory) {
        Map<String, Object> map = new HashMap<>();
        map.put("isEdit", true);
        map.put("obj", mcategory);
        Window win = (Window) Executions.createComponents("/view/mcategory/form.zul", null, map);
        win.doModal();
        win.setWidth("60%");
        win.setClosable(true);

        win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                doReset();
                BindUtils.postNotifyChange(null, null, McategoryVm.this, "*");

            }

        });
    }

    public Combobutton renderBtnAction(List<Menuitem> menuitemList) {
        Combobutton combobutton = new Combobutton("Action");
        Menupopup menupopup = new Menupopup();

        for (Menuitem menuitem : menuitemList) {
            menupopup.appendChild(menuitem);
        }

        combobutton.appendChild(menupopup);

        return combobutton;

    }

    @Command
    public void doDeleteConfirmation(Mcategory mcategory) {
        Messagebox.show("Apakah anda yakin?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, event -> {
            if (event.getName().equals("onOK")) {
                doDelete(mcategory);
                doReset();
            } else {
                MessageBox.error("Hapus di Batalkan");
            }
        });
    }

    public void doDelete(Mcategory mcategory) {
        try (Session session = StoreHibernateUtil.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                new McategoryDao().delete(session, mcategory);
                MessageBox.success(mcategory.getName() + " Berhasil di Hapus!");
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                e.printStackTrace();
                MessageBox.error(mcategory.getName() + " Gagal di Hapus!");
            }
        }
    }
}
