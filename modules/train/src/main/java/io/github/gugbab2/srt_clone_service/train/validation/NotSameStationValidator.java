package io.github.gugbab2.srt_clone_service.train.validation;

import io.github.gugbab2.srt_clone_service.train.dto.TrainSearchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotSameStationValidator implements ConstraintValidator<NotSameStation, TrainSearchRequest> {

    @Override
    public boolean isValid(TrainSearchRequest value, ConstraintValidatorContext context) {
        if (value == null || value.departureStation() == null || value.arrivalStation() == null) {
            return true;
        }
        return !value.departureStation().equals(value.arrivalStation());
    }
}
