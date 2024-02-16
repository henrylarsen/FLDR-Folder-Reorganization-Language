Use this file to commit information clearly documenting your check-ins' content. If you want to store more information/details besides what's required for the check-ins that's fine too. Make sure that your TA has had a chance to sign off on your check-in each week (before the deadline); typically you should discuss your material with them before finalizing it here.

# Checkin 1: File System Organization DSL

## Purpose

Our DSL will simplify the process of mass file manipulation for organization purposes. Our target users are people who struggle with disorganized files such as a cluttered Downloads folder or a large collection of unorganized photos–users that are looking for a quick and easy way to reorganize their files so they can better locate specific files in the future. Our DSL will enable users to quickly and easily restructure and modify their files by defining certain conditions and actions to take based on a file’s data.

## Rich Features

1.  Easy directory restructuring using conditions and actions.

2.  For-loops to iterate over a set of groups to reduce duplication.
    Example:

        ```
        FOREACH m in MONTHS:
            SUBFOLDER $m:
        ```

        This will allow for users to easily create a set of folders by iterating over a set of characteristics, either as a predefined set (MONTHS) or their own set ([“school”, “university”, “home”]).

3.  Predefined, reusable condition functions to reduce duplication in directory conditions.

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
- Identified two implementation paths: 1. Create intermediate Python script to run 2. Write our own interpreter (closer to model given in lecture; likely easier in practice)
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
- Wednesday, January 31st - User Study 1
- Wednesday, February 14th - Implementation Finished
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

# Check-in 3

## Language design and user study

### Example syntax and given tasks

<details>
<summary>
Input data
</summary>
This folder was provided to the user as input for the example syntax and tasks to complete.

### Downloads

| File name            | Date modified       | Type                   | Size      |
| -------------------- | ------------------- | ---------------------- | --------- |
| cats.jpg             | 2022-02-14 5:55 PM  | JPG File               | 200 KB    |
| info300-earpiece.jpg | 2023-06-15 11:14 PM | JPG File               | 245 KB    |
| activity_64.gpx      | 2023-08-22 10:34 AM | GPX File               | 315 KB    |
| morning_run(6).gpx   | 2023-11-23 9:04 AM  | GPX File               | 309 KB    |
| kettle.png           | 2024-01-23 6:35 PM  | PNG File               | 322 KB    |
| info300-syllabus.pdf | 2023-01-11 1:14 PM  | Adobe Acrobat Document | 131 KB    |
| colorful-sky.pdf     | 2023-12-31 6:25 PM  | Adobe Acrobat Document | 54,411 KB |
| phil230-syllabus.pdf | 2024-01-12 2:34 PM  | Adobe Acrobat Document | 143 KB    |

</details>
<details>
<summary>User study explanation</summary>

The following code can be used to sort a folder. There are three key components:

1. A RESTRUCTURE statement that indicates the file path of the folder to be restructured.
   _Note: this section was omitted in our study, but mentioned here to comprehensively cover our syntax._
2. A folder structure, starting with DOWNLOADS-SORTED in this case, that represents the desired state of the folder after it is restructured.
   This structure is composed of folders, optional subfolders, and the conditions that dictate what a given folder or subfolder will contain.
   - Conditions can be specified in the CONTAINS clause of a given folder and must be a statement that is true or false about a given file. If it is true, the file will be in the folder,
     provided it has not already been sorted into a different folder. If it is false, it will not be in the folder. Conditions can reference information (metadata) about a file, such as its name, date modified, size, or type.
     Any files that match the CONTAINS condition for the root folder will be loose in that folder. _Note: conditions for a parent folder do not necessarily need to be met by a file in one of its subfolders._
   - These folders can be created manually (like _gpxes_) or dynamically with a loop, like the _course_name_ section, using the FOREACH syntax. _Note: in our study, we explained loops as needed. We will not do that here._
   - Any folder may have any number of subfolders, and subfolders that are empty will still be created.
3. Any number of conditions, specified with the syntax starting with CONDITION below, that can then be used in your folder structure. Specifying conditions can help to simplify your code and prevent duplication.

   RESTRUCTURE "./Downloads"

   CONDITION is_during_year(year): $DATE_YEAR = year

   DOWNLOADS-SORTED:
   CONTAINS: $NAME INCLUDES “cat”
        HAS SUBFOLDERS:
            FOREACH course_name in [“info300”, “phil230”]:
                SUBFOLDER $course_name:
                    CONTAINS: $NAME INCLUDES $course_name
                gpxes:
                    CONTAINS: $TYPE IS gpx
                2024 photos:
                    CONTAINS: is_during_year(2024) AND ($TYPE IS ONEOF jpeg, png, jpg)
   other:
   CONTAINS: OTHER

This program will produce the following output:

    DOWNLOADS-SORTED
        - cats.jpg
        info300
            - info300-syllabus.pdf
            - info300-earpiece.jpg
        phil230
            - phil230-syllabus.pdf
        gpxes
            - activity_64.gpx
            - morning_run(6).gpx
        2024 photos
            - kettle.png
        other
            - colorful-sky.pdf

</details>

<details>
<summary>User study tasks
</summary>

1. Draw the output folder for the following program, with the Downloads folder as input.

   CONDITION is_during_year(year): $DATE_YEAR = year

   DOWNLOADS-SORTED-1
   CONTAINS: is_during_year(2020)
   HAS SUBFOLDERS:
   FOREACH file_type in [“png”, “pdf”, “jpg”]:
   SUBFOLDER 2024-$file_type:
   CONTAINS: is_during_year(2024) AND $TYPE is $file_type
   some-files:
   CONTAINS: $SIZE > 200 KB AND $SIZE < 300 KB
   other:
   CONTAINS: OTHER

Solution:

    DOWNLOADS-SORTED-1
        2024-png
            - kettle.png
        2024-pdf
            - phil230-syllabus.pdf
        2024-jpg
        some-files
            - info300-earpiece.jpg
        other
            - cats.jpg
            - activity_64.gpx
            - morning_run(6).gpx
            - info300-syllabus.pdf
            - colorful-sky.pdf

2.  Write a possible program that would produce the following folder.

    DOWNLOADS-SORTED-2 - kettle.png
    syllabi - info300-syllabus.pdf - phil230-syllabus.pdf
    gpxes - activity_64.gpx - morning_run(6).gpx
    december - colorful-sky.pdf
    other - info300-earpiece.jpg - cats.jpg

One possible solution:

    CONDITION name_includes(text): $NAME INCLUDES text

    DOWNLOADS-SORTED-2
        CONTAINS: name_includes("kettle")
        HAS SUBFOLDERS:
            syllabi:
                CONTAINS: name_includes("syllabus")
            gpxes:
                CONTAINS: $TYPE IS gpx
            december:
                CONTAINS: $DATE_MONTH = 12
            other:
                CONTAINS: OTHER

3. Write a possible program that would produce the following folder.

   DOWNLOADS-SORTED-3
   2022 - cats.jpg
   2023 - colorful-sky.pdf - info300-syllabus.pdf - info300-earpiece.jpg - activity_64.gpx - morning_run(6).gpx
   2024 - phil230-syllabus.pdf - kettle.png

One possible solution:

    CONDITION is_during_year(year): $DATE_YEAR = year

    DOWNLOADS-SORTED-3
        HAS SUBFOLDERS:
            FOREACH year in [2022, 2023, 2024]:
                SUBFOLDER $year:
                    CONTAINS: is_during_year(year)

</details>

### Notes and findings

We conducted our study with two users, one of whom had basic programming experience and the other with none. Our significant findings are as follows:

- Both users were able to complete all three tasks after our explanations and reference to the original examples. The first task was much quicker than the others, which required syntax generation and not prediction of behaviour.
- The user with programming experience chose to use conditions and FOREACH loops where possible. The user without programming experience did neither. The hard-coded names and file types were easier for them to understand than the dynamic subfolders.
- Both users were unsure about whether files would be sorted into the first folder with a condition they match or multiple folders. This should be explicit in our description of program behaviour.
- Both users were unsure about root directory-level CONTAINS clauses, in particular if those conditions would be propagated to its subdirectories. This should also be clearly specified to users.
- The user with programming experience was cautious about duplicating variable names between conditions and for-loops.

Some learnings for our final user study are as follows:

- Since the matching behaviour came into question, it would be good to have a case where files match multiple conditions to measure retention from our explanation of the language. This was avoided in our first study to reduce cognitive load for an already time-intensive (30-40 minutes) and complex series of tasks.
- The conditions required to produce the given directories were quite simple. In our final user study, we might want to involve more complex conditions using ANDs, ORs, and NOTs.
- It was interesting to note that the user with programming experience was eager to use all rich features (conditions and loops) while the user without experience hard-coded to avoid using these features. Our final study could involve subtasks--for example, write a condition that does x, or write a loop that sorts files by month--before the comprehensive tasks to investigate if confidence increases with subdivision of problems.

## Changes in language design

Based on our user studies and implementation work thus far, we have made the following changes:

- FOLDER specification before all folders, not only dynamic subfolders. This was inconsistent in our original syntax.
- Introduction of brackets for variables in template strings for disambiguation.
- Introduction of parentheses in boolean predicates for disambiguation.
- We did not include the RESTRUCTURE folder_name command in the user study, but that should have been included. The explanation and syntax example does include this, for completion's sake, but we did not test this syntax in this study. This should be a part of all future studies.

## Project timeline

We are on track with our proposed timeline from our last check-in, so we are not making any changes.

## Testing and error handling

Our snippets can be used as end-to-end tests. We have completed lexer and AST design, so these are ready for testing.
Our language is quite permissive, so we do not have many input invariants to verify. We only require that the inputs are non-archived folders that exist.

We are still determining which errors should be the responsibility of the parser and which the evaluator. Our strategy is to leverage our parser as much as possible to simply evaluator implementation and catch errors early.


# Check-in 4

## Status of Implementation

### Lexer/Parser
- Almost complete - working copy missing a few minor features.

### Evaluator and UI
- Good progress has been made.

### Tests
- Token and parser tests (ANTLR only) are passing.
- In need of evaluator and end-to-end tests.
- Could use more robust tokenizer tests.

## Final User Study
- Test more varied behaviour, including:
  - files that match multiple conditions (check that users can navigate our instructions about how to match).
    - One folder/subfolder instance where the parent condition also applies to the child.
    - Another folder/subfolder with a condition what shouldn't apply to the children.
      - Intent: to see how users use boolean conditions
- Change our syntax example to show expected behaviour for the above cases.
- Test UI interaction.

These were the areas from User Study 1 that participants needed clarification on.

## Adjusted Timeline

- Wednesday, February 14th - Implementation Finished
- Sunday, February 18th - User Study 2
- Saturday, February 24th - Video Finished

Moving User Study 2 up a few days to leave time to fix bugs, smooth error handling, and change what is needed.

# Check-in 5

## Status of user study (should be completed this week at the latest)

_If you've done it, what were the findings? Did it go smoothly? What are the key elements of feedback you've learned from it?_

Final user study is happening before Monday, February 18th, to be conducted by Henry & Louise

## Are there any last changes to your design, implementation or tests? 

_What will these improvements enable?_

Some syntactic changes (bracketing around HAS SUBFOLDERS contents for specificity); more tests for coverage

## Plans for final video (possible draft version).

_Who is responsible for the rest? Make sure to check it for length, working sound etc._

Harry to do actual demo recording with support from others

## Planned timeline for the remaining days. What is there left to do?

- E2e testing
- UI work (preview changes)
- User study
- Incorporate feedback from study
