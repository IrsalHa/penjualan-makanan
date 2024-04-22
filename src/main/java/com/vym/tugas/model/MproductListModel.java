/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 20 - 3 - 2024
 */

package com.vym.tugas.model;

import com.vym.tugas.dao.McategoryDao;
import com.vym.tugas.dao.MproductDao;
import com.vym.tugas.domain.Mcategory;
import com.vym.tugas.domain.Mproduct;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MproductListModel extends AbstractPagingListModel<Mproduct> implements Sortable<Mproduct> {
    private int _size = -1;
    private List<Mproduct> oList;

    public MproductListModel(int startPageNumber, int pageSize, String filter, String orderby) {
        super(startPageNumber, pageSize, filter, orderby);
    }
    @Override
    public int getTotalSize(String filter) {
        MproductDao oDao = new MproductDao();
        try {
            _size = oDao.pageCount(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _size;
    }

    @Override
    protected List<Mproduct> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {
        MproductDao oDao = new MproductDao();
        try {
            oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oList;
    }

    @Override
    public void sort(Comparator<Mproduct> comparator, boolean b) {
        Collections.sort(oList, comparator);
        fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
    }

    @Override
    public String getSortDirection(Comparator<Mproduct> comparator) {
        return null;
    }
}
