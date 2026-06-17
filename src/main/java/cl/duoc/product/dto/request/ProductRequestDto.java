/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos necesarios para crear o actualizar un producto")
public class ProductRequestDto {

    @Schema(description = "Nombre del producto", example = "Notebook Lenovo IdeaPad")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Descripcion comercial del producto", example = "Notebook de 15 pulgadas con 16 GB RAM")
    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @Schema(description = "Precio unitario del producto", example = "549990")
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Schema(description = "Cantidad disponible en inventario", example = "25")
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    @Schema(description = "Categoria a la que pertenece el producto", example = "tecnologia")
    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;
}
