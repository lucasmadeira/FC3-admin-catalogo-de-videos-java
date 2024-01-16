package br.com.lucamadeira.admin.catalogo.application.category.delete;

import br.com.lucamadeira.admin.catalogo.application.category.update.UpdateCategoryCommand;
import br.com.lucamadeira.admin.catalogo.domain.category.Category;
import br.com.lucamadeira.admin.catalogo.domain.category.CategoryGateway;
import br.com.lucamadeira.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {
    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK(){

        //given

        final var aCategory = Category.newCategory("Film", "A categoria mais assistida", true);
              final var expectedId = aCategory.getId();

        //when
        doNothing().when(categoryGateway).deleteById(eq(expectedId));
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        verify(categoryGateway, times(1)).deleteById(expectedId);

    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK(){
        //given
        final var expectedId = CategoryID.from("123");

        //when
        doNothing().when(categoryGateway).deleteById(eq(expectedId));
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        verify(categoryGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAValidId_whenGatewayThrowsError_shouldReturnException(){
        //given

        final var aCategory = Category.newCategory("Film", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        //when
        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).deleteById(eq(expectedId));
        Assertions.assertThrows(IllegalStateException.class,() -> useCase.execute(expectedId.getValue()));

        //then
        verify(categoryGateway, times(1)).deleteById(expectedId);
    }
}
