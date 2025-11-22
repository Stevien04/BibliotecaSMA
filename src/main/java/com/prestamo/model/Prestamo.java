/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prestamo.model;

/**
 *
 * @author Razse
 */
import java.time.LocalDate;

public class Prestamo {
    

    private Long id;
    private String tituloLibro;
    private String lector;
    private PrestamoEstado estado;
    private LocalDate fechaPrestamo;

    public Prestamo() {
    }

    public Prestamo(Long id, String tituloLibro, String lector, PrestamoEstado estado, LocalDate fechaPrestamo) {
        this.id = id;
        this.tituloLibro = tituloLibro;
        this.lector = lector;
        this.estado = estado;
        this.fechaPrestamo = fechaPrestamo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getLector() {
        return lector;
    }

    public void setLector(String lector) {
        this.lector = lector;
    }

    public PrestamoEstado getEstado() {
        return estado;
    }

    public void setEstado(PrestamoEstado estado) {
        this.estado = estado;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
}