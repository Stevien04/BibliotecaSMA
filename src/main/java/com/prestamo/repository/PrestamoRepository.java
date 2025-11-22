/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prestamo.repository;

/**
 *
 * @author Razse
 */
import com.prestamo.model.Prestamo;
import com.prestamo.model.PrestamoEstado;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class PrestamoRepository {
    

    private final List<Prestamo> prestamos = new CopyOnWriteArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public PrestamoRepository() {
        prestamos.add(new Prestamo(sequence.getAndIncrement(), "Cien años de soledad", "Ana Torres", PrestamoEstado.PRESTADO, LocalDate.now().minusDays(4)));
        prestamos.add(new Prestamo(sequence.getAndIncrement(), "El Quijote", "Luis Pérez", PrestamoEstado.PENDIENTE_DEVOLUCION, LocalDate.now().minusDays(10)));
    }

    public Prestamo guardar(Prestamo prestamo) {
        prestamo.setId(sequence.getAndIncrement());
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setEstado(PrestamoEstado.PRESTADO);
        prestamos.add(prestamo);
        return prestamo;
    }

    public List<Prestamo> findAll() {
        return prestamos.stream()
                .sorted(Comparator.comparing(Prestamo::getId))
                .collect(Collectors.toList());
    }

    public List<Prestamo> findByEstado(PrestamoEstado estado) {
        return prestamos.stream()
                .filter(prestamo -> prestamo.getEstado() == estado)
                .sorted(Comparator.comparing(Prestamo::getId))
                .collect(Collectors.toList());
    }

    public Optional<Prestamo> findById(Long id) {
        return prestamos.stream()
                .filter(prestamo -> prestamo.getId().equals(id))
                .findFirst();
    }
}