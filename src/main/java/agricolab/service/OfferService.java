package agricolab.service;

import agricolab.dao.OfferDAO;
import agricolab.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
public class OfferService {

    private OfferDAO offerDAO;

    @Autowired
    public OfferService(OfferDAO offerdao) {
        this.offerDAO = offerdao;
    }

    public boolean addOffer(Offer offer) {
        // Check for orders on the same product
        if (!offerDAO.getOffersByUserAndProduct(offer.getSellerEmail(), offer.getProductName()).isEmpty()) {
            System.out.println("ya hiciste una oferta de este producto y sigue activa, debes esperar a su" +
                    " fin o cancelarla antes de crear otra");
            return false;
        }
        return offerDAO.createOffer(offer);
    }

    public Offer getOffer(String id) {
        return offerDAO.getOffer(id);
    }

    public boolean updateOffer(Offer offer) {
        return offerDAO.updateOffer(offer);
    }

    public ArrayList<Offer> getAllOffers() {
        return offerDAO.getAllOffers();
    }

    public ArrayList<Offer> gerOffersByUser(String email) {
        return offerDAO.getOffersByUser(email);
    }

    //filtros
    public ArrayList<Offer> getActiveOffers(String productName, double minPrice, double maxPrice,
                                            int presentation, int order, int page, int pivot) throws ExecutionException, InterruptedException {
        ArrayList<Offer> offers = new ArrayList<>();
        ArrayList<Offer> inverted = new ArrayList<>();
        ArrayList<Offer> ofertas = offerDAO.getActiveOffers(productName, minPrice, maxPrice, presentation,
                order, page, pivot);
        if (page == 0) {
            for (int i = ofertas.size(); i > 0; i--) {
                inverted.add(ofertas.get(i - 1));
            }
            ofertas = inverted;
        }
        if ((order == 3) && ((minPrice != 0) || (maxPrice != 0))) {
            if ((minPrice != 0) && (maxPrice != 0)) {
                for (Offer o : ofertas) {
                    if (o.getPricePresentation() <= maxPrice && o.getPricePresentation() >= minPrice) {
                        offers.add(o);
                    }
                }
            }
            if ((minPrice != 0) && (maxPrice == 0)) {
                for (Offer o : ofertas) {
                    if (o.getPricePresentation() >= minPrice) {
                        offers.add(o);
                    }
                }
            }
            if ((minPrice == 0) && (maxPrice != 0)) {
                for (Offer o : ofertas) {
                    if (o.getPricePresentation() <= maxPrice) {
                        offers.add(o);
                    }
                }
            }
        } else {
            offers = ofertas;

        }
        return offers;
    }

    public void deleteOffer(String id) {
        offerDAO.deleteOffer(id);
    }

    public int getLastOfferId() {
        return offerDAO.getLastOfferId();
    }

}
