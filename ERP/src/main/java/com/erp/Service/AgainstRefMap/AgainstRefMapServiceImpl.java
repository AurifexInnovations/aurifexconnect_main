package com.erp.Service.AgainstRefMap;

import com.erp.Enum.ReferenceType;
import com.erp.Model.AgainstRefMap;
import com.erp.Model.Ledger;
import com.erp.Model.Master;
import com.erp.Repository.AgainstRefMap.AgainstRefMapRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgainstRefMapServiceImpl implements AgainstRefMapService{

    private final AgainstRefMapRepository againstRefMapRepository;

    @Override
    public void againRefMap(Master master, Ledger ledger, ReferenceType referenceType, double amount) {

        AgainstRefMap againstRefMap = new AgainstRefMap();
        againstRefMap.setAmount(amount);
        againstRefMap.setReferenceType(referenceType);
        againstRefMap.setMaster(master);
        againstRefMap.setLedger(ledger);

        againstRefMapRepository.save(againstRefMap);
    }
}
