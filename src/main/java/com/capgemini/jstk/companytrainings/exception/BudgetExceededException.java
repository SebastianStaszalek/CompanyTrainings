package com.capgemini.jstk.companytrainings.exception;

public class BudgetExceededException extends RuntimeException{

    public BudgetExceededException(String message) {
        super(message);
    }
}
