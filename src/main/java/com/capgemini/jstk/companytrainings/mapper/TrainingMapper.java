package com.capgemini.jstk.companytrainings.mapper;

import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TrainingMapper {


    public TrainingTO map(TrainingEntity trainingEntity) {
        if(trainingEntity != null) {
            TrainingTO trainingTO = new TrainingTO();

            trainingTO.setId(trainingEntity.getId());
            trainingTO.setVersion(trainingEntity.getVersion());
            trainingTO.setTitle(trainingEntity.getTitle());
            trainingTO.setTrainingType(trainingEntity.getTrainingType());
            trainingTO.setDuration(trainingEntity.getDuration());
            trainingTO.setTrainingCharacter(trainingEntity.getTrainingCharacter());
            trainingTO.setStartDate(trainingEntity.getStartDate());
            trainingTO.setEndDate(trainingEntity.getEndDate());
            trainingTO.setCostPerStudent(trainingEntity.getCostPerStudent());
            trainingTO.setStatus(trainingEntity.getStatus());
            trainingTO.setTags(trainingEntity.getTags());

            return trainingTO;
        }
        return null;
    }

    public TrainingEntity map(TrainingTO trainingTO) {
        return map(trainingTO, new TrainingEntity());
    }

    public TrainingEntity map(TrainingTO trainingTO, TrainingEntity trainingEntity) {
        if(trainingTO != null) {
            trainingEntity.setId(trainingTO.getId());
            trainingEntity.setTitle(trainingTO.getTitle());
            trainingEntity.setTrainingType(trainingTO.getTrainingType());
            trainingEntity.setDuration(trainingTO.getDuration());
            trainingEntity.setTrainingCharacter(trainingTO.getTrainingCharacter());
            trainingEntity.setStartDate(trainingTO.getStartDate());
            trainingEntity.setEndDate(trainingTO.getEndDate());
            trainingEntity.setCostPerStudent(trainingTO.getCostPerStudent());
            trainingEntity.setStatus(trainingTO.getStatus());
            trainingEntity.setTags(trainingTO.getTags());

            return trainingEntity;
        }
        return null;
    }

    public List<TrainingTO> map2TO(List<TrainingEntity> trainingEntities) {
        return trainingEntities.stream().map(this::map).collect(Collectors.toList());
    }

    public Set<TrainingTO> map2TOSet(Set<TrainingEntity> trainingEntities) {
        return trainingEntities.stream().map(this::map).collect(Collectors.toSet());
    }

}
