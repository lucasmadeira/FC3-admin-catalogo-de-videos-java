package br.com.lucamadeira.admin.catalogo.application.category.retrieve.list;

import br.com.lucamadeira.admin.catalogo.application.UseCase;
import br.com.lucamadeira.admin.catalogo.domain.category.CategorySearchQuery;
import br.com.lucamadeira.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
