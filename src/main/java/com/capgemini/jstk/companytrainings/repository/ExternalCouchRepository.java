package com.capgemini.jstk.companytrainings.repository;

import com.capgemini.jstk.companytrainings.domain.ExternalCouchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalCouchRepository extends JpaRepository<ExternalCouchEntity, Long> {

}
