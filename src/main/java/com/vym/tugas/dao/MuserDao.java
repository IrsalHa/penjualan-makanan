/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 18 - 3 - 2024
 */

package com.vym.tugas.dao;

import com.vym.tugas.domain.Muser;

public class MuserDao implements Dao<Muser>{
    @Override
    public Class<Muser> getEntityClass() {
        return Muser.class;
    }
}
