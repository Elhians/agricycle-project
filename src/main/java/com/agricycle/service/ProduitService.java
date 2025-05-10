package com.agricycle.service;

import com.agricycle.model.Produit;
import com.agricycle.repository.ProduitRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public Produit ajouterProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public List<Produit> listerProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

    public void supprimerProduit(Long id) {
        produitRepository.deleteById(id);
    }
}
