/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 23 - 3 - 2024
 */

package com.vym.tugas.dao;

import com.vym.tugas.domain.Mcategory;

public class McategoryDao implements Dao<Mcategory> {
    @Override
    public Class<Mcategory> getEntityClass() {
        return Mcategory.class;
    }
}
