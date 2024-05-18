package org.garden.adapter.in.rest.queries;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.garden.domain.entity.MapObject;
import org.garden.domain.entity.RaisedBed;

import java.util.List;

@Path("/v0/allocation")
public class LocationAllocationResource {


    @GET
    public Uni<List<MapObject>> getAll(@PathParam("type") String type) {
        if (type != null && type.equals("raised_bed")) {
            return RaisedBed.listAll();
        }
        return MapObject.listAll();
    }


}
