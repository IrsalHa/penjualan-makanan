/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.validator;

import com.vym.tugas.domain.Muser;
import com.vym.utils.helper.MessageBox;
import com.vym.utils.helper.ValidatorUtil;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

public class MuserValidator extends AbstractValidator {

    boolean isEdit;

    public MuserValidator(boolean isInsert) {
        this.isEdit = isInsert;
    }

    @Override
    public void validate(ValidationContext ctx) {
        try {
            ValidatorUtil validatorUtil = new ValidatorUtil(Muser.class);

            String name = (String) ctx.getProperties("name")[0].getValue();
            String userid = (String) ctx.getProperties("userid")[0].getValue();
            String password = null;

            if (!isEdit) {
                password = (String) ctx.getProperties("password")[0].getValue();
            }

            if (userid == null || userid.isEmpty() || userid.trim().length() > validatorUtil.getMaxlength("userid")) {
                this.addInvalidMessage(ctx, "userid", Labels.getLabel("validation.common.notempty") + " dan " + Labels.getLabel("validation.common.maxlength", new Object[]{validatorUtil.getMaxlength("userid")}));
            }

            if (name == null || name.isEmpty() || name.trim().length() > validatorUtil.getMaxlength("name")) {
                this.addInvalidMessage(ctx, "name", Labels.getLabel("validation.common.notempty") + " dan " + Labels.getLabel("validation.common.maxlength", new Object[]{validatorUtil.getMaxlength("name")}));
            }

            if (!isEdit) {
                if (password == null || password.isEmpty() || password.trim().length() > validatorUtil.getMaxlength("password")) {
                    this.addInvalidMessage(ctx, "password", Labels.getLabel("validation.common.notempty") + " dan " + Labels.getLabel("validation.common.maxlength", new Object[]{validatorUtil.getMaxlength("password")}));
                }
            }

        } catch (Exception e) {
            MessageBox.error("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
