/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.product.dto.request.ProductRequestDto;
import cl.duoc.product.dto.response.ProductResponseDto;
import cl.duoc.product.exception.ResourceNotFoundException;
import cl.duoc.product.model.ProductModel;
import cl.duoc.product.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductModel createProduct() {
        return new ProductModel(
                1L, "Alimento premium", "Alimento para perro adulto", new BigDecimal("24990"), 20, "alimentos", true);
    }

    private ProductRequestDto createRequest() {
        return new ProductRequestDto(
                "Alimento premium", "Alimento para perro adulto", new BigDecimal("24990"), 20, "alimentos");
    }

    @Test
    void createProduct_shouldSaveActiveProduct() {
        ProductModel savedProduct = createProduct();
        when(productRepository.save(any(ProductModel.class))).thenReturn(savedProduct);

        ProductResponseDto result = productService.createProduct(createRequest());

        ArgumentCaptor<ProductModel> captor = ArgumentCaptor.forClass(ProductModel.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getActivo()).isTrue();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNombre()).isEqualTo("Alimento premium");
    }

    @Test
    void getAllProducts_shouldMapRepositoryResults() {
        when(productRepository.findAll()).thenReturn(List.of(createProduct()));

        List<ProductResponseDto> result = productService.getAllProducts();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCategoria()).isEqualTo("alimentos");
        verify(productRepository).findAll();
    }

    @Test
    void getActiveProducts_shouldUseActiveProductsQuery() {
        when(productRepository.findByActivoTrue()).thenReturn(List.of(createProduct()));

        List<ProductResponseDto> result = productService.getActiveProducts();

        assertThat(result).hasSize(1).allMatch(ProductResponseDto::getActivo);
        verify(productRepository).findByActivoTrue();
    }

    @Test
    void getProductsByCategoria_shouldReturnMatchingActiveProducts() {
        when(productRepository.findByCategoriaAndActivoTrue("alimentos")).thenReturn(List.of(createProduct()));

        List<ProductResponseDto> result = productService.getProductsByCategoria("alimentos");

        assertThat(result).extracting(ProductResponseDto::getCategoria).containsExactly("alimentos");
        verify(productRepository).findByCategoriaAndActivoTrue("alimentos");
    }

    @Test
    void getProductById_shouldReturnProductWhenItExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(createProduct()));

        ProductResponseDto result = productService.getProductById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPrecio()).isEqualByComparingTo("24990");
    }

    @Test
    void getProductById_shouldThrowWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Producto no encontrado con id: 99");
    }

    @Test
    void updateProduct_shouldModifyExistingProduct() {
        ProductModel existingProduct = createProduct();
        ProductRequestDto request = new ProductRequestDto(
                "Alimento actualizado", "Nueva descripcion", new BigDecimal("27990"), 15, "premium");
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        ProductResponseDto result = productService.updateProduct(1L, request);

        assertThat(result.getNombre()).isEqualTo("Alimento actualizado");
        assertThat(result.getStock()).isEqualTo(15);
        assertThat(result.getActivo()).isTrue();
        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateProduct_shouldNotSaveWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(99L, createRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(productRepository, never()).save(any(ProductModel.class));
    }

    @Test
    void deactivateProduct_shouldMarkProductAsInactive() {
        ProductModel existingProduct = createProduct();
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        ProductResponseDto result = productService.deactivateProduct(1L);

        assertThat(result.getActivo()).isFalse();
        verify(productRepository).save(existingProduct);
    }
}
