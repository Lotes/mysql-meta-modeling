--input
CREATE TRIGGER ins_transaction BEFORE INSERT ON account
FOR EACH ROW PRECEDES ins_sum
SET
  @deposits = @deposits + IF(NEW.amount>0,NEW.amount,0),
  @withdrawals = @withdrawals + IF(NEW.amount<0,-NEW.amount,0);
--output
EXPORTS ins_transaction:TRIGGER
IMPORTS ins_sum:TRIGGER
IMPORTS account:TABLE_LIKE