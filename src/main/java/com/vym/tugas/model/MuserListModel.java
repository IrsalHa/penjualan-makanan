/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 20 - 3 - 2024
 */

package com.vym.tugas.model;

import com.vym.tugas.dao.MuserDao;
import com.vym.tugas.domain.Muser;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MuserListModel extends AbstractPagingListModel<Muser> implements Sortable<Muser> {
    private int _size = -1;
    private List<Muser> oList;

    public MuserListModel(int startPageNumber, int pageSize, String filter, String orderby) {
        super(startPageNumber, pageSize, filter, orderby);
    }
    @Override
    public int getTotalSize(String filter) {
        MuserDao oDao = new MuserDao();
        try {
            _size = oDao.pageCount(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _size;
    }

    @Override
    protected List<Muser> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {
        MuserDao oDao = new MuserDao();
        try {
            oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oList;
    }

    @Override
    public void sort(Comparator<Muser> comparator, boolean b) {
        Collections.sort(oList, comparator);
        fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
    }

    @Override
    public String getSortDirection(Comparator<Muser> comparator) {
        return null;
    }
}
