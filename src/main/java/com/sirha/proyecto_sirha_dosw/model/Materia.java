package com.sirha.proyecto_sirha_dosw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Document(collection = "materias") // Nombre más apropiado para la colección
public class Materia {

    @Id
    private String id;

    @NotBlank(message = "El código de la materia es obligatorio")
    @Size(min = 3, max = 10, message = "El código debe tener entre 3 y 10 caracteres")
    private String codigo;

    @NotBlank(message = "El acrónimo de la materia es obligatorio")
    @Size(min = 2, max = 6, message = "El acrónimo debe tener entre 2 y 6 caracteres")
    private String acronimo;

    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Size(min = 5, max = 100, message = "El nombre debe tener entre 5 y 100 caracteres")
    private String nombre;

    @NotNull(message = "Los créditos de la materia son obligatorios")
    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    private Integer creditos;

    @NotNull(message = "La estrategia de cálculo es obligatoria")
    private EstrategiaCalculo estrategiaCalculo;

    private List<String> prerequisitos; // Códigos de materias prerrequisito

    // Constructores
    public Materia() {
        // Constructor vacío para Spring Data
    }

    public Materia(String codigo, String acronimo, String nombre, int creditos,
                   EstrategiaCalculo estrategiaCalculo) {
        this.codigo = codigo;
        this.acronimo = acronimo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.estrategiaCalculo = estrategiaCalculo;
    }

    public Materia(String codigo, String acronimo, String nombre, int creditos,
                   EstrategiaCalculo estrategiaCalculo, String descripcion,
                   Integer intensidadHoraria, Boolean esObligatoria, Integer semestreRecomendado) {
        this(codigo, acronimo, nombre, creditos, estrategiaCalculo);
    }

    // Método principal para calcular nota
    public Double calcularNota(List<Double> notas) {
        if (notas == null || notas.isEmpty()) {
            throw new IllegalArgumentException("La lista de notas no puede ser nula o vacía");
        }

        // Validar que las notas estén en el rango permitido (0.0 - 5.0)
        for (Double nota : notas) {
            if (nota < 0.0 || nota > 5.0) {
                throw new IllegalArgumentException("Las notas deben estar entre 0.0 y 5.0");
            }
        }

        return this.estrategiaCalculo.calcularNota(notas);
    }

    // Métodos de validación
    public boolean tienePrerrequisitos() {
        return prerequisitos != null && !prerequisitos.isEmpty();
    }

    public boolean cumplePrerrequisitos(List<String> materiasAprobadas) {
        if (!tienePrerrequisitos()) {
            return true;
        }

        return materiasAprobadas.containsAll(prerequisitos);
    }

    // Métodos de utilidad
    public String obtenerInfoCompleta() {
        return String.format("%s - %s (%s) - %d créditos",
                codigo, nombre, acronimo, creditos);
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    public EstrategiaCalculo getEstrategiaCalculo() {
        return estrategiaCalculo;
    }

    public void setEstrategiaCalculo(EstrategiaCalculo estrategiaCalculo) {
        this.estrategiaCalculo = estrategiaCalculo;
    }

    public List<String> getPrerequisitos() {
        return prerequisitos;
    }

    public void setPrerequisitos(List<String> prerequisitos) {
        this.prerequisitos = prerequisitos;
    }

}