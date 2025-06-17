package com.german.apirest.springboot.app.springbootcrud.entities;

import com.german.apirest.springboot.app.springbootcrud.validation.IsRequired;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Representa un producto en la base de datos.
 * <p>
 * Mapeado a la tabla <code>products</code> mediante JPA.
 * Cada instancia contiene información de SKU, nombre, precio y descripción,
 * con validaciones aplicadas sobre los campos.
 * </p>
 *
 * @author German
 * @version 1.0
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * Identificador único generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * SKU (Stock Keeping Unit) único del producto.
     * <p>
     * Validado como obligatorio mediante {@link IsRequired}.
     * </p>
     */
    @IsRequired
    private String sku;
    
    /**
     * Nombre del producto.
     * <p>
     * Obligatorio (custom {@link IsRequired} con mensaje personalizado)
     * y longitud entre 3 y 20 caracteres.
     * </p>
     */
    @IsRequired(message = "{IsRequired.product.name}")
    @Size(min = 3, max = 20)
    private String name;
    
    /**
     * Precio del producto en centavos (o la unidad monetaria definida).
     * <p>
     * Obligatorio y debe ser al menos 500.
     * </p>
     */
    @Min(value = 500, message = "{Min.product.price}")
    @NotNull(message = "{NotNull.product.price}")
    private Integer price;

    /**
     * Descripción detallada del producto.
     * <p>
     * Validado como obligatorio mediante {@link IsRequired}.
     * </p>
     */
    @IsRequired
    private String description;

    /**
     * Obtiene el ID del producto.
     *
     * @return identificador único del producto.
     */
    public Long getId() {
        return id;
    }

    /**
     * Asigna el ID del producto.
     *
     * @param id identificador único a asignar.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el SKU del producto.
     *
     * @return cadena que representa el SKU.
     */
    public String getSku() {
        return sku;
    }

    /**
     * Asigna el SKU del producto.
     *
     * @param sku valor de SKU a establecer.
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return nombre del producto.
     */
    public String getName() {
        return name;
    }

    /**
     * Asigna el nombre del producto.
     *
     * @param name nombre a establecer (3-20 caracteres).
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return precio actual (mínimo 500).
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * Asigna el precio del producto.
     *
     * @param price precio en la unidad definida; debe ser >= 500.
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * Obtiene la descripción del producto.
     *
     * @return texto descriptivo.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Asigna la descripción del producto.
     *
     * @param description texto explicativo a establecer.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}