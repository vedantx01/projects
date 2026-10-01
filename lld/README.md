# Low-Level Design in Java

A hands-on, dependency-free Java project organized by **subject** and then by
**concept**. Each concept folder contains a short lesson; selected folders also
contain runnable Java examples. The project uses Java 17 language features and
can be compiled with a JDK—no Maven or external libraries are required.

## Run

From PowerShell at the project root:

```powershell
.\run.ps1
```

The script compiles the examples into `build\classes` and runs the learning
menu. To compile manually:

```powershell
$sources = Get-ChildItem .\src\main\java -Filter *.java -Recurse | ForEach-Object FullName
javac --release 17 -d .\build\classes $sources
java -cp .\build\classes com.lowleveldesign.Main
```

## Project structure

```text
src/main/java/com/lowleveldesign/
  Main.java
  subjects/
    object-oriented-design/<concept>/
    object-lifecycle/<concept>/
    class-lifecycle/<concept>/
    solid-and-design-principles/<concept>/
    design-patterns/
      creational/<pattern>/
      structural/<pattern>/
      behavioral/<pattern>/
    class-design/<concept>/
    uml-and-diagrams/<diagram>/
    data-structures/<structure>/
    api-and-interface-design/<concept>/
    concurrency-design/<concept>/
    error-handling/<concept>/
    memory-and-resource-management/<concept>/
    database-and-persistence/<pattern>/
```

## Learning path

1. Start with object and class lifecycle, then review SOLID and the supporting
   principles (YAGNI, cohesion, coupling, and DRY).
2. Study class relationships and UML before comparing the design patterns.
3. Work through the data structures and API design concepts.
4. Finish with concurrency, error handling, resource management, and persistence.

The concept folders are intentionally small and independent. Read each
`README.md`, run the Java examples where present, then implement the exercises
in that folder. Begin with the relevant subject guide:

- [Object-oriented design](src/main/java/com/lowleveldesign/subjects/object_oriented_design/README.md)
- [Object lifecycle](src/main/java/com/lowleveldesign/subjects/object_lifecycle/README.md)
- [Class lifecycle](src/main/java/com/lowleveldesign/subjects/class_lifecycle/README.md)
- [SOLID and design principles](src/main/java/com/lowleveldesign/subjects/solid_and_design_principles/README.md)
- [Design patterns](src/main/java/com/lowleveldesign/subjects/design_patterns/README.md)
- [Class design](src/main/java/com/lowleveldesign/subjects/class_design/README.md)
- [UML and diagrams](src/main/java/com/lowleveldesign/subjects/uml_and_diagrams/README.md)
- [Data structures](src/main/java/com/lowleveldesign/subjects/data_structures/README.md)
- [API and interface design](src/main/java/com/lowleveldesign/subjects/api_and_interface_design/README.md)
- [Concurrency design](src/main/java/com/lowleveldesign/subjects/concurrency_design/README.md)
- [Error handling](src/main/java/com/lowleveldesign/subjects/error_handling/README.md)
- [Memory and resource management](src/main/java/com/lowleveldesign/subjects/memory_and_resource_management/README.md)
- [Database and persistence](src/main/java/com/lowleveldesign/subjects/database_and_persistence/README.md)

The `Main` class is an index of runnable demonstrations.
