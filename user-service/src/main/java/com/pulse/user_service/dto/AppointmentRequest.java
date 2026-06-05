package com.pulse.user_service.dto;

public class AppointmentRequest {
    private Long patientId;
    private Long doctorId;
    private String date;
    private String time;
    private String type;
    private String reason;

    public Long getPatientId() { return patientId; }
    public Long getDoctorId()  { return doctorId; }
    public String getDate()    { return date; }
    public String getTime()    { return time; }
    public String getType()    { return type; }
    public String getReason()  { return reason; }

    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setDoctorId(Long doctorId)   { this.doctorId = doctorId; }
    public void setDate(String date)         { this.date = date; }
    public void setTime(String time)         { this.time = time; }
    public void setType(String type)         { this.type = type; }
    public void setReason(String reason)     { this.reason = reason; }
}