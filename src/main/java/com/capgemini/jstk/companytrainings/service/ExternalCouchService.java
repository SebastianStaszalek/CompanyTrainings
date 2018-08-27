package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.dto.ExternalCouchTO;

import java.util.List;

public interface ExternalCouchService {

    ExternalCouchTO findExternalCouchById(Long id);

    List<ExternalCouchTO> getAllExternalCouches();

    ExternalCouchTO save(ExternalCouchTO externalCouch);

    ExternalCouchTO update(ExternalCouchTO externalCouch);

    void deleteExternalCouch(ExternalCouchTO externalCouch);
}
