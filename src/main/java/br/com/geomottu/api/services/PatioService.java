package br.com.geomottu.api.services;

import br.com.geomottu.api.config.security.SecurityUtils;
import br.com.geomottu.api.dto.patio.PatioDto;
import br.com.geomottu.api.dto.patio.PatioGetDto;
import br.com.geomottu.api.exceptions.IdNaoEncontradoException;
import br.com.geomottu.api.model.entities.Filial;
import br.com.geomottu.api.model.entities.Patio;
import br.com.geomottu.api.model.entities.Usuario;
import br.com.geomottu.api.repository.FilialRepository;
import br.com.geomottu.api.repository.PatioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatioService {

    private final PatioRepository patioRepository;
    private final FilialRepository filialRepository;
    private final SecurityUtils securityUtils;

    public Patio save(PatioDto dto) throws IdNaoEncontradoException {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();

        Patio patio = new Patio(dto);

        if (securityUtils.isAdmin(usuarioLogado)) {
            Filial filial = filialRepository.findById(dto.filialId())
                    .orElseThrow(() -> new IdNaoEncontradoException("Filial não encontrada com ID: " + dto.filialId()));
            patio.setFilial(filial);
        } else {
            patio.setFilial(usuarioLogado.getFilial());
        }

        return patioRepository.save(patio);
    }

    public List<PatioGetDto> getAll() {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        if (securityUtils.isAdmin(usuarioLogado)) {
            return patioRepository.findAll().stream().map(PatioGetDto::new).toList();
        } else {
            return patioRepository.findAllByFilial(usuarioLogado.getFilial()).stream().map(PatioGetDto::new).toList();
        }
    }

    public PatioGetDto getById(Long id) throws IdNaoEncontradoException {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        if (securityUtils.isAdmin(usuarioLogado)) {
            return patioRepository.findById(id)
                    .map(PatioGetDto::new)
                    .orElseThrow(() -> new IdNaoEncontradoException("Filial não encontrada com ID: " + id));
        } else {
            return patioRepository.findByIdAndFilial(id, usuarioLogado.getFilial())
                    .map(PatioGetDto::new)
                    .orElseThrow(() -> new IdNaoEncontradoException("Filial não encontrada com ID: " + id));
        }
    }

    public PatioGetDto update(Long id, PatioGetDto dto) throws IdNaoEncontradoException {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();

        Patio patio = patioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Pátio não encontrado"));

        if (securityUtils.isAdmin(usuarioLogado)) {
            Filial filial = filialRepository.findById(dto.filialId())
                    .orElseThrow(() -> new IdNaoEncontradoException("Filial não encontrada com ID: " + dto.filialId()));
            patio.setFilial(filial);
        } else {
            if (dto.filialId() != null && !patio.getFilial().getId().equals(dto.filialId())) {
                throw new AccessDeniedException("Usuários não podem mover um pátio para outra filial.");
            }
        }

        patio.setNome(dto.nome());
        patio.setCapacidadeTotal(dto.capacidadeTotal());

        Patio patioAtualizado = patioRepository.save(patio);

        return new PatioGetDto(patioAtualizado);
    }

    public void delete(Long id) throws IdNaoEncontradoException {
        Patio patioParaDeletar = patioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Pátio não encontrado"));
        patioRepository.deleteById(patioParaDeletar.getId());
    }
}
