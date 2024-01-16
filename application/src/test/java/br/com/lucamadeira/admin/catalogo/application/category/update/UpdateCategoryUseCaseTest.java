package br.com.lucamadeira.admin.catalogo.application.category.update;

import br.com.lucamadeira.admin.catalogo.application.category.create.CreateCategoryCommand;
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

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    // 1. Teste do caminho feliz
    // 2. Teste passando uma propriedade invalida(name)
    // 3. Teste atualizando uma categoria para  inativa
    // 4. Teste simulando um erro generico do gateway
    // 5. Teste atualizar categoria passando id invalido

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){
        //given

        final var aCategory = Category.newCategory("Film", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = true;
        final var expectedId = aCategory.getId();
        final var aComand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        //when
        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.with(aCategory)));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        //then
        final var actualOutput = useCase.execute(aComand).get();

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(argThat(
                aCategoryUpdate ->{
                    return Objects.equals(aCategory.getId(), aCategoryUpdate.getId())
                            && Objects.equals(expectedName, aCategoryUpdate.getName())
                            && Objects.equals(expectedDescription, aCategoryUpdate.getDescription())
                            && Objects.equals(expectedIsActive, aCategoryUpdate.isActive())
                            && Objects.equals(aCategory.getCreatedAt(), aCategoryUpdate.getCreatedAt())
                            && aCategory.getUpdatedAt().isBefore(aCategoryUpdate.getUpdatedAt())
                            && Objects.isNull(aCategoryUpdate.getDeletedAt());
                }
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException(){
        //given
        final var aCategory = Category.newCategory("Film", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var aComand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,expectedDescription,expectedIsActive);

        //when
        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.with(aCategory)));

        //then
        final var notification  = useCase.execute(aComand).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Mockito.verify(categoryGateway, times(1)).findById(any());
        Mockito.verify(categoryGateway, times(0)).update(any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId(){
        //given

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = false;
        final var expectedId = aCategory.getId();
        final var aComand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        //when
        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.with(aCategory)));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());


        //then
        final var actualOutput = useCase.execute(aComand).get();

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(argThat(
                aCategoryUpdate ->{
                    return Objects.equals(aCategory.getId(), aCategoryUpdate.getId())
                            && Objects.equals(expectedName, aCategoryUpdate.getName())
                            && Objects.equals(expectedDescription, aCategoryUpdate.getDescription())
                            && Objects.equals(expectedIsActive, aCategoryUpdate.isActive())
                            && Objects.equals(aCategory.getCreatedAt(), aCategoryUpdate.getCreatedAt())
                            && aCategory.getUpdatedAt().isBefore(aCategoryUpdate.getUpdatedAt())
                            && Objects.nonNull(aCategoryUpdate.getDeletedAt());
                }
        ));
    }

    @Test
    public void givenAValidaCommand_whenGatewayThrowsRandomException_shouldReturnAException(){

        //given
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";
        final var aComand = UpdateCategoryCommand.with(expectedId.getValue(),expectedName,expectedDescription,expectedIsActive);

        //when
        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.with(aCategory)));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException("Gateway error"));

        //then
        final var notification  = useCase.execute(aComand).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(argThat(
                aCategoryUpdate ->{
                    return Objects.equals(aCategory.getId(), aCategoryUpdate.getId())
                            && Objects.equals(expectedName, aCategoryUpdate.getName())
                            && Objects.equals(expectedDescription, aCategoryUpdate.getDescription())
                            && Objects.equals(expectedIsActive, aCategoryUpdate.isActive())
                            && Objects.equals(aCategory.getCreatedAt(), aCategoryUpdate.getCreatedAt())
                            && aCategory.getUpdatedAt().isBefore(aCategoryUpdate.getUpdatedAt())
                            && Objects.isNull(aCategoryUpdate.getDeletedAt());
                }
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException(){
        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = false;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;
        final var aComand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive);

        //when
        when(categoryGateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());

        //then
        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aComand));

        //then
       Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
       Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
       verify(categoryGateway, times(1)).findById(CategoryID.from(expectedId));
       verify(categoryGateway, times(0)).update(any());
    }

}
