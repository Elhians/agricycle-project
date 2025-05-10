package com.agricycle.controller;

import com.agricycle.model.Produit;
import com.agricycle.service.ProduitService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @PostMapping
    public Produit ajouter(@RequestBody Produit produit) {
        return produitService.ajouterProduit(produit);
    }

    @GetMapping
    public List<Produit> lister() {
        return produitService.listerProduits();
    }

    @GetMapping("/{id}")
    public Produit getProduit(@PathVariable Long id) {
        return produitService.getProduitById(id);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        produitService.supprimerProduit(id);
    }
}
