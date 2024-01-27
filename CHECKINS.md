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

# Check-In 2

## Component Division and Assignment

### Tokenizer/Parser: DSL Source -> AST output

Tasks:

- Parsing Conditionals
- Conditionals Functions
  - Basic syntax parsing, no checks for circular dependencies
    - Folder structure / subfolders
      - Tests

**Assigned**: Harry, Henry

### Interpreter: AST input, Folder on disk/path -> Search Tree, Folder on disk

Tasks:

- For-each interpreting
- Expanding conditional functions
  - checking for circular dependencies (stretch / static check)
- Tests

**Assigned**: Mazen, Louise

### Evaluator/Searcher: Search Tree, individual file -> String summary, location in the folder structure

- Tests

**Assigned**: Ronald

### AST Design/Search Tree Design

Assigned: All

### End-to-end Testing

Assigned: TBD (depending on workload)

### Video

Assigned: TBD (depending on workload)

### Project Coordination

Assigned: Mazen, Louise

### Check-in Writeups

Assigned: All (rotation + collaboration)

### Timeline

Weekly Team Check-Ins: Monday, 5:00 - 6:00pm (Hybrid, Location TBD)

- Monday, January 29th, 7:00pm - AST and Search Tree design (post-meeting)
- Wednesday, January 31st  - User Study 1
- Wednesday, February 14th   - Implementation Finished
- Wednesday, February 21st - User Study 2
- Saturday, February 24th - Video Finished

Check-ins involve reporting on progress, raising blocking issues, and coming up for solutions to stay on track. Team will collaboratively determine the best course of action in these meetings to meet deadlines. For features involving multiple people, designs, tasks, and division of workload will be negotiated between them and reported back to the overall team. The expectation for these 'sub-teams' as they regularly communicate and are responsible for each other.

### Project Notes

- Input pre-conditions:
  - Non-zip source directory (does not have to be flat)
- Expand loops as a macro before inputting into a search tree.
  - Do not expand conditions due to recursive properties

### Minutes: TA Check-In 2

- G: Is recursion supported? It feels natural for a file system (subfolder structure)
  - Could be a loop or recursive, and recursion could be better for reducing complexity
  - Ha: more declarative; just describing where things go rather than writing a long procedure
- G: Can users have two inputs or inputs of different types for the conditions?
  - Yes
- G: Are conditions macros?
  - Yes
- Clarification about what the folder structure actually represents (new structure with input files within, not moving files around within existing structure). G seemed to think the user was drawing up their current directory structure rather than a new one

- G: Can users define a variable outside a loop?
  - Ha: what would the use case be?
    - G: if we need fuller features, we might need to extend the use case
    - M: variables can be referenced in conditions
- G: he’ll add whatever feedback from instructors on the original proposal to an issue on GH

- G: add invariants (pre- and post-conditions) to check-in 2
  - DSL → AST
  - AST → interpreter/evaluator
  - What conditions do you want to check?
  - Can add no left recursion, no ambiguity, etc.
    - Think about constraints for file system input
- G: timeline can just be week-by-week, e.g., implementation done by week 5, E2E testing week 6

### Summary of progress

- Project divided into 3 main components of Tokenizer/Parser, Interpreter, and the Evaluator/Searcher with 2:2:1 members on each component respectively based on expected difficulty. Basic tasks defined for each module as for the expectations of what each component should accomplish.
- Group roadmap checkpoints established, main checkpoint is getting implementation finished February 14th to allow for second-round user study to be done and creating project video.
