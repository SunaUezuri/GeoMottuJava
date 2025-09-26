package br.com.geomottu.api.services;

import br.com.geomottu.api.config.security.SecurityUtils;
import br.com.geomottu.api.dto.patio.PatioDto;
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

    public List<Patio> getAll() {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        if (securityUtils.isAdmin(usuarioLogado)) {
            return patioRepository.findAll();
        } else {
            return patioRepository.findAllByFilial(usuarioLogado.getFilial());
        }
    }

    public Patio getById(Long id) throws IdNaoEncontradoException {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();
        if (securityUtils.isAdmin(usuarioLogado)) {
            return patioRepository.findById(id)
                    .orElseThrow(() -> new IdNaoEncontradoException("Pátio não encontrado com ID: " + id));
        } else {
            return patioRepository.findByIdAndFilial(id, usuarioLogado.getFilial())
                    .orElseThrow(() -> new IdNaoEncontradoException("Pátio não encontrado ou não pertence à sua filial. ID: " + id));
        }
    }

    public Patio update(Long id, PatioDto dto) throws IdNaoEncontradoException {
        Usuario usuarioLogado = securityUtils.getUsuarioLogado();

        Patio patio = getById(id);

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

        return patioRepository.save(patio);
    }

    public void delete(Long id) throws IdNaoEncontradoException {
        Patio patioParaDeletar = getById(id);
        patioRepository.delete(patioParaDeletar);
    }
}
