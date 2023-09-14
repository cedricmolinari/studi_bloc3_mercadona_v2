package com.mercadona.promotionmanagement.core.service;

import com.mercadona.promotionmanagement.core.entity.Categorie;
import com.mercadona.promotionmanagement.core.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    @Autowired
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }

    public Categorie findById(Integer id) {
        return categorieRepository.findById(id).orElse(null);
    };
}
