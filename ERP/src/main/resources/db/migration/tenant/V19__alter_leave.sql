ALTER TABLE leave_request
DROP CONSTRAINT leave_request_leave_type_check;

ALTER TABLE leave_request
ADD CONSTRAINT leave_request_leave_type_check
CHECK (leave_type IN ('SICK', 'CASUAL', 'ANNUAL'));
