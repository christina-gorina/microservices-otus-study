package com.christinagorina.logistics.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "kris", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertWarehouse", author = "kris")
    public void insertWarehouse(MongoDatabase db) {
        MongoCollection<Document> warehouseCollection = db.getCollection("warehouses");
        List<Document> warehouseList = new ArrayList<>();
        var warehouse1 = Document.parse("{'name':'Yangelya','location': {'coordinates':[55.594575, 37.599154],'type':'Point'}}");
        var warehouse2 = Document.parse("{'name':'Pragskaya','location': {'coordinates':[55.614403, 37.606506],'type':'Point'}}");
        var warehouse3 = Document.parse("{'name':'Yugnaya','location': {'coordinates':[55.621719, 37.611742],'type':'Point'}}");
        var warehouse4 = Document.parse("{'name':'Chertanovskaya','location': {'coordinates':[55.640432, 37.607936],'type':'Point'}}");
        warehouseList.add(warehouse1);
        warehouseList.add(warehouse2);
        warehouseList.add(warehouse3);
        warehouseList.add(warehouse4);
        warehouseCollection.insertMany(warehouseList);
    }

}
