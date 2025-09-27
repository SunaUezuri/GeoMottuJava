package br.com.geomottu.api.controllers;

import br.com.geomottu.api.dto.filial.FilialDto;
import br.com.geomottu.api.services.FilialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/filiais")
@RequiredArgsConstructor
public class FilialController {

    private final FilialService filialService;

    @GetMapping
    public String listAll(Model model) {
        model.addAttribute("filiais", filialService.getAll());
        return "filiais/lista";
    }

    @GetMapping("/nova")
    public String showCreateForm(Model model) {
        model.addAttribute("filialDto", new FilialDto(null, null, null, null, null));
        return "filiais/form";
    }

    @PostMapping("/nova")
    public String createFilial(@ModelAttribute("filialDto") FilialDto dto, RedirectAttributes redirectAttributes) {
        try {
            filialService.save(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Filial criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar filial: " + e.getMessage());
        }
        return "redirect:/filiais";
    }

    @GetMapping("/{id}/editar")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("filial", filialService.getById(id));
            return "filiais/form";
        } catch (Exception e) {
            return "redirect:/filiais";
        }
    }

    @PostMapping("/{id}/editar")
    public String updateFilial(@PathVariable Long id, @ModelAttribute("filialDto") FilialDto dto, RedirectAttributes redirectAttributes) {
        try {
            filialService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Filial atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar filial: " + e.getMessage());
        }
        return "redirect:/filiais";
    }

    @PostMapping("/{id}/deletar")
    public String deleteFilial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            filialService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Filial deletada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao deletar filial: " + e.getMessage());
        }
        return "redirect:/filiais";
    }
}
