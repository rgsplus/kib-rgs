package nl.rgs.kib.shared.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListItemValue;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UniqueItemIdValidator implements ConstraintValidator<UniqueItemIds, List<InspectionListItem>> {
    @Override
    public boolean isValid(List<InspectionListItem> items, ConstraintValidatorContext context) {
        if (items == null) {
            return true;
        }

        List<InspectionListItemValue> inspectionListItems = items.stream().map(InspectionListItem::getValue).filter(Objects::nonNull).toList();
        Set<String> uniqueIds = new HashSet<>();
        for (InspectionListItemValue item : inspectionListItems) {
            if (!uniqueIds.add(item.getId())) {
                return false;
            }
        }
        return true;
    }
}