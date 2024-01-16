package br.com.lucamadeira.admin.catalogo.application;

import br.com.lucamadeira.admin.catalogo.domain.category.Category;

public abstract class UseCase<IN,OUT> {
   public abstract OUT execute(IN anIn);
}