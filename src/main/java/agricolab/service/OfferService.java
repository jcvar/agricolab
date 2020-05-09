package agricolab.service;

import agricolab.dao.OfferDAO;
import agricolab.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class OfferService {

    private OfferDAO offerDAO;

    @Autowired
    public OfferService(OfferDAO offerdao) {
        offerDAO = offerdao;
    }

    public String addOffer(Offer offer){return offerDAO.createOffer(offer);}

    public Offer getOffer(String id){
        return offerDAO.getOffer(id);
    }

    public boolean updateOffer(int id, Offer offer){return offerDAO.updateOffer(id, offer);}

    public ArrayList<Offer> getAllOffers(){
        return offerDAO.getAllOffers();
    }

    public ArrayList<Offer> gerOffersByUser(String email){
        return offerDAO.gerOffersByUser(email);
    }

    public  ArrayList<Offer> getActiveOffers(){ return offerDAO.getActiveOffers(); }

    public void deleteOffer(String id){offerDAO.deleteOffer(id);}

    public int getLastOfferId (){ return offerDAO.getLastOfferId(); }

}
