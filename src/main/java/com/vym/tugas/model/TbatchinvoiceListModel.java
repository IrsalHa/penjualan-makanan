/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 20 - 3 - 2024
 */

package com.vym.tugas.model;

import com.vym.tugas.dao.TbatchinvoiceDao;
import com.vym.tugas.domain.Tbatchinvoice;
import com.vym.tugas.domain.Tbatchinvoice;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TbatchinvoiceListModel extends AbstractPagingListModel<Tbatchinvoice> implements Sortable<Tbatchinvoice> {
    private int _size = -1;
    private List<Tbatchinvoice> oList;

    public TbatchinvoiceListModel(int startPageNumber, int pageSize, String filter, String orderby) {
        super(startPageNumber, pageSize, filter, orderby);
    }
    @Override
    public int getTotalSize(String filter) {
        TbatchinvoiceDao oDao = new TbatchinvoiceDao();
        try {
            _size = oDao.pageCount(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _size;
    }

    @Override
    protected List<Tbatchinvoice> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {
        TbatchinvoiceDao oDao = new TbatchinvoiceDao();
        try {
            oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oList;
    }

    @Override
    public void sort(Comparator<Tbatchinvoice> comparator, boolean b) {
        Collections.sort(oList, comparator);
        fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
    }

    @Override
    public String getSortDirection(Comparator<Tbatchinvoice> comparator) {
        return null;
    }
}
