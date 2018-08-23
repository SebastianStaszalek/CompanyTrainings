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
            return TrainingTO.builder()
                    .id(trainingEntity.getId())
                    .version(trainingEntity.getVersion())
                    .title(trainingEntity.getTitle())
                    .trainingType(trainingEntity.getTrainingType())
                    .duration(trainingEntity.getDuration())
                    .trainingCharacter(trainingEntity.getTrainingCharacter())
                    .startDate(trainingEntity.getStartDate())
                    .endDate(trainingEntity.getEndDate())
                    .costPerStudent(trainingEntity.getCostPerStudent())
                    .tags(trainingEntity.getTags())
                    .build();
        }
        return null;
    }

    public TrainingEntity map(TrainingTO trainingTO) {
        return map(trainingTO, new TrainingEntity());
    }

    public TrainingEntity map(TrainingTO trainingTO, TrainingEntity trainingEntity) {
        if(trainingTO != null) {
            trainingEntity.setId(trainingTO.getId());
            //trainingEntity.setVersion(trainingTO.getVersion());
            trainingEntity.setTitle(trainingTO.getTitle());
            trainingEntity.setTrainingType(trainingTO.getTrainingType());
            trainingEntity.setDuration(trainingTO.getDuration());
            trainingEntity.setTrainingCharacter(trainingTO.getTrainingCharacter());
            trainingEntity.setStartDate(trainingTO.getStartDate());
            trainingEntity.setEndDate(trainingTO.getEndDate());
            trainingEntity.setCostPerStudent(trainingTO.getCostPerStudent());
            trainingEntity.setTags(trainingTO.getTags());

            return trainingEntity;
        }
        return null;
    }

    public List<TrainingTO> map2TO(List<TrainingEntity> trainingEntities) {
        return trainingEntities.stream().map(this::map).collect(Collectors.toList());
    }

}
