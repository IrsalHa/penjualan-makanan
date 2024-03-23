/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.validator;

import com.vym.tugas.domain.Mcategory;
import com.vym.utils.helper.MessageBox;
import com.vym.utils.helper.ValidatorUtil;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

public class McategoryValidator extends AbstractValidator {

    @Override
    public void validate(ValidationContext ctx) {
        try {
            ValidatorUtil validatorUtil = new ValidatorUtil(Mcategory.class);

            String name = (String) ctx.getProperties("name")[0].getValue();

            if (name == null || name.isEmpty() || name.trim().length() > validatorUtil.getMaxlength("name")) {
                this.addInvalidMessage(ctx, "name", Labels.getLabel("validation.common.notempty") + " dan " + Labels.getLabel("validation.common.maxlength", new Object[]{validatorUtil.getMaxlength("name")}));
            }

        } catch (Exception e) {
            MessageBox.error("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
