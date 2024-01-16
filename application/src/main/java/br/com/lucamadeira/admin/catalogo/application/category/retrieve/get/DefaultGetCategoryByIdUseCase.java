package br.com.lucamadeira.admin.catalogo.application.category.retrieve.get;

import br.com.lucamadeira.admin.catalogo.domain.category.CategoryGateway;
import br.com.lucamadeira.admin.catalogo.domain.category.CategoryID;
import br.com.lucamadeira.admin.catalogo.domain.exceptions.DomainException;
import br.com.lucamadeira.admin.catalogo.domain.validation.Error;

import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryUseCase{

    private CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }


    @Override
    public CategoryOutput execute(final String anIn) {
       final var anCategoryID = CategoryID.from(anIn);
       return this.categoryGateway.findById(anCategoryID)
               .map(CategoryOutput::from)
               .orElseThrow(notFound(anCategoryID));
    }

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }
}
