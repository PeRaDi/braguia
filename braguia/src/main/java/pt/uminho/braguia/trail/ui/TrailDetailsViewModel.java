package pt.uminho.braguia.trail.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.trail.data.TrailRepository;
import pt.uminho.braguia.trail.domain.Trail;

@HiltViewModel
public class TrailDetailsViewModel extends ViewModel {

    private LiveData<Trail> trail;
    private TrailRepository repository;

    @Inject
    public TrailDetailsViewModel(TrailRepository repository) {
        this.repository = repository;
    }

    public LiveData<Trail> getTrail(Long id) {
        if(trail == null) {
            trail = repository.getTrailById(id);
        }
        return trail;
    }
}