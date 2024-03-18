/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 14 - 6 - 2023
 */

package com.vym.tugas.viewmodel;


import com.vym.tugas.dao.MmenuDao;
import com.vym.tugas.domain.Mmenu;
import com.vym.tugas.domain.Muser;
import org.zkoss.bind.annotation.*;
import org.zkoss.zhtml.Li;
import org.zkoss.zhtml.Ul;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInitializationVm {

    private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
    private Muser oUser;
    private boolean isOpen = false;
    @Wire
    private Div divContent;
    @Wire
    private Div divAccord;
    @Wire
    private Div navMobile;
    @Wire
    private org.zkoss.zhtml.Ul ulMenu;

    @Init
    public void init() {
        oUser = (Muser) zkSession.getAttribute("oUser");
        if (oUser == null) {
            Executions.sendRedirect("/");
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        try {
            oUser = (Muser) zkSession.getAttribute("oUser");
            doRenderMenu();
            Executions.createComponents("/view/dashboard/dashboard.zul", divContent, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doRenderMenu() {
        try {
            org.zkoss.zhtml.Ul ulParentMenu = null;

            List<Mmenu> Menus = new MmenuDao().getAll();
            HashMap<String, List<Mmenu>> mapMenu = new HashMap<>();
            for (final Mmenu menu : Menus) {
                if(mapMenu.containsKey(menu.getMenugroup())){
                    List<Mmenu> mmenus = mapMenu.get(menu.getMenugroup());
                    mmenus.add(menu);
                    mapMenu.replace(menu.getMenugroup(), mmenus);
                    continue;
                }

                List<Mmenu> mmenus = new ArrayList<>();
                mmenus.add(menu);
                mapMenu.put(menu.getMenugroup(), mmenus);
            }

            for(Map.Entry<String, List<Mmenu>> entry: mapMenu.entrySet()){
                if(entry.getValue().size() == 1){
                    ulMenu.appendChild(doCreateMenuLink(entry.getValue().get(0)));
                    continue;
                }

                Div divDropdown = new Div();
                divDropdown.setClass("dropdown");
                ulParentMenu = new Ul();
                ulParentMenu.setClientAttribute("class", "dropdown-menu");
                ulParentMenu.setClientAttribute("data-bs-popper", "static");

                Li li = new Li();
                li.appendChild(new Html(entry.getKey()));
                li.setClientAttribute("class", "nav-link dropdown-toggle");
                li.setClientAttribute("data-bs-toggle", "dropdown");
                li.setClientAttribute("href", "#");

                divDropdown.appendChild(li);

                ulMenu.appendChild(divDropdown);

                for(Mmenu mmenu : entry.getValue()){
                    doCreateMenuDropdown(mmenu, ulParentMenu);
                }

                divDropdown.appendChild(ulParentMenu);
                ulMenu.appendChild(divDropdown);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doCreateMenuDropdown(Mmenu menu, org.zkoss.zhtml.Ul ulParentMenu) {


        Li liChildMenu = new Li();
        A a = new A(menu.getMenusubgroup());
        a.setClass("dropdown-item");
        a.addEventListener(Events.ON_CLICK, event -> {
            divContent.getChildren().clear();
            divContent.setVisible(false);
            Executions.createComponents(menu.getMenupath(), divContent, null);
            divContent.setVisible(true);
        });
        liChildMenu.appendChild(a);

        ulParentMenu.appendChild(liChildMenu);
    }

    public Li doCreateMenuLink(Mmenu menu) {
        Li li = new Li();
        li.setClientAttribute("class", "nav-item");
        A a = new A(menu.getMenuname());
        a.setClass("nav-link");
        a.addEventListener(Events.ON_CLICK, event -> {
            divContent.getChildren().clear();
            divContent.setVisible(false);
            Executions.createComponents(menu.getMenupath(), divContent, null);
            divContent.setVisible(true);
        });
        li.appendChild(a);

        return li;
    }

    @Command
    @NotifyChange("isOpen")
    public void doMenuMobile() {
        isOpen = !isOpen;
        navMobile.setClass("mobile-offcanvas nav navbar navbar-expand-xl hover-nav horizontal-nav mx-md-auto" + (isOpen ? " show " : ""));
    }

    @Command
    public void doLogout() {
        if (zkSession.getAttribute("oUser") != null) {
            zkSession.removeAttribute("oUser");
        }

        Executions.sendRedirect("/logout.zul");
    }

    @Command
    public void openProfile() {
        divContent.getChildren().clear();
        divContent.setVisible(false);
        Executions.createComponents("/view/profile/profile.zul", divContent, null);
        divContent.setVisible(true);
    }


}
