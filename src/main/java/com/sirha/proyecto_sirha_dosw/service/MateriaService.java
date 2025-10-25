package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.MateriaDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MateriaService {

    private final MateriaRepository materiaRepository;

    @Autowired
    public MateriaService (MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }
    public Materia createMateria(@Valid MateriaDTO materiaDTO) throws SirhaException {
        if(materiaRepository.findByAcronimo(materiaDTO.getAcronimo()).isPresent()){
            throw new SirhaException(SirhaException.MATERIA_YA_EXISTE);
        }
        if(materiaRepository.findByNombre(materiaDTO.getNombre()).isPresent()){
            throw new SirhaException(SirhaException.MATERIA_YA_EXISTE);
        }
        Materia materia = new Materia();
        materia.setAcronimo(materiaDTO.getAcronimo());
        materia.setNombre(materiaDTO.getNombre());
        materia.setCreditos(materiaDTO.getCreditos());
        return materiaRepository.save(materia);
    }

    public void deleteMateria(String id) throws SirhaException{
        if(!materiaRepository.existsById(id)){
            throw new SirhaException(SirhaException.MATERIA_NO_ENCONTRADA);
        }
        materiaRepository.deleteById(id);
    }
}
