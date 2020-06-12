package agricolab.dao;

import agricolab.model.ID;
import agricolab.model.Order;
import agricolab.service.OrderService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Repository
public class OrderFirestoreDAO implements OrderDAO {


    private final OfferDAO offerDAO;

    @Autowired
    public OrderFirestoreDAO(OfferDAO offerDAO) {
        this.offerDAO = offerDAO;
    }

    //Basic CRUD(CREATE READ UPDATE DELETE)
    @Override
    public boolean createOrder(Order order) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference ref = db.collection("order");

        try {
            //ref.document(id.toString()).set(order);
            order.setId(setOrderId().toString());
            ref.document(order.getId()).set(order).get();
            System.out.println(order);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    //UNUSED METHODS ------------------------------------------------
    @Override
    public int getLastOrderId() {
        int ret = 0;
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection("ids").document("idorder");
        ApiFuture<DocumentSnapshot> future = ref.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                ret = Objects.requireNonNull(document.toObject(ID.class)).getId();
            } else {
                System.out.println("No such document!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    // READ
    @Override
    public Order getOrder(String id) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection("order").document(id);
        ApiFuture<DocumentSnapshot> future = ref.get();
        Order ret = null;
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                ret = document.toObject(Order.class);
            } else {
                System.out.println("No such document!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    //RETRIEVES ALL ORDERS FROM ORDER COLLECTION
    @Override
    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> allRequest = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference requestRef = db.collection("order");
        ApiFuture<QuerySnapshot> docs = requestRef.get();
        List<QueryDocumentSnapshot> docList;
        try {
            docList = docs.get().getDocuments();
            for (QueryDocumentSnapshot a : docList) {
                allRequest.add(a.toObject(Order.class));
            }
            System.out.println(allRequest);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return allRequest;
    }


    //BUYER METHODS ------------------------------------------


    @Override
    public ArrayList<Order> getOrdersByBuyer(String email, String productName, int state) {
        ArrayList<Order> userOrder = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference orderRef = db.collection("order");
        Query q = orderRef.whereEqualTo("buyerEmail", email).whereLessThanOrEqualTo("state", 1);

        if (!productName.equals("all")) {
            q = q.whereEqualTo("productName", productName);
        }
        if (state != -1) {
            q = q.whereEqualTo("state", state);
        }
        ApiFuture<QuerySnapshot> docs = q.get();
        List<QueryDocumentSnapshot> docList;
        try {
            docList = docs.get().getDocuments();
            for (QueryDocumentSnapshot a : docList) {
                userOrder.add(a.toObject(Order.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return userOrder;
    }

    @Override
    public ArrayList<Order> getActiveOrdersByBuyer(String email, String productName, int state) {
        ArrayList<Order> userOrder = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference orderRef = db.collection("order");
        Query q = orderRef.whereEqualTo("buyerEmail", email).whereGreaterThanOrEqualTo("state", 2);

        if (!productName.equals("all")) {
            q = q.whereEqualTo("productName", productName);
        }
        if (state != -1) {
            q = q.whereEqualTo("state", state);
        }
        ApiFuture<QuerySnapshot> docs = q.get();
        List<QueryDocumentSnapshot> docList;
        try {
            docList = docs.get().getDocuments();
            for (QueryDocumentSnapshot a : docList) {
                userOrder.add(a.toObject(Order.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return userOrder;
    }

    //SELLER METHODS -----------------------------------------

    @Override
    public ArrayList<Order> getActiveOrdersBySeller(String email, String productName, int state) {
        ArrayList<Order> userOrder = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference orderRef = db.collection("order");
        Query q = orderRef.whereEqualTo("sellerEmail", email).whereGreaterThanOrEqualTo("state", 2);

        if (!productName.equals("all")) {
            q = q.whereEqualTo("productName", productName);
        }
        if (state != -1) {
            q = q.whereEqualTo("state", state);
        }
        ApiFuture<QuerySnapshot> docs = q.get();
        List<QueryDocumentSnapshot> docList;
        try {
            docList = docs.get().getDocuments();
            for (QueryDocumentSnapshot a : docList) {
                userOrder.add(a.toObject(Order.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return userOrder;
    }
    //BUYER METHODS ------------------------------------------

    //SELLER METHODS -----------------------------------------
    @Override
    public ArrayList<Order> getOrdersBySeller(String email, String productName, int state) {
        ArrayList<Order> userOrder = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference orderRef = db.collection("order");
        Query q = orderRef.whereEqualTo("sellerEmail", email).whereLessThanOrEqualTo("state", 1);

        if (!productName.equals("all")) {
            q = q.whereEqualTo("productName", productName);
        }
        if (state != -1) {
            q = q.whereEqualTo("state", state);
        }
        ApiFuture<QuerySnapshot> docs = q.get();
        List<QueryDocumentSnapshot> docList;
        try {
            docList = docs.get().getDocuments();
            for (QueryDocumentSnapshot a : docList) {
                userOrder.add(a.toObject(Order.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return userOrder;
    }

    @Override
    public boolean updateOrderStatus(String id, int i) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("order").document(id);
        ApiFuture<WriteResult> future = docRef.update("state", i);
        WriteResult result = null;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //System.out.println("Write result: " + result);
        // true if success, false if still null
        return result != null;
    }

    // AUXILIARY METHODS-------------------------------------------------

    public ID setOrderId() {
        ID ret = new ID();
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection("ids").document("idorder");
        ApiFuture<DocumentSnapshot> future = ref.get();
        DocumentSnapshot document;
        try {
            document = future.get();
            if (document.exists()) {
                ret = document.toObject(ID.class);
            } else {
                System.out.println("No such document!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(ret).setId(ret.getId() + 1);
        ref.set(ret);
        return ret;
    }
}
