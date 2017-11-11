package org.brownmun.web.admin.committee;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.brownmun.model.Attendance;
import org.brownmun.model.committee.AwardInfo;
import org.brownmun.model.repo.AttendanceInfo;
import org.brownmun.util.Formatting;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class AttendanceExportView extends AbstractXlsxView
{
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Sheet sheet = workbook.createSheet("Attendance");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Position");
        header.createCell(1).setCellValue("Committee");
        header.createCell(2).setCellValue("School");
        header.createCell(3).setCellValue("Delegate");
        header.createCell(4).setCellValue("Position Paper");
        header.createCell(5).setCellValue("Session I");
        header.createCell(6).setCellValue("Session II");
        header.createCell(7).setCellValue("Session III");
        header.createCell(8).setCellValue("Session IV");

        List<AttendanceInfo> attendance = (List<AttendanceInfo>) model.get("attendance");

        int i = 1;
        for (AttendanceInfo a : attendance)
        {
            Row row = sheet.createRow(i++);
            row.createCell(0).setCellValue(a.getPositionName());
            row.createCell(1).setCellValue(a.getCommitteeName());
            row.createCell(2).setCellValue(a.getSchoolName());
            row.createCell(3).setCellValue(a.getDelegateName());
            row.createCell(4).setCellValue(Formatting.yesNo(a.getAttendance().isPositionPaperSubmitted()));
            for (int session = 1; session <= Attendance.MAX_SESSION; session++)
            {
                row.createCell(4 + session).setCellValue(Formatting.yesNo(a.getAttendance().isPresentForSession(session)));
            }
        }
    }
}
