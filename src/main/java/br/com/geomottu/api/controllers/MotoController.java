package br.com.geomottu.api.controllers;

import br.com.geomottu.api.dto.moto.MotoDto;
import br.com.geomottu.api.exceptions.IdNaoEncontradoException;
import br.com.geomottu.api.model.entities.Moto;
import br.com.geomottu.api.services.MotoService;
import br.com.geomottu.api.services.PatioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/motos")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;
    private final PatioService patioService;

    @GetMapping
    public String listAll(Model model) {
        model.addAttribute("motos", motoService.getAll());
        return "motos/lista";
    }

    @GetMapping("/nova")
    public String showCreateForm(Model model) {
        model.addAttribute("motoDto", new MotoDto(null, null, null, null, null));
        model.addAttribute("patios", patioService.getAll());
        return "motos/form";
    }

    @PostMapping("/nova")
    public String createMoto(@Valid @ModelAttribute("motoDto") MotoDto dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patios", patioService.getAll());
            return "motos/form";
        }
        try {
            motoService.save(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Moto criada com sucesso!");
            return "redirect:/motos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar moto: " + e.getMessage());
            return "redirect:/motos/nova";
        }
    }

    @GetMapping("/{id}/editar")
    public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Moto moto = motoService.getById(id);
            // Converte a entidade para o DTO para preencher o formulário
            MotoDto dto = new MotoDto(moto.getPlaca(), moto.getChassi(), moto.getTipoMoto(), moto.getEstadoMoto(), moto.getPatio().getId());
            model.addAttribute("motoDto", dto);
            model.addAttribute("motoId", id);
            model.addAttribute("patios", patioService.getAll()); // Admin pode ver todos os pátios para realocar a moto
            return "motos/form";
        } catch (IdNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/motos";
        }
    }

    @PostMapping("/{id}/editar")
    public String updateMoto(@PathVariable Long id,
                             @Valid @ModelAttribute("motoDto") MotoDto dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("motoId", id);
            model.addAttribute("patios", patioService.getAll());
            return "motos/form";
        }
        try {
            motoService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Moto atualizada com sucesso!");
            return "redirect:/motos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar moto: " + e.getMessage());
            return "redirect:/motos/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/deletar")
    public String deleteMoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            motoService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Moto deletada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao deletar moto: " + e.getMessage());
        }
        return "redirect:/motos";
    }
}