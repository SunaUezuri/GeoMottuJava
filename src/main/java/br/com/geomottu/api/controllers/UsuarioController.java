package br.com.geomottu.api.controllers;

import br.com.geomottu.api.config.security.SecurityUtils;
import br.com.geomottu.api.dto.usuario.UpdateRoleDto;
import br.com.geomottu.api.dto.usuario.UsuarioDto;
import br.com.geomottu.api.model.entities.Usuario;
import br.com.geomottu.api.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;

    // ADMIN: Lista todos os usuários
    @GetMapping
    public String listAll(Model model) {
        model.addAttribute("usuarios", usuarioService.getAll());
        return "usuarios/lista";
    }

    // USER e ADMIN: Mostra o próprio perfil
    @GetMapping("/perfil")
    public String viewProfile(Model model) {
        try {
            Usuario usuarioLogado = securityUtils.getUsuarioLogado();
            model.addAttribute("usuario", usuarioService.getById(usuarioLogado.getId()));
            return "usuarios/perfil";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    // USER e ADMIN: Página para editar o perfil
    @GetMapping("/{id}/editar")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.getById(id);
            UsuarioDto dto = new UsuarioDto(usuario.getNome(), usuario.getTipoPerfil(), "", usuario.getFilial().getId());
            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarioDto", dto);
            return "usuarios/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    // USER e ADMIN: Processa a atualização do perfil
    @PostMapping("/{id}/editar")
    public String updateUser(@PathVariable Long id, @ModelAttribute("usuarioDto") UsuarioDto dto, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Perfil atualizado com sucesso!");
            return "redirect:/usuarios/perfil";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar perfil: " + e.getMessage());
            return "redirect:/usuarios/" + id + "/editar";
        }
    }

    // ADMIN: Processa a mudança de perfil de um usuário
    @PostMapping("/{id}/role")
    public String updateUserRole(@PathVariable Long id, @ModelAttribute("updateRoleDto") UpdateRoleDto dto, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.updateRole(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Perfil do usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar perfil: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    // USER e ADMIN: Deleta o próprio perfil (ou qualquer um se for admin)
    @PostMapping("/{id}/deletar")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioLogado = securityUtils.getUsuarioLogado();
            usuarioService.delete(id);

            if (usuarioLogado.getId().equals(id)) {
                return "redirect:/logout";
            }
            redirectAttributes.addFlashAttribute("successMessage", "Usuário deletado com sucesso!");
            return "redirect:/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao deletar usuário: " + e.getMessage());
            return "redirect:/usuarios";
        }
    }
}
