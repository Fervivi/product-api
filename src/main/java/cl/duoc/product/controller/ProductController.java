/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.product.controller;

import cl.duoc.product.dto.request.ProductRequestDto;
import cl.duoc.product.dto.response.ProductResponseDto;
import cl.duoc.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones para consultar y administrar productos")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(
            summary = "Listar productos",
            description = "Obtiene todos los productos registrados.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos encontrados",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema = @Schema(implementation = ProductResponseDto.class))))
            })
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Listar productos activos",
            description = "Obtiene solo los productos que se encuentran activos.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos activos encontrados",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema = @Schema(implementation = ProductResponseDto.class))))
            })
    public ResponseEntity<List<ProductResponseDto>> getActiveProducts() {
        return ResponseEntity.ok(productService.getActiveProducts());
    }

    @GetMapping("/category/{categoria}")
    @Operation(
            summary = "Buscar productos por categoria",
            description = "Obtiene los productos asociados a una categoria.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Productos encontrados para la categoria",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema = @Schema(implementation = ProductResponseDto.class))))
            })
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategoria(
            @Parameter(description = "Categoria del producto", example = "tecnologia") @PathVariable String categoria) {
        return ResponseEntity.ok(productService.getProductsByCategoria(categoria));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener producto por ID",
            description = "Obtiene el detalle de un producto especifico.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Producto encontrado",
                        content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
            })
    public ResponseEntity<ProductResponseDto> getProductById(
            @Parameter(description = "Identificador del producto", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @Operation(
            summary = "Crear producto",
            description = "Registra un nuevo producto.",
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Producto creado",
                        content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
                @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
                @ApiResponse(responseCode = "409", description = "Conflicto al crear el producto", content = @Content)
            })
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza los datos de un producto existente.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Producto actualizado",
                        content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
                @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
                @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
                @ApiResponse(
                        responseCode = "409",
                        description = "Conflicto al actualizar el producto",
                        content = @Content)
            })
    public ResponseEntity<ProductResponseDto> updateProduct(
            @Parameter(description = "Identificador del producto", example = "1") @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto request) {

        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Desactivar producto",
            description = "Marca un producto como inactivo.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Producto desactivado",
                        content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
                @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
                @ApiResponse(
                        responseCode = "409",
                        description = "Conflicto al desactivar el producto",
                        content = @Content)
            })
    public ResponseEntity<ProductResponseDto> deactivateProduct(
            @Parameter(description = "Identificador del producto", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(productService.deactivateProduct(id));
    }
}
