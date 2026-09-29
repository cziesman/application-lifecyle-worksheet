package com.example.lifecycle.model;

import java.util.List;

public record WorksheetStory(String id, String name, String applicability, int fieldCount, List<WorksheetField> fields) {}
