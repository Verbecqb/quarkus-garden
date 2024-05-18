package org.garden.domain.entity;


import com.fasterxml.jackson.annotation.JsonTypeName;
import io.quarkus.logging.Log;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.WebApplicationException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.garden.constants.CellStatus;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity
@DiscriminatorValue("raised_bed")
@JsonTypeName("raised_bed")
@Data
@EqualsAndHashCode(callSuper = true)
public class RaisedBed extends MapObject {

    @NotBlank(message = "Name should be provided.")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<ScheduleRaisedBedItems> cells = new LinkedList<>();

    public void addSchedule(long itemId, int x, int y, int w, int h, LocalDate from, LocalDate to) {
        // In case plant duration is more than a year, raise an exception - not supported.
        if (DAYS.between(from, to) > 365) {
            throw new WebApplicationException("Application only supports plants up to one year.");
        }
        from.datesUntil(to.plusDays(1)).forEach(day -> {
                    for (int i = x ; i < x+w ; i++) {
                        for (int j = y ; j < y+h ; j++) {
                            cells.add(ScheduleRaisedBedItems.builder()
                                            .day(day)
                                            .coordinateX(i).coordinateY(j)
                                            .item_id(itemId)
                                            .build()
                            );
                        }
                    }
                }
        );
    }


    /**
     * Return true if the pos & space is EMPTY for all days in cellStatues
     * @param cellStatuses The list of cellStatus, by day
     * @param pos
     * @param space_w
     * @param space_l
     * @return
     */
    public boolean isPositionAvailable(
            @NotNull List<CellStatus[][]> cellStatuses,
            @Size(min = 2, max = 2) Integer[] pos,
            Integer space_w, Integer space_l) {

        for (CellStatus[][] cells : cellStatuses) {

                CellStatus[][] cellStatusSubArray = this.cellStatusByDaySubArray(cells, pos, space_w, space_l);
                // If the cells are not EMPTY for given day, return false
                if (!Arrays.stream(cellStatusSubArray)
                                .flatMap(Arrays::stream).toList().stream()
                                .allMatch(CellStatus.EMPTY::equals)) return false;
        }
        // Reached the point when all cells are empty for ALL days.
        return true;

    }


    public Optional<Integer[]> findAvailableSpace(
            int space_w, int space_l,
            @NotNull LocalDate startIncluded,
            @NotNull LocalDate endIncluded) throws IllegalArgumentException {

        Log.info("findAvailableSpace# called");

        if (space_w > this.getWidth() || space_l > this.getLength()) throw new IllegalArgumentException("Required space does not fit in the raised bed.");

        if (startIncluded.isAfter(endIncluded)) throw  new IllegalArgumentException("Start date should be before end date.");

        List<CellStatus[][]>  cellStatuses = cellStatusByDateRange( startIncluded,  endIncluded);
        for (int i = 0; i <= this.getWidth() - space_w; i++) {
                for (int j = 0; j <= this.getLength() - space_l; j++) {

                    if (isPositionAvailable(cellStatuses, new Integer[]{i, j}, space_w, space_l)) {
                        return Optional.of(new Integer[]{i, j});
                    }
                }
            }

        // No
        return Optional.empty();
    }

    public List<CellStatus[][]>  cellStatusByDateRange(LocalDate startDate, LocalDate endDate) {
        List<CellStatus[][]> res = new ArrayList<>();
        startDate.datesUntil(endDate.plusDays(1)).forEach(day -> res.add(cellStatusByDay(day)));
        return res;
    }

    private CellStatus[][]  cellStatusByDay(LocalDate day) {

        CellStatus[][] res = new CellStatus[this.getWidth()][this.getLength()];
        for (CellStatus[] i : res) {
            Arrays.fill(i, CellStatus.EMPTY);
        }

        cells.stream()
                .filter(e -> e.day.isEqual(day))
                .forEach(obj -> res[obj.getCoordinateX()][obj.getCoordinateY()] = CellStatus.TAKEN);

        return res;

    }

    public CellStatus[][] cellStatusByDaySubArray(CellStatus[][] cellStatus, Integer[] pos, int space_w, int space_l) throws IllegalArgumentException {

        if (pos[0] + space_w > cellStatus.length || pos[1] + space_l > cellStatus[0].length) {
            throw new IllegalArgumentException("Pos and required space falls outside.");
        }

        CellStatus[][] cpy = new CellStatus[space_w][space_l];
        for (int i = pos[0], a = 0; i < pos[0] + space_w; i++, a++) {
            for (int j = pos[1], b = 0; j < pos[1] + space_l; j++, b++) {
                cpy[a][b] = cellStatus[i][j];
            }
        }
        return cpy;
    }



}
