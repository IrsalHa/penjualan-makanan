/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.utils.helper;

import javax.persistence.Column;

public class ValidatorUtil {
    final Class<?> typeParameterClass;

    public ValidatorUtil(Class<?> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public int getMaxlength(String field) throws NoSuchFieldException {
        return this.typeParameterClass.getDeclaredField(field).getAnnotation(Column.class).length();
    }
}
