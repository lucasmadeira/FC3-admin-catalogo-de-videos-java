package br.com.lucamadeira.admin.catalogo.application.category.retrieve.get;
import br.com.lucamadeira.admin.catalogo.domain.category.Category;
import br.com.lucamadeira.admin.catalogo.domain.category.CategoryGateway;
import br.com.lucamadeira.admin.catalogo.domain.category.CategoryID;
import br.com.lucamadeira.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){
        //given

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = true;
        final var expectedId = aCategory.getId();


        //when
        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.with(aCategory)));

        //then
        final var actualOutput = useCase.execute(expectedId.getValue());

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Assertions.assertEquals(expectedId,actualOutput.id());
        Assertions.assertEquals(expectedName,actualOutput.name());
        Assertions.assertEquals(expectedDescription,actualOutput.description());
        Assertions.assertEquals(expectedIsActive,actualOutput.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualOutput.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualOutput.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(),actualOutput.deletedAt());


    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound(){
        //given
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        //when
        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        //then
       final var actualException = Assertions.assertThrows(
               DomainException.class, () -> useCase.execute(expectedId.getValue()));

       Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowException_shouldReturnException(){
        //given
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gateway error";

        //when
        when(categoryGateway.findById(eq(expectedId))).thenThrow(new IllegalStateException(expectedErrorMessage));

        //then
        final var actualException = Assertions.assertThrows(
                IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
