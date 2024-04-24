/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.viewmodel;

import com.vym.tugas.dao.MproductDao;
import com.vym.tugas.domain.Mproduct;
import com.vym.tugas.domain.Tbatchinvoice;
import com.vym.tugas.model.MproductListModel;
import com.vym.tugas.model.TbatchinvoiceListModel;
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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TbatchinvoiceVm {
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
        grid.setRowRenderer(new RowRenderer<Tbatchinvoice>() {
            @Override
            public void render(Row row, Tbatchinvoice data, int index) {
                row.getChildren().add(new Label(String.valueOf(index + 1)));

                List<Menuitem> menuitemList = new ArrayList<>();

                Menuitem menuitemEdit = new Menuitem();
                menuitemEdit.setLabel("View");
                menuitemEdit.setIconSclass("z-icon-eye");
                menuitemEdit.addEventListener(Events.ON_CLICK, event -> {
                    doOpenView(data);
                });
                menuitemList.add(menuitemEdit);

                row.getChildren().add(renderBtnAction(menuitemList));
                row.getChildren().add(new Label(data.getInvoiceno()));
                row.getChildren().add(new Label(data.getTotalPrice().toPlainString()));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")
                        .withZone(ZoneId.systemDefault());
                row.getChildren().add(new Label(formatter.format(data.getCreatedAt())));
            }
        });
    }

    @NotifyChange("*")
    public void doReset() {
        grid.setModel(new TbatchinvoiceListModel(0, 20, "0=0", "tbatchinvoice_pk"));
    }

    @Command
    public void doOpenView(Tbatchinvoice tbatchinvoice) {
        Map<String, Object> map = new HashMap<>();
        map.put("obj", tbatchinvoice);
        Window win = (Window) Executions.createComponents("/view/mproduct/form.zul", null, map);
        win.doModal();
        win.setWidth("60%");
        win.setClosable(true);

        win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                doReset();
                BindUtils.postNotifyChange(null, null, TbatchinvoiceVm.this, "*");

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


}
