--input
CREATE DEFINER=lotes EVENT myevent
    ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 1 HOUR
    DO
      UPDATE mytable SET mycol = mycol + 1;
--output
IMPORTS lotes:USER
EXPORTS myevent:EVENT
IMPORTS mytable:TABLE_LIKE