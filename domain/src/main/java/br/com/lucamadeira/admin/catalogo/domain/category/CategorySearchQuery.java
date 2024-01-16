package br.com.lucamadeira.admin.catalogo.domain.category;

public record CategorySearchQuery(
        int page, //pagina
        int perPage,// registros por pagina
        String terms, // termos de busca, filtro
        String sort,// atributos para ordenacao
        String direction // se é Ascendente ou decrecente
) {
}
