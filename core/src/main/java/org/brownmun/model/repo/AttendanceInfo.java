package org.brownmun.model.repo;

import org.brownmun.model.Attendance;

public interface AttendanceInfo
{
    Attendance getAttendance();
    String getPositionName();
    String getCommitteeName();
    String getDelegateName();
    String getSchoolName();
}
