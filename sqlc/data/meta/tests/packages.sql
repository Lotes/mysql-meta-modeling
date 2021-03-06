#import "../src/*"
#import "$helpers/*"

CALL assert_is_false(is_package(1234567890), 'Random number is not a package.');

SET @package_id = create_package('test', -1);
CALL assert_is_true(is_package(@package_id), 'Newly created package must be a package.');

SET @new_child1_id = create_package('child1', @package_id);
SET @new_child2_id = create_package('child2', @package_id);
SET @new_child2_2_id = create_package('child2', @package_id);

CALL assert_is_true(is_package(@new_child1_id), 'Child 1 must exist.');
CALL assert_is_true(is_package(@new_child2_id), 'Child 2 must exist.');
CALL assert_is_false(is_package(@new_child2_2_id), 'Child 3 must not exist.');