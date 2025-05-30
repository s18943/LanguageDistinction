# Language Detection via Perceptron – Documentation

## Table of Contents

1. [Introduction](#introduction)
2. [Quick Start](#quick-start)
3. [Project Structure](#project-structure)
4. [Build & Run](#build--run)
5. [Data Preparation](#data-preparation)
6. [Core Classes & Algorithms](#core-classes--algorithms)
7. [Extending the Project](#extending-the-project)
8. [Known Limitations & Future Work](#known-limitations--future-work)
9. [License](#license)

---

## Introduction

This project is a **simple command‑line language detector** that learns to recognise languages using **binary perceptrons** trained on letter‑frequency features (26‑dimensional vectors of *a–z* proportions). Each language is handled by an independent perceptron; at prediction time the perceptron that fires (output `1`) declares the language.

> **Why a perceptron?** A single‑layer perceptron is easy to implement, fast to train, and works surprisingly well on problems that are (approximately) linearly separable—such as discriminating languages by letter distribution.

---

## Quick Start

```bash
# compile
mvn package            # or gradle build / javac …

# run – arguments: <learning‑rate> <trainDir> <testDir>
java -jar target/language-detector.jar 0.05 ./data/train ./data/test
```

On completion the program prints the overall **accuracy** on the test set and then enters an **interactive mode** where you can paste arbitrary text and receive a language guess.

---

## Project Structure

```
com.company
├── Main.java              – entry‑point; orchestrates training/testing & CLI
├── Perceptron.java        – binary perceptron implementation
├── InelVector.java        – feature + label container (not shown above)
├── LanguageDeterminator.java – picks the perceptron with highest confidence
└── util / helpers         – static helpers in Main (getLetterProportion, …)
```

*Only `Main` and `Perceptron` are listed in this document; other classes are straightforward and omitted for brevity.*

---

## Build & Run

### Requirements

* **JDK 8** or newer
* Maven (or Gradle / plain javac)
* A corpus of labelled text files organised per language (see next section)

### Command‑line Arguments

| Position | Name       | Description                            |
| -------- | ---------- | -------------------------------------- |
| `0`      | `a`        | Learning‑rate *(double)* – e.g. `0.05` |
| `1`      | `TrainSet` | Path to training root folder           |
| `2`      | `TestSet`  | Path to test root folder               |

Example:

```bash
java -jar language-detector.jar 0.01 ./data/train ./data/test
```

*Hint:* Paths may be absolute or relative; Windows \ and Unix / are both accepted by `java.nio.file`.

---

## Data Preparation

The detector expects the following directory layout:

```
/data
├── train
│   ├── en    # English samples
│   │   ├── doc1.txt
│   │   └── …
│   ├── fr    # French samples
│   └── …
└── test
    ├── en
    ├── fr
    └── …
```

* Each **sub‑folder name is the language label**.
* Training and test sets must contain **at least one file** each.
* Text files should be **plain UTF‑8**. Non‑ASCII letters are ignored; only *a–z* are counted.

---

## Core Classes & Algorithms

### Main.java

Responsible for:

1. **Parsing arguments** and initialising lists.
2. **Scanning the training directory** (`getFromFolderByFilter`), building:

   * `alphabet` – list of language labels.
   * `trainLanguges` – list of `InelVector` containing 26‑dimensional feature vectors plus label.
3. **Creating and training** one `Perceptron` per language (`perceptronLanguges`).
4. **Evaluating** on the test set and printing accuracy.
5. Providing a simple **interactive shell** (`i` to input text, `e` to exit).

Key helper methods:

```java
static List<String> getFromFolderByFilter(String folder, Predicate<Path> pr)
```

Recursively collects paths that satisfy the predicate (directories or files).

```java
static String getLetterProportion(char[] text)
```

Returns a comma‑separated string of **normalised frequencies** for letters *a–z* (values in \[0,1]).

### Perceptron.java

A classic **binary perceptron**:

* **Fields:** learning‑rate `a`, threshold `t`, weight vector `W`, misc. helpers.
* **Train(...)** – shuffles the training set, iteratively updates weights until the misclassification rate ≤ `f` %.
* **Learn(...)** – online weight update: $w_i ← w_i + (d−y)·a·x_i$, $t ← t − (d−y)·a$.
* **Test(...)** – returns `1` if weighted sum $> t$, else `0`.
* **Weight(...)** – logistic activation (unused in classification but available).
* **determ(...)** – convenience wrapper that returns the language if this perceptron fires.

### Feature Extraction

Only **26 lowercase ASCII letters** are considered. For a given text the count of each letter is divided by the total letter count, yielding a vector on the 25‑simplex (sums to 1).

---

## Extending the Project

* **Add a new language** → create a folder under both `train` and `test` with sample texts.
* **Tune hyper‑parameters** (learning rate `a`, acceptable error `f`, max iterations).
* **Replace features** – e.g. bigram frequency, character n‑grams, or TF‑IDF.
* **Swap classifier** – multi‑class logistic regression, SVM, neural net, etc.

---

## Known Limitations & Future Work

* Binary perceptrons cannot express **non‑linearly separable** boundaries.
* Accuracy degrades on **very short inputs** (< 20 letters).
* Unicode letters (å, æ, ü, …) are ignored – enhance feature extractor to count them.
* No **persistence** – weights reset each run; serialise `Perceptron` state to disk.
* Error threshold `f` is hard‑coded per language; could be parameterised.

---

## License

Released under the **MIT License** – see [`LICENSE`](LICENSE) for details.
