#import "packages"
#import "../src/classes/function_create_class"
#import "../src/classes/function_is_class"
#import "../src/classes/function_class_add_super_type"

CALL assert_is_false(is_class(1234567890), 'Random number is not a class.');

SET @creature_id = create_class('Creature', 'CLASS', @package_id);
CALL assert_is_true(is_class(@creature_id), 'Newly created class must be a class.');


/* Simple inheritance test case:
 * 
 *         Creature
 *          /    \
 *       Human  Animal
 *       /   \      \
 *      /     \      \
 *  Student Employee  Bird
 */

SET @human_id = create_class('Human', 'CLASS', @package_id);
SET @animal_id = create_class('Animal', 'CLASS', @package_id);
SET @student_id = create_class('Student', 'CLASS', @package_id);
SET @employee_id = create_class('Employee', 'CLASS', @package_id);
SET @bird_id = create_class('Bird', 'CLASS', @package_id);

CALL assert_is_true(class_add_super_type(@human_id, @creature_id), "Human must be a sub class of Creature.");
CALL assert_is_true(class_add_super_type(@bird_id, @animal_id), "Bird must be a sub class of Animal.");
CALL assert_is_true(class_add_super_type(@student_id, @human_id), "Student must be a sub class of Human.");
CALL assert_is_true(class_add_super_type(@employee_id, @human_id), "Employee must be a sub class of Human.");
CALL assert_is_true(class_add_super_type(@animal_id, @creature_id), "Animal must be a sub class of Creature.");

