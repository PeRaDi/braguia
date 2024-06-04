package pt.uminho.braguia.trail.data.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TrailEntityDetails {

    @Embedded
    TrailEntity trail;

    @Relation(
            parentColumn = "id",
            entityColumn = "trailId"
    )
    List<RelTrailEntity> relTrails;

    @Relation(
            parentColumn = "id",
            entityColumn = "trailId"
    )
    List<EdgeEntity> edges;

}
