--input
CREATE TRIGGER ins_transaction BEFORE INSERT ON account
FOR EACH ROW PRECEDES ins_sum
SET
  @deposits = @deposits + IF(NEW.amount>0,NEW.amount,0),
  @withdrawals = @withdrawals + IF(NEW.amount<0,-NEW.amount,0);
--output
CREATED ins_transaction:TRIGGER
NEEDED ins_sum:TRIGGER
NEEDED account:TABLE_LIKE