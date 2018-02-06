package org.brownmun.web.admin.awards;

import org.apache.poi.ss.usermodel.*;
import org.brownmun.model.committee.AwardInfo;
import org.brownmun.model.committee.Committee;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class AwardsExportView extends AbstractXlsxView
{
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Sheet sheet = workbook.createSheet("Awards");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Committee ID");
        header.createCell(1).setCellValue("Committee Name");
        header.createCell(2).setCellValue("Award Type");
        header.createCell(3).setCellValue("Position ID");
        header.createCell(4).setCellValue("Position Name");
        header.createCell(5).setCellValue("Delegate Name");
        header.createCell(6).setCellValue("School ID");
        header.createCell(7).setCellValue("School Name");

        List<AwardInfo> awards = (List<AwardInfo>) model.get("awards");

        int i = 1;
        for (AwardInfo award : awards)
        {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(award.getCommitteeId());
            row.createCell(1).setCellValue(award.getCommitteeName());
            row.createCell(2).setCellValue(award.getAwardType().getDisplayName());
            row.createCell(3).setCellValue(award.getPositionId());
            row.createCell(4).setCellValue(award.getPositionName());
            row.createCell(5).setCellValue(award.getDelegateName());
            row.createCell(6).setCellValue(award.getSchoolId());
            row.createCell(7).setCellValue(award.getSchoolName());
        }
    }
}
