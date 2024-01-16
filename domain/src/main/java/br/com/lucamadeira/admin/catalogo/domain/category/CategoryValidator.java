package br.com.lucamadeira.admin.catalogo.domain.category;

import br.com.lucamadeira.admin.catalogo.domain.validation.Error;
import br.com.lucamadeira.admin.catalogo.domain.validation.ValidationHandler;
import br.com.lucamadeira.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final Category category;
    public CategoryValidator(final Category category, final ValidationHandler aHandler){
        super(aHandler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = category.getName();

        if(name == null){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int lenght = name.trim(). length();
        if(lenght> NAME_MAX_LENGTH || lenght < NAME_MIN_LENGTH){
            this.validationHandler().append(new Error("'name' must be beteween 3 an 255 characters"));
        }
    }
}
