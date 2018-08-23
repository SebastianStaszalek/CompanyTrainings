package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.ExternalCouchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ExternalCouchRepository extends JpaRepository<ExternalCouchEntity, Long> {

    ExternalCouchEntity findById(Long id);

    ExternalCouchEntity save(ExternalCouchEntity externalCouch);

    void deleteById(Long id);

}
