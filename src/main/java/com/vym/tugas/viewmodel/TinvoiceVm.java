/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.viewmodel;

import com.vym.tugas.dao.TinvoiceDao;
import com.vym.tugas.domain.Tbatchinvoice;
import com.vym.tugas.domain.Tinvoice;
import com.vym.utils.db.StoreHibernateUtil;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.*;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
public class TinvoiceVm {
    List<Tinvoice> tinvoiceList = new ArrayList<>();
    Tbatchinvoice tbatchinvoice = new Tbatchinvoice();
    @Wire
    private Div invoiceDiv;
    @Wire
    private org.zkoss.zhtml.H1 judul;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tbatchinvoice obj) {
        Selectors.wireComponents(view, this, false);
        try {
            this.tbatchinvoice = obj;
            tinvoiceList = new TinvoiceDao().wheres("tbatchinvoiceFk = " + obj.getId());
            doReset();
            doRenderTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotifyChange("*")
    public void doReset() {
    }

    public void doRenderTable() throws JRException {

        Session session = StoreHibernateUtil.getSessionFactory().openSession();

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
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        session.close();

    }

}
