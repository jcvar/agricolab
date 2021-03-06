package agricolab.firestoredao;

import agricolab.dao.UserDAO;
import agricolab.model.Mailing;
import agricolab.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository("UserFirestore")
public class UserFirestoreDAO implements UserDAO {

    @Override
    public boolean createUser(User user) {

        Firestore db = FirestoreClient.getFirestore();
        db.collection(FirestoreDAO.COLLECTION_USER).document(user.getEmail()).set(user);
        return true;
    }

    @Override
    public User getUser(String id) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(FirestoreDAO.COLLECTION_USER).document(id);
        ApiFuture<DocumentSnapshot> future = ref.get();
        DocumentSnapshot document;
        User ret = null;
        try {
            document = future.get();
            if (document.exists()) {
                ret = document.toObject(User.class);
            } else {
                System.out.println("No such document!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public boolean deleteUser(String email) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference requestRef = db.collection(FirestoreDAO.COLLECTION_USER);
        try {
            WriteResult writeResult = requestRef.document(email).delete().get();
            System.out.println("Successful delete from user " + email);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference userRef = db.collection(FirestoreDAO.COLLECTION_USER);
        ApiFuture<QuerySnapshot> docs = userRef.get();
        List<QueryDocumentSnapshot> docList;
        try {
            docList = docs.get().getDocuments();
            for (QueryDocumentSnapshot a : docList) {
                allUsers.add(a.toObject(User.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    @Override
    public Mailing getMailingByUser(String email) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference request = db.collection(FirestoreDAO.COLLECTION_USER);
        ApiFuture<DocumentSnapshot> obj = request.document(email).get();
        return Objects.requireNonNull(Objects.requireNonNull(obj.get().toObject(User.class)).getMailing());
    }

    @Override
    public boolean createMailing(User user) {//, Mailing mailing) {
        Firestore db = FirestoreClient.getFirestore();
        //Map<String, Object> updates = new HashMap<>();
        //updates.put("mailing", mailing);
        db.collection(FirestoreDAO.COLLECTION_USER).document(user.getEmail()).set(user);
        return true;
    }

    @Override
    public boolean updateUserData(String email, Mailing mailing, long phoneNumber) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference user = db.collection(FirestoreDAO.COLLECTION_USER);
        User u = getUser(email);
        u.setMailing(mailing);
        u.setPhoneNumber(phoneNumber);
        ApiFuture<WriteResult> update = user.document(email).set(u);
        return true;
    }

    @Override
    public boolean updateUserQualification(String email, double qualification) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference user = db.collection(FirestoreDAO.COLLECTION_USER);
        User u = getUser(email);
        if (u != null) {
            try {
                user.document(email).update("qualification", qualification, "numberOfReviews", u.getNumberOfReviews() + 1).get();
                return true;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void updateOffersRecieved(String email, int newNum) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(FirestoreDAO.COLLECTION_USER).document(email);
        Map<String, Object> updates = new HashMap<>();
        updates.put("numberOfOrdersRecieved", newNum);
        ApiFuture<WriteResult> future = ref.update(updates);
    }

    @Override
    public void updateOffersMade(String email, int newNum) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(FirestoreDAO.COLLECTION_USER).document(email);
        Map<String, Object> updates = new HashMap<>();
        updates.put("numberOfOrdersDone", newNum);
        ApiFuture<WriteResult> future = ref.update(updates);
    }

}
