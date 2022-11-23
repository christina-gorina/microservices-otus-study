package com.christinagorina.logistics.mongock.changelog;

import com.christinagorina.logistics.repostory.PersonRepository;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.christinagorina.logistics.model.Person;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "stvort", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertLermontov", author = "ydvorzhetskiy")
    public void insertLermontov(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("persons");
        //var doc = new Document().append("name", "Lermontov");
        var doc = Document.parse("{\"name\": \"Lermontov\"}");
        myCollection.insertOne(doc);
    }

    @ChangeSet(order = "003", id = "insertPushkin", author = "stvort")
    public void insertPushkin(PersonRepository repository) {
        repository.save(new Person("Pushkin"));
    }
}
