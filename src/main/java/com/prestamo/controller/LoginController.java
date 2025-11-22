package com.prestamo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "Login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String usuario,
            @RequestParam String password,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if (credencialesValidas(usuario, password)) {
            HttpSession nuevaSession = request.getSession(true);
            String rol = obtenerRol(usuario);
            nuevaSession.setAttribute("usuario", usuario);
            nuevaSession.setAttribute("rol", rol);
            return rol.equals("ADMIN") ? "redirect:/administracion" : "redirect:/prestamo";
        }

        redirectAttributes.addFlashAttribute("error", "Credenciales incorrectas. Usa las claves de ejemplo para ingresar.");
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String cerrarSesion(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        redirectAttributes.addFlashAttribute("mensaje", "Sesión finalizada correctamente.");
        return "redirect:/login";
    }

    private boolean credencialesValidas(String usuario, String password) {
        return ("lector".equalsIgnoreCase(usuario) && "lector123".equals(password))
                || ("admin".equalsIgnoreCase(usuario) && "admin123".equals(password));
    }

    private String obtenerRol(String usuario) {
        return "admin".equalsIgnoreCase(usuario) ? "ADMIN" : "LECTOR";
    }
}