INSERT INTO groupTable (group_name)
VALUES('Category 1');

INSERT INTO notes(name, group_id)
VALUES('Note 1', 1);

UPDATE notes 
SET
    name = 'Change note 1 name'
WHERE
    id = 1;
    