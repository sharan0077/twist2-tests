Spec execution
===============

* In an empty directory initialize a "java" project

Basic spec execution
--------------------

* Create "Sample scenario" in "Basic spec execution" with the following steps
    |    step text        |         implementation                          |
    |---------------------|-------------------------------------------------|
    |      First step     |System.out.println("inside first step"); |
    |     Second step     |System.out.println("inside second step");|
    |     Third step      |System.out.println("inside third step"); |


* Execute the current project and ensure success
* Console should contain "inside first step"
* Console should contain "inside second step"
* Console should contain "inside third step"

Concept execution
-----------------

* Create concept "Sample concept" with following steps
    |         step text                |          implementation                                                  |
    |----------------------------------|--------------------------------------------------------------------------|
    |Concept step one                  |System.out.println("inside first concept step");                          |
    |Concept step two                  |System.out.println("inside second concept step");                         |
    |Concept step three "three"        |System.out.println("inside concept step with param : " + param0);         |

* Create "Scenario for concept execution" in "Concept execution" with the following steps
    |     step text     |         implementation                          |
    |-------------------|-------------------------------------------------|
    |Sample concept     |                                                 |
    |Fourth step        |System.out.println("inside fourth step");        |

* Execute the spec "Concept execution" and ensure success
* Console should contain following lines in order
        |      console output               |
        |-----------------------------------|
        |inside first concept step          |
        |inside second concept step         |
        |inside fourth step                 |

Steps with parameters
---------------------

* Create "Parametrized steps" in "Steps with parameters" with the following steps
|          step text          |            implementation                                                                            |
|-----------------------------|------------------------------------------------------------------------------------------------------|
|Step one "first"             |System.out.println("inside first step with parameter : " + param0);                                   |
|Step two "second" "2"        |System.out.println("inside second step with different parameters : " + param0 + " " + param1);        |

* Execute the spec "Steps with parameters" and ensure success
* Console should contain following lines in order
        |                    console output                               |
        |-----------------------------------------------------------------|
        |inside first step with parameter : first                         |
        |inside second step with different parameters : second 2          |

Steps with table as parameters
------------------------------

* Create step "step with inline table" in scenario "table as parameters" in spec "Steps with table as parameters" with inline table
|    id          |       value       |
|----------------|-------------------|
|  id1           |         1         |
|  id2           |         2         |

* Add implementation "for(int i = 0; i < 2 ;i++){ System.out.println(\"inside step with params : \" + table.getRows().get(i).get(0)+\" \"+table.getRows().get(i).get(1));}" to step "step with inline table <table>" with inline table
* Execute the spec "Steps with table as parameters" and ensure success
* Console should contain following lines in order
        |                    console output                                |
        |------------------------------------------------------------------|
        |inside step with params : id1 1                                   |
        |inside step with params : id2 2                                   |



