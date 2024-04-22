/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 22 - 4 - 2024
 */

package com.vym.tugas.viewmodel;

import com.vym.tugas.dao.MproductDao;
import com.vym.tugas.domain.Mproduct;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class KasirVm {
    @Wire
    private Grid grid;
    @Wire
    private Decimalbox dboxTotalPrice;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        try {
            renderGrid();
            doReset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renderGrid(){
        grid.setRowRenderer((RowRenderer<Mproduct>) (row, data, index) -> {
            row.getChildren().add(new Label(data.getName()));

            Decimalbox price = new Decimalbox();
            price.setFormat("Rp #,##0");
            price.setLocale("id-ID");
            price.setReadonly(true);
            price.setValue(data.getPrice());
            row.getChildren().add(price);

            row.getChildren().add(new Label(Long.toString(data.getStock())));
            row.getChildren().add(new Label(data.getMcategory().getName()));

            Button btnAdd = new Button();
            btnAdd.setLabel("Add");
            row.getChildren().add(btnAdd);
        });
    }

    @NotifyChange("*")
    public void doReset(){
        grid.setModel(new ListModelList<>(new MproductDao().getAll()));
    }
}
