# Memoro resources

## Notes

Note is group of note items.

### Items

#### Adding new note

POST
/notes/{note-identificator}/items
{
    "text" : "world",
    "note" : {note-db-id},
    "priority": 1,
    "checked?": true
}
