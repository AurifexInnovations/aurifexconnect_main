package com.erp.Service.AgainstRefMap;

import com.erp.Enum.ReferenceType;
import com.erp.Model.Ledger;
import com.erp.Model.Master;

public interface AgainstRefMapService {

    void againRefMap(Master master , Ledger ledger, ReferenceType referenceType,double amount);
}
