Use this file to commit information clearly documenting your check-ins' content. If you want to store more information/details besides what's required for the check-ins that's fine too. Make sure that your TA has had a chance to sign off on your check-in each week (before the deadline); typically you should discuss your material with them before finalizing it here.

# Checkin 1: File System Organization DSL
## Purpose
Our DSL will simplify the process of mass file manipulation for organization purposes. Our target users are people who struggle with disorganized files such as a cluttered Downloads folder or a large collection of unorganized photos–users that are looking for a quick and easy way to reorganize their files so they can better locate specific files in the future. Our DSL will enable users to quickly and easily restructure and modify their files by defining certain conditions and actions to take based on a file’s data.


## Rich Features
1. Easy directory restructuring using conditions and actions.

2. For-loops to iterate over a set of groups to reduce duplication.
Example: 

    ```
    FOREACH m in MONTHS:
        SUBFOLDER $m:
    ```

    This will allow for users to easily create a set of folders by iterating over a set of characteristics, either as a predefined set (MONTHS) or their own set ([“school”, “university”, “home”]). 

3. Predefined, reusable condition functions to reduce duplication in directory conditions.

    Example condition:
    ```
	CONDITION cool_photos(min_date): PHOTOS WITH $DATE > min_date AND $NAME
    ```
    Example usage: 
    ```
	cool folder:
        CONTAINS: cool_photos2020 OR Photos date > 2010
    ```

    This feature enables users to define their own conditions that can be reused throughout their entire folder restructuring procedure. Combined with the other features, this will allow users to simplify their code by reducing duplication.

## Minor Features
- Actions based on file metadata: Renaming, Deletion

- Logical operators: AND, OR, NOT; parenthesis for scoping

- Comparison operators: IS, IS ONE OF, >, <, =, <=, >=, MATCHES, CONTAINS

- Catch-all condition for files that meet none of the other conditions: OTHER

- (Stretch) Conflict resolution/priority: allow users to define behaviour for files that meet multiple conditions 

- (Stretch) Image metadata: allow users to have conditions using image metadata (location, device, etc.)

## TA Feedback
- Said the DSL was thought-through, sufficiently rich, and achievable in time and effort constraints of project
- Recommended Python as a base language
- Identified two implementation paths:
    1. Create intermediate Python script to run
    2. Write our own interpreter (closer to model given in lecture; likely easier in practice)
Did not identify any particular feature as intrinsically more complicated than the others; no natural place to reduce complexity

## Follow-up Tasks
- Investigate how our syntax might be altered to make the DSL more accessible. Some research questions to take into our task-driven user study:
- Can we widen our user group with syntax more similar to natural English language?
- Which accessibility/verbosity trade-offs might be low-hanging fruit, while maintaining some level of consistency within our procedure syntax?

## Full Example
```
CONDITION cool_photos(min_date): $TYPE IS png AND $DATE_YEAR > min_date AND $NAME CONTAINS "cool"

FOLDER1
    CONTAINS: ____
    HAS SUBFOLDERS:
        FOREACH category in ["school", "university", "home"]:
        SUBFOLDER $category:
            … (some rules)
            HAS SUBFOLDERS:
                SUBFOLDER $category-test	
        cool folder:
            CONTAINS: cool_photos(2020) OR $DATE > 20100101
            RENAME: cool_photos(2020) AS “cool_” + $DATE
            HAS SUBFOLDERS
                FOREACH m in MONTHS:
                SUBFOLDER $m:
                    …
        pdfs: 
            CONTAINS: $TYPE IS pdf
        subfolder3:
            CONTAINS: $TYPE (IS ONEOF pdf, png, jpg) AND ($DATE < 20230302)
        subfolder4
            CONTAINS: OTHER
```