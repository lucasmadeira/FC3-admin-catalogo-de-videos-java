package br.com.lucamadeira.admin.catalogo.application.category.update;

import br.com.lucamadeira.admin.catalogo.domain.category.Category;
import br.com.lucamadeira.admin.catalogo.domain.category.CategoryGateway;
import br.com.lucamadeira.admin.catalogo.domain.category.CategoryID;
import br.com.lucamadeira.admin.catalogo.domain.exceptions.DomainException;
import br.com.lucamadeira.admin.catalogo.domain.validation.Error;
import br.com.lucamadeira.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }


    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
       final var anID = CategoryID.from(aCommand.id());
       final var aCategory =  this.categoryGateway.findById(anID)
                .orElseThrow(() -> DomainException.with(new Error(notFound(anID))));

       aCategory.update(aCommand.name(),aCommand.description(),aCommand.isActive());
       final var notification = Notification.create();
       aCategory.validate(notification);

       return notification.hasError() ? Left(notification): update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return API.Try(() -> categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private static String notFound(final CategoryID anID) {
        return "Category with ID %s was not found".formatted(anID.getValue());
    }
}
