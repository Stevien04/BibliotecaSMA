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
        return "redirect:/prestamo/registrar";
    }

    @GetMapping("/prestamo/registrar")
    public String verRegistrarPrestamo(Model model, HttpSession session, @RequestParam(name = "titulo", required = false) String titulo) {
        if (!usuarioTieneRol(session, "LECTOR")) {
            return "redirect:/login?rol=lector";
        }
        Prestamo prestamoForm = (Prestamo) model.asMap().get("prestamoFallido");
        if (prestamoForm == null) {
            prestamoForm = crearPrestamoForm(session, titulo);
        }
        agregarInfoUsuario(model, session);
        model.addAttribute("libros", servicePrestamo.catalogoLibros());
        model.addAttribute("prestamoForm", prestamoForm);
        model.addAttribute("abrirModal", true);
        return "Registrar";
    }

    @GetMapping("/prestamo/mis")
    public String verMisPrestamos(Model model, HttpSession session) {
        if (!usuarioTieneRol(session, "LECTOR")) {
            return "redirect:/login?rol=lector";
        }
        agregarInfoUsuario(model, session);
        String lector = (String) session.getAttribute("usuario");
        model.addAttribute("prestamos", servicePrestamo.listarPrestamosPorLector(lector));
        return "MisPrestamos";
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
        try {
            servicePrestamo.crearPrestamo(prestamoForm.getTituloLibro(), lector, prestamoForm.getFechaPrestamo(), prestamoForm.getFechaDevolucion());
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud enviada, un administrador debe aprobarla antes de entregarte el libro.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("prestamoFallido", prestamoForm);
            return "redirect:/prestamo/registrar";
        }
        return "redirect:/prestamo/mis";
    }

    @PostMapping("/prestamo/{id}/devolucion")
    public String solicitarDevolucion(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!usuarioTieneRol(session, "LECTOR")) {
            return "redirect:/login?rol=lector";
        }
        servicePrestamo.solicitarDevolucion(id);
        redirectAttributes.addFlashAttribute("mensaje", "Solicitud de devolución enviada al administrador.");
        return "redirect:/prestamo/mis";
    }

    @GetMapping("/administracion")
    public String verAdministracion(Model model, HttpSession session) {
        if (!usuarioTieneRol(session, "ADMIN")) {
            return "redirect:/login?rol=admin";
        }
        agregarInfoUsuario(model, session);
        model.addAttribute("solicitudes", servicePrestamo.listarPendientesAprobacion());
        model.addAttribute("pendientes", servicePrestamo.listarPendientesDevolucion());
        return "Administracion";
    }

    @PostMapping("/administracion/{id}/aprobar")
    public String aprobarPrestamo(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!usuarioTieneRol(session, "ADMIN")) {
            return "redirect:/login?rol=admin";
        }
        servicePrestamo.aprobarPrestamo(id);
        redirectAttributes.addFlashAttribute("mensaje", "Préstamo aprobado correctamente.");
        return "redirect:/administracion";
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

    private Prestamo crearPrestamoForm(HttpSession session, String titulo) {
        Prestamo prestamoForm = new Prestamo();
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
        return prestamoForm;
    }
}
