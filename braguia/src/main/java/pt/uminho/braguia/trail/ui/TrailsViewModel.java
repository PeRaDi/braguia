package pt.uminho.braguia.trail.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.network.CacheManager;
import pt.uminho.braguia.trail.data.TrailLocalDatasource;
import pt.uminho.braguia.trail.data.TrailRemoteDatasource;
import pt.uminho.braguia.trail.data.TrailRepository;
import pt.uminho.braguia.trail.domain.Trail;

@HiltViewModel
public class TrailsViewModel extends ViewModel {

    private LiveData<List<Trail>> trails;

    @Inject
    public TrailsViewModel(TrailRepository repository) {
        trails = repository.getTrails();
    }

    public LiveData<List<Trail>> getTrails() {
        return trails;
    }
}