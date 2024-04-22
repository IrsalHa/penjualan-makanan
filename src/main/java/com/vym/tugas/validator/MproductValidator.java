/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 19 - 3 - 2024
 */

package com.vym.tugas.validator;

import com.vym.tugas.domain.Mcategory;
import com.vym.tugas.domain.Mproduct;
import com.vym.utils.helper.MessageBox;
import com.vym.utils.helper.ValidatorUtil;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Combobox;

import java.math.BigDecimal;

public class MproductValidator extends AbstractValidator {

    Combobox cbMcategory;

    public void validate(ValidationContext ctx, Combobox cbMcategory) {
        this.cbMcategory = cbMcategory;
        validate(ctx);
    }

    @Override
    public void validate(ValidationContext ctx) {
        try {
            ValidatorUtil validatorUtil = new ValidatorUtil(Mproduct.class);

            String name = (String) ctx.getProperties("name")[0].getValue();
            Integer stock = (Integer) ctx.getProperties("stock")[0].getValue();
            BigDecimal price = (BigDecimal) ctx.getProperties("price")[0].getValue();

            if (name == null || name.isEmpty() || name.trim().length() > validatorUtil.getMaxlength("name")) {
                this.addInvalidMessage(ctx, "name", Labels.getLabel("validation.common.notempty") + " dan " + Labels.getLabel("validation.common.maxlength", new Object[]{validatorUtil.getMaxlength("name")}));
            }

            if (stock == null) {
                this.addInvalidMessage(ctx, "stock", Labels.getLabel("validation.common.notempty"));
            }

            if (price == null) {
                this.addInvalidMessage(ctx, "price", Labels.getLabel("validation.common.notempty"));
            }

            if(cbMcategory.getSelectedItem() == null){
                this.addInvalidMessage(ctx, "listmcategory", Labels.getLabel("validation.common.notempty"));
            }

        } catch (Exception e) {
            MessageBox.error("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
