package br.com.lucamadeira.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error anError); // adiciona um erro
    ValidationHandler append(ValidationHandler anHandler); // adiciona uma lista de erros

    ValidationHandler validate(Validation aValidation);

    List<Error> getErrors(); // lista de erros contendo as mensagens de erros

    default boolean hasError(){
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError(){
        if(getErrors()!=null && !getErrors().isEmpty()){
            return getErrors().get(0);
        }

        return null;
    }

    public interface Validation{
        void validate();
    }
}