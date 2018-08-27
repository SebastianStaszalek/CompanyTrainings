package com.capgemini.jstk.companytrainings.exception.message;

public final class Message {

    public static final String TRAININGS_EXCEEDED = "The number of 3 trainings per year cannot be exceeded";
    public static final String BUDGET_EXCEEDED = "The total cost 15 000 per year cannot be exceeded";
    public static final String EXTRA_BUDGET_EXCEEDED = "The total cost 50 000 per year cannot be exceeded";

    public static final String STUDENT_ALREADY_EXISTS = "The employee was already added to this training as student";
    public static final String STUDENT_ALREADY_ADDED_AS_COUCH = "The employee already added to this training as couch";
    public static final String COUCH_ALREADY_EXISTS = "The employee was already added to this training as couch";
    public static final String COUCH_ALREADY_ADDED_AS_STUDENT = "The employee already added to this training as student";

    public static final String TRAINING_CANCELED = "Training is cancelled!";
    public static final String TRAINING_FINISHED = "Training is already finished!";

    public static final String INCORRECT_DATES = "The end date cannot be before start date!";

    public static final String EMPTY_ID = "Id field should not be empty";
    public static final String EMPTY_OBJECT = "All fields are empty!";
    public static final String EMPTY_FIELD = "Search field should not empty!";
    public static final String TRAINING_NOT_FOUND = "Training was not found";
    public static final String EMPLOYEE_NOT_FOUND = "Employee was not found";
    public static final String EXTERNAL_COUCH__NOT_FOUND = "Employee was not found";
}
