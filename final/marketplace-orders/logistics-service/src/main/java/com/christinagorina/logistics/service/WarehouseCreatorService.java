package com.christinagorina.logistics.service;

import com.christinagorina.logistics.model.Location;
import com.christinagorina.logistics.model.ProductItem;
import com.christinagorina.logistics.model.Reserve;
import com.christinagorina.logistics.model.Warehouse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseCreatorService {

    public List<Warehouse> createWarehouse() {
        Warehouse warehouse1 = new Warehouse();
        List<Float> coordinates1 = new ArrayList<>();
        coordinates1.add(55.641920f);
        coordinates1.add(37.523465f);
        Location location1 = new Location();
        location1.setCoordinates(coordinates1);
        location1.setType("Point");
        List<Reserve> reserveList1 = new ArrayList<>();
        List<ProductItem> productItemList1 = new ArrayList<>();
        ProductItem productItem1_1 = new ProductItem(null, UUID.fromString("16cb7a21-eb5e-4d41-a176-8f49ca425b0c"), "productItem1", 10);
        ProductItem productItem1_3 = new ProductItem(null, UUID.fromString("3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8"), "productItem3", 5);
        ProductItem productItem1_4 = new ProductItem(null, UUID.fromString("44c053f4-f408-4193-906a-4270e978c70c"), "productItem4", 1);
        ProductItem productItem1_5 = new ProductItem(null, UUID.fromString("5188d680-7dfc-4f5a-b6bb-04d7ca14486c"), "productItem5", 25);
        ProductItem productItem1_6 = new ProductItem(null, UUID.fromString("6c6f53c7-d100-4946-a161-012c49028905"), "productItem6", 12);
        ProductItem productItem1_7 = new ProductItem(null, UUID.fromString("793a5f4e-8179-44ca-a98b-e47fc8e1ae35"), "productItem7", 10);
        productItemList1.add(productItem1_1);
        productItemList1.add(productItem1_3);
        productItemList1.add(productItem1_4);
        productItemList1.add(productItem1_5);
        productItemList1.add(productItem1_6);
        productItemList1.add(productItem1_7);
        warehouse1.setName("Belyaevo");
        warehouse1.setLocation(location1);
        warehouse1.setProductItems(productItemList1);
        warehouse1.setReserveList(reserveList1);

        Warehouse warehouse2 = new Warehouse();
        List<Float> coordinates2 = new ArrayList<>();
        coordinates2.add(55.710974f);
        coordinates2.add(37.677204f);
        Location location2 = new Location();
        location2.setCoordinates(coordinates2);
        location2.setType("Point");
        List<Reserve> reserveList2 = new ArrayList<>();
        List<ProductItem> productItemList2 = new ArrayList<>();
        ProductItem productItem2_1 = new ProductItem(null, UUID.fromString("16cb7a21-eb5e-4d41-a176-8f49ca425b0c"), "productItem1", 3);
        ProductItem productItem2_2 = new ProductItem(null, UUID.fromString("261e9635-3ed0-485f-8f28-56aa156958ea"), "productItem2", 20);
        ProductItem productItem2_3 = new ProductItem(null, UUID.fromString("3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8"), "productItem3", 3);
        ProductItem productItem2_4 = new ProductItem(null, UUID.fromString("44c053f4-f408-4193-906a-4270e978c70c"), "productItem4", 8);
        ProductItem productItem2_5 = new ProductItem(null, UUID.fromString("5188d680-7dfc-4f5a-b6bb-04d7ca14486c"), "productItem5", 1);
        ProductItem productItem2_6 = new ProductItem(null, UUID.fromString("6c6f53c7-d100-4946-a161-012c49028905"), "productItem6", 10);
        ProductItem productItem2_7 = new ProductItem(null, UUID.fromString("793a5f4e-8179-44ca-a98b-e47fc8e1ae35"), "productItem7", 5);
        ProductItem productItem2_8 = new ProductItem(null, UUID.fromString("8af6b13d-d72f-4612-89c0-2aa0a09524b0"), "productItem8", 15);
        productItemList2.add(productItem2_1);
        productItemList2.add(productItem2_2);
        productItemList2.add(productItem2_3);
        productItemList2.add(productItem2_4);
        productItemList2.add(productItem2_5);
        productItemList2.add(productItem2_6);
        productItemList2.add(productItem2_7);
        productItemList2.add(productItem2_8);
        warehouse2.setName("Dubrovka");
        warehouse2.setLocation(location2);
        warehouse2.setProductItems(productItemList2);
        warehouse2.setReserveList(reserveList2);

        Warehouse warehouse3 = new Warehouse();
        List<Float> coordinates3 = new ArrayList<>();
        coordinates3.add(55.788717f);
        coordinates3.add(37.741908f);
        Location location3 = new Location();
        location3.setCoordinates(coordinates3);
        location3.setType("Point");
        List<Reserve> reserveList3 = new ArrayList<>();
        List<ProductItem> productItemList3 = new ArrayList<>();
        ProductItem productItem3_1 = new ProductItem(null, UUID.fromString("16cb7a21-eb5e-4d41-a176-8f49ca425b0c"), "productItem1", 5);
        ProductItem productItem3_2 = new ProductItem(null, UUID.fromString("261e9635-3ed0-485f-8f28-56aa156958ea"), "productItem2", 12);
        ProductItem productItem3_3 = new ProductItem(null, UUID.fromString("3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8"), "productItem3", 10);
        ProductItem productItem3_5 = new ProductItem(null, UUID.fromString("5188d680-7dfc-4f5a-b6bb-04d7ca14486c"), "productItem5", 4);
        ProductItem productItem3_6 = new ProductItem(null, UUID.fromString("6c6f53c7-d100-4946-a161-012c49028905"), "productItem6", 8);
        ProductItem productItem3_9 = new ProductItem(null, UUID.fromString("95e028b4-c360-4143-b299-ce9931c5adc0"), "productItem9", 10);
        productItemList3.add(productItem3_1);
        productItemList3.add(productItem3_2);
        productItemList3.add(productItem3_3);
        productItemList3.add(productItem3_5);
        productItemList3.add(productItem3_6);
        productItemList3.add(productItem3_9);
        warehouse3.setName("Izmaylovo");
        warehouse3.setLocation(location3);
        warehouse3.setProductItems(productItemList3);
        warehouse3.setReserveList(reserveList3);

        Warehouse warehouse4 = new Warehouse();
        List<Float> coordinates4 = new ArrayList<>();
        coordinates4.add(55.640432f);
        coordinates4.add(37.607936f);
        Location location4 = new Location();
        location4.setCoordinates(coordinates4);
        location4.setType("Point");
        List<Reserve> reserveList4 = new ArrayList<>();
        List<ProductItem> productItemList4 = new ArrayList<>();
        ProductItem productItem4_1 = new ProductItem(null, UUID.fromString("16cb7a21-eb5e-4d41-a176-8f49ca425b0c"), "productItem1", 5);
        ProductItem productItem4_2 = new ProductItem(null, UUID.fromString("261e9635-3ed0-485f-8f28-56aa156958ea"), "productItem2", 13);
        ProductItem productItem4_3 = new ProductItem(null, UUID.fromString("3a48c0ea-0a2c-4d17-97f2-7bc51e8b10f8"), "productItem3", 1);
        ProductItem productItem4_4 = new ProductItem(null, UUID.fromString("44c053f4-f408-4193-906a-4270e978c70c"), "productItem4", 31);
        ProductItem productItem4_7 = new ProductItem(null, UUID.fromString("793a5f4e-8179-44ca-a98b-e47fc8e1ae35"), "productItem7", 8);
        productItemList4.add(productItem4_1);
        productItemList4.add(productItem4_2);
        productItemList4.add(productItem4_3);
        productItemList4.add(productItem4_4);
        productItemList4.add(productItem4_7);
        warehouse4.setName("Chertanovskaya");
        warehouse4.setLocation(location4);
        warehouse4.setProductItems(productItemList4);
        warehouse4.setReserveList(reserveList4);


        List<Warehouse> warehouseList = new ArrayList<>();
        warehouseList.add(warehouse1);
        warehouseList.add(warehouse2);
        warehouseList.add(warehouse3);
        warehouseList.add(warehouse4);

        return warehouseList;

    }

}
