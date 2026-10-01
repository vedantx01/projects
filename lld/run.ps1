$ErrorActionPreference = "Stop"

$projectRoot = $PSScriptRoot
$sourceRoot = Join-Path $projectRoot "src\main\java"
$outputRoot = Join-Path $projectRoot "build\classes"

if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
    throw "A JDK is required. Install a JDK that supports Java 17 or newer."
}

$sources = Get-ChildItem $sourceRoot -Filter "*.java" -Recurse |
    ForEach-Object { $_.FullName }

if ($sources.Count -eq 0) {
    throw "No Java source files were found under $sourceRoot."
}

New-Item -ItemType Directory -Path $outputRoot -Force | Out-Null
& javac --release 17 -d $outputRoot $sources
if ($LASTEXITCODE -ne 0) {
    throw "Java compilation failed with exit code $LASTEXITCODE."
}

& java -cp $outputRoot com.lowleveldesign.Main
if ($LASTEXITCODE -ne 0) {
    throw "The learning application failed with exit code $LASTEXITCODE."
}
