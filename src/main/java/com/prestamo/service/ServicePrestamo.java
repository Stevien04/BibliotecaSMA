/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prestamo.service;

/**
 *
 * @author Razse
 */
import com.prestamo.model.Prestamo;
import com.prestamo.model.PrestamoEstado;
import com.prestamo.repository.PrestamoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ServicePrestamo {

    private final PrestamoRepository prestamoRepository;

    public ServicePrestamo(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }

    public List<Prestamo> listarPrestamosPorLector(String lector) {
        return prestamoRepository.findAll().stream()
                .filter(prestamo -> prestamo.getLector().equalsIgnoreCase(lector))
                .toList();
    }

    public List<Prestamo> listarPendientesDevolucion() {
        return prestamoRepository.findByEstado(PrestamoEstado.PENDIENTE_DEVOLUCION);
    }

    public List<Prestamo> listarPendientesAprobacion() {
        return prestamoRepository.findByEstado(PrestamoEstado.PENDIENTE_APROBACION);
    }

    public Prestamo crearPrestamo(String tituloLibro, String lector, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        Assert.hasText(tituloLibro, "El título es obligatorio");
        Assert.hasText(lector, "El nombre del lector es obligatorio");
        Assert.notNull(fechaPrestamo, "La fecha de préstamo es obligatoria");
        Assert.notNull(fechaDevolucion, "La fecha de devolución es obligatoria");
        Assert.isTrue(!fechaPrestamo.isBefore(LocalDate.now()), "La fecha de préstamo no puede ser pasada");
        Assert.isTrue(!fechaDevolucion.isBefore(fechaPrestamo), "La devolución debe ser igual o posterior a la fecha de préstamo");
        Assert.isTrue(!prestamoRepository.existePrestamoActivo(tituloLibro, lector), "Ya tienes una reserva activa para este libro");
        Prestamo prestamo = new Prestamo();
        prestamo.setTituloLibro(tituloLibro);
        prestamo.setLector(lector);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setEstado(PrestamoEstado.PENDIENTE_APROBACION);
        return prestamoRepository.guardar(prestamo);
    }

    public List<String> catalogoLibros() {
        return List.of(
                "Cien años de soledad",
                "El Quijote",
                "Pedro Páramo",
                "Rayuela",
                "Ficciones"
        );
    }

    public void solicitarDevolucion(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el préstamo " + id));
        if (prestamo.getEstado() == PrestamoEstado.PRESTADO) {
            prestamo.setEstado(PrestamoEstado.PENDIENTE_DEVOLUCION);
        }
    }

    public void aprobarPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el préstamo " + id));
        if (prestamo.getEstado() == PrestamoEstado.PENDIENTE_APROBACION) {
            prestamo.setEstado(PrestamoEstado.PRESTADO);
            if (prestamo.getFechaPrestamo() == null) {
                prestamo.setFechaPrestamo(LocalDate.now());
            }
            if (prestamo.getFechaDevolucion() == null || prestamo.getFechaDevolucion().isBefore(prestamo.getFechaPrestamo())) {
                prestamo.setFechaDevolucion(prestamo.getFechaPrestamo().plusDays(7));
            }
        }
    }

    public void aceptarDevolucion(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el préstamo " + id));
        if (prestamo.getEstado() == PrestamoEstado.PENDIENTE_DEVOLUCION) {
            prestamo.setEstado(PrestamoEstado.DEVUELTO);
        }
    }
}
