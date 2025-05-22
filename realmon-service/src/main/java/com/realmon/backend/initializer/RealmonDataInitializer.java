package com.realmon.backend.initializer;

import com.realmon.backend.repository.RealmonRepository;
import com.realmon.common.model.entity.Realmon;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RealmonDataInitializer {

    private final RealmonRepository repository;

    @PostConstruct
    public void init() {
        // clear old data
        repository.deleteAll();

        // insert new data
        List<Realmon> realmons = List.of(
                new Realmon(null, "European Robin", "Erithacus rubecula", "bird", 53.2790, -9.0588,
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8c/Robin_Redbreast_at_Nant_BH.jpg/800px-Robin_Redbreast_at_Nant_BH.jpg",
                        "https://en.wikipedia.org/wiki/European_robin"),

                new Realmon(null, "Red Fox", "Vulpes vulpes", "land-animal", 53.2792, -9.0601,
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Vulpes_vulpes_laying_in_snow.jpg/800px-Vulpes_vulpes_laying_in_snow.jpg",
                        "https://en.wikipedia.org/wiki/Red_fox"),

                new Realmon(null, "Common Blue Butterfly", "Polyommatus icarus", "insect", 53.2801, -9.0580,
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Common_Blue_butterfly.jpg/800px-Common_Blue_butterfly.jpg",
                        "https://en.wikipedia.org/wiki/Common_blue"),

                new Realmon(null, "Daffodil", "Narcissus", "plant", 53.2777, -9.0595,
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Narcissus_February_Gold_02.jpg/800px-Narcissus_February_Gold_02.jpg",
                        "https://en.wikipedia.org/wiki/Narcissus_(plant)"),

                new Realmon(null, "Atlantic Puffin", "Fratercula arctica", "bird", 53.2785, -9.0612,
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/Atlantic_Puffin.jpg/800px-Atlantic_Puffin.jpg",
                        "https://en.wikipedia.org/wiki/Atlantic_puffin")
        );

        repository.saveAll(realmons);
    }
}
