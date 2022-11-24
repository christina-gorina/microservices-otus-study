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
        var warehouse1 = Document.parse("{'name':'Yangelya','location': {'coordinates':[55.594575, 37.599154],'type':'Point'}" +
                ",'productItems': [" +
                "{'uuid': '16cb7a21-eb5e-4d41-a176-8f49ca425b0c','warehouseCount':10}, " +
                "{'uuid': '3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8','warehouseCount':5}" +
                "{'uuid': '44c053f4-f408-4193-906a-4270e978c70c','warehouseCount':1}" +
                "{'uuid': '5188d680-7dfc-4f5a-b6bb-04d7ca14486c','warehouseCount':25}" +
                "]," +
                "reserve:[]" +
                "}");
        var warehouse2 = Document.parse("{'name':'Pragskaya','location': {'coordinates':[55.614403, 37.606506],'type':'Point'}" +
                ",'productItems': [" +
                "{'uuid': '16cb7a21-eb5e-4d41-a176-8f49ca425b0c','warehouseCount':3}, " +
                "{'uuid': '261e9635-3ed0-485f-8f28-56aa156958ea','warehouseCount':20}" +
                "{'uuid': '3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8','warehouseCount':3}" +
                "{'uuid': '44c053f4-f408-4193-906a-4270e978c70c','warehouseCount':8}" +
                "{'uuid': '5188d680-7dfc-4f5a-b6bb-04d7ca14486c','warehouseCount':1}" +
                "]" +
                "reserve:[]" +
                "}");
        var warehouse3 = Document.parse("{'name':'Yugnaya','location': {'coordinates':[55.621719, 37.611742],'type':'Point'}" +
                ",'productItems': [" +
                "{'uuid': '16cb7a21-eb5e-4d41-a176-8f49ca425b0c','warehouseCount':5}, " +
                "{'uuid': '261e9635-3ed0-485f-8f28-56aa156958ea','warehouseCount':12}" +
                "{'uuid': '3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8','warehouseCount':10}" +
                "{'uuid': '5188d680-7dfc-4f5a-b6bb-04d7ca14486c','warehouseCount':4}" +
                "]" +
                "reserve:[]" +
                "}");
        var warehouse4 = Document.parse("{'name':'Chertanovskaya','location': {'coordinates':[55.640432, 37.607936],'type':'Point'}" +
                ",'productItems': [" +
                "{'uuid': '16cb7a21-eb5e-4d41-a176-8f49ca425b0c','warehouseCount':5}, " +
                "{'uuid': '261e9635-3ed0-485f-8f28-56aa156958ea','warehouseCount':13}" +
                "{'uuid': '3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8','warehouseCount':1}" +
                "{'uuid': '44c053f4-f408-4193-906a-4270e978c70c','warehouseCount':31}" +
                "]" +
                "reserve:[]" +
                "}");
        warehouseList.add(warehouse1);
        warehouseList.add(warehouse2);
        warehouseList.add(warehouse3);
        warehouseList.add(warehouse4);
        warehouseCollection.insertMany(warehouseList);
    }

}
