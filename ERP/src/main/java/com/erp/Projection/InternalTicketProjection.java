package com.erp.Projection;

public interface InternalTicketProjection {

    Long getId();
    String getTitle();
    String getPriority();
    String getStatus();
    Long getAssignedTo();
    Long getCreatedBy();
    String getCreatedByName();
    String getAssignedToName();
    java.time.LocalDateTime getCreatedDate();
    java.time.LocalDateTime getLastUpdated();
}
