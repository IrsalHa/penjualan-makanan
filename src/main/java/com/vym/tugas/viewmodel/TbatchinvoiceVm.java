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
import net.sf.jasperreports.engine.*;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.jdbc.Work;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

                Menuitem menuDownload = new Menuitem();
                menuDownload.setLabel("Download");
                menuDownload.setIconSclass("z-icon-download");
                menuDownload.addEventListener(Events.ON_CLICK, event -> {
                    downloadPdf(data);
                });
                menuitemList.add(menuDownload);

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
        UserInitializationVm.divContent2.getChildren().clear();
        UserInitializationVm.divContent2.setVisible(false);
        Executions.createComponents("tbatchinvoice/details.zul", UserInitializationVm.divContent2, map);
        UserInitializationVm.divContent2.setVisible(true);
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

    public void downloadPdf(Tbatchinvoice tbatchinvoice) throws JRException {
        try(Session session = StoreHibernateUtil.getSessionFactory().openSession()){
            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    try {

                        String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
                        JasperReport jasperReport = JasperCompileManager.compileReport(path + "/jasper/Invoice.jrxml");
                        JasperCompileManager.compileReportToFile(path + "/jasper/Invoice.jrxml", path + "/jasper/Invoice.jasper");
                        Map<String, Object> params = new HashMap<>();
                        params.put("INVOICENO", tbatchinvoice.getInvoiceno());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                .withZone(ZoneId.systemDefault());
                        params.put("INVOICEDATE",formatter.format(tbatchinvoice.getCreatedAt()));
                        params.put("PK", tbatchinvoice.getId());

                        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        JasperExportManager.exportReportToPdfStream(jasperPrint, output);
                        String filename = UUID.randomUUID().toString() + ".pdf";
                        try (OutputStream outputStream = new FileOutputStream(path + "/invoice/" + filename)) {
                            output.writeTo(outputStream);
                        }
                        Filedownload.save(new File(path + "/invoice/" + filename), "application/pdf");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }


}
