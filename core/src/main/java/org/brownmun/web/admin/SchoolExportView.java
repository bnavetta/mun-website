package org.brownmun.web.admin;

import org.apache.poi.ss.usermodel.*;
import org.brownmun.model.School;
import org.brownmun.model.delegation.SchoolInfo;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SchoolExportView extends AbstractXlsxView {
    static int ID_COL = 0;
    static int NAME_COL = 1;
    static int ADDRESS_COL = 2;
    static int PHONE_NUMBER_COL = 3;
    static int APPLYING_FOR_AID_COL = 4;
    static int AID_AMOUNT_COL = 5;
    static int AID_DOCUMENTATION_COL = 6;
    static int BUSUN_HOTEL_COL = 7;
    static int HOTEL_NAME_COL = 8;
    static int SHUTTLE_COL = 9;
    static int SHUTTLE_FRIDAY_AFTERNOON_COL = 10;
    static int SHUTTLE_FRIDAY_NIGHT_COL = 11;
    static int SHUTTLE_SATURDAY_COL = 12;
    static int SHUTTLE_SUNDAY_COL = 13;
    static int COMMUTING_COL = 14;
    static int CAR_COUNT_COL = 15;
    static int CAR_DAYS_COL = 16;
    static int BUS_COUNT_COL = 17;
    static int BUS_DAYS_COL = 18;
    static int ARRIVAL_TIME_COL = 19;
    static int LUGGAGE_STORAGE_COL = 20;
    static int BAND_COLOR_COL = 21;
    static int REQUESTED_DELEGATES_COL = 22;
    static int REQUESTED_CHAPERONES_COL = 23;
    static int PARLIAMENTARY_PROCEDURE_TRAINING_COL = 24;
    static int CRISIS_TRAINING_COL = 25;
    static int TOUR_COL = 26;
    static int INFO_SESSION_COL = 27;
    static int CHAPERONE_INFO_COL = 28;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Sheet sheet = workbook.createSheet("Schools");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom(BorderStyle.THICK);
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 16);
        headerFont.setFontName("Helvetica");
        headerStyle.setFont(headerFont);

        Row header = sheet.createRow(0);
        header.setRowStyle(headerStyle);
        header.createCell(ID_COL).setCellValue("School ID");
        header.createCell(NAME_COL).setCellValue("School Name");
        header.createCell(ADDRESS_COL).setCellValue("Address");
        header.createCell(PHONE_NUMBER_COL).setCellValue("Phone Number");
        header.createCell(APPLYING_FOR_AID_COL).setCellValue("Applying for Aid");
        header.createCell(AID_AMOUNT_COL).setCellValue("Aid Amount");
        header.createCell(AID_DOCUMENTATION_COL).setCellValue("Aid Documentation");
        header.createCell(BUSUN_HOTEL_COL).setCellValue("Using BUSUN Hotel");
        header.createCell(HOTEL_NAME_COL).setCellValue("Hotel Name");
        header.createCell(SHUTTLE_COL).setCellValue("Using Shuttle");
        header.createCell(SHUTTLE_FRIDAY_AFTERNOON_COL).setCellValue("Friday Afternoon Shuttle");
        header.createCell(SHUTTLE_FRIDAY_NIGHT_COL).setCellValue("Friday Night Shuttle");
        header.createCell(SHUTTLE_SATURDAY_COL).setCellValue("Saturday Shuttle");
        header.createCell(SHUTTLE_SUNDAY_COL).setCellValue("Sunday Shuttle");
        header.createCell(COMMUTING_COL).setCellValue("Commuting");
        header.createCell(CAR_COUNT_COL).setCellValue("Car Pass Count");
        header.createCell(CAR_DAYS_COL).setCellValue("Days of Car Passes");
        header.createCell(BUS_COUNT_COL).setCellValue("Bus Pass Count");
        header.createCell(BUS_DAYS_COL).setCellValue("Days of Bus Passes");
        header.createCell(ARRIVAL_TIME_COL).setCellValue("Arrival Time");
        header.createCell(LUGGAGE_STORAGE_COL).setCellValue("Luggage Storage");
        header.createCell(BAND_COLOR_COL).setCellValue("Band Color");
        header.createCell(REQUESTED_DELEGATES_COL).setCellValue("Requested Delegates");
        header.createCell(REQUESTED_CHAPERONES_COL).setCellValue("Requested Chaperones");
        header.createCell(PARLIAMENTARY_PROCEDURE_TRAINING_COL).setCellValue("Parliamentary Procedure Training Count");
        header.createCell(CRISIS_TRAINING_COL).setCellValue("Crisis Training Count");
        header.createCell(TOUR_COL).setCellValue("Tour Count");
        header.createCell(INFO_SESSION_COL).setCellValue("Info Session Count");
        header.createCell(CHAPERONE_INFO_COL).setCellValue("Chaperone Info");

        List<School> schools = (List<School>) model.get("schools");
        int rowCount = 1;
        for (School school : schools) {
            SchoolInfo info = school.getInfo();
            Row row = sheet.createRow(rowCount++);
            row.createCell(ID_COL).setCellValue(school.getId());
            row.createCell(NAME_COL).setCellValue(school.getName());
            row.createCell(ADDRESS_COL).setCellValue(Objects.toString(info.getAddress(), "<none>"));
            row.createCell(PHONE_NUMBER_COL).setCellValue(info.getPhoneNumber());
            row.createCell(APPLYING_FOR_AID_COL).setCellValue(info.isApplyingForAid());
            row.createCell(AID_AMOUNT_COL).setCellValue(info.getAidAmount());
            row.createCell(AID_DOCUMENTATION_COL).setCellValue(info.getAidDocumentation());
            row.createCell(BUSUN_HOTEL_COL).setCellValue(info.isUsingBusunHotel());
            row.createCell(HOTEL_NAME_COL).setCellValue(info.isUsingBusunHotel() ? info.getHotel().getName() : info.getOtherHotel());
            row.createCell(SHUTTLE_COL).setCellValue(info.getUsingShuttle());
            row.createCell(SHUTTLE_FRIDAY_AFTERNOON_COL).setCellValue(info.getShuttleService().isFridayAfternoon());
            row.createCell(SHUTTLE_FRIDAY_NIGHT_COL).setCellValue(info.getShuttleService().isFridayNight());
            row.createCell(SHUTTLE_SATURDAY_COL).setCellValue(info.getShuttleService().isSaturday());
            row.createCell(SHUTTLE_SUNDAY_COL).setCellValue(info.getShuttleService().isSunday());
            row.createCell(COMMUTING_COL).setCellValue(info.getCommuting());
            row.createCell(CAR_COUNT_COL).setCellValue(info.getCarCount());
            row.createCell(CAR_DAYS_COL).setCellValue(info.getCarDays());
            row.createCell(BUS_COUNT_COL).setCellValue(info.getBusCount());
            row.createCell(BUS_DAYS_COL).setCellValue(info.getBusDays());
            row.createCell(ARRIVAL_TIME_COL).setCellValue(info.getArrivalTime());
            row.createCell(LUGGAGE_STORAGE_COL).setCellValue(info.getLuggageStorage().toString());
            row.createCell(BAND_COLOR_COL).setCellValue(info.getBandColor().toString());
            row.createCell(REQUESTED_DELEGATES_COL).setCellValue(info.getRequestedDelegates());
            row.createCell(REQUESTED_CHAPERONES_COL).setCellValue(info.getRequestedChaperones());
            row.createCell(PARLIAMENTARY_PROCEDURE_TRAINING_COL).setCellValue(info.getParliTrainingCount());
            row.createCell(CRISIS_TRAINING_COL).setCellValue(info.getCrisisTrainingCount());
            row.createCell(TOUR_COL).setCellValue(info.getTourCount());
            row.createCell(INFO_SESSION_COL).setCellValue(info.getInfoSessionCount());
            row.createCell(CHAPERONE_INFO_COL).setCellValue(info.getChaperoneInfo());
        }

        Row summary = sheet.createRow(rowCount + 2);
        summary.createCell(0).setCellValue("Totals");

        summary.createCell(1).setCellValue(schools.size());

        summary.createCell(AID_AMOUNT_COL)
                .setCellValue(schools.stream().mapToDouble(s -> s.getInfo().getAidAmount()).sum());

        summary.createCell(SHUTTLE_COL)
                .setCellValue(schools.stream().filter(s -> s.getInfo().getUsingShuttle()).count());
        summary.createCell(SHUTTLE_FRIDAY_AFTERNOON_COL)
                .setCellValue(schools.stream().filter(s -> s.getInfo().getShuttleService().isFridayAfternoon()).count());
        summary.createCell(SHUTTLE_FRIDAY_NIGHT_COL)
                .setCellValue(schools.stream().filter(s -> s.getInfo().getShuttleService().isFridayNight()).count());
        summary.createCell(SHUTTLE_SATURDAY_COL)
                .setCellValue(schools.stream().filter(s -> s.getInfo().getShuttleService().isSaturday()).count());
        summary.createCell(SHUTTLE_SUNDAY_COL)
                .setCellValue(schools.stream().filter(s -> s.getInfo().getShuttleService().isSunday()).count());

        summary.createCell(COMMUTING_COL)
                .setCellValue(schools.stream().filter(s -> s.getInfo().getCommuting()).count());
        summary.createCell(CAR_COUNT_COL)
                .setCellValue(schools.stream().mapToInt(s -> s.getInfo().getCarCount()).sum());
        summary.createCell(BUS_COUNT_COL)
                .setCellValue(schools.stream().mapToInt(s -> s.getInfo().getBusCount()).sum());

        summary.createCell(REQUESTED_DELEGATES_COL)
                .setCellValue(schools.stream().mapToInt(s -> s.getInfo().getRequestedDelegates()).sum());
        summary.createCell(REQUESTED_CHAPERONES_COL)
                .setCellValue(schools.stream().mapToInt(s -> s.getInfo().getRequestedChaperones()).count());

        summary.createCell(PARLIAMENTARY_PROCEDURE_TRAINING_COL)
                .setCellValue(schools.stream().mapToInt(s -> s.getInfo().getParliTrainingCount()).sum());
        summary.createCell(CRISIS_TRAINING_COL)
                .setCellValue(schools.stream().mapToInt(s -> s.getInfo().getCrisisTrainingCount()).sum());
        summary.createCell(TOUR_COL)
                .setCellValue(schools.stream().mapToInt(s -> s.getInfo().getTourCount()).sum());
        summary.createCell(INFO_SESSION_COL)
                .setCellValue(schools.stream().mapToInt(s -> s.getInfo().getInfoSessionCount()).sum());
    }
}
