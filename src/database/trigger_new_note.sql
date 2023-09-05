CREATE TRIGGER IF NOT EXISTS format_datetime
INSTEAD OF INSERT ON Notes
BEGIN
    -- Format the date using strftime for rows meeting the conditions
        INSERT INTO your_table_name (UserID, Date_created, Date_modified, Title, Content) VALUES (
        
            NEW.UserID,
            strftime('%Y-%m-%d %H:%M:%S', NEW.Date_created),
            strftime('%Y-%m-%d %H:%M:%S', NEW.Date_modified),
            NEW.Title,
            NEW.Content
        );
END;