package br.com.lucamadeira.admin.catalogo.application.category.create;

import br.com.lucamadeira.admin.catalogo.domain.category.CategoryGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    // 1. Teste do caminho feliz
    // 2. Teste passando uma propriedade invalida(name)
    // 3. Teste criando uma categoria inativa
    // 4. Teste simulando um erro generico do gateway
    @Test
    public void givenAValidaCommand_whenCallsCreateCategory_shouldReturnCategoryId(){

        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = true;
        final var aComand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        //when
        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());



        final var actualOutput = useCase.execute(aComand).get();

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).create(argThat(
                aCategory ->{
                    return Objects.equals(expectedName, aCategory.getName())
                    && Objects.equals(expectedDescription, aCategory.getDescription())
                    && Objects.equals(expectedIsActive, aCategory.isActive())
                    && Objects.nonNull(aCategory.getId())
                    && Objects.nonNull(aCategory.getCreatedAt())
                    && Objects.nonNull(aCategory.getUpdatedAt())
                    && Objects.isNull(aCategory.getDeletedAt());
                }
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException(){
        //given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var aComand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);


        //then
        final var notification  = useCase.execute(aComand).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Mockito.verify(categoryGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidaCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnCategoryId(){

        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = false;
        final var aComand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        //when
        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aComand).get();

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).create(argThat(
                aCategory ->{
                    return Objects.equals(expectedName, aCategory.getName())
                            && Objects.equals(expectedDescription, aCategory.getDescription())
                            && Objects.equals(expectedIsActive, aCategory.isActive())
                            && Objects.nonNull(aCategory.getCreatedAt())
                            && Objects.nonNull(aCategory.getUpdatedAt())
                            && Objects.nonNull(aCategory.getDeletedAt());
                }
        ));
    }

    @Test
    public void givenAValidaCommand_whenGatewayThrowsRandomException_shouldReturnAException(){

        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive  = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";
        final var aComand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        //when
        when(categoryGateway.create(any())).thenThrow(new IllegalStateException("Gateway error"));

        //then
        final var notification  = useCase.execute(aComand).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(1)).create(argThat(
                aCategory ->{
                    return Objects.equals(expectedName, aCategory.getName())
                            && Objects.equals(expectedDescription, aCategory.getDescription())
                            && Objects.equals(expectedIsActive, aCategory.isActive())
                            && Objects.nonNull(aCategory.getCreatedAt())
                            && Objects.nonNull(aCategory.getUpdatedAt())
                            && Objects.isNull(aCategory.getDeletedAt());
                }
        ));
    }
}
