/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 18 - 3 - 2024
 */

package com.vym.tugas.dao;

import com.vym.tugas.domain.Mmenu;

public class MmenuDao implements Dao<Mmenu> {
    @Override
    public Class<Mmenu> getEntityClass() {
        return Mmenu.class;
    }
}
