/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 24 - 4 - 2024
 */

package com.vym.tugas.dao;

import com.vym.tugas.domain.Tbatchinvoice;

public class TbatchinvoiceDao implements Dao<Tbatchinvoice>{
    @Override
    public Class<Tbatchinvoice> getEntityClass() {
        return Tbatchinvoice.class;
    }

}
