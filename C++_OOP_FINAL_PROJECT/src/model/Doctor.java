package model;

import java.sql.Date;

public class Doctor {
    private Long doctorId;
    private String name;
    private String identifier;
    private String status;
    private String location;
    private String contact;
    private Date assignedSince;
    public String role;

    // Getters and setters
    public Long getDoctorId() { return doctorId; }
    public String getRole() { return role; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public Date getAssignedSince() { return assignedSince; }
    public void setAssignedSince(Date assignedSince) { this.assignedSince = assignedSince; }
}
