package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.service.InspectionListCodeService;
import nl.rgs.kib.model.list.InspectionListCodeStatus;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InspectionListCodeServiceImpl implements InspectionListCodeService {

    private final ArrayList<InspectionListCode> items = new ArrayList<>(
            List.of(
                    new InspectionListCode(
                            new ObjectId(),
                            "name",
                            InspectionListCodeStatus.CONCEPT,
                            new ArrayList<>()
                    )
            )
    );

    @Override
    public List<InspectionListCode> findAll() {
        return items;
    }


}
