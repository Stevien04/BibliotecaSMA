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

    public List<Prestamo> listarPendientesDevolucion() {
        return prestamoRepository.findByEstado(PrestamoEstado.PENDIENTE_DEVOLUCION);
    }

    public Prestamo crearPrestamo(String tituloLibro, String lector) {
        Assert.hasText(tituloLibro, "El título es obligatorio");
        Assert.hasText(lector, "El nombre del lector es obligatorio");
        Prestamo prestamo = new Prestamo();
        prestamo.setTituloLibro(tituloLibro);
        prestamo.setLector(lector);
        return prestamoRepository.guardar(prestamo);
    }

    public void solicitarDevolucion(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el préstamo " + id));
        if (prestamo.getEstado() == PrestamoEstado.PRESTADO) {
            prestamo.setEstado(PrestamoEstado.PENDIENTE_DEVOLUCION);
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