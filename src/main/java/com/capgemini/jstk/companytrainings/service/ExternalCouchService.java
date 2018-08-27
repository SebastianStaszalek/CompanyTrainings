package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.dto.ExternalCouchTO;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;

import java.util.List;
import java.util.Set;

public interface ExternalCouchService {

    ExternalCouchTO findExternalCouchById(Long id);

    List<ExternalCouchTO> getAllExternalCouches();

    ExternalCouchTO save(ExternalCouchTO externalCouch);

    ExternalCouchTO update(ExternalCouchTO externalCouch);

    void deleteExternalCouch(ExternalCouchTO externalCouch);

    Set<TrainingTO> getAllTrainingsByCouchId(Long studentId);
}
