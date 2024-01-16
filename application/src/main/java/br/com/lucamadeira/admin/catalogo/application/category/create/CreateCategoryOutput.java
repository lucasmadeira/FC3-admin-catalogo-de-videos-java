package br.com.lucamadeira.admin.catalogo.application.category.create;

import br.com.lucamadeira.admin.catalogo.domain.category.Category;
import br.com.lucamadeira.admin.catalogo.domain.category.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {
    public static CreateCategoryOutput from(final Category aCategory){
        return new CreateCategoryOutput(aCategory.getId());
    }
}
