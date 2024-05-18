package org.garden.items.adapter.in.rest;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

@Schema(description = "A request to create an item of type plant_id")
public record CreateItemRequestDTO(@NotNull long plant_id ) {}
