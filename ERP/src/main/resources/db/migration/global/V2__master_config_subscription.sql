
CREATE TABLE IF NOT EXISTS public.subscription (
    subscriptionid BIGSERIAL PRIMARY KEY,
    userid VARCHAR(255) NOT NULL,
    accountuser VARCHAR(255),
    subscriptionplan VARCHAR(100) NOT NULL,
    planperiod VARCHAR(50),
    planstartdate DATE,
    planenddate DATE,
    branchcode VARCHAR(50),
    companycode VARCHAR(50),
    paymentstatus VARCHAR(20),
    paymentid VARCHAR(255),
    activeYn CHAR(1)
);


-- Drop table if exists
DROP TABLE IF EXISTS public.masterconfig;

-- Create table
CREATE TABLE IF NOT EXISTS public.masterconfig
(
    masterconfigid BIGINT NOT NULL,
    servicename TEXT NOT NULL,
    jsondata TEXT,
    userid TEXT,
    activeyn CHARACTER(1) DEFAULT NULL,
    createdon TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedon TIMESTAMP,
    CONSTRAINT masterconfig_pkey PRIMARY KEY (masterconfigid),
    CONSTRAINT masterconfig_servicename_key UNIQUE (servicename)
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.masterconfig
    OWNER TO postgres;

-- Insert sample data
INSERT INTO public.masterconfig (
    masterconfigid, servicename, jsondata, userid, activeyn, createdon
)
VALUES
(
    1001,
    'UserProfileService',
    '[{"plan-period": "1 year", "price": 5000}, {"plan-period": "1 mon", "price": 500}, {"plan-period": "3mon", "price": 1500}]',
    'admin_user_01',
    'Y',
    '2025-09-15 22:12:37.261931'
),
(
    1002,
    'Plans',
    '{
      "Branch": {
        "amount": 25000,
        "frequency": "year"
      },
      "Technician": {
        "amount": 2000,
        "frequency": "month"
      },
      "Extra Billing Person": {
        "amount": 10000,
        "frequency": "year"
      },
      "Extra Account User": {
        "amount": 12000,
        "frequency": "year"
      }
    }',
    '10101',
    'Y',
    '2025-10-10 21:09:05.881273'
),
(
    1003,
    'Prod-Payment',
    '{"key_id": "rzp_test_RLoNqgebrcSP59", "key_secret": "IysuohQEOtCMmlpdtmQ8B96x"}',
    'admin_user_01',
    'Y',
    '2025-10-19 10:44:04.353288'
);