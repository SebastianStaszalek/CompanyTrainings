package com.capgemini.jstk.companytrainings.service;

import com.capgemini.jstk.companytrainings.dto.ExternalCouchTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
public class ExternalCouchServiceTest {

    @Autowired
    ExternalCouchService couchService;

    @Autowired
    TestTO testTO;

    @Test
    public void shouldAddEmployeeToDatabase() {
        //given
        ExternalCouchTO externalCouch = testTO.createFirstExternalCouch();

        //when
        ExternalCouchTO savedCouch = couchService.save(externalCouch);

        //then
        assertThat(savedCouch.getId()).isNotNull();
        assertThat(externalCouch.getCompanyName()).isEqualTo(savedCouch.getCompanyName());
    }

    @Test
    public void shouldFindEmployeeById() {
        //given
        ExternalCouchTO externalCouch = testTO.createFirstExternalCouch();

        ExternalCouchTO savedCouch = couchService.save(externalCouch);

        //when
        ExternalCouchTO couchToCheck = couchService.findExternalCouchById(savedCouch.getId());

        //then
        assertThat(savedCouch.getId()).isEqualTo(couchToCheck.getId());
        assertThat(externalCouch.getLastName()).isEqualTo(couchToCheck.getLastName());

    }

    @Test
    public void shouldUpdateEmployee() {
        //given
        ExternalCouchTO externalCouch = testTO.createFirstExternalCouch();

        ExternalCouchTO savedCouch = couchService.save(externalCouch);

        //when
        savedCouch.setCompanyName("Be positive");
        savedCouch.setLastName("Kowalski");

        ExternalCouchTO updatedCouch = couchService.update(savedCouch);
        ExternalCouchTO newVersionCouch = couchService.findExternalCouchById(savedCouch.getId());
        //then
        assertThat(savedCouch.getId()).isEqualTo(updatedCouch.getId());
        assertThat(savedCouch.getVersion()).isNotEqualTo(newVersionCouch.getVersion());
    }

    @Test
    public void shouldGetAllEmployees() {
        //given
        List<ExternalCouchTO> couchesList = couchService.getAllExternalCouches();

        //when
        ExternalCouchTO externalCouch = testTO.createFirstExternalCouch();
        couchService.save(externalCouch);

        List<ExternalCouchTO> listToCheck = couchService.getAllExternalCouches();

        //then
        assertThat(listToCheck.size()).isEqualTo(couchesList.size()+1);
    }

    @Test
    public void shouldDeleteEmployee() {
        //given
        ExternalCouchTO externalCouch = testTO.createFirstExternalCouch();
        ExternalCouchTO savedCouch = couchService.save(externalCouch);

        List<ExternalCouchTO> couchesList = couchService.getAllExternalCouches();

        //when
        couchService.deleteExternalCouch(savedCouch);
        ExternalCouchTO CouchToCheck = couchService.findExternalCouchById(savedCouch.getId());

        List<ExternalCouchTO> listToCheck = couchService.getAllExternalCouches();

        //then
        assertThat(CouchToCheck).isNull();
        assertThat(couchesList.size()).isNotEqualTo(listToCheck);

    }
}
