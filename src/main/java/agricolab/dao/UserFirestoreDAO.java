package agricolab.dao;

import agricolab.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository("Firestore")
public class UserFirestoreDAO implements UserDAO {

    @Override
    public boolean createUser(User user) {


        for (User u : getAllUsers()){
            if(u.getEmail().equals(user.getEmail())){
                System.out.println("ya existe un usuario registrado con este correo, por favor intenta te nuevo");
                return false;
            }
        }
        if (user.getAge()<(18)){
            System.out.println("debes ser mayor de edad para hacer uso de nuestra herramienta");
            return false;
        }else{
            Firestore db=FirestoreClient.getFirestore();
            db.collection("user").document(user.getEmail()).set(user);
            System.out.println(user);
            return true;
        }
    }

    @Override
    public User getUser(String id){
        Firestore db=FirestoreClient.getFirestore();
        DocumentReference ref = db.collection("user").document(id);
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
    public ArrayList<User> getAllUsers(){
        ArrayList<User> allUsers= new ArrayList<>();
        Firestore db= FirestoreClient.getFirestore();
        CollectionReference userRef=db.collection("user");
        ApiFuture<QuerySnapshot> docs= userRef.get();
        List<QueryDocumentSnapshot> docList;
        try {
            docList = docs.get().getDocuments();
            for (QueryDocumentSnapshot a: docList){
                allUsers.add(a.toObject(User.class));
            }
            System.out.println(allUsers);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    @Override
    public int updateUser(User u1, User u2) {
        return 0;
    }

    @Override
    public void deleteUser(String email) {
        Firestore db= FirestoreClient.getFirestore();
        CollectionReference requestRef=db.collection("user");
        ApiFuture<WriteResult> writeResult = requestRef.document(email).delete();
    }
}
