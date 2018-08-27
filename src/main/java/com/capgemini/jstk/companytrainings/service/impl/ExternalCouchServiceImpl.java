package com.capgemini.jstk.companytrainings.service.impl;

import com.capgemini.jstk.companytrainings.domain.ExternalCouchEntity;
import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.dto.ExternalCouchTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import com.capgemini.jstk.companytrainings.exception.message.Message;
import com.capgemini.jstk.companytrainings.mapper.ExternalCouchMapper;
import com.capgemini.jstk.companytrainings.mapper.TrainingMapper;
import com.capgemini.jstk.companytrainings.repository.ExternalCouchRepository;
import com.capgemini.jstk.companytrainings.service.ExternalCouchService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ExternalCouchServiceImpl implements ExternalCouchService {

    private ExternalCouchMapper couchMapper;
    private ExternalCouchRepository couchRepository;

    private TrainingMapper trainingMapper;

    @Autowired
    public ExternalCouchServiceImpl(ExternalCouchMapper couchMapper, ExternalCouchRepository couchRepository,
                                    TrainingMapper trainingMapper) {
        this.couchMapper = couchMapper;
        this.couchRepository = couchRepository;
        this.trainingMapper = trainingMapper;
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
        Preconditions.checkNotNull(externalCouch, Message.EMPTY_OBJECT);
        ExternalCouchEntity couchEntity = couchMapper.map(externalCouch);

        return couchMapper.map(couchRepository.save(couchEntity));
    }

    @Override
    public ExternalCouchTO update(ExternalCouchTO externalCouch) {
        Preconditions.checkNotNull(externalCouch.getId(), Message.EMPTY_ID);
        ExternalCouchEntity couchEntity = couchRepository.findOne(externalCouch.getId());

        if(!externalCouch.getVersion().equals(couchEntity.getVersion())) {
            throw new OptimisticLockException();
        }

        ExternalCouchEntity couchToUpdate = couchMapper.map(externalCouch, couchEntity);

        return couchMapper.map(couchToUpdate);
    }

    @Override
    public void deleteExternalCouch(ExternalCouchTO externalCouch) {
        Preconditions.checkNotNull(externalCouch.getId(), Message.EMPTY_ID);
        couchRepository.delete(externalCouch.getId());
    }

    @Override
    public Set<TrainingTO> getAllTrainingsByCouchId(Long studentId) {
        Preconditions.checkNotNull(studentId, Message.EMPTY_ID);
        ExternalCouchEntity couchEntity = couchRepository.findOne(studentId);

        Set<TrainingEntity> trainingsList = couchEntity.getTrainings();

        return trainingMapper.map2TOSet(trainingsList);
    }
}
