INSERT INTO accounts (
    id,
    account_number,
    name,
    balance,
    create_at,
    last_update,
    customer_type,
    account_status,
    account_kind
)
VALUES (
    accounts_id_seq.NEXTVAL,
    '00000000000001',
    'Bank fee account',
    0,
    TRUNC(CURRENT_DATE),
    TRUNC(CURRENT_DATE),
    'LEGAL',
    'ACTIVE',
    'BANK'
);