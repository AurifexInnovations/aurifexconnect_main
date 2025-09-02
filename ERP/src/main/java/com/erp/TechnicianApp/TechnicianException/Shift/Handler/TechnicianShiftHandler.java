package com.erp.TechnicianApp.TechnicianException.Shift.Handler;

import com.erp.TechnicianApp.TechnicianException.Shift.NoActiveShiftException;
import com.erp.TechnicianApp.TechnicianException.Shift.ShiftAlreadyActiveException;
import com.erp.TechnicianApp.TechnicianException.Shift.TechnicianNotFoundException;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.SimpleErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TechnicianShiftHandler {

    @ExceptionHandler(TechnicianNotFoundException.class)
    public ResponseEntity<SimpleErrorResponse> handleTechnicianNotFound(TechnicianNotFoundException e) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NoActiveShiftException.class)
    public ResponseEntity<SimpleErrorResponse> handleNoActiveShift(NoActiveShiftException e) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ShiftAlreadyActiveException.class)
    public ResponseEntity<SimpleErrorResponse> handleShiftAlreadyActive(ShiftAlreadyActiveException e) {
        return ResponseBuilder.error(HttpStatus.CONFLICT, e.getMessage());
    }
}
