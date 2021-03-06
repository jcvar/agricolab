package agricolab.firestoredao;

import agricolab.dao.CommentDAO;
import agricolab.model.Comment;
import agricolab.model.ID;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Repository
public class CommentFirestoreDAO implements CommentDAO {

    @Override
    public ID setOfferId() {
        ID ret = new ID();
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(FirestoreDAO.COLLECTION_IDS).document("idcomment");
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

    @Override
    public boolean createComment(Comment comment) {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference ref = db.collection(FirestoreDAO.COLLECTION_COMMENT);
        ID id = setOfferId();
        comment.setId(id.toString());
        ref.document(id.toString()).set(comment);
        System.out.println(comment);
        return true;
    }

    @Override
    public Comment getComment(String id) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(FirestoreDAO.COLLECTION_COMMENT).document(id);
        ApiFuture<DocumentSnapshot> future = ref.get();
        DocumentSnapshot document;
        Comment ret = null;
        try {
            document = future.get();
            if (document.exists()) {
                ret = document.toObject(Comment.class);
            } else {
                System.out.println("No such document!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public ArrayList<Comment> getAllCommentsByOffer(String offerId) {
        ArrayList<Comment> comments = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference ref = db.collection(FirestoreDAO.COLLECTION_COMMENT);
        ApiFuture<QuerySnapshot> docs = ref.whereEqualTo("offerReference", offerId).get();
        List<QueryDocumentSnapshot> docList;
        try {
            docList = docs.get().getDocuments();
            for (QueryDocumentSnapshot a : docList) {
                comments.add(a.toObject(Comment.class));
            }
            System.out.println(comments);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return comments;
    }

}
