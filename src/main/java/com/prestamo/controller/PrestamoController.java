/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prestamo.controller;

/**
 *
 * @author Razse
 */
import jakarta.servlet.http.HttpSession;
import com.prestamo.model.Prestamo;
import com.prestamo.service.ServicePrestamo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
public class PrestamoController {

    private final ServicePrestamo servicePrestamo;

    public PrestamoController(ServicePrestamo servicePrestamo) {
        this.servicePrestamo = servicePrestamo;
    }

    @GetMapping({"/", "/menu"})
    public String inicio(HttpSession session) {
        Object rolSesion = session.getAttribute("rol");
        if ("ADMIN".equals(rolSesion)) {
            return "redirect:/administracion";
        }
        if ("LECTOR".equals(rolSesion)) {
            return "redirect:/prestamo";
        }
        return "redirect:/login";
    }

    @GetMapping("/prestamo")
    public String verPrestamo(Model model, HttpSession session, @RequestParam(name = "titulo", required = false) String titulo) {
        Prestamo prestamoForm = new Prestamo();
        if (!usuarioTieneRol(session, "LECTOR")) {
            return "redirect:/login?rol=lector";
        }
        Object usuario = session.getAttribute("usuario");
        if (usuario instanceof String usuarioNombre) {
            prestamoForm.setLector(usuarioNombre);
        }
        LocalDate hoy = LocalDate.now();
        prestamoForm.setFechaPrestamo(hoy);
        prestamoForm.setFechaDevolucion(hoy.plusDays(7));
        if (titulo != null && !titulo.isBlank()) {
            prestamoForm.setTituloLibro(titulo);
        }
        agregarInfoUsuario(model, session);
        model.addAttribute("prestamos", servicePrestamo.listarPrestamos());
        model.addAttribute("libros", servicePrestamo.catalogoLibros());
        model.addAttribute("prestamoForm", prestamoForm);
        return "Prestamo";
    }

    @PostMapping("/prestamo")
    public String registrarPrestamo(@ModelAttribute("prestamoForm") Prestamo prestamoForm,
            RedirectAttributes redirectAttributes, HttpSession session) {
        if (!usuarioTieneRol(session, "LECTOR")) {
            return "redirect:/login?rol=lector";
        }
        String lector = (String) session.getAttribute("usuario");
        if (lector == null || lector.isBlank()) {
            return "redirect:/login?rol=lector";
        }
        servicePrestamo.crearPrestamo(prestamoForm.getTituloLibro(), lector, prestamoForm.getFechaPrestamo(), prestamoForm.getFechaDevolucion());
        redirectAttributes.addFlashAttribute("mensaje", "Préstamo registrado para " + lector);
        return "redirect:/prestamo";
    }

    @PostMapping("/prestamo/{id}/devolucion")
    public String solicitarDevolucion(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!usuarioTieneRol(session, "LECTOR")) {
            return "redirect:/login?rol=lector";
        }
        servicePrestamo.solicitarDevolucion(id);
        redirectAttributes.addFlashAttribute("mensaje", "Solicitud de devolución enviada al administrador.");
        return "redirect:/prestamo";
    }

    @GetMapping("/administracion")
    public String verAdministracion(Model model, HttpSession session) {
        if (!usuarioTieneRol(session, "ADMIN")) {
            return "redirect:/login?rol=admin";
        }
        agregarInfoUsuario(model, session);
        model.addAttribute("pendientes", servicePrestamo.listarPendientesDevolucion());
        return "Administracion";
    }

    @PostMapping("/administracion/{id}/aceptar")
    public String aceptarDevolucion(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!usuarioTieneRol(session, "ADMIN")) {
            return "redirect:/login?rol=admin";
        }
        servicePrestamo.aceptarDevolucion(id);
        redirectAttributes.addFlashAttribute("mensaje", "Devolución aceptada.");
        return "redirect:/administracion";
    }

    private boolean usuarioTieneRol(HttpSession session, String rol) {
        Object rolSesion = session.getAttribute("rol");
        return rol.equals(rolSesion);
    }

    private void agregarInfoUsuario(Model model, HttpSession session) {
        model.addAttribute("usuarioActivo", session.getAttribute("usuario"));
        model.addAttribute("rolActivo", session.getAttribute("rol"));
    }
}
