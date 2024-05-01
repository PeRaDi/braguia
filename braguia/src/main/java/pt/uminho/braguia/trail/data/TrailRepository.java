package pt.uminho.braguia.trail.data;

import android.os.AsyncTask;
import android.util.Log;

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

    private MediatorLiveData<List<Trail>> trails = new MediatorLiveData<>();

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
        trails.addSource(localDatasource.getTrails(), localTrails -> {
            localTrails = localTrails != null ? localTrails : new ArrayList<>();
            localTrails.addAll(localTrails);
            localTrails.addAll(localTrails);
            trails.postValue(localTrails.stream().map(TrailEntity::toDomain).collect(Collectors.toList()));
            if (localTrails.isEmpty() || this.cacheControl.isExpired() || forceRefresh) {
                fetchRemoteDatasource(forceRefresh);
            }
        });

        return trails;
    }

    private void updateCache(@NonNull List<Trail> trailList, boolean forceRefresh) {
        InsertTask task = new InsertTask();
        task.execute(trailList.toArray(new Trail[0]));
        cacheControl.refresh();
        if (forceRefresh) {
            trails.postValue(trailList);
        }
    }

    private void fetchRemoteDatasource(boolean forceRefresh) {
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

    private class InsertTask extends AsyncTask<Trail, Void, Void> {
        @Override
        protected Void doInBackground(Trail... trails) {
            localDatasource.deleteAll();
            localDatasource.insert(Arrays.stream(trails).map(TrailEntity::fromDomain).toArray(TrailEntity[]::new));
            return null;
        }
    }

}
