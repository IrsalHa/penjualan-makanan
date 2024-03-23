/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 20 - 3 - 2024
 */

package com.vym.tugas.model;

import org.hibernate.Session;
import org.zkoss.zul.AbstractListModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPagingListModel<T> extends AbstractListModel {
    private static final long serialVersionUID = 6613208067174831719L;

    private int startPageNumber;
    private int pageSize;
    private int itemStartNumber;
    private Session session;

    //internal use only
    private List<T> items = new ArrayList<T>();

    public AbstractPagingListModel(int startPageNumber, int pageSize, String filter, String orderby) {
        super();

        this.startPageNumber = startPageNumber;
        this.pageSize = pageSize;
        this.itemStartNumber = startPageNumber * pageSize;

        items = getPageData(itemStartNumber, pageSize, filter, orderby);
    }

    public abstract int getTotalSize(String filter);

    protected abstract List<T> getPageData(int itemStartNumber, int pageSize, String filter, String orderby);


    public Object getElementAt(int index) {
        return items.get(index);
    }


    public int getSize() {
        return items.size();
    }

    public List<T> getItems() {
        return items;
    }

    public int getStartPageNumber() {
        return this.startPageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getItemStartNumber() {
        return itemStartNumber;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
