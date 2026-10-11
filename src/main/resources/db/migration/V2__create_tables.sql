CREATE TABLE accounts (
    id              NUMBER(19, 0) NOT NULL,
    account_number  VARCHAR2(14 CHAR) NOT NULL,
    name            VARCHAR2(255 CHAR),
    national_id     VARCHAR2(255 CHAR),
    birth           DATE,
    phone_number    VARCHAR2(255 CHAR),
    address         VARCHAR2(255 CHAR),
    postal_code     VARCHAR2(255 CHAR),
    last_update     DATE,
    balance         NUMBER(19, 0) DEFAULT 0 NOT NULL,
    create_at       DATE NOT NULL,
    customer_type   VARCHAR2(255 CHAR) NOT NULL,
    account_status  VARCHAR2(255 CHAR) DEFAULT 'ACTIVE' NOT NULL,
    account_kind    VARCHAR2(255 CHAR) NOT NULL,

    CONSTRAINT pk_accounts
        PRIMARY KEY (id),

    CONSTRAINT uq_accounts_number
        UNIQUE (account_number),

    CONSTRAINT uq_accounts_national_id
        UNIQUE (national_id),

    CONSTRAINT ck_accounts_balance
        CHECK (balance >= 0)
);


CREATE TABLE account_history (
    id          NUMBER(19, 0) NOT NULL,
    field_name  VARCHAR2(255 CHAR),
    old_value   VARCHAR2(255 CHAR),
    new_value   VARCHAR2(255 CHAR),
    time        TIMESTAMP(6) WITH TIME ZONE,
    user_name   VARCHAR2(255 CHAR),
    account_id  NUMBER(19, 0) NOT NULL,

    CONSTRAINT pk_account_history
        PRIMARY KEY (id),

    CONSTRAINT fk_history_account
        FOREIGN KEY (account_id)
        REFERENCES accounts (id)
);


CREATE TABLE transaction_requests (
    id                  NUMBER(19, 0) NOT NULL,
    amount              NUMBER(19, 0) NOT NULL,
    source              VARCHAR2(255 CHAR),
    destination         VARCHAR2(255 CHAR),
    failure_reason      VARCHAR2(255 CHAR),
    transaction_number  NUMBER(19, 0),
    complete_at         TIMESTAMP(6) WITH TIME ZONE,
    status              VARCHAR2(255 CHAR) DEFAULT 'PENDING' NOT NULL,
    transaction_type    VARCHAR2(255 CHAR) NOT NULL,
    parent_id           NUMBER(19, 0),

    CONSTRAINT pk_transaction_requests
        PRIMARY KEY (id),

    CONSTRAINT uq_transaction_number
        UNIQUE (transaction_number),

    CONSTRAINT uq_transaction_parent
        UNIQUE (parent_id),

    CONSTRAINT fk_transaction_parent
        FOREIGN KEY (parent_id)
        REFERENCES transaction_requests (id),

    CONSTRAINT ck_transaction_amount
        CHECK (amount >= 0),

    CONSTRAINT ck_transaction_status
        CHECK (status IN ('PENDING', 'SUCCESSFUL', 'FAILURE')),

    CONSTRAINT ck_transaction_type
        CHECK (
            transaction_type IN ('DEPOSIT', 'WITHDRAW', 'TRANSFER', 'FEE')
        )
);