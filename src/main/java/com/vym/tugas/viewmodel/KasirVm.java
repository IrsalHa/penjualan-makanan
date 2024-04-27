/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 22 - 4 - 2024
 */

package com.vym.tugas.viewmodel;

import com.vym.tugas.dao.MproductDao;
import com.vym.tugas.dao.TbatchinvoiceDao;
import com.vym.tugas.dao.TcounterEngineDao;
import com.vym.tugas.dao.TinvoiceDao;
import com.vym.tugas.domain.Mproduct;
import com.vym.tugas.domain.Muser;
import com.vym.tugas.domain.Tbatchinvoice;
import com.vym.tugas.domain.Tinvoice;
import com.vym.utils.db.StoreHibernateUtil;
import com.vym.utils.helper.MessageBox;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Session;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Big;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
public class KasirVm {
    @Wire
    private Grid grid;
    @Wire
    private Grid cart;
    @Wire
    private Decimalbox dboxTotalPrice;
    @Wire
    private Decimalbox dboxCustomerBalance;
    @Wire
    private Textbox tboxFilter;

    private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
    private Muser oUser = (Muser) zkSession.getAttribute("oUser");
    private List<Tinvoice> mcart;
    private BigDecimal totalPrice = new BigDecimal(0);
    private Tbatchinvoice tbatchinvoice;
    private List<Tinvoice> tinvoiceList = new ArrayList<>();


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

    public void renderGrid() {
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
            btnAdd.setLabel("+");
            btnAdd.setAutodisable("true");
            btnAdd.setClass("btn btn-primary btn-sm mr-2");
            btnAdd.addEventListener(Events.ON_CLICK, event -> {
                boolean present = false;

                for (Tinvoice tinvoice : mcart) {
                    if (data.getId() == tinvoice.getMproductFk().getId()) {
                        tinvoice.setQty(tinvoice.getQty() + 1);
                        tinvoice.setPrice(tinvoice.getPrice().add(data.getPrice()));
                        present = true;
                        break;
                    }
                }

                if (!present) {
                    Tinvoice tinvoice = new Tinvoice();
                    tinvoice.setMproductFk(data);
                    tinvoice.setPrice(data.getPrice());
                    tinvoice.setQty(1);
                    mcart.add(tinvoice);
                }

                totalPrice = totalPrice.add(data.getPrice());
                cart.setModel(new ListModelList<>(mcart));
                BindUtils.postNotifyChange(this, "mcart");
                BindUtils.postNotifyChange(this, "totalPrice");
            });

            Button btnMin = new Button();
            btnMin.setLabel("-");
            btnMin.setAutodisable("true");
            btnMin.setClass("btn btn-danger btn-sm");
            btnMin.addEventListener(Events.ON_CLICK, event -> {
                boolean present = false;

                for (Tinvoice tinvoice : mcart) {
                    if (data.getId() == tinvoice.getMproductFk().getId()) {
                        tinvoice.setQty(tinvoice.getQty() - 1);
                        tinvoice.setPrice(tinvoice.getPrice().subtract(data.getPrice()));

                        if(tinvoice.getQty() < 1){
                           mcart.remove(tinvoice);
                        }
                        present = true;
                        break;
                    }
                }

                if(!present){
                    return;
                }

                totalPrice = totalPrice.subtract(data.getPrice());
                cart.setModel(new ListModelList<>(mcart));
                BindUtils.postNotifyChange(this, "mcart");
                BindUtils.postNotifyChange(this, "totalPrice");

            });

            Div divAction = new Div();
            divAction.appendChild(btnAdd);
            divAction.appendChild(btnMin);

            row.getChildren().add(divAction);
        });

        cart.setRowRenderer((RowRenderer<Tinvoice>) (row, data, index) -> {
            Tinvoice _tinvoice = data;
            row.setAttribute("obj", _tinvoice);

            row.getChildren().add(new Label(data.getMproductFk().getName()));

            Decimalbox price = new Decimalbox();
            price.setFormat("Rp #,##0");
            price.setLocale("id-ID");
            price.setReadonly(true);
            price.setValue(_tinvoice.getPrice());

            row.getChildren().add(price);

            Intbox qty = new Intbox();
            qty.setReadonly(true);
            qty.setValue(_tinvoice.getQty());
            qty.addEventListener(Events.ON_CHANGE, event -> {
                boolean done = false;

                if (qty.getValue() == null || qty.getValue() < 0) {
                    Iterator<Tinvoice> iterator = mcart.iterator();
                    while (iterator.hasNext()) {
                        Tinvoice tinvoice = iterator.next();
                        if (data.getMproductFk().getId() == tinvoice.getMproductFk().getId()) {
                            iterator.remove();
                            done = true;
                            break;
                        }
                    }
                }

                if (done) {
                    cart.setModel(new ListModelList<>(mcart));
                    totalPrice = totalPrice.subtract(_tinvoice.getPrice());
                    BindUtils.postNotifyChange(this, "totalPrice");
                    BindUtils.postNotifyChange(this, "mcart");
                    return;
                }

                totalPrice = totalPrice.subtract(_tinvoice.getPrice());
                _tinvoice.setPrice(new BigDecimal(qty.getValue()).multiply(_tinvoice.getMproductFk().getPrice()));
                totalPrice = totalPrice.add(_tinvoice.getPrice());
                price.setValue(_tinvoice.getPrice());

                BindUtils.postNotifyChange(this, "mcart");
                BindUtils.postNotifyChange(this, "totalPrice");
            });

            row.getChildren().add(qty);

        });
    }

    public void doSearch() {
        grid.setModel(new ListModelList<>(new MproductDao().wheres("name like '%" + tboxFilter.getValue().trim() + "%' ")));
    }

    public void doProceed(){
        BigDecimal customerBalance = dboxCustomerBalance.getValue();

        if(customerBalance == null || customerBalance.compareTo(BigDecimal.ZERO) == 0){
            MessageBox.error("Please Input Customer Balance!");
            return;
        }

        if(customerBalance.compareTo(totalPrice) < 0){
            MessageBox.error("Customer balance is insufficient!");
            return;
        }

        try(Session session = StoreHibernateUtil.getSessionFactory().openSession()){
            try {
                session.beginTransaction();

                BigDecimal restBalance = customerBalance.subtract(totalPrice);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy/")
                        .withZone(ZoneId.systemDefault());
                tbatchinvoice.setInvoiceno(new TcounterEngineDao().getValue(formatter.format(Instant.now())));
                tbatchinvoice.setTotalPrice(totalPrice);
                tbatchinvoice.setCreatedAt(Instant.now());

                new TbatchinvoiceDao().saveOrUpdate(session, tbatchinvoice);

                for(Tinvoice tinvoice : mcart ){
                    tinvoice.setTbatchinvoiceFk(tbatchinvoice);
                    tinvoice.setMuserFk(oUser);

                    new TinvoiceDao().saveOrUpdate(session, tinvoice);
                }


                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

                formatRp.setCurrencySymbol("Rp. ");
                formatRp.setMonetaryDecimalSeparator(',');
                formatRp.setGroupingSeparator('.');

                kursIndonesia.setDecimalFormatSymbols(formatRp);

                session.getTransaction().commit();
                String rest = "";

                if(restBalance.compareTo(BigDecimal.ZERO) > 0){
                    rest += "Change: " + kursIndonesia.format(restBalance);
                }

                MessageBox.success(rest);
                UserInitializationVm.divContent2.getChildren().clear();
            }catch (Exception e){
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }
    }

    @NotifyChange("*")
    public void doReset() {
        tbatchinvoice = new Tbatchinvoice();
        grid.setModel(new ListModelList<>(new MproductDao().getAll()));
        mcart = new ArrayList<Tinvoice>();
        cart.setModel(new ListModelList<>(mcart));
    }
}
