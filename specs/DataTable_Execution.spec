DataTables Execution
====================

* In an empty directory initialize a "java" project

Spec Execution with DataTable
-----------------------------

* Create spec "Spec Execution with DataTable" with the following dataTable
 | name  | category |
 |-------|----------|
 | apple |  fruit   |
 |tomato |vegetable |

* Create step "simple step one <name> <category>" in scenario "DataTable" in spec "Spec Execution with DataTable"
* Add implementation "System.out.println(\"inside simple step one with params : \"+param0+\" \"+param1);" to step "simple step one <param0> <param1>"
* Create step "simple step two <name>" in scenario "DataTables" in spec "Spec Execution with DataTable"
* Add implementation "System.out.println(\"inside simple step two with params : \"+param0);" to step "simple step two <param0>"
* Execute the spec "Spec Execution with DataTable" and ensure success
* Console should contain following lines in order
        |                console output                       |
        |-----------------------------------------------------|
        |inside simple step one with params : apple fruit     |
        |inside simple step two with params : apple           |
        |inside simple step one with params : tomato vegetable|
        |inside simple step two with params : tomato          |







