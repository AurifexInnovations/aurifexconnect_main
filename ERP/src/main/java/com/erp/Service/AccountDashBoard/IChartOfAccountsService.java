package com.erp.Service.AccountDashBoard;

import com.erp.Dto.ChartOfAccountsDTO;

public interface IChartOfAccountsService {
    ChartOfAccountsDTO getById(Integer id);
    ChartOfAccountsDTO create(ChartOfAccountsDTO dto);
    ChartOfAccountsDTO update(Integer id, ChartOfAccountsDTO dto);
    void delete(Integer id);
}