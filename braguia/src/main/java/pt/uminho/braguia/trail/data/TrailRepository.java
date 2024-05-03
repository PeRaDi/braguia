package pt.uminho.braguia.trail.data;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import pt.uminho.braguia.network.CacheControl;
import pt.uminho.braguia.trail.data.db.TrailEntity;
import pt.uminho.braguia.trail.domain.Trail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailRepository {

    private final TrailLocalDatasource localDatasource;
    private final TrailRemoteDatasource remoteDatasource;
    private final CacheControl cacheControl;

    private MediatorLiveData<List<Trail>> trailsMediatorLiveData = new MediatorLiveData<>();
    private MediatorLiveData<Trail> trailMediatorLiveData = new MediatorLiveData<>();

    public TrailRepository(TrailLocalDatasource localDatasource,
                           TrailRemoteDatasource remoteDatasource,
                           CacheControl cacheControl) {
        this.localDatasource = localDatasource;
        this.remoteDatasource = remoteDatasource;
        this.cacheControl = cacheControl;
    }

    public LiveData<List<Trail>> getTrails() {
        return getTrails(false);
    }

    public LiveData<List<Trail>> getTrails(boolean forceRefresh) {
        trailsMediatorLiveData.addSource(localDatasource.getTrails(), localTrails -> {
            localTrails = localTrails != null ? localTrails : new ArrayList<>();
            trailsMediatorLiveData.postValue(localTrails.stream().map(TrailEntity::toDomain).collect(Collectors.toList()));
            Boolean expired = this.cacheControl.isExpired();
            if (localTrails.isEmpty() || expired || forceRefresh) {
                fetchTrailsFromRemoteDatasource(forceRefresh);
            }
        });

        return trailsMediatorLiveData;
    }

    public LiveData<Trail> getTrailById(Long id) {
        return getTrailById(id, false);
    }

    public LiveData<Trail> getTrailById(Long id, boolean forceRefresh) {
//        trailMediatorLiveData.addSource(localDatasource.getTrailById(id), localTrail -> {
//            trailMediatorLiveData.postValue(localTrail != null ? localTrail.toDomain() : null);
//            if (localTrail == null || this.cacheControl.isExpired()) {
                fetchTrailFromRemoteDatasource(id, forceRefresh);
//            }
//        });
//

        return trailMediatorLiveData;
    }

    private void updateCache(@NonNull List<Trail> trailList, boolean forceRefresh) {
        InsertTask task = new InsertTask();
        task.execute(trailList.toArray(new Trail[0]));
        cacheControl.refresh();
        if (forceRefresh) {
            trailsMediatorLiveData.postValue(trailList);
        }
    }

    private void fetchTrailsFromRemoteDatasource(boolean forceRefresh) {
        remoteDatasource.getTrails().enqueue(new Callback<List<Trail>>() {
            @Override
            public void onResponse(Call<List<Trail>> call, Response<List<Trail>> response) {
                List<Trail> trailList = response.isSuccessful() ? response.body() : new ArrayList<>();
                updateCache(trailList, forceRefresh);
            }

            @Override
            public void onFailure(Call<List<Trail>> call, Throwable t) {
                updateCache(new ArrayList<>(), forceRefresh);
            }
        });
    }

    private void fetchTrailFromRemoteDatasource(Long id, boolean forceRefresh) {
        if(id == null) {
            return;
        }
        remoteDatasource.getTrail(id.toString()).enqueue(new Callback<Trail>() {
            @Override
            public void onResponse(Call<Trail> call, Response<Trail> response) {
                //List<Trail> trailList = response.isSuccessful() ? Arrays.asList(response.body()) : new ArrayList<>();
                //updateCache(trailList, forceRefresh);
                trailMediatorLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Trail> call, Throwable t) {
                trailMediatorLiveData.setValue(null);
            }
        });
    }

    private class InsertTask extends AsyncTask<Trail, Void, Void> {
        @Override
        protected Void doInBackground(Trail... trails) {
            localDatasource.deleteAll();
            localDatasource.insert(Arrays.stream(trails).map(TrailEntity::fromDomain).toArray(TrailEntity[]::new));
            return null;
        }
    }

}
