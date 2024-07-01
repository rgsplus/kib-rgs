package nl.rgsnl.kib.kib_rgs.service.impl;

import nl.rgsnl.kib.model.list.InspectionListCode;
import nl.rgsnl.kib.model.list.InspectionListCodeStatus;
import nl.rgsnl.kib.service.InspectionListCodeService;
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
