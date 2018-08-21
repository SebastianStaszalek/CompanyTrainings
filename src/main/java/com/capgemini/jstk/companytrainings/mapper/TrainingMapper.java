package com.capgemini.jstk.companytrainings.mapper;

import com.capgemini.jstk.companytrainings.domain.TrainingEntity;
import com.capgemini.jstk.companytrainings.dto.TrainingTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingMapper {


    public TrainingTO map(TrainingEntity trainingEntity) {
        if(trainingEntity != null) {
            return TrainingTO.builder()
                    .id(trainingEntity.getId())
                    .title(trainingEntity.getTitle())
                    .trainingType(trainingEntity.getTrainingType())
                    .duration(trainingEntity.getDuration())
                    .startDate(trainingEntity.getStartDate())
                    .endDate(trainingEntity.getEndDate())
                    .costPerStudent(trainingEntity.getCostPerStudent())
                    .tags(trainingEntity.getTags())
                    .build();
        }
        return null;
    }

    public TrainingEntity map(TrainingTO trainingTO) {
        if(trainingTO != null) {
            return TrainingEntity.builder()
                    .id(trainingTO.getId())
                    .title(trainingTO.getTitle())
                    .trainingType(trainingTO.getTrainingType())
                    .duration(trainingTO.getDuration())
                    .startDate(trainingTO.getStartDate())
                    .endDate(trainingTO.getEndDate())
                    .costPerStudent(trainingTO.getCostPerStudent())
                    .tags(trainingTO.getTags())
                    .build();
        }
        return null;
    }

    public List<TrainingTO> map2TO(List<TrainingEntity> trainingEntities) {
        return trainingEntities.stream().map(this::map).collect(Collectors.toList());
    }
}
