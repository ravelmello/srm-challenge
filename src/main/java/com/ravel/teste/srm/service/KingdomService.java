package com.ravel.teste.srm.service;

import com.ravel.teste.srm.dto.KingdomDTO;
import com.ravel.teste.srm.entity.Kingdom;
import com.ravel.teste.srm.repository.KingdomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KingdomService {
    private final KingdomRepository kingdomRepository;

    public KingdomService(KingdomRepository kingdomRepository) {
        this.kingdomRepository = kingdomRepository;
    }


    public void saveKingdom(KingdomDTO kingdomDTO) throws Exception {

        if(verifyIfKingdomExists(kingdomDTO.getKingdom())){
            throw new Exception();
        }

        Kingdom kingdom = new Kingdom();
        kingdom.setKingdomName(kingdomDTO.getKingdom());
        kingdom.setDescription(kingdomDTO.getDescription());
        kingdomRepository.save(kingdom);
    }

    private boolean verifyIfKingdomExists(String kingdom) {
        return kingdomRepository.existsByKingdomName(kingdom);
    }

    public List<Kingdom> getAll() {
        return kingdomRepository.findAll();
    }
}
