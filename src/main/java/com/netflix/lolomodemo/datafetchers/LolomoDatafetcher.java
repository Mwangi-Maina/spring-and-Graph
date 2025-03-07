package com.netflix.lolomodemo.datafetchers;

import com.netflix.graphql.dgs.*;
import com.netflix.lolomodemo.ArtWorkService;
import com.netflix.lolomodemo.ShowsRepository;
import com.netflix.lolomodemo.codegen.types.SearchFilter;
import com.netflix.lolomodemo.codegen.types.Show;
import com.netflix.lolomodemo.codegen.types.ShowCategory;


import java.util.List;

@DgsComponent
public class LolomoDatafetcher {
    private final ShowsRepository showsRepository;
    private final ArtWorkService artWorkService;

    public LolomoDatafetcher(ShowsRepository showsRepository, ArtWorkService artWorkService){
           this.showsRepository=showsRepository;
        this.artWorkService = artWorkService;
    }
    @DgsQuery
    public List<ShowCategory> lolomo() {
        return List.of (
          ShowCategory.newBuilder().id(1).name("Top 10").shows(showsRepository.showsForCategory(1)).build(),
          ShowCategory.newBuilder().id(2).name("Continue Watching").shows(showsRepository.showsForCategory(2)).build()
        );
    }
    @DgsData(parentType = "Show")
    public String artworkUrl(DgsDataFetchingEnvironment dfe) {
        Show show = dfe.getSourceOrThrow();
          return artWorkService.generateForTitle(show.getTitle());
    }
    @DgsQuery
    public List<Show> search(@InputArgument SearchFilter searchFilter){
      return   showsRepository.allShows().stream()
                .filter(s-> s.getTitle().toLowerCase().startsWith(searchFilter.getTitle())).toList();

    }
}