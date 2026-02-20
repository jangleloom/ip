# MrDucky User Guide

![Ui](Ui.png)

MrDucky is a simple task tracker with a friendly GUI. You can add, list, find, mark, unmark, and delete tasks.

## Quick Start
1. Launch the app.
2. Type a command in the input box and press Enter.

## Commands

### Add a todo
`todo <description>`

Example:
`todo read book`

### Add a deadline
`deadline <description> /by d/MM/yyyy HHmm`

Example:
`deadline return book /by 23/02/2026 1800`

### Add an event
`event <description> /from d/MM/yyyy HHmm /to d/MM/yyyy HHmm`

Example:
`event project meeting /from 24/02/2026 1400 /to 24/02/2026 1530`

### List tasks
`list`

### Mark a task as done
`mark <index>`

Example:
`mark 1`

### Unmark a task
`unmark <index>`

Example:
`unmark 1`

### Delete a task
`delete <index>`

Example:
`delete 2`

### Find tasks by keyword
`find <keyword>`

Example:
`find book`

### Help
`help`

### Exit
`bye`

## Notes
- Indexes are 1-based (the first task is `1`).
- Dates must follow `d/MM/yyyy HHmm` (e.g. `2/12/2019 1800`).

## Common Errors
- If an index is out of range, MrDucky shows an error message.
- If the date format is invalid, MrDucky asks you to use the correct format.

## Data Storage
Tasks are saved to `data/mrducky.txt` in the project folder.
