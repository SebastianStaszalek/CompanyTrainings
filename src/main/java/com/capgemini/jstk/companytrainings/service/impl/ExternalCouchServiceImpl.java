package com.capgemini.jstk.companytrainings.service.impl;

import com.capgemini.jstk.companytrainings.domain.ExternalCouchEntity;
import com.capgemini.jstk.companytrainings.dto.ExternalCouchTO;
import com.capgemini.jstk.companytrainings.mapper.ExternalCouchMapper;
import com.capgemini.jstk.companytrainings.repository.ExternalCouchRepository;
import com.capgemini.jstk.companytrainings.service.ExternalCouchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.util.List;

//TODO: napisz jakies testy na usuwanie!
@Service
@Transactional
public class ExternalCouchServiceImpl implements ExternalCouchService {

    private ExternalCouchMapper couchMapper;
    private ExternalCouchRepository couchRepository;

    @Autowired
    public ExternalCouchServiceImpl(ExternalCouchMapper couchMapper, ExternalCouchRepository couchRepository) {
        this.couchMapper = couchMapper;
        this.couchRepository = couchRepository;
    }

    @Override
    public ExternalCouchTO findExternalCouchById(Long id) {
        return couchMapper.map(couchRepository.findOne(id));
    }

    @Override
    public List<ExternalCouchTO> getAllExternalCouches() {
        return couchMapper.map2TO(couchRepository.findAll());
    }

    @Override
    public ExternalCouchTO save(ExternalCouchTO externalCouch) {
        ExternalCouchEntity couchEntity = couchMapper.map(externalCouch);

        return couchMapper.map(couchRepository.save(couchEntity));
    }

    @Override
    public ExternalCouchTO update(ExternalCouchTO externalCouch) {
        ExternalCouchEntity couchEntity = couchRepository.findOne(externalCouch.getId());

        if(!externalCouch.getVersion().equals(couchEntity.getVersion())) {
            throw new OptimisticLockException();
        }

        ExternalCouchEntity couchToUpdate = couchMapper.map(externalCouch, couchEntity);

        return couchMapper.map(couchToUpdate);
    }

    @Override
    public void deleteExternalCouch(ExternalCouchTO externalCouch) {
        couchRepository.delete(externalCouch.getId());
    }
}
