/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 24 - 4 - 2024
 */

package com.vym.tugas.dao;

import com.vym.tugas.domain.Tinvoice;

public class TinvoiceDao implements Dao<Tinvoice> {
    @Override
    public Class<Tinvoice> getEntityClass() {
        return Tinvoice.class;
    }
}
