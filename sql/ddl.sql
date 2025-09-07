-- Quote definition

CREATE TABLE "Quote" 
(`content` VARCHAR , 
`description` VARCHAR , 
`example` VARCHAR , 
`id` INTEGER PRIMARY KEY AUTOINCREMENT , 
`text` VARCHAR , 
`title` VARCHAR , 
`type` INTEGER );

-- Book definition

CREATE TABLE "Book" (`definition` VARCHAR , `id` INTEGER PRIMARY KEY AUTOINCREMENT , `title` VARCHAR );


-- Chapter definition

CREATE TABLE Chapter (
	id INTEGER,
	title VARCHAR,
	book_id INTEGER,
	CONSTRAINT CHAPTER_PK PRIMARY KEY (id),
	CONSTRAINT Chapter_Book_FK FOREIGN KEY (id) REFERENCES Book(id)
);

-- Idea definition

CREATE TABLE Idea (
	id INTEGER,
	title VARCHAR,
	description VARCHAR,
	book_id INTEGER,
	chapter_id INTEGER,
	CONSTRAINT IDEA_PK PRIMARY KEY (id),
	CONSTRAINT Idea_Book_FK FOREIGN KEY (book_id) REFERENCES Book(id),
	CONSTRAINT Idea_Chapter_FK FOREIGN KEY (id) REFERENCES Chapter(id)
);