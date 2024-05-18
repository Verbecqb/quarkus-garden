package org.garden;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.garden.constants.CellStatus;
import org.garden.domain.entity.GardenMapAggregate;
import org.garden.domain.entity.RaisedBed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import org.garden.domain.events.schema.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class RaisedBedEntityTest {

    GardenMapAggregate aggregate = new GardenMapAggregate();

    private final int WIDTH = 4;
    private final int LENGTH = 5;


    private void displayRaisedBed(CellStatus[][] cellStatuses) {

        int i = 0;
        int j = 0;

        for (int tmp = 0; tmp < cellStatuses[0].length; tmp++) {
            if (tmp == 0) System.out.print("  " + tmp + " ");
            else System.out.print(tmp + " ");
        }

        for (CellStatus[] re : cellStatuses) {
            System.out.println();
            for (CellStatus cellStatus : re) {
                if (j == 0) System.out.print(i + " ");
                System.out.print((cellStatus == CellStatus.EMPTY ? "O" : "X") + " ");
                j++;
            }
            i++;
            j = 0;
        }
        System.out.println();
    }

    @BeforeEach
    void setUp() {

        PanacheMock.mock(RaisedBed.class);

        RaisedBed raisedBed1 = new RaisedBed();
        raisedBed1.setLength(LENGTH);
        raisedBed1.setWidth(WIDTH);
        raisedBed1.setName("Raisedbed 1");


        raisedBed1.addSchedule(0, 2, 2, 2, 1, LocalDate.now(), LocalDate.now());
        raisedBed1.addSchedule(1, 3, 2, 1, 1, LocalDate.now(), LocalDate.now());

        raisedBed1.addSchedule(2, 2, 2, 2, 2, LocalDate.now().plusDays(1), LocalDate.now().plusDays(1));
        raisedBed1.addSchedule(3, 0, 0, 1, 2, LocalDate.now().plusDays(1), LocalDate.now().plusDays(1));
        raisedBed1.addSchedule(4, 3, 0, 1, 1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(1));

        raisedBed1.addSchedule(5, 2, 2, 2, 2, LocalDate.now().plusDays(2), LocalDate.now().plusDays(2));
        raisedBed1.addSchedule(6, 0, 0, 1, 2, LocalDate.now().plusDays(2), LocalDate.now().plusDays(2));
        raisedBed1.addSchedule(7, 3, 0, 1, 1, LocalDate.now().plusDays(2), LocalDate.now().plusDays(2));
        raisedBed1.addSchedule(8, 1, 1, 1, 4, LocalDate.now().plusDays(2), LocalDate.now().plusDays(2));

        aggregate.addMapObject(raisedBed1);

        List<CellStatus[][]> map = raisedBed1.cellStatusByDateRange(LocalDate.now(), LocalDate.now().plusDays(2));
        map.forEach(mapByDay -> {
            System.out.println("Raised bed 1 - Map on Day : ");
            displayRaisedBed(mapByDay);
        });


    }

    @Test
    public void returned_event_allocation() {

        assertInstanceOf(
                AllocationEvent.class,
                aggregate.allocateItem(
                        0, 6, 2,
                        LocalDate.now(), LocalDate.now().plusDays(1))
        );

    }

    @Test
    public void returned_item_bigger_than_space_expect_rejected_event() {

        assertEquals(
                ((AllocationEvent) aggregate.allocateItem(
                        0, 6, 2,
                        LocalDate.now(), LocalDate.now().plusDays(1))).getAllocationStatus(),
                AllocationStatus.REJECTED
                );
    }

    @Test
    public void enough_space_first_day() {
        assertEquals(
                ((AllocationEvent) aggregate.allocateItem(
                        0, 2, 4,
                        LocalDate.now(), LocalDate.now())).getAllocationStatus(),
                AllocationStatus.RESERVED
        );
    }

    @Test
    public void enough_space_first_and_second_day_not_third_reject_expected() {
        assertEquals(
                ((AllocationEvent) aggregate.allocateItem(
                        0, 2, 3,
                        LocalDate.now(), LocalDate.now().plusDays(2))).getAllocationStatus(),
                AllocationStatus.REJECTED
        );
    }

    @Test
    public void enough_space_day1_and_day2_other_event_reserved() {
        assertEquals(
                ((AllocationEvent) aggregate.allocateItem(
                        0, 4, 1,
                        LocalDate.now(), LocalDate.now().plusDays(1))).getAllocationStatus(),
                AllocationStatus.RESERVED
        );
    }

    @Test
    public void enough_space_day1_and_day2_event_reserved() {
        assertEquals(
                ((AllocationEvent) aggregate.allocateItem(
                        0, 2, 2,
                        LocalDate.now(), LocalDate.now().plusDays(1))).getAllocationStatus(),
                AllocationStatus.RESERVED
        );
    }

    @Test
    public void no_space_on_bed1_space_onbed2_event_reserved() {

        RaisedBed raisedBed2 = new RaisedBed();
        raisedBed2.setLength(LENGTH-1);
        raisedBed2.setWidth(WIDTH-1);
        raisedBed2.setName("Raisedbed 2");
        raisedBed2.addSchedule(0, 0, 0, 1, 4, LocalDate.now(), LocalDate.now().plusDays(3));

        aggregate.addMapObject(raisedBed2);

        List<CellStatus[][]> map2 = raisedBed2.cellStatusByDateRange(LocalDate.now(), LocalDate.now().plusDays(2));
        map2.forEach(mapByDay -> {
            System.out.println("Raised bed 2 -Map on Day : ");
            displayRaisedBed(mapByDay);
        });

        assertEquals(
                ((AllocationEvent) aggregate.allocateItem(
                        0, 2, 3,
                        LocalDate.now().plusDays(1), LocalDate.now().plusDays(2))).getAllocationStatus(),
                AllocationStatus.RESERVED
        );

        // Remove raised bed 2 for otehr tests
        aggregate.getMapObjects().remove(raisedBed2);
    }

    @Test
    public void no_space_on_bed1_nor_bed2_event_reject() {

        RaisedBed raisedBed2 = new RaisedBed();
        raisedBed2.setLength(LENGTH);
        raisedBed2.setWidth(WIDTH);
        raisedBed2.setName("Raisedbed 2");
        raisedBed2.addSchedule(0, 0, 0, 1, 4, LocalDate.now(), LocalDate.now().plusDays(3));

        aggregate.addMapObject(raisedBed2);

        List<CellStatus[][]> map2 = raisedBed2.cellStatusByDateRange(LocalDate.now(), LocalDate.now().plusDays(2));
        map2.forEach(mapByDay -> {
            System.out.println("Raised bed 2 -Map on Day : ");
            displayRaisedBed(mapByDay);
        });

        assertEquals(
                ((AllocationEvent) aggregate.allocateItem(
                        0, 5, 4,
                        LocalDate.now().plusDays(1), LocalDate.now().plusDays(2))).getAllocationStatus(),
                AllocationStatus.REJECTED
        );

        // Remove raised bed 2 for other tests
        aggregate.getMapObjects().remove(raisedBed2);
    }





}
