package com.capgemini.jstk.companytrainings.mapper;

import com.capgemini.jstk.companytrainings.domain.ExternalCouchEntity;
import com.capgemini.jstk.companytrainings.dto.ExternalCouchTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExternalCouchMapper {

    public ExternalCouchTO map(ExternalCouchEntity externalCouchEntity) {
        if(externalCouchEntity != null) {
            ExternalCouchTO externalCouchTO = new ExternalCouchTO();

            externalCouchTO.setId(externalCouchEntity.getId());
            externalCouchTO.setVersion(externalCouchEntity.getVersion());
            externalCouchTO.setFirstName(externalCouchEntity.getFirstName());
            externalCouchTO.setLastName(externalCouchEntity.getLastName());
            externalCouchTO.setCompanyName(externalCouchEntity.getCompanyName());

            return externalCouchTO;
        }
        return null;
    }

    public ExternalCouchEntity map(ExternalCouchTO externalCouchTO) {
        return map(externalCouchTO, new ExternalCouchEntity());
    }

    public ExternalCouchEntity map(ExternalCouchTO externalCouchTO, ExternalCouchEntity externalCouchEntity ) {
        if(externalCouchTO != null) {
            externalCouchEntity.setId(externalCouchTO.getId());
            externalCouchEntity.setFirstName(externalCouchTO.getFirstName());
            externalCouchEntity.setLastName(externalCouchTO.getLastName());
            externalCouchEntity.setCompanyName(externalCouchTO.getCompanyName());

            return externalCouchEntity;
        }
        return null;
    }

    public List<ExternalCouchTO> map2TO(List<ExternalCouchEntity> externalCouchEntities) {
        return externalCouchEntities.stream().map(this::map).collect(Collectors.toList());
    }

    public Set<ExternalCouchTO> map2TOSet(Set<ExternalCouchEntity> externalCouchEntities) {
        return externalCouchEntities.stream().map(this::map).collect(Collectors.toSet());
    }
}
