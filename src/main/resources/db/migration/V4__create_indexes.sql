
CREATE INDEX ix_transactions_pending
    ON transaction_requests (status, id);

CREATE INDEX ix_transactions_source
    ON transaction_requests (source, status, complete_at, id);

CREATE INDEX ix_transactions_destination
    ON transaction_requests (destination, status, complete_at, id);

CREATE INDEX ix_account_history_account
    ON account_history (account_id);