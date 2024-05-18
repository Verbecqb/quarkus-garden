package org.garden.items.domain.constants;

import org.garden.items.domain.entity.ItemAggregate;
import org.garden.items.domain.port.out.event.DomainEvent;
import org.garden.items.domain.port.out.event.ItemCreatedDomainEvent;

import java.util.Optional;

public enum PlantState {

    FAILURE {
        @Override
        public Optional<DomainEvent> nextStep(ItemAggregate item) {
            throw new IllegalStateException("Failure is a final state.");
        }
    },

    AWAITING_ALLOCATION {
        @Override
        public Optional<DomainEvent> nextStep(ItemAggregate itemAggregate) {
            if (itemAggregate.getPlantTypeId() == null) throw new IllegalStateException("Plant should have a type");
            itemAggregate.setState(PlantState.ALLOCATED);
            return Optional.of(ItemCreatedDomainEvent.builder().build());
        }
    },
    
    ALLOCATED {
        @Override
        public Optional<DomainEvent> nextStep(ItemAggregate item) {
            item.setState(PlantState.READY_FOR_SEEDING_TRAY);
            return Optional.empty(); //TODO generate an event?
        }
    },

    READY_FOR_SEEDING_TRAY {
        @Override
        public Optional<DomainEvent>  nextStep(ItemAggregate item) {
            item.setState(PlantState.SEEDING);
            return Optional.empty(); //TODO generate an event?
        }
    },

    SEEDING {
        @Override
        public Optional<DomainEvent>  nextStep(ItemAggregate item) {
            item.setState(PlantState.READY_FOR_TRANSPLANT);
            return Optional.empty(); //TODO generate an event?
        }
    },

    READY_FOR_TRANSPLANT {
        @Override
        public Optional<DomainEvent>  nextStep(ItemAggregate item) {
            item.setState(PlantState.TRANSPLANTED);
            return Optional.empty(); //TODO generate an event?
        }
    },


    TRANSPLANTED {
        @Override
        public Optional<DomainEvent>  nextStep(ItemAggregate item) {
            item.setState(PlantState.READY_FOR_PICKUP);
            return Optional.empty(); //TODO generate an event?
        }
    },

    READY_FOR_PICKUP {
        @Override
        public Optional<DomainEvent>  nextStep(ItemAggregate item) {
            item.setState(PlantState.PICKEDUP);
            return Optional.empty(); //TODO generate an event?
        }
    },

    PICKEDUP {
        @Override
        public Optional<DomainEvent> nextStep(ItemAggregate item) {

            return Optional.empty(); //TODO generate an event?

        }
    };


    public abstract Optional<DomainEvent> nextStep(ItemAggregate item);

}
