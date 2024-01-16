package br.com.lucamadeira.admin.catalogo.application.category.retrieve.list;

import br.com.lucamadeira.admin.catalogo.domain.category.CategoryGateway;
import br.com.lucamadeira.admin.catalogo.domain.category.CategorySearchQuery;
import br.com.lucamadeira.admin.catalogo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase{

    private CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final CategorySearchQuery aQuery) {
        return categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);

    }
}
