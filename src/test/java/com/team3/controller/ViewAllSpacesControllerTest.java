package com.team3.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.team3.model.Space;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

class ViewAllSpacesControllerTest {

    // Sample data used for US-5 tests
    private List<Space> sampleSpaces = Arrays.asList(
        new Space("Conference Room", 20, "Projector, Whiteboard"),
        new Space("Auditorium", 200, "Stage, Sound System"),
        new Space("Study Room", 6, "Quiet Space")
    );

    @Test
    void testEmptySearchShowsAll() {
        FilteredList<Space> filtered = new FilteredList<>(FXCollections.observableArrayList(sampleSpaces), p -> true);

        filtered.setPredicate(space -> true);

        assertEquals(3, filtered.size());
    }

    @Test
    void testExactMatch() {
        FilteredList<Space> filtered = new FilteredList<>(FXCollections.observableArrayList(sampleSpaces), p -> true);

        filtered.setPredicate(space -> space.getName().toLowerCase().contains("auditorium"));

        assertEquals(1, filtered.size());
        assertEquals("Auditorium", filtered.get(0).getName());
    }

    @Test
    void testPartialMatch() {
        FilteredList<Space> filtered = new FilteredList<>(FXCollections.observableArrayList(sampleSpaces), p -> true);

        filtered.setPredicate(space -> space.getName().toLowerCase().contains("room"));

        assertEquals(2, filtered.size());
    }

    @Test
    void testCaseInsensitiveMatch() {
        FilteredList<Space> filtered = new FilteredList<>(FXCollections.observableArrayList(sampleSpaces), p -> true);

        filtered.setPredicate(space -> space.getName().toLowerCase().contains("conference".toLowerCase()));

        assertEquals(1, filtered.size());
        assertEquals("Conference Room", filtered.get(0).getName());
    }

    @Test
    void testNoMatch() {
        FilteredList<Space> filtered = new FilteredList<>(FXCollections.observableArrayList(sampleSpaces), p -> true);

        filtered.setPredicate(space -> space.getName().toLowerCase().contains("zzz"));

        assertEquals(0, filtered.size());
    }
}
