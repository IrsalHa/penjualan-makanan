/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 23 - 3 - 2024
 */

package com.vym.tugas.dao;

import com.vym.tugas.domain.Mproduct;

public class MproductDao implements Dao<Mproduct> {
    @Override
    public Class<Mproduct> getEntityClass() {
        return Mproduct.class;
    }
}
